package me.thutson3876.fantasyclasses.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.ChatUtils;

public class CustomDamageListener implements Listener {

	private static final FantasyClasses plugin = FantasyClasses.getPlugin();
	
	private static final double BAD_LUCK_MOD = 0.1;

	public CustomDamageListener() {
		plugin.registerEvents(this);
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if(event.getEntity() instanceof LivingEntity) {
			LivingEntity ent = (LivingEntity)event.getEntity();
			if(ent.hasPotionEffect(PotionEffectType.UNLUCK)) {
				double dmgMod = 1 + (BAD_LUCK_MOD * (ent.getPotionEffect(PotionEffectType.UNLUCK).getAmplifier() + 1));
				event.setDamage(event.getDamage() * dmgMod);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		Entity damager = e.getDamager();
		Entity victim = e.getEntity();
		
		if(damager instanceof Player) {
			FantasyPlayer fplayer = plugin.getPlayerManager().getPlayer((Player) damager);

			if(fplayer != null) {
				if(!fplayer.hasFriendlyFire()) {
					if(victim instanceof Player) {
						e.setCancelled(true);
					}
				}
				
				if(!e.isCancelled()) {
					if(fplayer.hasDamageMeters()) {
						fplayer.getPlayer().sendMessage(ChatUtils.chat("&7You dealt &c" + AbilityUtils.doubleRoundToXDecimals(e.getFinalDamage(), 2) + " &7damage"));
					}
				}
			}
		}
		else if(damager instanceof Projectile) {
			if(((Projectile)damager).getShooter() instanceof Player) {
				FantasyPlayer fplayer = plugin.getPlayerManager().getPlayer((Player) ((Projectile)damager).getShooter());
				if(fplayer != null) {
					if(!fplayer.hasFriendlyFire()) {
						if(victim instanceof Player) {
							e.setCancelled(true);
						}
					}
					
					if(!e.isCancelled()) {
						if(fplayer.hasDamageMeters()) {
							fplayer.getPlayer().sendMessage(ChatUtils.chat("&7You dealt &c" + AbilityUtils.doubleRoundToXDecimals(e.getFinalDamage(), 2) + " &7damage"));
						}
					}
				}
			}
		}
	}
}
