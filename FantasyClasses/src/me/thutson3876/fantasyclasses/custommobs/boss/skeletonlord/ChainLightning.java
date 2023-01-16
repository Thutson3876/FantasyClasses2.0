package me.thutson3876.fantasyclasses.custommobs.boss.skeletonlord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.custommobs.boss.MobAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class ChainLightning implements MobAbility {

	private final int radius = 3;
	private Map<Entity, Double> toStrikeTargets = new HashMap<>();
	
	@Override
	public String getName() {
		return "Chain Lightning";
	}

	@Override
	public void run(Mob entity) {
		List<Player> targets = AbilityUtils.getNearbyPlayers(entity, 16);
		Random rng = new Random();
		
		strikeTarget(targets.get(rng.nextInt(targets.size())));
	}

	private void strikeTarget(Player p) {
		p.getWorld().playSound(p.getEyeLocation(), Sound.BLOCK_CONDUIT_ACTIVATE, 5.0f, 0.7f);
	    
	    World world = p.getWorld();
	    toStrikeTargets.put(p, 0.0);
	    new BukkitRunnable() {

			@Override
			public void run() {
				if(toStrikeTargets.get(p) < 6.3) {
					double x = radius * Math.cos(toStrikeTargets.get(p));
			        double z = radius * Math.sin(toStrikeTargets.get(p));
					
			        world.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() + x, p.getEyeLocation().getY(), p.getLocation().getZ() + z, 1);
					toStrikeTargets.put(p, toStrikeTargets.get(p) + 0.1);
					return;
				}
				
				this.cancel();
				
				world.strikeLightning(p.getLocation());
				for(Player p : AbilityUtils.getNearbyPlayers(p, radius))
					strikeTarget(p);
			}
	    	
	    }.runTaskTimer(FantasyClasses.getPlugin(), 1, 1);
	}
}
