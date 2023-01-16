package me.thutson3876.fantasyclasses.professions.enchanter;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareAnvilEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class AnvilAdept extends AbstractAbility {

	private int reductionAmt = 1;
	
	public AnvilAdept(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Anvil Adept";
		this.skillPointCost = 1;
		this.maximumLevel = 3;

		this.createItemStack(Material.ANVIL);
	}
	
	@EventHandler
	public void onPrepareAnvilEvent(PrepareAnvilEvent e) {
		boolean isViewing = false;
		for(HumanEntity ent : e.getViewers()) {
			if(ent.equals(player)) {
				isViewing = true;
			}
		}
		
		if(!isViewing)
			return;
		
		e.getInventory().setRepairCost(newRepairCost(e.getInventory().getRepairCost()));
	}
	
	private int newRepairCost(int currentCost) {
		int newCost = currentCost - this.reductionAmt;
		return newCost < 1 ? 1 : newCost;
	}

	@Override
	public String getInstructions() {
		return "Use an anvil";
	}

	@Override
	public String getDescription() {
		return "When you use an anvil, reduce the the level cost by &6" + this.reductionAmt + "&r. Cost will not be reduced below 1";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.reductionAmt = this.currentLevel;
	}

}
