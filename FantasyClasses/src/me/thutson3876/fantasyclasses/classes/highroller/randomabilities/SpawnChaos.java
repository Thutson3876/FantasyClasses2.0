package me.thutson3876.fantasyclasses.classes.highroller.randomabilities;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.custommobs.CustomMob;
import me.thutson3876.fantasyclasses.util.ChatUtils;

public class SpawnChaos implements RandomAbility {
	
	private static int spawnAmt = 2;
	
	@Override
	public void run(Player p) {
		Block b = p.getTargetBlockExact(50);
		Location loc;
		if(b == null) {
			loc = p.getLocation();
		}
		else {
			loc = b.getLocation();
		}
		
		loc.getWorld().playSound(loc, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.2f, 0.4f);
		loc.getWorld().spawnParticle(Particle.PORTAL, loc, 20);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				for(int i = 0; i < spawnAmt; i++) {
					Random rng = new Random();
					CustomMob.values()[rng.nextInt(CustomMob.values().length)].newMob(loc);
				}
				
				p.sendMessage(ChatUtils.chat("&4Here's the plan: you run.... away"));
			}
			
		}.runTaskLater(FantasyClasses.getPlugin(), 2 * 20);
		
	}

}
