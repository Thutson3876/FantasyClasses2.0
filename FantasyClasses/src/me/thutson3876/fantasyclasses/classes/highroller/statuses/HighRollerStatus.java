package me.thutson3876.fantasyclasses.classes.highroller.statuses;

import me.thutson3876.fantasyclasses.status.StatusType;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;

import me.thutson3876.fantasyclasses.status.StatusEffect;

public class HighRollerStatus extends StatusType {

	protected HighRollerStatus(String name, double tickRate, 
			StatusEffect tickEffect, StatusEffect applicationEffect) {
		super(name, tickRate, 1, tickEffect, applicationEffect);
	}
	
	protected static void playerFeedback(LivingEntity host, String name, Sound sound) {
		host.sendMessage(ChatUtils.chat("&d" + name + "!"));
		host.getWorld().playSound(host.getLocation(), sound, 0.6f, 1.0f);
	}

}
