package me.libraryaddict.arcade.game.searchanddestroy.kits;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import me.libraryaddict.arcade.game.searchanddestroy.abilities.PorcupineAbility;
import me.libraryaddict.core.C;
import me.libraryaddict.core.inventory.utils.ItemBuilder;

/**
 * This class manages how Porcupine will show up in the Kit HUD and what items it will recieve
 * This file should be in your kits folder
 * @author birdy117
 *
 */
public class Porcupine extends SnDKit{

	public Porcupine() {
		super("Porcupine", new String[] {
			  "A spiky brawler who excels at dealing with archers with his high projectile protection. The more arrows he has in him, the more damage he reflects right back at ya!",
			  "Damage reflected will be dealt as true damage. However, it lacks items to heal with and it's reflection damage is capped.",
			  "Inspired by OnettOnslaught, by birdy117"
		}, new PorcupineAbility());
		
		setPrice(200);
		
		setItems(new ItemStack[] {
				new ItemStack(Material.IRON_SWORD)
				
		});
	}
	
	public ItemStack[] getArmor() {
		return new ItemStack[] {
				new ItemBuilder(Material.CHAINMAIL_HELMET).build(),
				new ItemBuilder(Material.IRON_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 3).addLore(C.Gray + "Super Thorns").build(),
				new ItemBuilder(Material.CHAINMAIL_LEGGINGS).build(),
				new ItemBuilder(Material.IRON_BOOTS).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 3).build()
		};
		
	}
	
	@Override
	public ItemStack getIcon() {
		return new ItemBuilder(Material.DEAD_BUSH).addDullEnchant().addLore(C.Gray + "Super Thorns").build();
	}

	@Override
	public Material[] getArmorMats() {
		return null;
	}

	@Override
	public Material getMaterial() {
		return null;
	}

}