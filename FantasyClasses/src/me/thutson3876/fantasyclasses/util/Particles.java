package me.thutson3876.fantasyclasses.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.Validate;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.FantasyClasses;

public class Particles {

	private static Map<UUID, Integer> tickStorage = new HashMap<>();

	public static void helix(Entity ent, Particle particle, double radius, double maxRotation, int tickRate,
			double yScaling) {
		World world = ent.getWorld();
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);

		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				if (tick < maxRotation) {
					double x = radius * Math.cos(tick);
					double z = radius * Math.sin(tick);

					world.spawnParticle(particle, ent.getLocation().getX() + x,
							ent.getLocation().getY() + (tick * yScaling), ent.getLocation().getZ() + z, 1);
					tickStorage.put(key, tick + 1);
					return;
				}

				tickStorage.remove(key);
				this.cancel();
			}

		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);
	}

	public static void helix(Location loc, Particle particle, double radius, double maxRotation, int tickRate,
			double yScaling) {
		World world = loc.getWorld();
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);

		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				if (tick < maxRotation) {
					double x = radius * Math.cos(tick);
					double z = radius * Math.sin(tick);

					world.spawnParticle(particle, loc.getX() + x, loc.getY() + (tick * yScaling), loc.getZ() + z, 1);
					tickStorage.put(key, tick + 1);
					return;
				}

				tickStorage.remove(key);
				this.cancel();
			}

		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);
	}

	// works poorly
	public static void lateralHelix(Entity ent, Particle particle, double radius, double maxRotation, int tickRate,
			double xScaling, double zScaling) {
		World world = ent.getWorld();
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);

		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				if (tick < maxRotation) {
					double x = radius * Math.cos(tick) * xScaling;
					double y = radius * Math.sin(tick);
					double z = radius * Math.cos(tick);

					world.spawnParticle(particle, ent.getLocation().getX() + x, ent.getLocation().getY() + y,
							ent.getLocation().getZ() + z, 1);
					tickStorage.put(key, tick + 1);
					return;
				}

				tickStorage.remove(key);
				this.cancel();
			}

		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);
	}

	public static void twirlingRing(Entity ent, Particle particle, double radius, double maxRotation, int tickRate) {
		World world = ent.getWorld();
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);

		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				if (tick < maxRotation) {
					double x = radius * Math.cos(tick);
					double z = radius * Math.sin(tick);

					world.spawnParticle(particle, ent.getLocation().getX() + x, ent.getLocation().getY(),
							ent.getLocation().getZ() + z, 1);
					tickStorage.put(key, tick + 1);
					return;
				}

				tickStorage.remove(key);
				this.cancel();
			}

		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);
	}

	public static void twirlingRing(Location loc, Particle particle, double radius, double maxRotation, int tickRate) {
		World world = loc.getWorld();
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);

		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				if (tick < maxRotation) {
					double x = radius * Math.cos(tick);
					double z = radius * Math.sin(tick);

					world.spawnParticle(particle, loc.getX() + x, loc.getY(), loc.getZ() + z, 1);
					tickStorage.put(key, tick + 1);
					return;
				}

				tickStorage.remove(key);
				this.cancel();
			}

		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);
	}

	public static void explosion(Location center, Particle particle, double radius, int tickRate) {
		List<List<Location>> spheres = new ArrayList<>();
		for (int i = 0; i < radius; i++) {
			spheres.add(Sphere.generateSphere(center, i, true));
		}

		World world = center.getWorld();
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);

		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				if (tick < spheres.size()) {
					for(Location l : spheres.get(tick))
						world.spawnParticle(particle, l, 1);

					tickStorage.put(key, tick + 1);
					return;
				}

				tickStorage.remove(key);
				this.cancel();
			}

		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);

	}

	public static void drawLine(Location point1, Location point2, double space) {
		World world = point1.getWorld();
		Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
		double distance = point1.distance(point2);
		Vector p1 = point1.toVector();
		Vector p2 = point2.toVector();
		Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
		double length = 0;
		for (; length < distance; p1.add(vector)) {
			world.spawnParticle(Particle.SONIC_BOOM, p1.getX(), p1.getY(), p1.getZ(), 1);
			length += space;
		}
	}

	public static void pulsingCircle(Location center, double radius, double pulseGapPercent, int tickRate,
			Particle particle) {
		World world = center.getWorld();
		UUID key = UUID.randomUUID();
		tickStorage.put(key, 0);

		new BukkitRunnable() {

			@Override
			public void run() {
				int tick = tickStorage.get(key);
				double tempRadius = pulseGapPercent * radius * tick;
				if (tempRadius <= radius) {
					for (Location l : Sphere.generateCircle(center, tempRadius * tick, tickRate, false)) {
						world.spawnParticle(particle, l, 1);
					}

					tickStorage.put(key, tick + 1);
					return;
				}

				tickStorage.remove(key);
				this.cancel();
			}

		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);
	}
}
