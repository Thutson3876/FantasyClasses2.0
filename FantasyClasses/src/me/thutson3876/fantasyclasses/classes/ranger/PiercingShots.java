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

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class PiercingShots extends AbstractAbility {

	Map<AbstractArrow, Location> arrowMap = new HashMap<>();
	private double minDistance = 10.0;

	public PiercingShots(Player p) {
		super(p, Priority.HIGH);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Piercing Shots";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.CROSSBOW);
	}

	@EventHandler
	public void onEntityShootBowEvent(EntityShootBowEvent e) {
		if(!e.getEntity().equals(player))
			return;
		
		if (!(e.getProjectile() instanceof AbstractArrow))
		      return;
		
		AbstractArrow arrow = (AbstractArrow)e.getProjectile();
		arrow.setPierceLevel(arrow.getPierceLevel() + 1);
		
		arrowMap.put(arrow, arrow.getLocation());
	}
	
	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		Projectile eventProjectile = e.getEntity();
		if (!this.arrowMap.containsKey(eventProjectile))
			return;
		
		Location loc = this.arrowMap.remove(eventProjectile);
		
		Entity hitEntity = e.getHitEntity();
		
		if(hitEntity == null) {
			return;
		}
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		if(thisEvent.isCancelled())
			return;
		
	
		
		int pierceAmt = (int) (loc.distance(hitEntity.getLocation()) / minDistance);
		
		AbstractArrow arrow = (AbstractArrow) eventProjectile;
		arrow.setPierceLevel(arrow.getPierceLevel() + pierceAmt - 1);
		
		this.arrowMap.remove(eventProjectile);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Shoot an arrow";
	}

	@Override
	public String getDescription() {
		return "Your arrows are shot with such ferocity that they can pierce through &61 &radditional target for every &6" + AbilityUtils.doubleRoundToXDecimals(minDistance, 2)
				+ " &rblocks travelled";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.minDistance = 15.0 - 5.0 * this.currentLevel;
	}

}
