package me.thutson3876.fantasyclasses.classes.berserker;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;

public class Taunt extends AbstractAbility {
		
	private double range = 8.0;
		
	public Taunt(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Taunt";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.NETHERITE_HELMET);
	}
	
	@Override
	public String getInstructions() {
		return "Become Enraged";
	}

	@Override
	public String getDescription() {
		return "When you become Enraged, you become the target of all mobs within &6" + this.range + " &rblocks";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}
	
	@Override
	public void deInit() {
		if(fplayer == null || fplayer.getChosenClass() == null)
			return;
		
		AbstractFantasyClass clazz = this.getFantasyPlayer().getChosenClass();
		if (!(clazz instanceof Berserker))
			return;

		Berserker berserker = (Berserker) clazz;
		
		berserker.setTauntRange(0);
	}

	@Override
	public void applyLevelModifiers() {
		if(fplayer == null || fplayer.getChosenClass() == null)
			return;
		
		AbstractFantasyClass clazz = this.getFantasyPlayer().getChosenClass();
		if (!(clazz instanceof Berserker))
			return;

		Berserker berserker = (Berserker) clazz;
		
		berserker.setTauntRange(range);
	}

}
