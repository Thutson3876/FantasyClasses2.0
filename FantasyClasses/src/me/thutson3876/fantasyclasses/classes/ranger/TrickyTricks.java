package me.thutson3876.fantasyclasses.classes.ranger;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class TrickyTricks extends AbstractAbility {

	private double procChance = 0.05;
	
	public TrickyTricks(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Tricky Tricks";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.GUNPOWDER);
	}
	
	@Override
	public String getInstructions() {
		return "Land a Trick Shot on a target";
	}

	@Override
	public String getDescription() {
		return "Your trick shots have a &6" + AbilityUtils.doubleRoundToXDecimals(procChance * 100, 2) + "% &rchance to trigger twice";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		procChance = 0.05 * this.currentLevel;
		
		if(fplayer == null || fplayer.getChosenClass() == null)
			return;
		
		Ability abil = fplayer.getClassAbility(TrickShots.class);
		if(abil != null)
			((TrickShots)abil).setDoubleTriggerChance(procChance);
	}
	
	@Override
	public void deInit() {
		if(fplayer == null)
			return;
		
		Ability abil = fplayer.getClassAbility(TrickShots.class);
		if(abil != null)
			((TrickShots)abil).setDoubleTriggerChance(0.0);
	}

}
