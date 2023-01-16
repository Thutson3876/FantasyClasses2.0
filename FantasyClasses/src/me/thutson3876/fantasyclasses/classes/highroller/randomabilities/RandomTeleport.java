package me.thutson3876.fantasyclasses.classes.highroller.randomabilities;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.util.ChatUtils;

public class RandomTeleport implements RandomAbility {

	@Override
	public void run(Player p) {
		Random rng = new Random();
		Location loc = p.getLocation();
		Location newLoc;
		do {
			newLoc = loc.add(rng.nextInt(200) - 100, rng.nextInt(200) - 100, rng.nextInt(200) - 100);
		} while(!newLoc.getBlock().isPassable() || !newLoc.getBlock().getRelative(BlockFace.UP).isPassable());
		
		p.teleport(newLoc);
		newLoc.getWorld().playSound(newLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 0.8f, 1.0f);
		newLoc.getWorld().spawnParticle(Particle.WARPED_SPORE, newLoc, 4);
		
		p.sendMessage(ChatUtils.chat("&4Not as easy as it looks, is it?"));
	}

}
