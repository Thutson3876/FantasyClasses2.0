package me.thutson3876.fantasyclasses.professions.hunter;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class Resourceful extends AbstractAbility {

	double chance = 0.25;
	
	public Resourceful(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Resourceful";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.IRON_INGOT);
	}

	@EventHandler
	public void onCraftItemEvent(CraftItemEvent e) {
		if(e.getWhoClicked() == null)
			return;
		
		if(!e.getWhoClicked().equals(player))
			return;
		
		
		
		this.onTrigger(false);
	}

	@Override
	public String getInstructions() {
		return "Become afflicted with &dNausea&r, &dHunger&r, or &dPoison";
	}

	@Override
	public String getDescription() {
		return "Effects that impact your stomach are reduced by half";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		
	}

}
