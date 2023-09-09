package me.thutson3876.fantasyclasses.status.general;

import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.status.StatusType;

public class Strider extends StatusType {

	private static final double DEFAULT_DURATION = 2 * 20;
	private static final int DEFAULT_AMP = 2;
	
	public Strider() {
		super("Strider", 99, 1, null, (host, duration, stacks) -> {
			
			host.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int)duration, DEFAULT_AMP));
			host.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, (int)duration + 20, DEFAULT_AMP));
			host.getWorld().playSound(host, Sound.ENTITY_RABBIT_JUMP, 3.0f, 1.0f);
			
		});
		
	}
	
	public static double getDefaultDuration() {
		return DEFAULT_DURATION;
	}

	public static double getDefaultAmp() {
		return DEFAULT_AMP;
	}
}
