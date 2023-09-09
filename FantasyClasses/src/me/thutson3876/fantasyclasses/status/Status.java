package me.thutson3876.fantasyclasses.status;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class Status {

	private final LivingEntity host;
	private final Entity applicator;
	private final StatusType type;
	private double initialDuration;
	private double remainingDuration;
	private double tickRate;
	
	private int stacks = 1;
	private StatusData data;
	
	public Status(StatusType type, LivingEntity host, Entity applicator, int stacks, double duration, StatusData data) {
		this.type = type;
		this.host = host;
		this.applicator = applicator;
		this.setTickRate(type.getTickRate());
		
		this.setStacks(stacks);
		
		initialDuration = duration;
		remainingDuration = duration;
		
		this.setData(data);
	}
	
	public void tick() {
		durationCountdownTick();
		
		if(this.remainingDuration % this.tickRate <= 0.05)
			this.type.effectTick(host, remainingDuration, stacks);
	}
	
	private void durationCountdownTick() {
		this.remainingDuration -= 1;
	}

	public double getInitialDuration() {
		return initialDuration;
	}
	
	public double getRemainingDuration() {
		return remainingDuration;
	}

	public void setRemainingDuration(double remainingDuration) {
		this.remainingDuration = remainingDuration;
	}

	public StatusType getType() {
		return type;
	}

	public LivingEntity getHost() {
		return host;
	}

	public Entity getApplicator() {
		return applicator;
	}

	public double getTickRate() {
		return tickRate;
	}

	public void setTickRate(double tickRate) {
		if(tickRate < 0.05)
			tickRate = 0.05;
		
		this.tickRate = tickRate;
	}

	public int getStacks() {
		return stacks;
	}

	public void setStacks(int stacks) {
		if(stacks < 0)
			stacks = 0;
		else if(stacks > type.getMaxStacks())
			stacks = type.getMaxStacks();
		
		this.stacks = stacks;
	}

	public StatusData getData() {
		return data;
	}

	public void setData(StatusData data) {
		this.data = data;
	}

	
}
