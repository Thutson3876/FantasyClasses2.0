package me.thutson3876.fantasyclasses.classes.monk;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;

public class EssenceFont extends AbstractAbility {

	public EssenceFont(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Essence Font";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.EXPERIENCE_BOTTLE);
	}

	@Override
	public String getInstructions() {
		return "Target allies with your damaging abilities";
	}

	@Override
	public String getDescription() {
		return "Become a font of life essence for your allies. Your damaging abilities heal players instead of dealing damage";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		if(fplayer == null || fplayer.getChosenClass() == null)
			return;
		
		AbstractFantasyClass clazz = this.getFantasyPlayer().getChosenClass();
		if (!(clazz instanceof Monk))
			return;

		Monk monk = (Monk) clazz;
		
		monk.setIsHealer(true);
	}
	
	@Override
	public void deInit() {
		if(fplayer == null || fplayer.getChosenClass() == null)
			return;
		
		AbstractFantasyClass clazz = this.getFantasyPlayer().getChosenClass();
		if (!(clazz instanceof Monk))
			return;

		Monk monk = (Monk) clazz;
		
		monk.setIsHealer(false);
	}

}
