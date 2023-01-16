package me.thutson3876.fantasyclasses.classes.highroller.randomabilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.util.ChatUtils;

public class CowSplosion implements RandomAbility {

	private List<Entity> cows = new ArrayList<>();
	
	@Override
	public void run(Player p) {
		World world = p.getWorld();
		Location loc = p.getLocation();
		cows = new ArrayList<>();
		for(int i = 0; i < 30; i++) {
			cows.add(world.spawnEntity(loc, EntityType.COW));
		}
		
		world.playSound(loc, Sound.ENTITY_CREEPER_PRIMED, 1.2f, 0.8f);
		p.sendMessage(ChatUtils.chat("&aMMMMMOOOOOOOOO!!!"));
		
		new BukkitRunnable() {

			@Override
			public void run() {
				for(Entity ent : cows) {
					if(ent == null || ent.isDead())
						continue;
					
					world.createExplosion(ent.getLocation(), 3.5f);
				}
			}
			
		}.runTaskLater(FantasyClasses.getPlugin(), 7 * 20);
	}

}
