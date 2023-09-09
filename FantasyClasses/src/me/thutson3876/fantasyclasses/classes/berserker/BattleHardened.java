package me.thutson3876.fantasyclasses.classes.berserker;

import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.events.CustomLivingEntityDamageEvent;
import me.thutson3876.fantasyclasses.events.DamageModifier;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class BattleHardened extends AbstractAbility {

	private double conversionRate = 0.2;
	private double reductionAmt = 0;

	public BattleHardened(Player p) {
		super(p, Priority.HIGHEST);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Battle Hardened";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.POLISHED_DEEPSLATE);
	}
	
	@EventHandler
	public void onCustomLivingEntityDamageEvent(CustomLivingEntityDamageEvent e) {
		if(e.isCancelled())
			return;
		
		if(this.isOnCooldown())
			return;
		
		if(e.getDamager().equals(this.player)) {
			if(player.getAttackCooldown() < 1.0)
				return;
			
			if(!e.getCause().equals(DamageCause.ENTITY_ATTACK))
				return;
			
			AbilityTriggerEvent thisEvent = this.callEvent();

			if (thisEvent.isCancelled())
				return;
			
			this.reductionAmt = e.getInitialDamage() * this.conversionRate;
			this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
		}
		else if(e.getVictim().equals(player) && this.reductionAmt > 0) {
			if(this.reductionAmt <= 0)
				return;
			
			e.addModifier(new DamageModifier("Battle Hardened", Operation.ADD_NUMBER, -reductionAmt));
		}
	}

	@Override
	public String getInstructions() {
		return "Take damage after landing a crit";
	}

	@Override
	public String getDescription() {
		return "When you land a critical strike, the next attack you take is reduced by up to &6"
				+ AbilityUtils.doubleRoundToXDecimals(conversionRate * 100, 1) + "% &rof the damage you dealt";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.conversionRate = 0.2 * this.currentLevel;
	}

}
