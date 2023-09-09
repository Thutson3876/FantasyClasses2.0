package me.thutson3876.fantasyclasses.util.particles.vanity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.util.geometry.VectorUtils;
import me.thutson3876.fantasyclasses.util.particles.CustomParticle;

public class Halo extends Vanity {

	private static final int CIRCLE_POINTS_MULT = 5;
	private static final int DEFAULT_TICKRATE = 2;
	private static final double DEFAULT_DENSITY = 1.0;
	private static final int DEFAULT_DURATION = 3 * 20;
	
	protected List<CustomParticle> particles = new ArrayList<>();
	
	protected double radius = 0.4;
	
	protected double density = 1.0;
	
	protected boolean doAlternateParticles = false;
	
	public Halo(CustomParticle particle) {
		super(DEFAULT_TICKRATE, DEFAULT_DENSITY, DEFAULT_DURATION);
		this.particles.add(particle);
	}
	
	public Halo(CustomParticle particle, double spawnChance) {
		super(DEFAULT_TICKRATE, DEFAULT_DENSITY, DEFAULT_DURATION, spawnChance);
		this.particles.add(particle);
	}
	
	public Halo(List<CustomParticle> particles, boolean doAlternateParticles) {
		super(DEFAULT_TICKRATE, DEFAULT_DENSITY, DEFAULT_DURATION);
		this.particles.addAll(particles);
		this.doAlternateParticles = doAlternateParticles;
	}
	
	public Halo(List<CustomParticle> particles, boolean doAlternateParticles, double spawnChance) {
		super(DEFAULT_TICKRATE, DEFAULT_DENSITY, DEFAULT_DURATION, spawnChance);
		this.particles.addAll(particles);
		this.doAlternateParticles = doAlternateParticles;
	}
	
	public Halo(List<CustomParticle> particles, double radius, boolean doAlternateParticles, int tickRate, double density, int duration, double spawnChance) {
		super(tickRate, density, duration, spawnChance);
		this.particles.addAll(particles);
		this.radius = radius;
		this.density = density;
		this.doAlternateParticles = doAlternateParticles;
	}

	@Override
	protected void tickDefault(UUID key, LivingEntity ent, int currentTick) {
		int alternationIndex = currentTick % particles.size();
		
		int circlePoints = (int) Math.round(CIRCLE_POINTS_MULT * scaling * (2 * Math.PI * radius));
		double increment = (2 * Math.PI) / circlePoints;
		
		Location entLoc = ent.getEyeLocation().add(0, 0.54, 0);
		
		double pitch = VectorUtils.getUseablePitch(ent);
		//double yaw = VectorUtils.getUseableYaw(ent);
		
		for (int i = 0; i < circlePoints; i++) {
            double angle = i * increment;
            double x = radius * Math.sin(angle);
            double z = radius * Math.cos(angle);
            Vector vec = new Vector(x, 0, z);

            //VectorUtils.rotateAroundAxisX(vec, pitch);
            VectorUtils.rotateAroundAxisY(vec, pitch);
            
            entLoc.add(vec);
            
            if(this.doAlternateParticles) {
            	spawnDirectionWithMovement(particles.get(alternationIndex), ent, key, entLoc);
            }
            else {
            	for(CustomParticle particle : this.particles)
            		spawnDirectionWithMovement(particle, ent, key, entLoc);
            }
            
            entLoc.subtract(vec);
        }
	}

	@Override
	protected void tickWhileCrouching(UUID key, LivingEntity ent, int currentTick) {
		tickDefault(key, ent, currentTick);
	}

	@Override
	protected void tickWhileGliding(UUID key, LivingEntity ent, int currentTick) {
		tickDefault(key, ent, currentTick);
	}

	
}
