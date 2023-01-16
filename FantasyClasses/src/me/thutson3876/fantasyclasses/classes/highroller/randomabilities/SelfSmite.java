package me.thutson3876.fantasyclasses.classes.highroller.randomabilities;

import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.util.ChatUtils;

public class SelfSmite implements RandomAbility {

	@Override
	public void run(Player p) {
		p.getWorld().strikeLightning(p.getLocation());
		p.getWorld().strikeLightning(p.getLocation());
		
		p.sendMessage(ChatUtils.chat("&4Huh, I guess it &odoes &r&4strike twice"));
	}

}
