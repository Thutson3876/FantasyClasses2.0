package me.thutson3876.fantasyclasses.util.chat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;

public class ChatUtils {

	private static final int DEFAULT_SPLIT_LENGTH = 38;
	
	public static String chat(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	public static String traitListToString(List<Ability> list) {
		String abilityNames = "";
		for (Ability abil : list) {
			abilityNames += ColorCode.DEFAULT_HIGHLIGHT + abil.getCommandName() + ColorCode.DEFAULT + ", ";
		}
		StringUtils.chop(abilityNames);
		StringUtils.chop(abilityNames);

		return chat("&3Abilities: " + abilityNames);
	}

	public static String toCommandName(String s) {
		return s.replaceAll(" ", "_");
	}

	public static String toRegularName(String s) {
		return s.replaceAll("_", " ");
	}

	public static List<String> splitStringAtLength(List<String> list, int splitLength, String colorCode) {
		List<String> returnList = new ArrayList<>();
		for (String s : list) {
			returnList.addAll(splitSingleString(s, splitLength, colorCode));
		}

		return returnList;
	}

	public static List<String> splitSingleString(String s, int splitLength, String colorCode) {
		List<String> returnList = new ArrayList<>();
		if (s == null)
			return null;
		if (s.length() > splitLength) {
			int i = s.indexOf(' ', splitLength - (splitLength / 2));
			if (i == -1)
				i = splitLength;

			String first = s.substring(0, i);
			String second = s.substring(i);
			if (first.length() <= splitLength && second.length() <= splitLength) {
				returnList.add(colorCode + first);
				returnList.add(colorCode + second);

				return returnList;
			}
			if (first.length() > splitLength) {
				returnList.addAll(splitSingleString(first, splitLength, colorCode));
			} else {
				returnList.add(colorCode + first);
			}

			if (second.length() > splitLength) {
				returnList.addAll(splitSingleString(second, splitLength, colorCode));
			} else {
				returnList.add(colorCode + second);
			}

		} else {
			returnList.add(colorCode + s);
		}

		return returnList;
	}

	public static List<String> splitStringAtLength(List<String> list, int splitLength) {
		List<String> returnList = new ArrayList<>();
		for (String s : list) {
			returnList.addAll(splitSingleString(s, splitLength));
		}

		return returnList;
	}

	private static List<String> splitSingleString(String s, int splitLength) {
		List<String> returnList = new ArrayList<>();
		if (s == null)
			return null;
		if (s.length() > splitLength) {
			int i = s.indexOf(' ', splitLength - (splitLength / 2));
			if (i == -1)
				i = splitLength;

			String first = s.substring(0, i);
			String second = s.substring(i);
			if (first.length() <= splitLength && second.length() <= splitLength) {
				returnList.add(first);
				returnList.add(second);

				return returnList;
			}
			if (first.length() > splitLength) {
				returnList.addAll(splitSingleString(first, splitLength));
			} else {
				returnList.add(first);
			}

			if (second.length() > splitLength) {
				returnList.addAll(splitSingleString(second, splitLength));
			} else {
				returnList.add(second);
			}

		} else {
			returnList.add(s);
		}

		return returnList;
	}

	public static String stripColorCodes(String s) {
		return ChatColor.stripColor(s);
	}

	public static void welcomeMessage(Player p) {
		FantasyPlayer fplayer = FantasyClasses.getPlugin().getPlayerManager().getPlayer(p);
		if (fplayer != null && fplayer.isFirstTime()) {
			p.sendMessage(ChatUtils.chat("&6Welcome to the Crusade!"));
			p.sendMessage(ChatUtils.chat("&3Use command &r/openmenu &3to get " + ColorCode.GOLD + "" + ColorCode.BOLD
					+ "FREE " + ColorCode.AQUA + "DIAMONDS" + ColorCode.RESET + "" + ColorCode.DARK_AQUA + "!"));

			return;
		}

		p.sendMessage(ChatUtils.chat("&6Welcome to the Crusade!"));
		p.sendMessage(ChatUtils.chat("&3Use command &r/openmenu &3to use the plug-in!"));

		// Old welcome message
		/*
		 * &p.sendMessage(ChatUtils.chat("&6Welcome to the Crusade!"));
		 * p.sendMessage(ChatUtils.
		 * chat("&3In order to use the FantasyClasses plug-in, use command &f/openmenu")
		 * ); p.sendMessage(ChatUtils.
		 * chat("&3To bind a &6Bindable &3ability use command &f/bindability <ability>")
		 * ); p.sendMessage(ChatUtils.
		 * chat("&3You may change talents freely, but your class and professions are locked once chosen."
		 * )); p.sendMessage(ChatUtils.
		 * chat("&3You may later change them via a class or profession reset book found by killing difficult enemies."
		 * )); p.sendMessage(ChatUtils.chat("&6GLHF!"));
		 */
	}

	public static int getDefaultSplitLength() {
		return DEFAULT_SPLIT_LENGTH;
	}
}
