package me.thutson3876.fantasyclasses.classes.highroller.statuses;

import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;

import me.thutson3876.fantasyclasses.events.CustomLivingEntityDamageEvent;
import me.thutson3876.fantasyclasses.events.DamageModifier;
import me.thutson3876.fantasyclasses.status.ApplyCause;
import me.thutson3876.fantasyclasses.status.general.Strider;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class RideTheWaves extends HighRollerStatus {

	//Gain strider every time you damage an enemy
	//Deal double damage while above an enemy
	
	public RideTheWaves() {
		super("Ride the Waves", 99, null, (host, duration, stacks) -> {
			Strider strider = new Strider();
			
			strider.apply(host, host, stacks, Strider.getDefaultDuration(), ApplyCause.PLAYER_ABILITY);
		});
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(CustomLivingEntityDamageEvent e) {
		if (e.isCancelled())
			return;

		Entity damager = e.getDamager();
		if(damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof Entity) {
			damager = (Entity) ((Projectile) damager).getShooter();
		}
		
		if(!(damager instanceof LivingEntity))
			return;

		LivingEntity leHost = (LivingEntity) damager;

		if(!statusManager.contains(leHost, this))
			return;
		
		if(!AbilityUtils.isAbove(leHost, e.getVictim()))
			return;

		e.addModifier(new DamageModifier("Ride The Waves", Operation.MULTIPLY_SCALAR_1, 1.00));
	}
}
