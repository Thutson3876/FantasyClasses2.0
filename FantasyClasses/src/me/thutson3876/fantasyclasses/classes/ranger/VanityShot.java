package me.thutson3876.fantasyclasses.classes.ranger;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;

public class VanityShot extends AbstractAbility {

	Map<AbstractArrow, Location> arrowMap = new HashMap<>();
	private double minDistance = 20.0;

	public VanityShot(Player p) {
		super(p, Priority.LOW);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Vanity Shot";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.GOLD_NUGGET);
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

		AbstractFantasyClass clazz = this.getFantasyPlayer().getChosenClass();
		if (!(clazz instanceof Ranger))
			return;

		Ranger ranger = (Ranger) clazz;

		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;

		ranger.addTrickArrow((Arrow) eventProjectile);

		new BukkitRunnable() {

			@Override
			public void run() {
				TrickShot.getRandom().run((Arrow) eventProjectile, hitEntity);
			}

		}.runTaskLater(plugin, 10);

		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Land an arrow shot from far away";
	}

	@Override
	public String getDescription() {
		return "When you land a shot on an entity that is at least &6" + minDistance
				+ " &rblocks away, the arrow becomes a random &6Trick Shot &rarrow";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
	}

}
