package me.thutson3876.fantasyclasses.professions.fisherman;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;

public class Hydrodynamics extends AbstractAbility {

	private float swimSpeedBonus = 0.07f;
	
	public Hydrodynamics(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Hydrodynamics";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.SALMON);	
	}

	@Override
	public String getInstructions() {
		return "Swim in water while &6Forsaken &6Ancestry &ris active";
	}

	@Override
	public String getDescription() {
		return "Your slick and slender build allows you to increase your swim speed by &6" + 35 * currentLevel + "% &rwhile &6Forsaken &6Ancestry &ris active";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		swimSpeedBonus = 0.07f * currentLevel;
		
		if(fplayer == null)
			return;
		
		Ability abil = fplayer.getClassAbility(ForsakenAncestry.class);
		if(abil != null)
			((ForsakenAncestry)abil).setSwimSpeedBonus(swimSpeedBonus);
	}
	
	@Override
	public void deInit() {
		player.setWalkSpeed(0.2f);
		
		if(fplayer == null)
			return;
		
		boolean isFisher = false;
		for(AbstractFantasyClass prof : fplayer.getChosenProfessions()) {
			if(prof != null && prof.getClass().equals(Fisherman.class))
				isFisher = true;
		}
		
		if(!isFisher)
			return;
		
		Ability abil = fplayer.getClassAbility(ForsakenAncestry.class);
		if(abil != null)
			((ForsakenAncestry)abil).setSwimSpeedBonus(0.0f);
	}
	
	public float getSwimSpeedBonus() {
		return this.swimSpeedBonus;
	}
}
