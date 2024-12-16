package me.thutson3876.fantasyclasses.classes.druid;

import java.util.List;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.geometry.Sphere;
import me.thutson3876.fantasyclasses.util.particles.CustomParticle;
import me.thutson3876.fantasyclasses.util.particles.GeneralParticleEffects;

public class Starfall extends AbstractAbility implements Bindable {

	private Material boundType = null;
	private int duration = 8 * 20;
	private int tickRate = 10;
	private int radius = 6;
	
	private int remainingDuration;
	
	public Starfall(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 16 * 20;
		this.displayName = "Starfall";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.WIND_CHARGE);	
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if(!e.getPlayer().equals(player))
			return;
		
		if(e.getItem() == null || !e.getItem().getType().equals(boundType))
			return;
		
		if(isOnCooldown())
			return;
		
		if(!e.hasBlock())
			return;
		
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		
		AbstractFantasyClass clazz = this.getFantasyPlayer().getChosenClass();
		if (!(clazz instanceof Druid))
			return;

		Druid druid = (Druid) clazz;

		List<Location> locations = Sphere.generateCircle(e.getClickedBlock().getLocation(), radius, false);
		
		remainingDuration = duration;
		
		GeneralParticleEffects.doubleEndedCircle(e.getClickedBlock().getLocation(), radius, tickRate, 720, duration, new CustomParticle(Particle.DUST_PLUME, 2, 0.1, Color.PURPLE));
		
		Random rng = new Random();
		
		new BukkitRunnable() {

			@Override
			public void run() {
				remainingDuration -= tickRate;
				if(remainingDuration <= 0) {
					this.cancel();
					return;
				}
				
				druid.spawnShootingStar(locations.get(rng.nextInt(locations.size()))); 
			}
			
		}.runTaskTimer(plugin, 0, tickRate);
			
		this.onTrigger(true);
	}
	
	@Override
	public String getInstructions() {
		return "While crouching, right-click with your bound item type";
	}

	@Override
	public String getDescription() {
		return "Summon a cascade of &6Shooting Stars &r in an area you choose. The area affected has a radius of &6" + radius;
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		radius = 3 + 3 * currentLevel;
		tickRate = 14 - 4 * currentLevel;
	}

	@Override
	public Material getBoundType() {
		return boundType;
	}

	@Override
	public void setBoundType(Material type) {
		boundType = type;
	}

}
