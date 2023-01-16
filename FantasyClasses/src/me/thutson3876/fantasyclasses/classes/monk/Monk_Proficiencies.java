package me.thutson3876.fantasyclasses.classes.monk;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class Monk_Proficiencies extends AbstractAbility {
	
	public Monk_Proficiencies(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Monk Proficiencies";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.BAMBOO);
	}
	

	@Override
	public String getInstructions() {
		return "Wear no armor to use your abilities";
	}

	@Override
	public String getDescription() {
		return "&aIncreases your movement speed &aby 30%";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}
	
	@Override
	protected void init() {
		if(fplayer == null)
			return;
		
		player.setWalkSpeed(0.26f);
		this.fplayer.setArmorType(0);
	}

	@Override
	public void applyLevelModifiers() {	
		
	}
}
