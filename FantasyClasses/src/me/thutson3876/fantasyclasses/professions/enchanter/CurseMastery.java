package me.thutson3876.fantasyclasses.professions.enchanter;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.professions.enchanter.customenchantments.Enchantments;

public class CurseMastery extends AbstractAbility {

	public CurseMastery(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Curse Mastery";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.GRINDSTONE);
	}
	
	@EventHandler
	public void onPrepareGrindstoneEvent(PrepareGrindstoneEvent e) {
		if(!e.getViewers().contains(player))
			return;
		
		ItemStack item = e.getResult();
		
		for(Enchantment curse : Enchantments.CURSE.getEnchants()) {
			if(item.containsEnchantment(curse))
				item.removeEnchantment(curse);
				
		}
		
		e.setResult(item);
	}

	@Override
	public String getInstructions() {
		return "Use a Grindstone on a Cursed item";
	}

	@Override
	public String getDescription() {
		return "You can now remove Curses from items via a Grindstone";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		
	}

}
