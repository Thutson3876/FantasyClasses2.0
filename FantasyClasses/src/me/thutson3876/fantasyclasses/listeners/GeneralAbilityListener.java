package me.thutson3876.fantasyclasses.listeners;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.ChatUtils;

public class GeneralAbilityListener implements Listener {

	private static final FantasyClasses plugin = FantasyClasses.getPlugin();

	public GeneralAbilityListener() {
		plugin.registerEvents(this);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onAbilityTriggerEvent(AbilityTriggerEvent e) {
		//e.getFplayer().getPlayer().sendMessage(ChatUtils.chat("&6" + e.getAbility().getName() + " &3triggered"));
		e.setCancelled(e.getFplayer().isViolatingArmorType());
	}
	
	//test code
	@EventHandler(priority = EventPriority.LOW)
	public void arrowVelocityTracker(EntityShootBowEvent e) {
		if(!(e.getEntity() instanceof Player))
			return;
		
		if(!(e.getProjectile() instanceof Arrow))
			return;
		
		
		Player player = (Player) e.getEntity();
		Arrow arrow = (Arrow) e.getProjectile();
		
		double velocityMagnitude = arrow.getVelocity().length();
		double damage = arrow.getDamage();
		
		player.sendMessage(ChatUtils.chat("&2Bow Charge: &f" + e.getForce()));
		player.sendMessage(ChatUtils.chat("&6Arrow Velocity: &f" + AbilityUtils.doubleRoundToXDecimals(velocityMagnitude, 3)));
		player.sendMessage(ChatUtils.chat("&4Arrow Damage: &f" + AbilityUtils.doubleRoundToXDecimals(damage, 3)));
		
	}
}
