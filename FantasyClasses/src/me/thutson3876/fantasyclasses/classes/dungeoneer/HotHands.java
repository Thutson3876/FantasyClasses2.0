package me.thutson3876.fantasyclasses.classes.dungeoneer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;

public class HotHands extends AbstractAbility {

	private int duration = 1 * 20;

	public HotHands(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 12 * 20;
		this.displayName = "Hot Hands";
		this.skillPointCost = 1;
		this.maximumLevel = 6;

		this.createItemStack(Material.LAVA_BUCKET);
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if (e.isCancelled())
			return;

		if (isOnCooldown())
			return;

		if (!e.getDamager().equals(player))
			return;

		if (!e.getCause().equals(DamageCause.ENTITY_ATTACK))
			return;

		AbilityTriggerEvent thisEvent = this.callEvent();
		e.getEntity().setFireTicks(duration);
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Attack an entity";
	}

	@Override
	public String getDescription() {
		return "Attacking an entity causes them to catch fire for &6" + (duration / 20)
				+ " &rseconds. Has a cooldown of &6" + (this.coolDowninTicks / 20) + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		duration = (currentLevel) * 20;
		this.coolDowninTicks = (16 - (currentLevel)) * 20;
	}

}
