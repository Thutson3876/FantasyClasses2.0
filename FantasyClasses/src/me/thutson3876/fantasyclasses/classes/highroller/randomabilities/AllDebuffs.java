package me.thutson3876.fantasyclasses.classes.highroller.randomabilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.util.ChatUtils;
import me.thutson3876.fantasyclasses.util.PotionList;

public class AllDebuffs implements RandomAbility {

	@Override
	public void run(Player p) {
		for(PotionEffectType t : PotionList.DEBUFF.getPotList()) {
			p.addPotionEffect(new PotionEffect(t, 30 * 20, 1));
		}
	
		p.sendMessage(ChatUtils.chat("&4Enjoy all the debuffs!"));
	}

}
