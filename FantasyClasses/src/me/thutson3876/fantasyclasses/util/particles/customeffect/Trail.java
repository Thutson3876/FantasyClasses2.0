package me.thutson3876.fantasyclasses.util.particles.customeffect;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import me.thutson3876.fantasyclasses.util.geometry.EntityBodyPosition;
import me.thutson3876.fantasyclasses.util.particles.CustomParticle;

public class Trail extends CustomEffect {

	List<CustomParticle> particleList = new ArrayList<>();
	
	final EntityBodyPosition position;
	final boolean doAlternate;
	
	public Trail(CustomParticle particle, int maxDuration,
			int tickRate, double scaling, EntityBodyPosition position) {
		super(particle, 1, 0, 6.3, maxDuration, tickRate, scaling, true);
		
		particleList.add(particle);
		this.position = position;
		this.doAlternate = false;
	}
	
	public Trail(List<CustomParticle> particleList, int maxDuration,
			int tickRate, double scaling, EntityBodyPosition position) {
		this(particleList, maxDuration, tickRate, scaling, position, false);
	}
	
	public Trail(List<CustomParticle> particleList, int maxDuration,
			int tickRate, double scaling, EntityBodyPosition position, boolean doAlternate) {
		super(particleList.get(0), 1, 0, 6.3, maxDuration, tickRate, scaling, true);
		
		this.particleList = particleList;
		this.position = position;
		this.doAlternate = doAlternate;
	}

	@Override
	protected void tick(Entity ent, int currentTick) {
		Location spawnLoc = position.getLocation(ent);
		
		tick(spawnLoc, currentTick);
	}

	@Override
	protected void tick(Location loc, int currentTick) {
		if(doAlternate) {
			particleList.get(currentTick % particleList.size()).spawn(loc);
			return;
		}
		
		for(CustomParticle p : this.particleList)
			p.spawn(loc);
	}

}
