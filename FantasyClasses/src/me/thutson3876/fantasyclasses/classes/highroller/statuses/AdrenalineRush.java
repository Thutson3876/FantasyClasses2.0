package me.thutson3876.fantasyclasses.classes.highroller.statuses;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.cooldowns.CooldownManager;
import me.thutson3876.fantasyclasses.events.RemoveStatusEvent;
import me.thutson3876.fantasyclasses.status.RemoveCause;
import me.thutson3876.fantasyclasses.status.StatusType;

public class AdrenalineRush extends HighRollerStatus {

	//Consuming a stack of ___ reduces your cooldowns by 1 second
	//speed 1
	
	private final double cdReduction = 20;
	
	public AdrenalineRush() {
		super("Adrenaline Rush", 99, null, (host, duration, stacks) -> {
			
			host.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) duration, 0));
			
		});
	}
	
	@EventHandler
	public void onStatusRemoveEvent(RemoveStatusEvent e) {
		StatusType statusType = e.getStatus().getType();
		
		if(!e.getCause().equals(RemoveCause.ABILITY_PLAYER))
			return;
		
		if(e.isCancelled())
			return;
		
		if(!(e.getDispeller() instanceof Player))
			return;
		
		LivingEntity leHost = (LivingEntity)e.getDispeller();
		
		if(!statusManager.contains(leHost, this))
			return;
		
		if(statusType instanceof Blindsided || statusType instanceof Broadsided) {
			CooldownManager cdManager = FantasyClasses.getPlugin().getCooldownManager();
			
			cdManager.modifyAllCooldowns((Player) leHost, -cdReduction);
			
		}
	}
}
