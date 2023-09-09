package me.thutson3876.fantasyclasses.classes.highroller;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.Broadsided;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.status.ApplyCause;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Broadside extends AbstractAbility {

	private int stackAmt = 1;
	private int duration = 8 * 20;

	public Broadside(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Broadside";
		this.skillPointCost = 1;
		this.maximumLevel = 3;

		this.createItemStack(Material.ARROW);
	}

	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		Projectile eventProjectile = e.getEntity();

		if (!(eventProjectile instanceof Arrow)) {
			return;
		}

		if (eventProjectile.getShooter() == null)
			return;

		if (!eventProjectile.getShooter().equals(this.player))
			return;

		Entity victim = e.getHitEntity();
		if (victim instanceof LivingEntity) {
			AbilityTriggerEvent thisEvent = this.callEvent();

			if (thisEvent.isCancelled())
				return;

			Broadsided debuff = new Broadsided();
			debuff.apply(player, (LivingEntity) victim, stackAmt, duration, ApplyCause.PLAYER_ABILITY);

			this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
		}
	}

	@Override
	public String getInstructions() {
		return "Shoot a creature with an arrow";
	}

	@Override
	public String getDescription() {
		return "Your arrow shots apply &dBroadside &rto your target, causing them to take &6"
				+ AbilityUtils.doubleRoundToXDecimals(stackAmt * Broadsided.getDmgMod() * 100, 2)
				+ "% &rmore damage from your melee attacks for &6"
				+ AbilityUtils.doubleRoundToXDecimals(duration / 20.0, 1) + " &rseconds";
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
