package me.thutson3876.fantasyclasses.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.thutson3876.fantasyclasses.status.ApplyCause;
import me.thutson3876.fantasyclasses.status.Status;
import me.thutson3876.fantasyclasses.status.StatusData;
import me.thutson3876.fantasyclasses.status.StatusType;

public class ApplyStatusEvent extends Event implements Cancellable{

	private boolean isCancelled = false;

	private static final HandlerList handlers = new HandlerList();
	
	private final Status status;
	private final LivingEntity target;
	private final Entity applier;
	private final ApplyCause cause;
	
	public ApplyStatusEvent(StatusType statusType, LivingEntity target, Entity applier, int stacks, double duration, ApplyCause cause) {
		this.target = target;
		this.applier = applier;
		this.cause = cause;
		
		this.status = new Status(statusType, target, applier, stacks, duration, new StatusData());
	}
	
	public Status getStatus() {
		return status;
	}

	public LivingEntity getTarget() {
		return target;
	}

	public Entity getApplier() {
		return applier;
	}
	
	public ApplyCause getCause() {
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
