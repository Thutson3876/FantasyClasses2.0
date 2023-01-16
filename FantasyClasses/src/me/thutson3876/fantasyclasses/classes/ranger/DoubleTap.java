package me.thutson3876.fantasyclasses.classes.ranger;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class DoubleTap extends AbstractAbility {

	private int numOfTargets = 1;
	
	public DoubleTap(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Double Tap";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.WITHER_SKELETON_SKULL);
	}

	@Override
	public String getInstructions() {
		return "Use &6Hunter's Mark";
	}

	@Override
	public String getDescription() {
		return "&6Hunter's Mark &rmarks &6" + this.numOfTargets + " &radditional targets";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void deInit() {
		if(fplayer == null)
			return;
		
		Ability abil = fplayer.getClassAbility(HuntersMark.class);
		if(abil == null)
			return;
		
		HuntersMark mark = (HuntersMark)abil;
		
		mark.setTargetAmt(1);
	}
	
	@Override
	public void applyLevelModifiers() {
		this.numOfTargets = this.currentLevel;
		
		if(fplayer == null || fplayer.getChosenClass() == null)
			return;
		
		Ability abil = fplayer.getClassAbility(HuntersMark.class);
		if(abil == null)
			return;
		
		HuntersMark mark = (HuntersMark)abil;
		
		mark.setTargetAmt(numOfTargets + 1);
	}

}
