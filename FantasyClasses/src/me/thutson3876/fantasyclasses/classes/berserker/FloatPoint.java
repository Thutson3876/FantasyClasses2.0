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
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class FloatPoint extends AbstractAbility implements Bindable {

	private Material type = null;
	
	private PotionEffect slowFall = new PotionEffect(PotionEffectType.SLOW_FALLING, 1 * 20, 0);
	private double yBoost = 0.3;
	
	public FloatPoint(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Float Point";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.LILY_PAD);
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
		
		if(AbilityUtils.getHeightAboveGround(player) < 0.5)
			return;
		
		if(!(e.getEntity() instanceof LivingEntity))
			return;
		
		LivingEntity le = (LivingEntity) e.getEntity();
		
		if(le.isDead())
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		if (thisEvent.isCancelled())
			return;
		
		Vector currentVelocity = player.getVelocity();
		//Reduces current momentum
		//currentVelocity.multiply(0.5);
		
		Vector newVelocity = currentVelocity.setY(yBoost);
		
		player.setVelocity(newVelocity);
		player.addPotionEffect(slowFall);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Hit a creature while mid-air with bound item type";
	}

	@Override
	public String getDescription() {
		return "Your mid-air attacks cause you to launch upwards slightly, and gently float back down";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		
	}

	@Override
	public Material getBoundType() {
		return type;
	}

	@Override
	public void setBoundType(Material type) {
		this.type = type;
	}

}
