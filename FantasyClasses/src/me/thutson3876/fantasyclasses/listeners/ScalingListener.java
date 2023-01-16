package me.thutson3876.fantasyclasses.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;

public class ScalingListener implements Listener {

	private static final FantasyClasses plugin = FantasyClasses.getPlugin();
	private static double maxDamageReduction = 1.0;
	private static double maxDamageIncrease = 1.0;
	
	
	public ScalingListener() {
		plugin.registerEvents(this);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		Entity damager = event.getDamager();
		Entity victim = event.getEntity();
		
		if(event.isCancelled())
			return;
		
		if(damager == null || victim == null)
			return;
		
		if(damager instanceof Player) {
			if(victim instanceof Mob) {
				FantasyPlayer fplayer = plugin.getPlayerManager().getPlayer((Player)damager);
				
				//Disables Damage to the Ender Dragon to prevent early server completion
				/*if(victim.getType().equals(EntityType.ENDER_DRAGON)) {
					event.setCancelled(true);
					return;
				}*/
				
				if(fplayer != null) {
					double x = (1 - ((double)fplayer.getPlayerLevel() / 150.0));
					double dmgMod = x > maxDamageReduction ? x : maxDamageReduction;
					event.setDamage(event.getDamage() *  dmgMod);
				}
			}
			
			return;
		}
		if(victim instanceof Player) {
			if(damager instanceof Mob) {
				FantasyPlayer fplayer = plugin.getPlayerManager().getPlayer((Player)victim);
				if(fplayer != null) {
					double x = (1 + ((double)fplayer.getPlayerLevel() / 150.0));
					double dmgMod = x < maxDamageIncrease ? x : maxDamageIncrease;
					event.setDamage(event.getDamage() * dmgMod);
				}
			}
			
			return;
		}
	}
}
