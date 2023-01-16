package me.thutson3876.fantasyclasses.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;

public class AbilityTriggerEvent extends Event implements Cancellable {

	private boolean isCancelled = false;

	private static final HandlerList handlers = new HandlerList();
	private final FantasyPlayer fplayer;
	private final Ability ability;
	private int cooldown;
	private double cooldownReductionPerTick = 1.00;

	public AbilityTriggerEvent(FantasyPlayer fplayer, Ability ability) {
		this.fplayer = fplayer;
		this.ability = ability;
		cooldown = ability.getCooldown();
	}

	public AbilityTriggerEvent(FantasyPlayer fplayer, Ability ability, int cooldown, double cooldownReductionPerTick) {
		this.fplayer = fplayer;
		this.ability = ability;
		this.cooldown = cooldown;
		this.cooldownReductionPerTick = cooldownReductionPerTick;
	}

	/**
	 * @return the cooldown
	 */
	public int getCooldown() {
		return cooldown;
	}

	/**
	 * @param cooldown the cooldown to set
	 */
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public double getCooldownReductionPerTick() {
		return cooldownReductionPerTick;
	}

	public void setCooldownReductionPerTick(double cooldownReductionPerTick) {
		this.cooldownReductionPerTick = cooldownReductionPerTick;
	}

	/**
	 * @return the fplayer
	 */
	public FantasyPlayer getFplayer() {
		return fplayer;
	}

	/**
	 * @return the ability
	 */
	public Ability getAbility() {
		return ability;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
