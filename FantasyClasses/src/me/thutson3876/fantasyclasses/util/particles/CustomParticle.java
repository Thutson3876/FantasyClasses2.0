package me.thutson3876.fantasyclasses.util.particles;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.util.Vector;

public class CustomParticle {

	private final Particle particleType;
	private int count;
	private double xOffset;
	private double yOffset;
	private double zOffset;
	private double extra;
	private Color color;
	
	public CustomParticle(Particle particleType, int count, double xOffset, double yOffset, double zOffset, double extra, Color color) {
		this.particleType = particleType;
		this.count = count;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.zOffset = zOffset;
		this.extra = extra;
		this.color = color;
	}
	
	public CustomParticle(Particle particleType, int count, double offset, double extra, Color color) {
		this.particleType = particleType;
		this.count = count;
		this.xOffset = offset;
		this.yOffset = offset;
		this.zOffset = offset;
		this.extra = extra;
		this.color = color;
	}
	
	public CustomParticle(Particle particleType, int count, double offset, Color color) {
		this.particleType = particleType;
		this.count = count;
		this.xOffset = offset;
		this.yOffset = offset;
		this.zOffset = offset;
		this.extra = 0;
		this.color = color;
	}
	
	public CustomParticle(Particle particleType, int count, double offset) {
		this.particleType = particleType;
		this.count = count;
		this.xOffset = offset;
		this.yOffset = offset;
		this.zOffset = offset;
		this.extra = 0;
		this.color = null;
	}
	
	public CustomParticle(Particle particleType, double offset) {
		this.particleType = particleType;
		this.count = 1;
		this.xOffset = offset;
		this.yOffset = offset;
		this.zOffset = offset;
		this.extra = 0;
		this.color = null;
	}
	
	public CustomParticle(Particle particleType) {
		this.particleType = particleType;
		this.count = 1;
		this.xOffset = 0;
		this.yOffset = 0;
		this.zOffset = 0;
		this.extra = 0;
		this.color = null;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}


	public double getxOffset() {
		return xOffset;
	}


	public void setxOffset(double xOffset) {
		this.xOffset = xOffset;
	}


	public double getyOffset() {
		return yOffset;
	}


	public void setyOffset(double yOffset) {
		this.yOffset = yOffset;
	}


	public double getzOffset() {
		return zOffset;
	}


	public void setzOffset(double zOffset) {
		this.zOffset = zOffset;
	}


	public double getExtra() {
		return extra;
	}


	public void setExtra(double extra) {
		this.extra = extra;
	}


	public Color getColor() {
		return color;
	}


	public void setColor(Color color) {
		this.color = color;
	}


	public Particle getParticleType() {
		return particleType;
	}

	public void spawn(Location loc) {
		if(Particle.REDSTONE.equals(particleType)) {
			redstoneSpawn(loc);
		}
		else if(DirectionalParticle.contains(particleType)) {
			directionalSpawn(loc);
		}
		else if(Particle.SPELL_MOB.equals(particleType) || Particle.SPELL_MOB_AMBIENT.equals(particleType)) {
			spellMobSpawn(loc);
		}
		else {
			defaultSpawn(loc);
		}
		
	}
	
	private void defaultSpawn(Location loc) {
			loc.getWorld().spawnParticle(particleType, loc, count, xOffset, yOffset, zOffset, extra);
	}
	
	public void directionalSpawn(Location loc, Vector direction, double speed) {
		loc.getWorld().spawnParticle(particleType, loc, 0, direction.getX(), direction.getY(), direction.getZ(), speed);
	}
	
	public void redstoneSpawn(Location loc, float size) {
		DustOptions dustOptions = new DustOptions(color, size);
		loc.getWorld().spawnParticle(particleType, loc, count, xOffset, yOffset, zOffset, dustOptions);
	}
	
	//extra is proportional to size
	private void redstoneSpawn(Location loc) {
		DustOptions dustOptions = new DustOptions(color, (float)extra);
		loc.getWorld().spawnParticle(particleType, loc, count, xOffset, yOffset, zOffset, dustOptions);
	}
	
	private void directionalSpawn(Location loc) {
		loc.getWorld().spawnParticle(particleType, loc, count, xOffset, yOffset, zOffset, extra);
	}
	
	private void spellMobSpawn(Location loc) {
		double red = color.getRed() / 255D;
		double green = color.getGreen() / 255D;
		double blue = color.getBlue() / 255D;
		loc.getWorld().spawnParticle(particleType, loc, 0, red, green, blue, 1);
	}
}
