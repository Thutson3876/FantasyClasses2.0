package me.thutson3876.fantasyclasses.status;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.events.ApplyStatusEvent;
import me.thutson3876.fantasyclasses.events.RemoveStatusEvent;

public class StatusType implements Listener {

	protected static StatusManager statusManager = FantasyClasses.getPlugin().getStatusManager();
	
	private final String name;
	
	private final double initialDuration;
	private final double tickRate;
	private final int maxStacks;
	
	private final StatusEffect tickEffect;
	private final StatusEffect applicationEffect;
	
	private boolean additiveStacks = false;
	
	private double duration;
	
	protected StatusType(String name, double tickRate, int maxStacks, StatusEffect tickEffect, StatusEffect applicationEffect) {
		this.name = name;
		this.initialDuration = duration;
		this.tickRate = tickRate;
		this.tickEffect = tickEffect;
		this.applicationEffect = applicationEffect;
		
		this.maxStacks = maxStacks;
	}
	
	protected StatusType(String name, double tickRate, int maxStacks, StatusEffect tickEffect, StatusEffect applicationEffect, boolean additiveStacks) {
		this.name = name;
		this.initialDuration = duration;
		this.tickRate = tickRate;
		this.tickEffect = tickEffect;
		this.applicationEffect = applicationEffect;
		
		this.maxStacks = maxStacks;
		
		this.additiveStacks = additiveStacks;
	}
	
	private void init() {
		FantasyClasses.getPlugin().registerEvents(this);
	}
	
	/*private void deInit() {
		HandlerList.unregisterAll(this);
	}*/
	
	@EventHandler
	public void onEntityDeathEvent(EntityDeathEvent e) {
		if(statusManager.contains(e.getEntity(), this)) {
			statusManager.remove(e.getEntity(), this);
		}
	}
	
	public Status apply(Entity applicator, LivingEntity host, int stacks, double duration, ApplyCause cause) {
		if(host == null)
			return null;
		
		StatusManager manager = statusManager;
		Status currentStatus = manager.get(host, this.getClass());
		int stackAmt = stacks;
		
		if(currentStatus != null) {
			if(this.additiveStacks)
				stackAmt += currentStatus.getStacks();
				
			currentStatus.getType().remove(host, RemoveCause.REPLACED);
		}
		
		ApplyStatusEvent applyEvent = new ApplyStatusEvent(this, host, applicator, stackAmt, duration, cause);
		
		if(host.isDead())
			applyEvent.setCancelled(true);
		
		Bukkit.getPluginManager().callEvent(applyEvent);
		
		if(applyEvent.isCancelled())
			return null;
		
		Status status = applyEvent.getStatus();
		statusManager.add(status);
		
		init();
		if(this.applicationEffect != null)
			applicationEffect.run(host, duration, stackAmt);
		return status;
	}
	
	public boolean remove(LivingEntity host, RemoveCause cause) {
		Status toRemove = statusManager.get(host, this.getClass());
		if(toRemove == null)
			return false;
		
		RemoveStatusEvent removeEvent = new RemoveStatusEvent(toRemove, host, null, cause);
		Bukkit.getPluginManager().callEvent(removeEvent);
		
		if(removeEvent.isCancelled())
			return false;
		
		//unregisters listener; causes 
		//deInit();
		statusManager.remove(host, this);
		return true;
	}
	
	public boolean remove(LivingEntity host, RemoveCause cause, Entity dispeller) {
		Status toRemove = statusManager.get(host, this.getClass());
		if(toRemove == null)
			return false;
		
		RemoveStatusEvent removeEvent = new RemoveStatusEvent(toRemove, host, dispeller, cause);
		Bukkit.getPluginManager().callEvent(removeEvent);
		
		if(removeEvent.isCancelled())
			return false;
		
		//deInit();
		statusManager.remove(host, this);
		return true;
	}
	
	public void effectTick(LivingEntity host, double duration, int stacks) {
		if(this.tickEffect != null)
			tickEffect.run(host, duration, stacks);
	}
	
	public String getName() {
		return name;
	}
	
	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public double getInitialDuration() {
		return initialDuration;
	}

	public double getTickRate() {
		return tickRate;
	}

	public StatusEffect getTickEffect() {
		return tickEffect;
	}
	
	public StatusEffect getApplicationEffect() {
		return applicationEffect;
	}

	public int getMaxStacks() {
		return maxStacks;
	}

	public boolean isAdditiveStacks() {
		return additiveStacks;
	}

	public void setAdditiveStacks(boolean additiveStacks) {
		this.additiveStacks = additiveStacks;
	}
}
