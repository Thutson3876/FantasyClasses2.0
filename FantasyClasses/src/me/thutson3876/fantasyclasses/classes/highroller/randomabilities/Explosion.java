package me.thutson3876.fantasyclasses.classes.highroller.randomabilities;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.util.chat.ChatUtils;

public class Explosion implements RandomAbility {

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
		p.getWorld().createExplosion(loc, 4.0f, true, true, p);
		p.sendMessage(ChatUtils.chat("&4BOOM!"));
	}

}
