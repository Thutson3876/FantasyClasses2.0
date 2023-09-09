package me.thutson3876.fantasyclasses.util.particles.vanity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.particles.CustomParticle;
import me.thutson3876.fantasyclasses.util.particles.DirectionalParticle;

public abstract class Vanity {
	
	protected static final FantasyClasses plugin = FantasyClasses.getPlugin();
	
	protected static final Random rng = new Random();
	
	protected int tickRate;
	
	protected double scaling;
	
	protected int duration;
	
	protected double spawnChance = 0.7;
	
	private Map<UUID, Integer> tickStorage = new HashMap<>();
	private Map<UUID, Location> prevLocStorage = new HashMap<>();
	
	protected Vanity(int tickRate, double scaling, int duration) {
		this.tickRate = tickRate;
		this.scaling = scaling;
		this.duration = duration;
	}
	
	protected Vanity(int tickRate, double scaling, int duration, double spawnChance) {
		this.tickRate = tickRate;
		this.scaling = scaling;
		this.duration = duration;
		this.spawnChance = spawnChance;
	}
	
	protected void spawnDirectionWithMovement(CustomParticle particle, LivingEntity ent, UUID key, Location spawnLoc) {
		if(rng.nextDouble() > this.spawnChance)
			return;
		
		if(!DirectionalParticle.contains(particle.getParticleType())) {
			particle.spawn(spawnLoc);
			return;
		}
		
		Location prevLoc = prevLocStorage.get(key);
		Location currentLoc = ent.getLocation();
		if(prevLoc == null || currentLoc == null || currentLoc.distance(prevLoc) < 0.01) {
			particle.spawn(spawnLoc);
			return;
		}
		if(rng.nextDouble() < this.spawnChance * 0.2) {
			Vector direction = AbilityUtils.getVectorBetween2Points(prevLoc, currentLoc, 0.5);
			particle.directionalSpawn(spawnLoc, direction, direction.length());
		}
	}
	
	protected boolean isPastDuration(UUID uuid) {
		return tickStorage.get(uuid) * tickRate > duration;
	}
	
	protected boolean shouldContinueRunning(UUID uuid) {
		return tickStorage.get(uuid) > -99;
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
	
	public int getDuration() {
		return tickRate;
	}

	public void setDuration(int tickRate) {
		this.tickRate = tickRate;
	}
	
	public void cancel(UUID uuid) {
		tickStorage.put(uuid, -100);
	}
	
	protected abstract void tickDefault(UUID key, LivingEntity ent, int currentTick);
	
	protected abstract void tickWhileCrouching(UUID key, LivingEntity ent, int currentTick);
	
	protected abstract void tickWhileGliding(UUID key, LivingEntity ent, int currentTick);
	
	protected void tick(UUID key, LivingEntity ent, int currentTick) {
		if(ent instanceof Player) {
			Player p = (Player)ent;
			if(p.isGliding() || p.isRiptiding() || p.isSwimming()) {
				tickWhileGliding(key, ent, currentTick);
				return;
			}
			else if(p.isSneaking()) {
				tickWhileCrouching(key, ent, currentTick);
				return;
			}
			
		}
		
		tickDefault(key, ent, currentTick);
	}
	
	public UUID run(LivingEntity ent) {
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				if(ent.isDead()) {
					return;
				}
				
				if(shouldContinueRunning(key)) {
					if(isPastDuration(key)) {
						tickStorage.put(key, 0);
						tick = 0;
					}
						
					
					tick(key, ent, tick);
					
					tickStorage.put(key, tick + 1);
					prevLocStorage.put(key, ent.getLocation());
					return;
				}
				
				tickStorage.remove(key);
				prevLocStorage.remove(key);
				this.cancel();
			}
			
		}.runTaskTimer(plugin, 1, tickRate);
		
		return key;
	}
	
	protected int getTick(UUID key) {
		return this.tickStorage.get(key);
	}
	
	protected Location getPrevLoc(UUID key) {
		return this.prevLocStorage.get(key);
	}
}
