package me.thutson3876.fantasyclasses.classes.highroller.randomabilities;

import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.util.chat.ChatUtils;

public class SelfExplosion implements RandomAbility {

	@Override
	public void run(Player p) {
		p.getWorld().createExplosion(p.getLocation(), 4.0f, true, true, p);
		
		p.sendMessage(ChatUtils.chat("&4BOOM!"));
	}

}
