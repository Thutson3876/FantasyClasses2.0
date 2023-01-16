package me.thutson3876.fantasyclasses.classes.ranger;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class TrueShot extends AbstractAbility {

	Map<AbstractArrow, Location> arrowMap = new HashMap<>();
	private double minDistance = 20.0;

	private double cooldownReduction = 0.5 * 20;

	public TrueShot(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "True Shot";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.SPECTRAL_ARROW);
	}

	@EventHandler
	public void onEntityShootBowEvent(EntityShootBowEvent e) {
		if (!e.getEntity().equals(player))
			return;

		if (!(e.getProjectile() instanceof AbstractArrow))
			return;

		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;

		AbstractArrow arrow = (AbstractArrow) e.getProjectile();

		arrowMap.put(arrow, arrow.getLocation());
	}

	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		Projectile eventProjectile = e.getEntity();
		if (!this.arrowMap.containsKey(eventProjectile))
			return;

		if (arrowMap.remove(eventProjectile).distance(eventProjectile.getLocation()) < minDistance)
			return;

		Entity hitEntity = e.getHitEntity();

		if (hitEntity == null) {
			return;
		}

		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;

		for (Ability a : plugin.getCooldownManager().getAllCooldownsOfPlayer(player)) {
			if (fplayer.getClassAbilities().contains(a)) {
				double cooldown = a.getCooldownContainer().getCooldownTime();
				double reductionPerTick = a.getCooldownContainer().getReductionPerTick();

				a.triggerCooldown(cooldown - cooldownReduction, reductionPerTick);
			}
		}

		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Land an arrow shot from far away";
	}

	@Override
	public String getDescription() {
		return "When you land a shot on an entity that is at least &6" + minDistance
				+ " &rblocks away, reduce your class cooldowns by &6"
				+ AbilityUtils.doubleRoundToXDecimals(cooldownReduction / 20.0, 2) + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.cooldownReduction = 0.5 * 20 * this.currentLevel;
	}

}
