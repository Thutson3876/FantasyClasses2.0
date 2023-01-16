package me.thutson3876.fantasyclasses.classes.combat;

import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;

public class LifeRip extends AbstractAbility {

	public LifeRip(Player p) {
		super(p, Priority.LOW);
	}

	@Override
	public void setDefaults() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getInstructions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getDealsDamage() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		// TODO Auto-generated method stub
		
	}

}
