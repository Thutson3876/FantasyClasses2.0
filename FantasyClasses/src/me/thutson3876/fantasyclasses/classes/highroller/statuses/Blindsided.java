package me.thutson3876.fantasyclasses.classes.highroller.statuses;

import org.bukkit.Sound;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.projectiles.ProjectileSource;

import me.thutson3876.fantasyclasses.events.CustomLivingEntityDamageEvent;
import me.thutson3876.fantasyclasses.events.DamageModifier;
import me.thutson3876.fantasyclasses.status.RemoveCause;
import me.thutson3876.fantasyclasses.status.Status;
import me.thutson3876.fantasyclasses.status.StatusType;

public class Blindsided extends StatusType {

	// take more damage from ranged attacks

	private static final double baseDmgMod = 0.1;
	private static final double baseDuration = 8 * 20;

	public Blindsided() {
		super("Blindsided", 99, 30, null, null);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onDamageEvent(CustomLivingEntityDamageEvent e) {
		if (!(e.getDamager() instanceof Projectile))
			return;

		if(!(e.getVictim() instanceof LivingEntity))
			return;
		
		LivingEntity leHost = (LivingEntity)e.getVictim();
		
		ProjectileSource shooter = ((Projectile) e.getDamager()).getShooter();

		if (!(shooter instanceof Entity))
			return;

		Entity entityShooter = (Entity) shooter;

		Status status = statusManager.get(leHost, this);
		
		if(status == null)
			return;
		
		if (!entityShooter.equals(status.getApplicator()))
			return;

		this.remove(leHost, RemoveCause.ABILITY_PLAYER, status.getApplicator());
		leHost.getWorld().playSound(leHost, Sound.ENTITY_BLAZE_HURT, 1.3f, 1.3f);
		
		double damageMod = baseDmgMod * status.getStacks();
		
		e.addModifier(new DamageModifier("Blindsided", Operation.MULTIPLY_SCALAR_1, damageMod));
	}

	public static double getBaseDamageMod() {
		return baseDmgMod;
	}
	
	public static double getBaseDuration() {
		return baseDuration;
	}
}
