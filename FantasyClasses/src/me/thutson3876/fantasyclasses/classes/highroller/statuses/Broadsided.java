package me.thutson3876.fantasyclasses.classes.highroller.statuses;

import org.bukkit.Sound;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import me.thutson3876.fantasyclasses.events.CustomLivingEntityDamageEvent;
import me.thutson3876.fantasyclasses.events.DamageModifier;
import me.thutson3876.fantasyclasses.status.RemoveCause;
import me.thutson3876.fantasyclasses.status.Status;
import me.thutson3876.fantasyclasses.status.StatusType;

public class Broadsided extends StatusType {

	//take more damage from melee attacks
	
	private static final double baseDmgMod = 0.1;
	
	public Broadsided() {
		super("Broadsided", 99, 30, null, null);
	}
	
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onDamageEvent(CustomLivingEntityDamageEvent e) {
		if(!(e.getVictim() instanceof LivingEntity))
			return;
		
		LivingEntity leHost = (LivingEntity)e.getVictim();
		
		Status status = statusManager.get(leHost, this);
		
		if(status == null)
			return;
		
		if(!e.getDamager().equals(status.getApplicator()))
			return;
		
		this.remove(leHost, RemoveCause.ABILITY_PLAYER, status.getApplicator());
		leHost.getWorld().playSound(leHost, Sound.ENTITY_BEE_HURT, 1.0f, 1.3f);
		
		double damageMod = baseDmgMod * status.getStacks();
		
		e.addModifier(new DamageModifier("Broadsided", Operation.MULTIPLY_SCALAR_1, damageMod));
	}
	
	public static double getDmgMod() {
		return baseDmgMod;
	}
}
