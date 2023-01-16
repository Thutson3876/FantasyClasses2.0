package me.thutson3876.fantasyclasses.classes.highroller.randomabilities;

import java.util.Collection;

import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.util.ChatUtils;

public class Cleanse implements RandomAbility {

	@Override
	public void run(Player p) {
		Collection<PotionEffect> effects = p.getActivePotionEffects();
		if(effects != null) {
			for(PotionEffect e : effects) {
				p.removePotionEffect(e.getType());
			}
		}
		
		p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		p.setFoodLevel(20);
		p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, 0));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 200, 0));
		
		p.playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 1.0f, 1.2f);
		p.sendMessage(ChatUtils.chat("&aThe healthy benefits of a coffee enema"));
	}

}
