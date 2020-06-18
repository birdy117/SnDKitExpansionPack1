package me.libraryaddict.arcade.game.searchanddestroy.kits;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import me.libraryaddict.arcade.game.searchanddestroy.FuseType;
import me.libraryaddict.arcade.game.searchanddestroy.abilities.TechnicianAbility;
import me.libraryaddict.core.C;
import me.libraryaddict.core.inventory.utils.ItemBuilder;

/**
 * This class manages how Technician\
 *  will show up in the Kit HUD and what items it will recieve
 * This file should be in your kits folder
 * @author birdy117
 *
 */
public class Technician extends SnDKit{

	public Technician() {
		super("Technician", new String[]
				{
				"A thinking man's kit that is a close cousin of Explosive",
				"Armed with a decent melee weapon but weak defenses, technicians are good at disorienting enemies with their flashbangs and then capatilizing on it",
				"by birdy117"
				},
				new TechnicianAbility());
		
		setPrice(300);
		
		
		setItems(buildFuse(FuseType.BOMB_SPEED, 5),
				new ItemBuilder(Material.IRON_SWORD).addEnchantment(Enchantment.KNOCKBACK, 1).build(),
				 new ItemBuilder(Material.MAGMA_CREAM).setTitle(C.Yellow + "Flashbang").addLore(C.Blue + "Blinds enemies after being thrown").setAmount(5).build()
				);
	}

	@Override
	public Material[] getArmorMats() {
		return new Material[] {
				Material.IRON_BOOTS, Material.GOLD_LEGGINGS, Material.GOLD_CHESTPLATE, Material.IRON_HELMET
		};
	}

	@Override
	public Material getMaterial() {
		return Material.MAGMA_CREAM;
	}

}
