package me.thutson3876.fantasyclasses.classes.dungeoneer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.geometry.Sphere;

public class FieryExplosion extends AbstractAbility implements Bindable {

	private Material boundType = null;

	private int radius = 2;
	private float yield = 1.0f;
	
	public FieryExplosion(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 16 * 20;
		this.displayName = "Fiery Explosion";
		this.skillPointCost = 3;
		this.maximumLevel = 2;

		this.createItemStack(Material.FIRE_CHARGE);
	}

	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		if (isOnCooldown())
			return;

		if (!e.getPlayer().equals(player))
			return;

		if (!e.getItemDrop().getItemStack().getType().equals(boundType))
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		e.setCancelled(true);
		
		World world = player.getWorld();
		for(Location l : Sphere.generateSphere(player.getLocation(), radius, true)) {
			Fireball charge = (Fireball) world.spawnEntity(l, EntityType.SMALL_FIREBALL);
			charge.setShooter(player);
			charge.setDirection(AbilityUtils.getVectorBetween2Points(l, player.getLocation(), 0).multiply(-1.0));
			charge.setYield(yield);
			charge.setIsIncendiary(true);
			charge.setInvulnerable(true);
		}
		
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());

	}

	@Override
	public String getInstructions() {
		return "Press your drop key while holding the bound item type";
	}

	@Override
	public String getDescription() {
		return "Summon a sphere of fire charges that are all aimed away from your location with a radius of &6" + radius + " &rand a yield of &6" + yield;
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		radius = 1 + 1 * currentLevel;
		yield = 0.5f + 0.8f * currentLevel;
	}

	@Override
	public Material getBoundType() {
		return this.boundType;
	}

	@Override
	public void setBoundType(Material type) {
		this.boundType = type;
	}
	
	

}
