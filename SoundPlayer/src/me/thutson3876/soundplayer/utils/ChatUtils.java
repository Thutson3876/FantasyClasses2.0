package me.thutson3876.soundplayer.utils;

import org.bukkit.ChatColor;

public class ChatUtils {

	public static String chat(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public static String stripColorCodes(String s) {
		return ChatColor.stripColor(s);
	}

}
