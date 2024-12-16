package me.thutson3876.fantasyclasses.classes.druid;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.particles.CustomParticle;
import me.thutson3876.fantasyclasses.util.particles.GeneralParticleEffects;

public class SolarBeam extends AbstractAbility implements Bindable {

	private Material boundType = null;
	private int duration = 8 * 20;
	private int tickRate = 10;
	private int radius = 6;
	
	private int remainingDuration;
	
	public SolarBeam(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 16 * 20;
		this.displayName = "Solar Beam";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.SUNFLOWER);	
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
		

		Location center = e.getClickedBlock().getLocation();
		
		
		remainingDuration = duration;
		
		GeneralParticleEffects.doubleEndedCircle(center, radius, tickRate, currentLevel, duration, new CustomParticle(Particle.DUST, 3, 0.1, Color.WHITE));
		GeneralParticleEffects.durationBasedHelix(center, new CustomParticle(Particle.DUST_PILLAR, 1, 0, Color.YELLOW), radius, 720, duration, tickRate, 10);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				remainingDuration -= tickRate;
				if(remainingDuration <= 0) {
					this.cancel();
					return;
				}
				
				for(LivingEntity ent : AbilityUtils.getNearbyLivingEntities(center, radius, radius, radius)) {
					if(ent instanceof Monster) {
						ent.setFireTicks(ent.getFireTicks() + tickRate + 1);
						continue;
					}
						
					
					AbilityUtils.heal(ent, coolDowninTicks, ent);
				}
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
		return "Summon a beam of sunlight that heals anything inside, except monsters. It burns those. The area affected has a radius of &6" + radius;
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		radius = 3 + 3 * currentLevel;
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
