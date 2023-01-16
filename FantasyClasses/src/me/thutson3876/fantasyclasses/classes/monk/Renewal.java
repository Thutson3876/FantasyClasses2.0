package me.thutson3876.fantasyclasses.classes.monk;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class Renewal extends AbstractAbility {

	public Renewal(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Renewal";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.BONE_MEAL);
	}

	@Override
	public String getInstructions() {
		return "Use &6Uplifting Spirits";
	}

	@Override
	public String getDescription() {
		return "Your &6Uplifting Spirits &rnow removes all debuffs from the target and applies Regeneration";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		if(fplayer == null || fplayer.getChosenClass() == null)
			return;
		
		Ability abil = fplayer.getClassAbility(UpliftingSpirits.class);
		if (abil == null)
			return;

		UpliftingSpirits uplift = (UpliftingSpirits) abil;
		
		uplift.setRemovesDebuffs(true);
	}
	
	@Override
	public void deInit() {
		if(fplayer == null || fplayer.getChosenClass() == null)
			return;
		
		Ability abil = fplayer.getClassAbility(UpliftingSpirits.class);
		if (abil == null)
			return;

		UpliftingSpirits uplift = (UpliftingSpirits) abil;
		
		uplift.setRemovesDebuffs(false);
	}

}
