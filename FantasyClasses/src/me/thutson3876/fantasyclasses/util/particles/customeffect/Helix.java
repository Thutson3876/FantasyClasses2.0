package me.thutson3876.fantasyclasses.util.particles.customeffect;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.util.particles.CustomParticle;

public class Helix extends CustomEffect {

	private boolean isReversed = false;
	private boolean isDirectional = false;
	
	public Helix(CustomParticle particle, double radius, double deltaRadius, double maxRotation, int maxDuration,
			int tickRate, double scaling, boolean isDurationBased, boolean isReversed, boolean isDirectional) {
		super(particle, radius, deltaRadius, maxRotation, maxDuration, tickRate, scaling, isDurationBased);
		
		this.setDirectional(isDirectional);
		this.isReversed = isReversed;
	}
	
	public Helix(CustomParticle particle, double radius, double deltaRadius, double maxRotation, int maxDuration,
			int tickRate, double scaling, boolean isDurationBased) {
		super(particle, radius, deltaRadius, maxRotation, maxDuration, tickRate, scaling, isDurationBased);
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
		
		double x = isReversed ? radius * Math.cos(rotationTick) : radius * Math.sin(rotationTick);
		double z = isReversed ? radius * Math.sin(rotationTick) : radius * Math.cos(rotationTick);

		Location spawnLoc = loc;
		spawnLoc.add(x, currentTick * scaling, z);
		
		if(isDirectional) {
			double prevX = isReversed ? radius * Math.cos(rotationTick - 1) : radius * Math.sin(rotationTick - 1);
			double prevZ = isReversed ? radius * Math.sin(rotationTick - 1) : radius * Math.cos(rotationTick - 1);
			
			Vector previousPoint = new Vector(prevX, (rotationTick - 1) * scaling, prevZ);
			Vector currentPoint = new Vector(x, rotationTick * scaling, z);
			
			Vector direction = currentPoint.subtract(previousPoint);
			particle.directionalSpawn(spawnLoc, direction, 0.9 / ((double)tickRate));
		}
		else
			particle.spawn(spawnLoc);
	}

	public boolean isReversed() {
		return isReversed;
	}

	public void setReversed(boolean isReversed) {
		this.isReversed = isReversed;
	}

	public boolean isDirectional() {
		return isDirectional;
	}

	public void setDirectional(boolean isDirectional) {
		this.isDirectional = isDirectional;
	}

}
