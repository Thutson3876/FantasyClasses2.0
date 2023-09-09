package me.thutson3876.fantasyclasses.classes.highroller.randomabilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.util.chat.ChatUtils;
import me.thutson3876.fantasyclasses.util.geometry.Sphere;

public class SpawnHorde implements RandomAbility {

	private final static Random rng = new Random();
	private final static List<EntityType> types = new ArrayList<>();
	
	static {
		types.add(EntityType.ZOMBIE);
		types.add(EntityType.SKELETON);
		types.add(EntityType.HUSK);
		types.add(EntityType.STRAY);
		types.add(EntityType.WITHER_SKELETON);
	}
	
	@Override
	public void run(Player p) {
		List<Location> locs = Sphere.generateSphere(p.getLocation(), 5, true);
		for(Location loc : locs) {
			loc.getWorld().spawnEntity(loc, types.get(rng.nextInt(types.size())), true);
		}
		
		p.sendMessage(ChatUtils.chat("&4FOR THE HORDE!!!"));
	}

}
