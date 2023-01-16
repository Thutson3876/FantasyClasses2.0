package me.thutson3876.fantasyclasses.classes.monk;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class SpicedBrew extends AbstractAbility {

	public SpicedBrew(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Spiced Brew";
		this.skillPointCost = 1;
		this.maximumLevel = 1;
		
		this.createItemStack(Material.FIRE_CORAL);
	}

	@Override
	public String getInstructions() {
		return "Use &6Fulfilling Mead";
	}

	@Override
	public String getDescription() {
		return "Your &6Fulfilling Mead &rnow heals you for a small amount and applies Strength and Speed instead of Regeneration and Resistance";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}
	
	@Override
	public void deInit() {
		if(fplayer == null || fplayer.getChosenClass() == null)
			return;
		
		Ability abil = fplayer.getClassAbility(FulfillingMead.class);
		if (abil == null)
			return;

		FulfillingMead mead = (FulfillingMead) abil;
		
		mead.toggleOffensives(false);
	}

	@Override
	public void applyLevelModifiers() {
		if(fplayer == null || fplayer.getChosenClass() == null)
			return;
		
		Ability abil = fplayer.getClassAbility(FulfillingMead.class);
		if (abil == null)
			return;

		FulfillingMead mead = (FulfillingMead) abil;
		
		mead.toggleOffensives(true);
	}

}
