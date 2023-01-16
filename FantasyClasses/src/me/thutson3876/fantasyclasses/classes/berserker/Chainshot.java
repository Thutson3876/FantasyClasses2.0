package me.thutson3876.fantasyclasses.classes.berserker;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class Chainshot extends AbstractAbility {
	
	public Chainshot(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0 * 20;
		this.displayName = "Chainshot";
		this.skillPointCost = 1;
		this.maximumLevel = 1;	
		
		this.createItemStack(Material.CHAIN);
	}

	@Override
	public String getInstructions() {
		return "Use &6Deep Dive";
	}

	@Override
	public String getDescription() {
		return "When you use &6Cannonball&r, you bring any entities nearby with you";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void deInit() {
		if(fplayer == null)
			return;
		
		Ability abil = fplayer.getClassAbility(Cannonball.class);
		if(abil == null)
			return;
		
		Cannonball cannonball = (Cannonball)abil;
		
		cannonball.setPullNearbyEntities(false);
	}
	
	@Override
	protected void init() {
		if(fplayer == null || fplayer.getChosenClass() == null)
			return;
		
		Ability abil = fplayer.getClassAbility(Cannonball.class);
		if(abil == null)
			return;
		
		Cannonball cannonball = (Cannonball)abil;
		
		cannonball.setPullNearbyEntities(true);
	}
	
	@Override
	public void applyLevelModifiers() {
		
	}

}
