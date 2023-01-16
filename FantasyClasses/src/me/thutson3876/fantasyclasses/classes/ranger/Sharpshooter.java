package me.thutson3876.fantasyclasses.classes.ranger;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Sharpshooter extends AbstractAbility {

	private double minDistance = 10.0;
	private double dmgReduction = 0.1;
	private double dmgMod = 0.01;

	private Map<Projectile, Location> shotProjectiles = new HashMap<>();
	
	public Sharpshooter(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Sharpshooter";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.SPECTRAL_ARROW);
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if (!(e.getDamager() instanceof Projectile))
			return;

		Projectile proj = (Projectile) e.getDamager();
		if(!shotProjectiles.containsKey(proj))
			return;

		AbilityTriggerEvent thisEvent = this.callEvent();
		
		if(thisEvent.isCancelled())
			return;
		
		Location entityLoc = e.getEntity().getLocation();
		double distance = shotProjectiles.get(proj).distance(entityLoc);

		if (distance < minDistance) {
			e.setDamage(e.getDamage() * (1 - this.dmgReduction));
			return;
		}
		
		
		e.setDamage(e.getDamage() * (1 + this.dmgMod * (distance - minDistance)));
		entityLoc.add(0, 1, 0);
		player.getWorld().spawnParticle(Particle.CRIT, entityLoc, (int) distance);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		if(shotProjectiles.containsKey(e.getEntity())){
			if(e.getHitBlock() != null) {
				shotProjectiles.remove(e.getEntity());
			}
		}
	}
	
	@EventHandler
	public void onEntityShootBowEvent(EntityShootBowEvent e) {
		if(e.getEntity().equals(player)) {
			shotProjectiles.put((Projectile) e.getProjectile(), player.getLocation());
		}
	}

	@Override
	public String getInstructions() {
		return "Deal damage from a distance";
	}

	@Override
	public String getDescription() {
		return "Your arrows deal &6" + AbilityUtils.doubleRoundToXDecimals(dmgMod * 100, 2)
				+ "% &rmore damage per block travelled. Scaling begins at a minimum distance of &6" + minDistance
				+ " &rblocks. Arrows deal &6" + AbilityUtils.doubleRoundToXDecimals(dmgReduction * 100, 2)
				+ "% &rless damage in close range";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		dmgMod = 0.01 * currentLevel;
	}

}
