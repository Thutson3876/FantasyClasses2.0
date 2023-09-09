package me.thutson3876.fantasyclasses.classes.highroller;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class CombatPotency extends AbstractAbility {

	private double dmgMod = 0.15;
	
	public CombatPotency(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Combat Potency";
		this.skillPointCost = 1;
		this.maximumLevel = 2;
		
		this.createItemStack(Material.IRON_AXE);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onHit(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		if(this.isOnCooldown())
			return;
		
		if(e.getDamager().equals(this.player)) {
			if(player.getAttackCooldown() < 1.0)
				return;
			
			if(!e.getCause().equals(DamageCause.ENTITY_ATTACK))
				return;
			
			if(!AbilityUtils.isCritical(player))
				return;
			
			AbilityTriggerEvent thisEvent = this.callEvent();

			if (thisEvent.isCancelled())
				return;
			
			e.setDamage(e.getDamage() * (1 + this.dmgMod));
			
			this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
		}
	}
	
	@Override
	public String getInstructions() {
		return "Land a critical hit";
	}

	@Override
	public String getDescription() {
		return "Your melee critical hits deal &6" + AbilityUtils.doubleRoundToXDecimals(dmgMod * 100, 1) + "% &rmore damage";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.dmgMod = 0.15 * this.dmgMod;
	}

}
