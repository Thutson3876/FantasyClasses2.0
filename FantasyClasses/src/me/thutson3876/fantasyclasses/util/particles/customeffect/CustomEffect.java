package me.thutson3876.fantasyclasses.util.particles.customeffect;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.util.particles.CustomParticle;

public abstract class CustomEffect {

	protected static final FantasyClasses plugin = FantasyClasses.getPlugin();
	
	protected CustomParticle particle;
	
	protected boolean isDurationBased = false;
	
	protected double maxRotation;
	
	protected int maxDuration;
	
	protected double radius;
	
	protected double deltaRadius;
	
	protected int tickRate;
	
	protected double scaling;
	
	private Map<UUID, Integer> tickStorage = new HashMap<>();
	
	protected CustomEffect(CustomParticle particle, double radius, double deltaRadius, double maxRotation, int maxDuration, int tickRate, double scaling, boolean isDurationBased) {
		this.particle = particle;
		this.radius = radius;
		this.deltaRadius = deltaRadius;
		this.maxRotation = maxRotation;
		this.maxDuration = maxDuration;
		this.tickRate = tickRate;
		this.scaling = scaling;
		this.isDurationBased = isDurationBased;
	}
	
	protected boolean isPastMaxDuration(UUID uuid) {
		return (tickStorage.get(uuid) * tickRate) >= maxDuration;
	}
	
	protected boolean isPastMaxRotation(UUID uuid) {
		return tickStorage.get(uuid) >= maxRotation;
	}
	
	protected boolean shouldContinueRunning(UUID uuid) {
		if(tickStorage.get(uuid) <= -99) {
			return false;
		}
		
		if(isDurationBased) {
			return !isPastMaxDuration(uuid);
		}
		
		return !isPastMaxRotation(uuid);
	}
	
	protected int rotationTick(int currentTick) {
		return (int) Math.round(currentTick % maxRotation);
	}
	
	public CustomParticle getParticle() {
		return particle;
	}

	public void setParticle(CustomParticle particle) {
		this.particle = particle;
	}

	public boolean isDurationBased() {
		return isDurationBased;
	}

	public void setDurationBased(boolean isDurationBased) {
		this.isDurationBased = isDurationBased;
	}

	public double getMaxRotation() {
		return maxRotation;
	}

	public void setMaxRotation(double maxRotation) {
		this.maxRotation = maxRotation;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getDeltaRadius() {
		return deltaRadius;
	}

	public void setDeltaRadius(double deltaRadius) {
		this.deltaRadius = deltaRadius;
	}

	public int getTickRate() {
		return tickRate;
	}

	public void setTickRate(int tickRate) {
		this.tickRate = tickRate;
	}

	public double getScaling() {
		return scaling;
	}

	public void setScaling(double scaling) {
		this.scaling = scaling;
	}
	
	public void cancel(UUID uuid) {
		tickStorage.put(uuid, -100);
	}

	protected abstract void tick(Entity ent, int currentTick);
	
	protected abstract void tick(Location loc, int currentTick);
	
	public UUID run(Entity ent) {
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				if(ent.isDead()) {
					this.cancel();
					return;
				}
				
				if(shouldContinueRunning(key)) {
					tick(ent, tick);
					
					tickStorage.put(key, tick + 1);
					return;
				}
				
				tickStorage.remove(key);
				this.cancel();
			}
			
		}.runTaskTimer(plugin, 1, tickRate);
		
		return key;
	}
	
	public UUID run(Location loc) {
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				
				if(shouldContinueRunning(key)) {
					tick(loc, tick);
					
					tickStorage.put(key, tick + 1);
					return;
				}
				
				tickStorage.remove(key);
				this.cancel();
			}
			
		}.runTaskTimer(plugin, 1, tickRate);
		
		return key;
	}
	
}
