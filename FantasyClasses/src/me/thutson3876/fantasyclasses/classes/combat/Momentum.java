package me.thutson3876.fantasyclasses.classes.combat;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;

public class Momentum extends AbstractAbility {

	private double dmgMod = 0.12;

	public Momentum(Player p) {
		super(p, Priority.NORMAL);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Momentum";
		this.skillPointCost = 1;
		this.maximumLevel = 3;

		this.createItemStack(Material.FEATHER);
	}
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageByEntityEvent e) {
		if (e.isCancelled())
			return;

		if (!e.getDamager().equals(this.player))
			return;

		double speed = 0;
		if (player.getVelocity().length() != 0) {
			speed = player.getVelocity().length();
		}

		else if (player.isSprinting()) {
			speed = 0.6;
		} else
			return;

		AbilityTriggerEvent thisEvent = this.callEvent();
		e.setDamage(e.getDamage() * (1.0 + speed * dmgMod));
		
		World world = player.getWorld();
		Entity ent = e.getEntity();
		
		world.playSound(ent, Sound.BLOCK_ANCIENT_DEBRIS_BREAK, (float)(1.0 + speed * 2.0), 0.85f);
		world.spawnParticle(Particle.CRIT_MAGIC, ent.getLocation().add(0, ent.getHeight() / 2.0, 0), 4 + (int)(15 * speed));
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getName() {
		return displayName;
	}

	@Override
	public String getInstructions() {
		return "Attack while sprinting or falling";
	}

	@Override
	public String getDescription() {
		return "Your attacks deal more damage based on your momentum";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return currentLevel > 0;
	}

	@Override
	public void applyLevelModifiers() {
		dmgMod = (0.12 * currentLevel);
	}

}
