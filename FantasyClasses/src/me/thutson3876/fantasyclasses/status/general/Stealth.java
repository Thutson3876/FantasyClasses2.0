package me.thutson3876.fantasyclasses.status.general;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.status.RemoveCause;
import me.thutson3876.fantasyclasses.status.StatusType;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Stealth extends StatusType {

	private static final double DEFAULT_DURATION = 20 * 20;
	private static final int DEFAULT_AMP = 0;
	private static final int DEFAULT_TICK_RATE = 20;
	private static final int DEFAULT_CD_REDUCTION = 0;
	
	public Stealth() {
		super("Stealth", DEFAULT_TICK_RATE, 1, (host, duration, stacks) -> {
			
			if(host instanceof Player) {
				FantasyClasses.getPlugin().getCooldownManager().modifyAllCooldowns((Player) host, -DEFAULT_CD_REDUCTION);
				
			}
			
		}, (host, duration, stacks) -> {
			
			host.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) duration, DEFAULT_AMP));
			host.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, (int) duration, 0));
			
		});
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getEntity();
		Entity damager = AbilityUtils.getTrueCause(e.getDamager());
		LivingEntity leHost = null;
		
		if (victim instanceof LivingEntity && statusManager.contains((LivingEntity) victim, this)) {
			leHost = (LivingEntity) victim;
		}
		else if(damager instanceof LivingEntity && !statusManager.contains((LivingEntity) damager, this)) {
			leHost = (LivingEntity) damager;
		}
		else {
			return;
		}
		
		this.remove(leHost, RemoveCause.ABILITY_PLAYER, e.getDamager());
	}
	
	public static double getDefaultDuration() {
		return DEFAULT_DURATION;
	}

	public static double getDefaultAmp() {
		return DEFAULT_AMP;
	}

	public static int getDefaultTickRate() {
		return DEFAULT_TICK_RATE;
	}

	public static int getDefaultCdReduction() {
		return DEFAULT_CD_REDUCTION;
	}

}
