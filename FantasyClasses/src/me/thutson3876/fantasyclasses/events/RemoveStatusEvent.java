package me.thutson3876.fantasyclasses.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.thutson3876.fantasyclasses.status.RemoveCause;
import me.thutson3876.fantasyclasses.status.Status;

public class RemoveStatusEvent extends Event implements Cancellable {
	private boolean isCancelled = false;

	private static final HandlerList handlers = new HandlerList();

	private final Status status;
	private final LivingEntity target;
	private final Entity dispeller;
	private final RemoveCause cause;

	public RemoveStatusEvent(Status status, LivingEntity target, Entity dispeller, RemoveCause cause) {
		this.target = target;
		this.status = status;
		this.dispeller = dispeller;
		this.cause = cause;
	}

	public Status getStatus() {
		return status;
	}

	public LivingEntity getTarget() {
		return target;
	}

	public Entity getDispeller() {
		return dispeller;
	}

	public RemoveCause getCause() {
		return cause;
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
