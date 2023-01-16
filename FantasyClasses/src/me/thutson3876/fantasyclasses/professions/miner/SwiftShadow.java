package me.thutson3876.fantasyclasses.professions.miner;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.professions.fisherman.Fisherman;

public class SwiftShadow extends AbstractAbility {

	public SwiftShadow(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Swift Shadow";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.FEATHER);
	}

	@Override
	public String getInstructions() {
		return "Use &6Darkvision";
	}

	@Override
	public String getDescription() {
		return "Darkvision also grants Speed 1 for its duration";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		if(fplayer == null)
			return;
		
		Ability abil = fplayer.getClassAbility(Darkvision.class);
		if(abil != null)
			((Darkvision)abil).setHasSpeed(true);
	}
	
	@Override
	public void deInit() {
		if(fplayer == null)
			return;
		
		boolean isMiner = false;
		for(AbstractFantasyClass prof : fplayer.getChosenProfessions()) {
			if(prof != null && prof.getClass().equals(Fisherman.class))
				isMiner = true;
		}
		
		if(!isMiner)
			return;
		
		Ability abil = fplayer.getClassAbility(Darkvision.class);
		if(abil != null)
			((Darkvision)abil).setHasSpeed(false);
	}

}
