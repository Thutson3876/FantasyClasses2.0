package me.thutson3876.fantasyclasses.util.particles.customeffect;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.geometry.EntityBodyPosition;
import me.thutson3876.fantasyclasses.util.particles.CustomParticle;

public class GradientTrail extends CustomEffect {

	Map<List<CustomParticle>, Integer> particleLists;
	final EntityBodyPosition position;
	
	Stack<Location> prevLocs;
	
	protected GradientTrail(Map<List<CustomParticle>, Integer> particleLists, int maxDuration,
			int tickRate, double scaling, EntityBodyPosition position) {
		super(null, 1, 0, 6.3, maxDuration, tickRate, scaling, true);
		
		this.particleLists = particleLists;
		this.position = position;
	}

	@Override
	protected void tick(Entity ent, int currentTick) {
		Location currentLoc = position.getLocation(ent);
		
		tick(currentLoc, currentTick);
	}

	@Override
	protected void tick(Location loc, int currentTick) {
		for(Entry<List<CustomParticle>, Integer> entry : particleLists.entrySet()) {
			if(entry.getValue() < prevLocs.size()) {
				Location spawnLoc = prevLocs.get(prevLocs.size() - 1 - entry.getValue());
				for(CustomParticle p : entry.getKey()) {
					Vector direction = AbilityUtils.getVectorBetween2Points(prevLocs.peek(), loc, 0.5);
					p.directionalSpawn(spawnLoc, direction, direction.length());
				}
			}
		}
		
		if(prevLocs.size() >= 5)
			prevLocs.remove(0);
		
		prevLocs.push(loc);
	}

}
