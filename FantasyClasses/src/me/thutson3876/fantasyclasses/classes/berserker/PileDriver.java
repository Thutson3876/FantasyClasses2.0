package me.thutson3876.fantasyclasses.classes.berserker;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class PileDriver extends AbstractAbility {

	public PileDriver(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0 * 20;
		this.displayName = "Pile Driver";
		this.skillPointCost = 1;
		this.maximumLevel = 1;	
		
		this.createItemStack(Material.HOPPER);
	}

	@Override
	public String getInstructions() {
		return "Use &6Deep Dive";
	}

	@Override
	public String getDescription() {
		return "When you use &6Deep &6Dive&r, you bring any entities near you down to the ground with you";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void deInit() {
		if(fplayer == null)
			return;
		
		Ability abil = fplayer.getClassAbility(DeepDive.class);
		if(abil == null)
			return;
		
		DeepDive dive = (DeepDive)abil;
		
		dive.setPullNearbyEntities(false);
	}
	
	@Override
	protected void init() {
		if(fplayer == null || fplayer.getChosenClass() == null)
			return;
		
		Ability abil = fplayer.getClassAbility(DeepDive.class);
		if(abil == null)
			return;
		
		DeepDive dive = (DeepDive)abil;
		
		dive.setPullNearbyEntities(true);
	}
	
	@Override
	public void applyLevelModifiers() {
		
	}

}
