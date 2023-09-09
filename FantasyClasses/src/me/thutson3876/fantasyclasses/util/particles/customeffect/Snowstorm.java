package me.thutson3876.fantasyclasses.util.particles.customeffect;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import me.thutson3876.fantasyclasses.util.particles.CustomParticle;

public class Snowstorm extends CustomEffect {

	private CustomParticle snow;

	public Snowstorm(double radius, int maxDuration, int tickRate) {
		super(new CustomParticle(Particle.REDSTONE, 1, 0, 0.2, Color.WHITE), radius / 2D, radius * 1.5, 12.6,
				maxDuration, tickRate, -0.1 * radius, true);
		
		snow = new CustomParticle(Particle.SNOWFLAKE, 10 + (int)(3 * radius), 0);
	}

	@Override
	protected void tick(Entity ent, int currentTick) {
		tick(ent.getLocation(), currentTick);
	}

	@Override
	protected void tick(Location loc, int currentTick) {
		double rotationTick = this.rotationTick(currentTick);
		double radiusTick = deltaRadius * (rotationTick / maxRotation);
		double radius = this.radius + radiusTick;

		double x = radius * Math.sin(rotationTick);
		double z = radius * Math.cos(rotationTick);

		double x2 = radius * Math.cos(rotationTick);
		double z2 = radius * Math.sin(rotationTick);

		Location spawnLoc1 = loc.clone();
		Location spawnLoc2 = loc.clone();

		spawnLoc1.add(x, this.radius + rotationTick * scaling, z);
		spawnLoc2.add(x2, this.radius + rotationTick * scaling, z2);

		if(currentTick % 2 == 0)
			snow.spawn(loc);

	}
}
