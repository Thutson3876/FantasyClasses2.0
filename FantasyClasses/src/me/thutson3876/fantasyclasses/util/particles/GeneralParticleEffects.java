package me.thutson3876.fantasyclasses.util.particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.Validate;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.util.geometry.Sphere;

public class GeneralParticleEffects {

	private static final double DEFAULT_MAX_ROTATION = 6.3;
	
	private static Map<UUID, Integer> tickStorage = new HashMap<>();

	public static double getDefaultMaxRotation() {
		return DEFAULT_MAX_ROTATION;
	}
	
	private static boolean isPastMaxDuration(int tick, int tickRate, int maxDuration) {
		return (tick * tickRate) >= maxDuration;
	}
	
	private static boolean isPastMaxRotation(int tick, double maxRotation) {
		return tick >= maxRotation;
	}
	
	public static void trail(Entity ent, CustomParticle particle, int maxDuration, int tickRate) {
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);

		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				
				if(isPastMaxDuration(tick, tickRate, maxDuration)) {
					tickStorage.remove(key);
					this.cancel();
					return;
				}
				
				particle.spawn(ent.getLocation());
			}
			
		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);
	}

	public static void helix(Entity ent, CustomParticle particle, double radius, double maxRotation, int maxDuration, int tickRate,
			double yScaling) {
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);

		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				if (!isPastMaxRotation(tick, maxRotation) && !isPastMaxDuration(tick, tickRate, maxDuration)) {
					double x = radius * Math.cos(tick);
					double z = radius * Math.sin(tick);

					Location spawnLoc = ent.getLocation();
					spawnLoc.add(x, tick * yScaling, z);
					particle.spawn(spawnLoc);
					
					tickStorage.put(key, tick + 1);
					return;
				}

				tickStorage.remove(key);
				this.cancel();
			}

		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);
	}
	

	public static void helix(Location loc, CustomParticle particle, double radius, double maxRotation, int maxDuration, int tickRate,
			double yScaling) {
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);

		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				if (!isPastMaxRotation(tick, maxRotation) && !isPastMaxDuration(tick, tickRate, maxDuration)) {
					double x = radius * Math.cos(tick);
					double z = radius * Math.sin(tick);

					Location spawnLoc = loc;
					spawnLoc.add(x, tick * yScaling, z);
					particle.spawn(spawnLoc);
					
					tickStorage.put(key, tick + 1);
					return;
				}

				tickStorage.remove(key);
				this.cancel();
			}

		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);
	}
	
	public static void deltaRadiusHelix(Entity ent, CustomParticle particle, double startRadius, double endRadius, double maxRotation, int maxDuration, int tickRate,
			double yScaling) {
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);

		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				double radiusTick = (startRadius - endRadius) * (tick / maxRotation);
				double radius = startRadius + radiusTick;
				
				if (!isPastMaxRotation(tick, maxRotation) && !isPastMaxDuration(tick, tickRate, maxDuration)) {
					double x = radius * Math.cos(tick);
					double z = radius * Math.sin(tick);

					Location spawnLoc = ent.getLocation();
					spawnLoc.add(x, tick * yScaling, z);
					particle.spawn(spawnLoc);
					
					tickStorage.put(key, tick + 1);
					return;
				}

				tickStorage.remove(key);
				this.cancel();
			}

		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);
	}
	
	public static void deltaRadiusHelix(Location loc, CustomParticle particle, double startRadius, double endRadius, double maxRotation, int maxDuration, int tickRate,
			double yScaling) {
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);

		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				double radiusTick = (startRadius - endRadius) * (tick / maxRotation);
				double radius = startRadius + radiusTick;
				
				if (!isPastMaxRotation(tick, maxRotation) && !isPastMaxDuration(tick, tickRate, maxDuration)) {
					double x = radius * Math.cos(tick);
					double z = radius * Math.sin(tick);

					Location spawnLoc = loc;
					spawnLoc.add(x, tick * yScaling, z);
					particle.spawn(spawnLoc);
					
					tickStorage.put(key, tick + 1);
					return;
				}

				tickStorage.remove(key);
				this.cancel();
			}

		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);
	}
	
	public static void durationBasedDeltaRadiusHelix(Location loc, CustomParticle particle, double startRadius, double endRadius, double maxRotation, int maxDuration, int tickRate,
			double yScaling) {
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);

		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				double radiusTick = (startRadius - endRadius) * (tick / maxRotation);
				double radius = startRadius + radiusTick;
				
				if (!isPastMaxRotation(tick, maxRotation) && !isPastMaxDuration(tick, tickRate, maxDuration)) {
					double x = radius * Math.cos(tick);
					double z = radius * Math.sin(tick);

					Location spawnLoc = loc;
					spawnLoc.add(x, tick * yScaling, z);
					particle.spawn(spawnLoc);
					
					tickStorage.put(key, tick + 1);
					return;
				}

				tickStorage.remove(key);
				this.cancel();
			}

		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);
	}

	// works poorly
	public static void lateralHelix(Entity ent, CustomParticle particle, double radius, double maxRotation, int maxDuration, int tickRate,
			double xScaling, double zScaling) {
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);

		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				if (!isPastMaxRotation(tick, maxRotation) && !isPastMaxDuration(tick, tickRate, maxDuration)) {
					double x = radius * Math.cos(tick) * xScaling;
					double y = radius * Math.sin(tick);
					double z = radius * Math.cos(tick) * zScaling;

					Location spawnLoc = ent.getLocation();
					spawnLoc.add(x, y, z);
					particle.spawn(spawnLoc);
					
					tickStorage.put(key, tick + 1);
					return;
				}

				tickStorage.remove(key);
				this.cancel();
			}

		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);
	}
	
	public static void durationBasedHelix(Location loc, CustomParticle particle, double radius, double maxRotation, int maxDuration, int tickRate,
			double yScaling) {
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);

		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				if (!isPastMaxDuration(tick, tickRate, maxDuration)) {
					if(isPastMaxRotation(tick, maxRotation)) {
						tick = 0;
					}
					double x = radius * Math.cos(tick);
					double z = radius * Math.sin(tick);

					Location spawnLoc = loc;
					spawnLoc.add(x, tick * yScaling, z);
					particle.spawn(spawnLoc);
					
					tickStorage.put(key, tick + 1);
					return;
				}

				tickStorage.remove(key);
				this.cancel();
			}

		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);
	}

	public static void twirlingRing(Entity ent, CustomParticle particle, double radius, double maxRotation, int maxDuration, int tickRate) {
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);

		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				if (!isPastMaxRotation(tick, maxRotation) && !isPastMaxDuration(tick, tickRate, maxDuration)) {
					double x = radius * Math.cos(tick);
					double z = radius * Math.sin(tick);

					Location spawnLoc = ent.getLocation();
					spawnLoc.add(x, 0, z);
					particle.spawn(spawnLoc);
					
					tickStorage.put(key, tick + 1);
					return;
				}

				tickStorage.remove(key);
				this.cancel();
			}

		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);
	}
	
	public static void twirlingRing(Location loc, CustomParticle particle, double radius, double maxRotation, int maxDuration, int tickRate) {
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);

		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				if (!isPastMaxRotation(tick, maxRotation) && !isPastMaxDuration(tick, tickRate, maxDuration)) {
					double x = radius * Math.cos(tick);
					double z = radius * Math.sin(tick);

					Location spawnLoc = loc;
					spawnLoc.add(x, 0, z);
					particle.spawn(spawnLoc);
					
					tickStorage.put(key, tick + 1);
					return;
				}

				tickStorage.remove(key);
				this.cancel();
			}

		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);
	}
	
	public static void deltaRadiusTwirlingRing(Entity ent, CustomParticle particle, double startRadius, double endRadius, double maxRotation, int maxDuration, int tickRate) {
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);

		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				double radiusTick = (startRadius - endRadius) * (tick / maxRotation);
				double radius = startRadius + radiusTick;
				
				if (!isPastMaxRotation(tick, maxRotation) && !isPastMaxDuration(tick, tickRate, maxDuration)) {
					double x = radius * Math.cos(tick);
					double z = radius * Math.sin(tick);

					Location spawnLoc = ent.getLocation();
					spawnLoc.add(x, 0, z);
					particle.spawn(spawnLoc);
					
					tickStorage.put(key, tick + 1);
					return;
				}

				tickStorage.remove(key);
				this.cancel();
			}

		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);
	}
	
	public static void deltaRadiusTwirlingRing(Location loc, CustomParticle particle, double startRadius, double endRadius, double maxRotation, int maxDuration, int tickRate) {
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);

		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				double radiusTick = (startRadius - endRadius) * (tick / maxRotation);
				double radius = startRadius + radiusTick;
				
				if (!isPastMaxRotation(tick, maxRotation) && !isPastMaxDuration(tick, tickRate, maxDuration)) {
					double x = radius * Math.cos(tick);
					double z = radius * Math.sin(tick);

					Location spawnLoc = loc;
					spawnLoc.add(x, 0, z);
					particle.spawn(spawnLoc);
					
					tickStorage.put(key, tick + 1);
					return;
				}

				tickStorage.remove(key);
				this.cancel();
			}

		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);
	}

	public static void explosion(Location center, CustomParticle particle, double radius, int tickRate) {
		List<List<Location>> spheres = new ArrayList<>();
		for (int i = 0; i < radius; i++) {
			spheres.add(Sphere.generateSphere(center, i, true));
		}

		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);

		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				if (tick < spheres.size()) {
					for(Location l : spheres.get(tick))
						particle.spawn(l);

					tickStorage.put(key, tick + 1);
					return;
				}

				tickStorage.remove(key);
				this.cancel();
			}

		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);

	}

	public static void drawLine(Location point1, Location point2, CustomParticle particle, double space) {
		World world = point1.getWorld();
		Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
		double distance = point1.distance(point2);
		Vector p1 = point1.toVector();
		Vector p2 = point2.toVector();
		Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
		double length = 0;
		for (; length < distance; p1.add(vector)) {
			Location spawnLoc = new Location(world, p1.getX(), p1.getY(), p1.getZ());
			particle.spawn(spawnLoc);
			length += space;
		}
	}

	public static void pulsingCircle(Location center, CustomParticle particle, double radius, double pulseGapPercent, int maxDuration, int tickRate) {
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);

		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				double tempRadius = pulseGapPercent * radius * tick;
				if (tempRadius <= radius && !isPastMaxDuration(tick, tickRate, maxDuration)) {
					for (Location l : Sphere.generateCircle(center, tempRadius * tick, tickRate, false)) {
						particle.spawn(l);
					}

					tickStorage.put(key, tick + 1);
					return;
				}

				tickStorage.remove(key);
				this.cancel();
			}

		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);
	}
	
	public static void doubleEndedCircle(Entity ent, double radius, int tickRate, double maxRotation, int maxDuration, CustomParticle particle) {
		World world = ent.getWorld();
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				if (!isPastMaxRotation(tick, maxRotation) && !isPastMaxDuration(tick, tickRate, maxDuration)) {
					double x = radius * Math.cos(tick);
					double z = radius * Math.sin(tick);

					Location center = ent.getLocation().add(0, ent.getHeight() / 2.0, 0);
			        Location current1 = new Location(world, center.getX() + x, center.getY(), center.getZ() + z);
			        Location current2 = new Location(world, center.getX() - x, center.getY(), center.getZ() - z);
			        
			        particle.spawn(current1);
			        particle.spawn(current2);
			        
					tickStorage.put(key, tick + 1);
					return;
				}
				
				tickStorage.remove(key);
				this.cancel();
				
			}
		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);
	}
	
	public static void doubleEndedCircle(Location loc, double radius, int tickRate, double maxRotation, int maxDuration, CustomParticle particle) {
		World world = loc.getWorld();
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				if (!isPastMaxRotation(tick, maxRotation) && !isPastMaxDuration(tick, tickRate, maxDuration)) {
					double x = radius * Math.cos(tick);
					double z = radius * Math.sin(tick);

			        Location current1 = new Location(world, loc.getX() + x, loc.getY(), loc.getZ() + z);
			        Location current2 = new Location(world, loc.getX() - x, loc.getY(), loc.getZ() - z);
			        
			        particle.spawn(current1);
			        particle.spawn(current2);
			        
					tickStorage.put(key, tick + 1);
					return;
				}
				
				tickStorage.remove(key);
				this.cancel();
				
			}
		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);
	}
}
