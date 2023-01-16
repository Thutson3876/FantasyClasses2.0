package me.thutson3876.fantasyclasses.classes.druid;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;

public class BestFriend extends AbstractAbility {

	public BestFriend(Player p) {
		super(p, Priority.HIGHEST);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 20;
		this.displayName = "Best Friend";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.BEETROOT);
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		Entity ent = e.getEntity();

		if (!e.getDamager().equals(player))
			return;

		if (!(ent instanceof Tameable))
			return;

		Tameable pet = (Tameable) ent;

		if (player.equals(pet.getOwner())) {
			AbilityTriggerEvent thisEvent = this.callEvent();
			e.setCancelled(true);
			this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
		}
	}

	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		Entity ent = e.getHitEntity();

		if (e.getEntity().getShooter() == null || !e.getEntity().getShooter().equals(player))
			return;

		if (!(ent instanceof Tameable))
			return;

		Tameable pet = (Tameable) ent;

		if (player.equals(pet.getOwner())) {
			AbilityTriggerEvent thisEvent = this.callEvent();
			e.setCancelled(true);
			this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
		}
	}

	@Override
	public String getInstructions() {
		return "Please don't hit your pets";
	}

	@Override
	public String getDescription() {
		return "Hitting your own pets causes no damage";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
	}

}
