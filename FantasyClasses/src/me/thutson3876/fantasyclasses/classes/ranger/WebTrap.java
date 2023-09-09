package me.thutson3876.fantasyclasses.classes.ranger;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;
import me.thutson3876.fantasyclasses.util.geometry.Sphere;
import me.thutson3876.fantasyclasses.util.particles.CustomParticle;
import me.thutson3876.fantasyclasses.util.particles.GeneralParticleEffects;

public class WebTrap extends AbstractAbility implements Bindable {

	private Material type = null;

	private Egg egg = null;

	private boolean isHollow = true;
	private List<PotionEffect> potionEffects = new ArrayList<>();

	private int radius = 2;
	private int durationInTicks = 3 * 20;

	private Location trapLoc = null;
	private int durationOfTrap = 10 * 20;
	private int timer = 0;

	public WebTrap(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 12 * 20;
		this.displayName = "Web Trap";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.STRING);
	}

	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		if (!e.getEntity().equals(egg))
			return;

		this.trapLoc = e.getEntity().getLocation().add(0, 0.2, 0);
		timer = 0;

		World world = trapLoc.getWorld();

		new BukkitRunnable() {

			@Override
			public void run() {
				timer++;
				if (timer > durationOfTrap) {
					trapLoc = null;
				}

				if (trapLoc == null) {
					this.cancel();
					return;
				}

				if (!world.getNearbyEntities(trapLoc, radius, radius / 2, radius).isEmpty()) {
					// Trap Triggered!
					player.sendMessage(ChatUtils.chat("&6Your trap triggered!"));
					trapLoc.getWorld().playSound(trapLoc, Sound.ENTITY_SPIDER_AMBIENT, 1.0f, 0.7F);
					spawnWebs();
					
					this.cancel();
					return;
				}

				if (timer - 1 % 10 == 0)
					GeneralParticleEffects.pulsingCircle(trapLoc, new CustomParticle(Particle.CAMPFIRE_COSY_SMOKE), radius - 1, 0.25, durationOfTrap, 1);

			}

		}.runTaskTimer(plugin, 1, 1);
	}

	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		if (isOnCooldown())
			return;

		if (!e.getPlayer().equals(player))
			return;

		if (!e.getItemDrop().getItemStack().getType().equals(type))
			return;

		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;

		e.setCancelled(true);

		throwTrap();

		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	private void throwTrap() {
		Vector velocity = player.getEyeLocation().getDirection().normalize().multiply(1.3);
		World world = player.getWorld();
		Location loc = player.getEyeLocation();
		egg = (Egg) world.spawnEntity(loc, EntityType.EGG);
		egg.setShooter(player);
		egg.setVelocity(velocity);
	}

	private void spawnWebs() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if(trapLoc == null)
					return;
				
				Location center = trapLoc;
				List<Location> spawnLocs = Sphere.generateCircle(center, radius, isHollow);

				for (Location l : spawnLocs) {
					Block b = l.getBlock();
					if (b.isPassable())
						b.setType(Material.COBWEB);
				}

				player.getWorld().playSound(center, Sound.BLOCK_WEEPING_VINES_PLACE, 1.5f, 1.5F);
				
				for (LivingEntity le : AbilityUtils.getNearbyLivingEntities(center, radius, radius, radius)) {
					for (PotionEffect effect : potionEffects)
						le.addPotionEffect(effect);
				}

				new BukkitRunnable() {

					@Override
					public void run() {
						for (Location l : spawnLocs) {
							if (l.getBlock().getType().equals(Material.COBWEB)) {
								l.getBlock().setType(Material.AIR);
							}
						}

						player.getWorld().playSound(center, Sound.ENTITY_SPIDER_DEATH, 0.8f, 1.2F);
					}
				}.runTaskLater(plugin, durationInTicks);
				
				trapLoc = null;
			}

		}.runTaskLater(plugin, 10);

	}

	@Override
	public String getInstructions() {
		return "Press your drop key while holding the bound item type";
	}

	@Override
	public String getDescription() {
		return "Throw out a trap. When it activates, any creature that walks onto it will cause an eruption of webbing to spawn from it";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.durationInTicks = 6 * 20 * this.currentLevel;
	}

	public List<PotionEffect> getPotionEffects() {
		return this.potionEffects;
	}

	public void setPotionEffects(List<PotionEffect> potionEffects) {
		this.potionEffects = potionEffects;
	}

	public boolean getHollow() {
		return this.isHollow;
	}

	public void setHollow(boolean isHollow) {
		this.isHollow = isHollow;
	}

	@Override
	public Material getBoundType() {
		return type;
	}

	@Override
	public void setBoundType(Material type) {
		this.type = type;
	}

}
