package me.thutson3876.fantasyclasses.classes.monk;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Serenity extends AbstractAbility {

	private double modPerTick = 0.01;
	
	public Serenity(Player p) {
		super(p);
	}


	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Serenity";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.LILY_OF_THE_VALLEY);
	}

	@EventHandler
	public void onAbilityUseEvent(AbilityTriggerEvent e) {
		if(!e.getFplayer().equals(this.fplayer))
			return;
		
		e.setCooldownReductionPerTick(e.getCooldownReductionPerTick() * (1 + modPerTick));
	}

	@Override
	public String getInstructions() {
		return "Stay mid-air and use abilities";
	}

	@Override
	public String getDescription() {
		return "Your abilities cooldown &6" + AbilityUtils.doubleRoundToXDecimals(modPerTick * 20 * 100, 2) + "% &rfaster when used mid-air";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		modPerTick = 0.01 * this.currentLevel;
	}

}
