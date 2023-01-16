package me.thutson3876.fantasyclasses.custommobs.boss.skeletonlord;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Mob;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.custommobs.SkeletalRider;
import me.thutson3876.fantasyclasses.custommobs.boss.MobAbility;

public class SummonRiders implements MobAbility {

	private double radius = 5;
	private int summonAmt = 6;
	
	@Override
	public String getName() {
		return "Summon Riders";
	}

	@Override
	public void run(Mob entity) {
		Location loc = entity.getLocation();
		World world = entity.getWorld();
		List<Location> spawnLocs = new ArrayList<>();
		
		world.playSound(loc, Sound.ENTITY_SKELETON_HORSE_DEATH, 5.0f, 0.8f);
		for(double i = 0; i < 6.3; i+=radius/summonAmt) {
			double x = radius * Math.cos(i);
	        double z = radius * Math.sin(i);
			
	        spawnLocs.add(new Location(world, loc.getX() + x, entity.getLocation().getY(), loc.getZ() + z));
		}
		
		for(Location l : spawnLocs) {
			world.spawnParticle(Particle.ELECTRIC_SPARK, l, 10);
		}
		
		new BukkitRunnable() {

			@Override
			public void run() {
				for(Location l : spawnLocs) {
					world.strikeLightning(l);
				}
			}
			
		}.runTaskLater(FantasyClasses.getPlugin(), 20);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				for(Location l : spawnLocs) {
					new SkeletalRider(l);
				}
			}
			
		}.runTaskLater(FantasyClasses.getPlugin(), 25);
	}


}
