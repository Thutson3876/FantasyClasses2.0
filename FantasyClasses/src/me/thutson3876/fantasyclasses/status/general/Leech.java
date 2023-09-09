package me.thutson3876.fantasyclasses.status.general;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.thutson3876.fantasyclasses.status.Status;
import me.thutson3876.fantasyclasses.status.StatusType;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Leech extends StatusType {

	private static final double leechPercent = 0.1;
	
	public Leech() {
		super("Leech", 99, 1, null, null);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		if(!(e.getDamager() instanceof LivingEntity))
			return;
		
		LivingEntity leHost = (LivingEntity)e.getDamager();
		
		Status status = statusManager.get(leHost, this);
		
		if(status == null)
			return;
		
		AbilityUtils.heal(leHost, leechPercent * status.getStacks() * e.getFinalDamage(), leHost);
	}

	public static double getLeechPercent() {
		return leechPercent;
	}
	
}
