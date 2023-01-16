package me.thutson3876.fantasyclasses.classes.monk;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class BalancedLanding extends AbstractAbility {

	private double dmgMod = 0.4;
	
	public BalancedLanding(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Balanced Landing";
		this.skillPointCost = 1;
		this.maximumLevel = 2;
		
		this.createItemStack(Material.FEATHER);
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent e) {
		if(!e.getEntity().equals(this.player))
			return;
		
		if(!e.getCause().equals(DamageCause.FALL))
			return;
			
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		if(thisEvent.isCancelled())
			return;
		
		e.setDamage(e.getDamage() * (1 - dmgMod));
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Take fall damage";
	}

	@Override
	public String getDescription() {
		return "When you take fall damage, reduce it by &6" + AbilityUtils.doubleRoundToXDecimals(dmgMod * 100, 1) + "%";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		dmgMod = 0.4 * currentLevel;
	}

}
