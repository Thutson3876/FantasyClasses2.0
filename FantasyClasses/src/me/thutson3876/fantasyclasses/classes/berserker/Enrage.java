package me.thutson3876.fantasyclasses.classes.berserker;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.status.ApplyCause;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Enrage extends AbstractAbility {

	private int duration = 4 * 20;

	public Enrage(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Enrage";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.CRACKED_STONE_BRICKS);
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if (e.isCancelled())
			return;

		if (this.isOnCooldown())
			return;

		if (!e.getDamager().equals(this.player))
			return;

		if (player.getAttackCooldown() < 1.0)
			return;

		if (!e.getCause().equals(DamageCause.ENTITY_ATTACK))
			return;

		if (!AbilityUtils.isCritical(player))
			return;

		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;

		Berserker.getEnraged().apply(player, player, 1, duration, ApplyCause.PLAYER_ABILITY);

		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Land a critical strike on a target";
	}

	@Override
	public String getDescription() {
		return "When you land a critical strike on an enemy, you become &dEnraged&r, gaining Resistance for &6" + this.duration / 20 + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
	}

}
