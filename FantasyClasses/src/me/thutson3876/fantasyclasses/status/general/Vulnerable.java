package me.thutson3876.fantasyclasses.status.general;

import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import me.thutson3876.fantasyclasses.events.CustomLivingEntityDamageEvent;
import me.thutson3876.fantasyclasses.events.DamageModifier;
import me.thutson3876.fantasyclasses.status.Status;
import me.thutson3876.fantasyclasses.status.StatusType;

public class Vulnerable extends StatusType {

	private static final double dmgMod = 0.01;
	
	public Vulnerable() {
		super("Vulnerable", 20, 500, null, null);
	}

	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamageByEntityEvent(CustomLivingEntityDamageEvent e) {
		if(e.isCancelled())
			return;
		
		if(!(e.getVictim() instanceof LivingEntity))
			return;
		
		if(!(e.getDamager() instanceof LivingEntity))
			return;
		
		LivingEntity leHost = (LivingEntity)e.getDamager();
		
		Status status = statusManager.get(leHost, this);
		
		if(status == null)
			return;
		
		e.addModifier(new DamageModifier(this.getName(), Operation.MULTIPLY_SCALAR_1, dmgMod * status.getStacks()));
	}
	
	public static double getDefaultMod() {
		return dmgMod;
	}
}
