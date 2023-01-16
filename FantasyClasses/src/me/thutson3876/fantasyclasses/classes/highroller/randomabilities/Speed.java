package me.thutson3876.fantasyclasses.classes.highroller.randomabilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.util.ChatUtils;

public class Speed implements RandomAbility {

	@Override
	public void run(Player p) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 4));
		p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 600, 0));
		
		p.sendMessage(ChatUtils.chat("&aDeja Vu!"));
		p.sendMessage(ChatUtils.chat("&aI've just been in this place before"));
		p.sendMessage(ChatUtils.chat("&a(Higher on the street)"));
		p.sendMessage(ChatUtils.chat("&aAnd I know it's my time to go"));
		p.sendMessage(ChatUtils.chat("&aCalling you, and the search is a mystery"));
		p.sendMessage(ChatUtils.chat("&a(Standing on my feet)"));
		p.sendMessage(ChatUtils.chat("&aIt's so hard when I try to be me, woah"));
		p.sendMessage(ChatUtils.chat("&aWhooooaaa!!"));
	}

}
