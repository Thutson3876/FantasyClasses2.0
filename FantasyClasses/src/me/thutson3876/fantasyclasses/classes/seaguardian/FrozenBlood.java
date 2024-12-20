package me.thutson3876.fantasyclasses.classes.seaguardian;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.events.CustomLivingEntityDamageEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class FrozenBlood extends AbstractAbility {

	private double healPercent = 0.5;
	
	public FrozenBlood(Player p) {
		super(p, Priority.HIGH);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0 * 20;
		this.displayName = "Frozen Blood";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.WEEPING_VINES);	
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onCustomLivingEntityDamageEvent(CustomLivingEntityDamageEvent e) {
		if(e.isCancelled())
			return;
		
		if(e.getDamager() == null || !AbilityUtils.isTrueCause(player, e.getDamager()))
			return;
		
		if(player.getHealth() / 30 > 0.5)
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		AbilityUtils.heal(player, healPercent * e.getFinalModifiedDamage(), player);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Hit an entity with an attack, while below half health";
	}

	@Override
	public String getDescription() {
		return "While below half health, your attacks heal you for &6" + AbilityUtils.doubleRoundToXDecimals(healPercent * 100, 1)  + "% &rof the damage they deal";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		healPercent = 0.25 * currentLevel;
	}
	
}
