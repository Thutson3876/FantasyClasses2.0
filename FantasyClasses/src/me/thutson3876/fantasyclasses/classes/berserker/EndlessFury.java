package me.thutson3876.fantasyclasses.classes.berserker;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class EndlessFury extends AbstractAbility {

	private double cooldownReduction = 0.5 * 20;
	
	public EndlessFury(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Endless Fury";
		this.skillPointCost = 2;
		this.maximumLevel = 2;

		this.createItemStack(Material.BLAZE_POWDER);
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		if(this.isOnCooldown())
			return;
		
		if(!e.getDamager().equals(this.player))
			return;
		
		if(!plugin.getStatusManager().contains(player, Berserker.getEnraged())) {
			return;
		}
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		for (Ability a : plugin.getCooldownManager().getAllCooldownsOfPlayer(player)) {
			if (fplayer.getClassAbilities().contains(a)) {
				double cooldown = a.getCooldownContainer().getCooldownTime();
				double reductionPerTick = a.getCooldownContainer().getReductionPerTick();

				a.triggerCooldown(cooldown - cooldownReduction, reductionPerTick);
			}
		}
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Deal damage while Enraged";
	}

	@Override
	public String getDescription() {
		return "Dealing damage while &dEnraged &rreduces your class cooldowns by &6"
				+ AbilityUtils.doubleRoundToXDecimals(cooldownReduction / 20.0, 2) + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.cooldownReduction = 0.5 * this.currentLevel * 20;
	}

}
