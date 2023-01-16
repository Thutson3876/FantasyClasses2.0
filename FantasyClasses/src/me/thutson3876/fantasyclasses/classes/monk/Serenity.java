package me.thutson3876.fantasyclasses.classes.monk;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Serenity extends AbstractAbility {

	private double modPerTick = 0.005;
	private double cooldownReductionRate = 1.00;
	
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
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		if(!e.getPlayer().equals(player))
			return;
		
		if(AbilityUtils.getHeightAboveGround(player) < 0.3) {
			if(cooldownReductionRate > 1.00) {
				cooldownReductionRate = 1.00;
			}
			
			return;
		}
		
		cooldownReductionRate += modPerTick;
	}
	
	@EventHandler
	public void onAbilityUseEvent(AbilityTriggerEvent e) {
		if(!e.getFplayer().equals(this.fplayer))
			return;
		
		e.setCooldownReductionPerTick(cooldownReductionRate);
	}

	@Override
	public String getInstructions() {
		return "Stay mid-air and use abilities";
	}

	@Override
	public String getDescription() {
		return "Your abilities cooldown &6" + AbilityUtils.doubleRoundToXDecimals(modPerTick * 20 * 100, 2) + "% &rfaster when used mid-air. This effect stacks for each second spent mid-air";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		modPerTick = 0.005 * this.currentLevel;
	}

}
