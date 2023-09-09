package me.thutson3876.fantasyclasses.classes.highroller.randomabilities;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;
import me.thutson3876.fantasyclasses.util.geometry.Sphere;

public class SpookReal implements RandomAbility {

	@Override
	public void run(Player p) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 300, 2));
		p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 2));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 2));
		
		p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 1.0f, 1.0f);
		
		p.sendMessage(ChatUtils.chat("&4Uh oh..."));
		
		List<Location> locs = Sphere.generateSphere(p.getLocation(), 8, true);
		Random rng = new Random();
		for(int i = 0; i < 7; i++) {
			p.getWorld().spawnEntity(locs.get(rng.nextInt(locs.size())), EntityType.GHAST);
		}
		
		for(int i = 0; i < 300; i++) {
			int buffer = rng.nextInt(100) + 20;
			
			new BukkitRunnable() {

				@Override
				public void run() {
					p.playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 0.4f + rng.nextFloat(), 1.0f);
					p.playSound(p.getLocation(), Sound.ENTITY_GHAST_AMBIENT, 0.4f + rng.nextFloat(), 1.0f);
				}
				
			}.runTaskLater(FantasyClasses.getPlugin(), buffer);
			i += buffer;
		}

	}

}
