package me.thutson3876.fantasyclasses.classes.berserker.statuses;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.status.StatusType;

public class Enraged extends StatusType {

	public Enraged() {
		
		super("Enraged", 99, 1, null, (host, duration, stacks)->{
			
			host.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) duration, 0));
			
		}, false);
	}


}
