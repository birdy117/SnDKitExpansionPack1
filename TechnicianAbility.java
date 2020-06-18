package me.libraryaddict.arcade.game.searchanddestroy.abilities;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.libraryaddict.arcade.game.GameTeam;
import me.libraryaddict.arcade.kits.Ability;
import me.libraryaddict.core.C;
import me.libraryaddict.core.recharge.Recharge;
import me.libraryaddict.core.time.TimeEvent;
import me.libraryaddict.core.time.TimeType;
import me.libraryaddict.core.utils.UtilEnt;
import me.libraryaddict.core.utils.UtilInv;
import me.libraryaddict.core.utils.UtilLoc;
import me.libraryaddict.core.utils.UtilNumber;
import me.libraryaddict.core.utils.UtilParticle;
import me.libraryaddict.core.utils.UtilParticle.ParticleType;
import me.libraryaddict.core.utils.UtilTime;

/**
 * This class manages Technicians abilities.
 * It ultimatley uses most of Explosive's code, but utilized flashbangs instead.
 * This file should be in your abilities folder
 * @author birdy117
 *
 */
public class TechnicianAbility extends Ability {
	
	//An ArrayList to keep track of all the dropped flashbangs
	private ArrayList<Pair<Long, Pair<Entity, Player>>> _flashbangs = new ArrayList<Pair<Long, Pair<Entity, Player>>>();

	/**
	 * This method helps determines if a Technician right clicks a flashbang in their hand
	 * @param event
	 */
	@EventHandler
	public void flashbangThrow(PlayerInteractEvent event) {
		//If the player does anything but right-click, don't do anything
		if(!event.getAction().name().contains("RIGHT"))
			return;
		
		//If the game isn't ongoing, don't do anything
		if(!isLive()) {
			return;
		}
		
		//If the right-clicked item isn't magma cream, then don't do anything
		if(!UtilInv.isItem(event.getItem(), Material.MAGMA_CREAM))
			return;
		
		Player thrower = event.getPlayer();
		
		//If the Technician is not alive, don't do anything
		if(!isAlive(thrower))
			return;
		
		//If the thrower doesn't have the Technician ability, don't do anything
		if(!hasAbility(thrower))
			return;
			
		//Makes sure only one flashbang can be thrown at a time
		long shortest = -1;

        for (int i = 0; i < 1; i++)
        {
            if (!Recharge.canUse(thrower, "Flashbang" + i))
            {
                long timeLeft = Recharge.getTimeLeft(thrower, "Flashbang" + i);

                if (shortest < 0 || timeLeft < shortest)
                {
                    shortest = timeLeft;
                }

                continue;
            }

            shortest = -1;

            Recharge.use(thrower, "Flashbang" + i, 5000);
            break;
        }

        if (shortest > 0)
        {
            thrower.sendMessage(C.Red + "You cannot throw another flashbang for "
                    + UtilNumber.getTime((int) Math.ceil(shortest / 1000D)) + "!");
            return;
        }
        
        //Stores the Magma Cream as an ItemStack Object to be used in UtilInv
        ItemStack flashbang = event.getItem();
        
        //Removes 1 magma cream from the throwers inventory
        UtilInv.remove(thrower, flashbang, 1);
        
        //Finds where the thrower is facing
        Item drop = event.getPlayer().getWorld().dropItem(thrower.getEyeLocation().subtract(0, 0.2, 0), flashbang);
        
        //Calculates where the flashbang will be thrown and how far
        Vector vec = thrower.getLocation().getDirection().normalize().multiply(0.8);
        
        //Drops the magma cream using the data above
        UtilEnt.velocity(drop, vec, false);
        
        //Adds the dropped flashbang to the ArrayList of flashbangs
        _flashbangs.add(Pair.of(System.currentTimeMillis(), Pair.of(drop, thrower)));
	}
	
	/*
	 * This method manages when the flashbang will explode after it has been thrown
	 */
	@EventHandler
	private void thrownFlashbang(TimeEvent event) {
        if (event.getType() != TimeType.TICK)
            return;

        if (!isLive())
            return;
        
        Iterator<Pair<Long, Pair<Entity, Player>>> flashbangIterator = _flashbangs.iterator();
        
        while(flashbangIterator.hasNext()) {
        	//Gets the first thrown flashbang and stores it as a Pair Object
        	Pair<Long, Pair<Entity, Player>> entry = flashbangIterator.next();

        	//Gets the first thrown magma cream and stores it as an Entity Object
            Entity entity = entry.getValue().getKey();
            
            //Gets the player that threw this flashbang
            Player player = entry.getValue().getValue();
            
            GameTeam team = getGame().getTeam(player);
            
            //If the magma cream has despawned or died, then it will be removed from the Iterator
            if (!entity.isValid())
            {
                flashbangIterator.remove();
                continue;
            }
            
            //If the total time of a thrown flashbang has not exceeded 3000 ticks(150 sec), then it will continue the script
            if (!UtilTime.elasped(entry.getKey(), 3000))
            {
                continue;
            }
            
            //Removes the thrown flashbang from the ground
            flashbangIterator.remove();
            entity.remove();
            
            //gets the flashbangs location
            Location loc = entity.getLocation();
            
            //Plays a particle & sound effect at the location
            entity.getWorld().playSound(loc, Sound.BLOCK_ANVIL_PLACE, 1.4F, 1.5F);  
            UtilParticle.playParticle(ParticleType.CLOUD, loc, 40);
            
            //Make a potion effect that will be applied to players(Blindess for 10 seconds)
            PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 100, 0);
            
            //Gets all living entities that will be blinded within a 5 block radius and stores them in a HashSet
            ArrayList<LivingEntity> blinded = UtilLoc.getInRadius(loc, 5, LivingEntity.class);
            
            for(LivingEntity caughtInBlast: blinded) {
            	if(!team.isInTeam(caughtInBlast)) {
            		caughtInBlast.addPotionEffect(blindness);
            	}
            }

        }
	}
}
