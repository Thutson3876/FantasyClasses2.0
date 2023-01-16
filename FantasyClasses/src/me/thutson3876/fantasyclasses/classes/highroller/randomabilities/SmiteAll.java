package me.thutson3876.fantasyclasses.classes.highroller.randomabilities;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.ChatUtils;

public class SmiteAll implements RandomAbility {

	@Override
	public void run(Player p) {
		for(Entity e : AbilityUtils.getEntitiesInAngle(p, 1.6, 12.0)) {
			e.getWorld().strikeLightning(e.getLocation());
		}
		
		p.sendMessage(ChatUtils.chat("&aUNLIMITED POWAAAA"));
	}

}
