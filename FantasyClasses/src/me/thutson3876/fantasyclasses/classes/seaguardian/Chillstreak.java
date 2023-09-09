package me.thutson3876.fantasyclasses.classes.seaguardian;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.math.MathUtils;

public class Chillstreak extends AbstractAbility {

	//private int stackAmt = 1;
	private int duration = 15 * 20;
	
	public Chillstreak(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0 * 20;
		this.displayName = "Chillstreak";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.FROSTED_ICE);
	}

	@Override
	public String getInstructions() {
		return "Deal damage with &6Remorseless Winter";
	}

	@Override
	public String getDescription() {
		return "Damaging a target with &6Remorseless Winter grants you a stack of &dFrost &dFever &rfor &6" + MathUtils.convertToDurationInSeconds(duration, 1);
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		
	}

}
