package me.thutson3876.fantasyclasses.events;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.thutson3876.fantasyclasses.util.DamageType;

public class CustomLivingEntityDamageEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	private boolean isCancelled = false;
	
	private final LivingEntity VICTIM;
	private final Entity DAMAGER;
	private final double INITIAL_DAMAGE;
	private final DamageCause CAUSE;
	
	private DamageType type;
	private boolean doesIgnoreArmor = false;
	
	private List<DamageModifier> multiplicativeModifiers = new ArrayList<>();
	private List<DamageModifier> additiveModifiers = new ArrayList<>();
	
	public CustomLivingEntityDamageEvent(LivingEntity victim, Entity damager, double damage, DamageCause cause) {
		VICTIM = victim;
		DAMAGER = damager;
		INITIAL_DAMAGE = damage;
		CAUSE = cause;
		
		for(DamageType dmgType : DamageType.values()) {
			if(dmgType.getDamageCauseList().contains(cause)) {
				type = dmgType;
				break;
			}
		}
	}
	
	public CustomLivingEntityDamageEvent(LivingEntity victim, Entity damager, double damage, DamageCause cause, DamageType type) {
		VICTIM = victim;
		DAMAGER = damager;
		INITIAL_DAMAGE = damage;
		CAUSE = cause;
		
		this.type = type;
	}

	public DamageType getType() {
		return type;
	}

	public void setType(DamageType type) {
		this.type = type;
	}

	public LivingEntity getVictim() {
		return VICTIM;
	}

	public Entity getDamager() {
		return DAMAGER;
	}

	public double getInitialDamage() {
		return INITIAL_DAMAGE;
	}
	
	public DamageCause getCause() {
		return CAUSE;
	}

	public boolean doesIgnoreArmor() {
		return doesIgnoreArmor;
	}

	public void setIgnoreArmor(boolean ignoresArmor) {
		this.doesIgnoreArmor = ignoresArmor;
	}

	public List<DamageModifier> getMultiplicativeModifiers() {
		return multiplicativeModifiers;
	}

	public List<DamageModifier> getAdditiveModifiers() {
		return additiveModifiers;
	}

	public void addModifier(DamageModifier modifier) {
		if(!modifier.getOperation().equals(Operation.ADD_NUMBER)) {
			multiplicativeModifiers.add(modifier);
			return;
		}
		
		additiveModifiers.add(modifier);
	}

	public boolean removeModifier(DamageModifier modifier) {
		if(!modifier.getOperation().equals(Operation.ADD_NUMBER)) 
			return multiplicativeModifiers.remove(modifier);
		
		return additiveModifiers.remove(modifier);
	}
	
	public boolean removeModifier(UUID id) {
		DamageModifier toBeRemoved = null;
		
		for(DamageModifier dmgMod : multiplicativeModifiers)
			if(dmgMod.getId().equals(id))
				toBeRemoved = dmgMod;
		
		if(toBeRemoved != null) {
			multiplicativeModifiers.remove(toBeRemoved);
			return true;
		}
		
		for(DamageModifier dmgMod : additiveModifiers)
			if(dmgMod.getId().equals(id))
				toBeRemoved = dmgMod;
		
		if(toBeRemoved != null) {
			additiveModifiers.remove(toBeRemoved);
			return true;
		}
		
		return false;
	}
	
	public double getFinalModifiedDamage() {
		double finalDamage = this.INITIAL_DAMAGE;
		double addedDamage = this.INITIAL_DAMAGE;
		
		for(DamageModifier dmgMod : this.additiveModifiers) {
			addedDamage = dmgMod.getResult(addedDamage);
		}
		
		finalDamage = addedDamage;
		for(DamageModifier dmgMod : this.multiplicativeModifiers) {
			finalDamage += dmgMod.getResult(addedDamage);
		}
		
		return finalDamage;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}
