package me.thutson3876.fantasyclasses.classes.highroller;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class GhostlyStrike extends AbstractAbility {

	private static final double CHANCE_PER_LEVEL = 0.15;
	
	private double chance = CHANCE_PER_LEVEL;
	private double duration = 3 * 20;

	public GhostlyStrike(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0 * 20;
		this.displayName = "Ghost Strike";
		this.skillPointCost = 1;
		this.maximumLevel = 3;

		this.createItemStack(Material.GHAST_TEAR);
	}

	@Override
	public String getInstructions() {
		return "Hit a creature with a melee attack";
	}

	@Override
	public String getDescription() {
		return "When you hit with a melee attack, you have a &6" + AbilityUtils.doubleRoundToXDecimals(chance * 100, 1)
				+ "% &rchance to gain &dStealth &rfor &6"
				+ AbilityUtils.doubleRoundToXDecimals(((double) duration) / 20.0, 1) + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		chance = CHANCE_PER_LEVEL * this.currentLevel;
	}

}
