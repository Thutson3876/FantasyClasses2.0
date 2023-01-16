package me.thutson3876.fantasyclasses.custommobs.boss.skeletonlord;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Mob;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.custommobs.boss.MobAbility;

public class LightningVortex implements MobAbility {

	private double i = 0;
	private final double baseRadius = 1.0;
	private double radius = baseRadius;
	
	@Override
	public String getName() {
		return "Lightning Vortex";
	}

	@Override
	public void run(Mob entity) {
		i = 0;
		
		World world = entity.getWorld();
		new BukkitRunnable() {

				@Override
				public void run() {
					if(i < 2 * 6.3) {
						radius = baseRadius + i;
						double x = radius * Math.cos(i);
				        double z = radius * Math.sin(i);
						
				        Location loc = new Location(world, entity.getLocation().getX() + x, entity.getLocation().getY(), entity.getLocation().getZ() + z);
				        world.strikeLightning(loc);
						i += 0.6;
						return;
					}
					
					this.cancel();
				}
		    	
		    }.runTaskTimer(FantasyClasses.getPlugin(), 5, 5);
	}

}
