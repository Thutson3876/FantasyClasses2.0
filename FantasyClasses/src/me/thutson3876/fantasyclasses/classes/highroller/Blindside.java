package me.thutson3876.fantasyclasses.classes.highroller;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.Blindsided;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.status.ApplyCause;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Blindside extends AbstractAbility {

	private int stackAmt = 1;
	private int duration = 8 * 20;

	public Blindside(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Blindside";
		this.skillPointCost = 1;
		this.maximumLevel = 3;

		this.createItemStack(Material.BONE);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onHitEvent(EntityDamageByEntityEvent e) {
		if (e.isCancelled())
			return;

		if (this.isOnCooldown())
			return;

		if (e.getDamager().equals(this.player)) {
			if (!e.getCause().equals(DamageCause.ENTITY_ATTACK))
				return;

			Entity victim = e.getEntity();
			if (!(victim instanceof LivingEntity))
				return;

			AbilityTriggerEvent thisEvent = this.callEvent();

			if (thisEvent.isCancelled())
				return;

			Blindsided debuff = new Blindsided();
			debuff.apply(player, (LivingEntity) victim, stackAmt, duration, ApplyCause.PLAYER_ABILITY);

			this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
		}
	}

	@Override
	public String getInstructions() {
		return "Hit a creature with a melee attack";
	}

	@Override
	public String getDescription() {
		return "Your melee attacks apply &dBlindside &rto your target, causing them to take &6"
				+ AbilityUtils.doubleRoundToXDecimals(stackAmt * Blindsided.getBaseDamageMod() * 100, 2)
				+ "% &rmore damage from your arrow shots for &6" + AbilityUtils.doubleRoundToXDecimals(duration / 20.0, 1) + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		stackAmt = currentLevel;
	}

}
