package me.thutson3876.fantasyclasses.classes.berserker;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;

public class Juggle extends AbstractAbility {

	private PotionEffect slowFall = new PotionEffect(PotionEffectType.SLOW_FALLING, 1 * 20, 0);
	private double yBoost = 0.3;
	private double knockBackMult = 0.2;
	
	public Juggle(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Juggle";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.BRICK);
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		if(this.isOnCooldown())
			return;
		
		if(!e.getDamager().equals(this.player))
			return;
		
		if(player.getAttackCooldown() < 1.0)
			return;
		
		if(!e.getCause().equals(DamageCause.ENTITY_ATTACK))
			return;
		
		if(e.getEntity().getLocation().getY() < player.getEyeLocation().getY())
			return;
		
		if(!(e.getEntity() instanceof LivingEntity))
			return;
		
		LivingEntity le = (LivingEntity) e.getEntity();
		
		if(le.isDead())
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		if (thisEvent.isCancelled())
			return;
		
		Vector newVelocity = new Vector(0, yBoost, 0);
		newVelocity.add(player.getEyeLocation().getDirection().normalize().multiply(knockBackMult));
		
		e.getEntity().setVelocity(newVelocity);
		le.addPotionEffect(slowFall);
		player.addPotionEffect(slowFall);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
		
	}
	
	@Override
	public String getInstructions() {
		return "Hit an entity above you";
	}

	@Override
	public String getDescription() {
		return "When you hit an entity that is above you, it is knocked up and you both gain Slow Fall";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		knockBackMult = 0.2 * this.currentLevel;
		yBoost = 0.3 * this.currentLevel;
		
		slowFall = new PotionEffect(PotionEffectType.SLOW_FALLING, this.currentLevel * 20, 0);
	}

}
