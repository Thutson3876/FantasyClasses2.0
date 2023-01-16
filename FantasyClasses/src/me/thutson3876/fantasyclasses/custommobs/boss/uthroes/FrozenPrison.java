package me.thutson3876.fantasyclasses.custommobs.boss.uthroes;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Mob;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.custommobs.boss.MobAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.Sphere;

public class FrozenPrison implements MobAbility {
	
	private int duration = 13 * 20;
	private int radius = 9;
	
	@Override
	public String getName() {
		return "Frozen Prison";
	}

	@Override
	public void run(Mob entity) {
		Location spawnAt = AbilityUtils.getMidpoint(entity.getLocation(), entity.getTarget().getLocation());
		List<Location> iceball = Sphere.generateSphere(spawnAt, radius, true);
		spawnAt.getWorld().playSound(spawnAt, Sound.BLOCK_GLASS_PLACE, 2.5f, 0.8f);
		
		for(int i = 0; i < iceball.size(); i++) {
			iceball.get(i).getBlock().setType(Material.PACKED_ICE);
		}
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for(Location l : iceball) {
					if(l.getBlock().getType().equals(Material.PACKED_ICE)) {
						l.getBlock().setType(Material.AIR);
					}
				}
				
				entity.getWorld().playSound(spawnAt, Sound.BLOCK_GLASS_BREAK, 1.2f, 1F);
			}
		}.runTaskLater(FantasyClasses.getPlugin(), duration);
	}
}
