package me.thutson3876.fantasyclasses.classes.seaguardian;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.geometry.Sphere;
import me.thutson3876.fantasyclasses.util.math.MathUtils;
import me.thutson3876.fantasyclasses.util.particles.CustomParticle;

public class FrozenPrison extends AbstractAbility implements Bindable {
	
	private static final Random rng = new Random();
	
	private int duration = 6 * 20;
	private int radius = 6;
	
	private Material type = null;
	
	public FrozenPrison(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 24 * 20;
		this.displayName = "Frozen Prison";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.BLUE_ICE);	
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if(isOnCooldown())
			return;
		
		if(!e.getPlayer().equals(player))
			return;
		
		if(e.getItem() == null || !e.getItem().getType().equals(this.type))
			return;
		
		if(!player.isSneaking())
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		Location spawnAt = player.getTargetBlock(null, 16).getLocation();
		List<Location> iceball = Sphere.generateSphere(spawnAt, radius, true);
		spawnAt.getWorld().playSound(spawnAt, Sound.BLOCK_GLASS_PLACE, 1.4f, 1.5f);
		
		for(int i = 0; i < iceball.size(); i++) {
			iceball.get(i).getBlock().setType(Material.PACKED_ICE);
		}
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for(Location l : iceball) {
					if(l.getBlock().getType().equals(Material.PACKED_ICE)) {
						l.getBlock().setType(Material.AIR);
					}
				}
				
				player.getWorld().playSound(spawnAt, Sound.BLOCK_GLASS_BREAK, 1.2f, 1F);
			}
		}.runTaskLater(plugin, duration);
		
		//Particles
		List<Location> particleLocs = Sphere.generateSphere(spawnAt, radius - 1, false);
		double spawnChance = 0.6;
		CustomParticle snow = new CustomParticle(Particle.SNOWFLAKE, 0.3);
		CustomParticle endrod = new CustomParticle(Particle.END_ROD, 0.3);
		
		for(Location l : particleLocs) {
			double chance = rng.nextDouble();
			if(chance < spawnChance) {
				if(chance > spawnChance / 2.0) {
					snow.spawn(l);
				}
				else {
					endrod.spawn(l);
				}
			}
		}
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Right-click with the bound item type while crouching";
	}

	@Override
	public String getDescription() {
		return "Summon a sphere of ice around your target that lasts for &6" + MathUtils.convertToDurationInSeconds(duration, 1) + " &rseconds. The sphere has a radius of &6" + radius + " &rblocks";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		duration = 6 * currentLevel * 20;
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
