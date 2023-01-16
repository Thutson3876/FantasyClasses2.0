package me.thutson3876.fantasyclasses.professions.fisherman;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class SharedBlessings extends AbstractAbility {

	private double range = 8.0;
	
	public SharedBlessings(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Shared Blessings";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.PRISMARINE_CRYSTALS);	
	}

	@Override
	public String getInstructions() {
		return "Use &6Forsaken &6Ancestry";
	}

	@Override
	public String getDescription() {
		return "You are able to grant Conduit Power to your nearby allies within &6" + AbilityUtils.doubleRoundToXDecimals(range, 2) + " &rblocks when you use &6Forsaken &6Ancestry";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		range = 8.0 * currentLevel;
	}
	
	public double getRange() {
		return this.range;
	}

}
