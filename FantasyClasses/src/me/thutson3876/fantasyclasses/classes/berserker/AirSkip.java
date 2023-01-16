package me.thutson3876.fantasyclasses.classes.berserker;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class AirSkip extends AbstractAbility {

	public AirSkip(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Air Skip";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.FEATHER);
	}

	@Override
	public String getInstructions() {
		return "Re-activate &6Leap &5while &5it's on cooldown";
	}

	@Override
	public String getDescription() {
		return "While you're mid-air, after using &6Leap&r, you may re-activate it to stall in air";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void deInit() {
		if(fplayer == null)
			return;
		
		Ability abil = fplayer.getClassAbility(Leap.class);
		if(abil == null)
			return;
		
		Leap leap = (Leap)abil;
		
		leap.setDoubleJump(false);
	}
	
	@Override
	protected void init() {
		if(fplayer == null || fplayer.getChosenClass() == null)
			return;
		
		Ability abil = fplayer.getClassAbility(Leap.class);
		if(abil == null)
			return;
		
		Leap leap = (Leap)abil;
		
		leap.setDoubleJump(true);
	}

	@Override
	public void applyLevelModifiers() {
		
	}

}
