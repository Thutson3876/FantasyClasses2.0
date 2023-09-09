package me.thutson3876.fantasyclasses.classes.seaguardian;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.events.CustomLivingEntityDamageEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.geometry.EntityBodyPosition;
import me.thutson3876.fantasyclasses.util.particles.CustomParticle;
import me.thutson3876.fantasyclasses.util.particles.customeffect.Trail;

public class GlacialSmite extends AbstractAbility {

	private int freezeAmt = 20;
	private int duration = 1 * 15;
	private int amp = 0;
	private PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, duration, amp);
	private PotionEffect fatigue = new PotionEffect(PotionEffectType.SLOW_DIGGING, duration, amp);
	
	private final CustomParticle onHit = new CustomParticle(Particle.GLOW);
	
	public GlacialSmite(Player p) {
		super(p, Priority.HIGH);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0 * 20;
		this.displayName = "Glacial Smite";
		this.skillPointCost = 1;
		this.maximumLevel = 3;

		this.createItemStack(Material.ICE);	
	}

	//triggers on basic attack
	/*@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(isOnCooldown())
			return;
		
		if(!e.getDamager().equals(player))
			return;
		
		if(player.getAttackCooldown() < 1.0)
			return;
		
		if(e.isCancelled())
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		Entity victim = e.getEntity();
		victim.setFreezeTicks(victim.getFreezeTicks() + freezeAmt);
		
		if(victim instanceof LivingEntity)
			applyPotionEffects((LivingEntity)victim);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}*/
	
	//trigger on trident hit
	@EventHandler
	public void onCustomLivingEntityDamageEvent(CustomLivingEntityDamageEvent e) {
		if(e.isCancelled())
			return;
		
		if(!(e.getDamager() instanceof Trident))
			return;
		
		if(e.getInitialDamage() <= 0.0)
			return;
		
		Trident trident = (Trident) e.getDamager();
		
		if(trident.getShooter() == null || !trident.getShooter().equals(player))
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		Entity victim = e.getVictim();	
		victim.setFreezeTicks(victim.getFreezeTicks() + freezeAmt);
		
		if(victim instanceof LivingEntity)
			applyPotionEffects((LivingEntity)victim);
		
		onHit.spawn(EntityBodyPosition.MID.getLocation(victim));
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}
	
	public void onThrowTridentEvent(ProjectileLaunchEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getEntityType().equals(EntityType.TRIDENT))
			return;
		
		Trident trident = (Trident) e.getEntity();
		
		ProjectileSource source = trident.getShooter();
		
		if(!player.equals(source))
			return;
		
		List<CustomParticle> particles = new ArrayList<>();
		
		particles.add(new CustomParticle(Particle.WATER_WAKE));
		particles.add(new CustomParticle(Particle.REDSTONE, 1, 0, 0.5, Color.WHITE));
		
		Trail trail = new Trail(particles, 100, 1, 1.0, EntityBodyPosition.MID, true);
		trail.run(trident);
	}

	@Override
	public String getInstructions() {
		return "Hit an entity with a trident";
	}

	@Override
	public String getDescription() {
		return "Your trident throws chill your target applying freeze, slowness, and mining fatigue for &6" + AbilityUtils.doubleRoundToXDecimals(duration / 20, 1)  + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		duration = (currentLevel) * 15;
		
		freezeAmt = (20 * currentLevel);
		
		slow = new PotionEffect(PotionEffectType.SLOW, duration, amp);
		fatigue = new PotionEffect(PotionEffectType.SLOW_DIGGING, duration, amp);
	}

	private void applyPotionEffects(LivingEntity ent) {
		if(ent.isDead())
			return;
		
		ent.addPotionEffect(slow);
		ent.addPotionEffect(fatigue);
	}
	
}
