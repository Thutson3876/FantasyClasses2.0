package me.thutson3876.fantasyclasses.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HealEvent extends Event implements Cancellable {

	private boolean isCancelled = false;
	
	private static final HandlerList handlers = new HandlerList();
	private final LivingEntity healer;
	private final LivingEntity target;
	private double healAmt;
	private final double initialHealAmt;
	
	public HealEvent(LivingEntity healer, double healAmt, LivingEntity target) {
		this.healer = healer;
		this.healAmt = healAmt;
		this.initialHealAmt = healAmt;
		this.target = target;
	}
	
	/**
	 * @return the healAmt
	 */
	public double getHealAmt() {
		return healAmt;
	}

	/**
	 * @param healAmt the healAmt to set
	 */
	public void setHealAmt(double healAmt) {
		this.healAmt = healAmt;
	}

	/**
	 * @return the healer
	 */
	public LivingEntity getHealer() {
		return healer;
	}

	/**
	 * @return the target
	 */
	public LivingEntity getTarget() {
		return target;
	}

	/**
	 * @return the initialHealAmt
	 */
	public double getInitialHealAmt() {
		return initialHealAmt;
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
