package me.thutson3876.fantasyclasses.classes.druid;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.ChatUtils;
import me.thutson3876.fantasyclasses.util.Particles;

public class NaturesBalance extends AbstractAbility implements Bindable {

	private Material boundType = null;
	private int duration = 3 * 20;
	private double maxDistance = 8.0;
	private double dmgMod = 0.1;
	private LivingEntity reductionTarget = null;
	private LivingEntity boostTarget = null;

	public NaturesBalance(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 18 * 20;
		this.displayName = "Nature's Balance";
		this.skillPointCost = 1;
		this.maximumLevel = 5;

		this.createItemStack(Material.VINE);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if (!e.getPlayer().equals(player))
			return;

		if (e.getItem() == null || !e.getItem().getType().equals(this.boundType))
			return;

		if (isOnCooldown())
			return;

		LivingEntity target = null;
		if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			Entity rayTraceTarget = AbilityUtils.rayTraceTarget(player, maxDistance);

			if (rayTraceTarget instanceof LivingEntity)
				target = (LivingEntity) rayTraceTarget;
		}

		if (target == null)
			return;

		//tell player they have buff from chat and particles and sound
		if (player.isSneaking()) {
			reductionTarget = target;
			
			if(target instanceof Player)
				((Player)target).sendMessage(ChatUtils.chat("&6" + player.getDisplayName() + " &3has granted you " + AbilityUtils.doubleRoundToXDecimals(dmgMod * 100.0, 2) + "% &2damage &gREDUCTION for &6" + (duration / 20) + " &3seconds."));
			
			Particles.helix(target, Particle.SUSPENDED, target.getWidth(), 2 * 6.3, 2, 0.1);
			
			new BukkitRunnable() {

				@Override
				public void run() {
					reductionTarget = null;
				}
				
			}.runTaskLater(plugin, duration);
		} else {
			boostTarget = target;
			
			if(target instanceof Player)
				((Player)target).sendMessage(ChatUtils.chat("&6" + player.getDisplayName() + " &3has granted you " + AbilityUtils.doubleRoundToXDecimals(dmgMod * 100.0, 2) + "% &3damage &4INCREASE for &6" + (duration / 20) + " &3seconds."));
			
			Particles.helix(target, Particle.SWEEP_ATTACK, target.getWidth(), 2 * 6.3, 2, 0.1);
			
			new BukkitRunnable() {

				@Override
				public void run() {
					boostTarget = null;
				}
				
			}.runTaskLater(plugin, duration);
		}

		AbilityTriggerEvent thisEvent = this.callEvent();
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		Entity victim = e.getEntity();
		
		if(damager.equals(boostTarget)) {
			e.setDamage(e.getDamage() * (1.0 + dmgMod));
		}
		else if(damager instanceof Projectile) {
			Projectile proj = (Projectile) damager;
			if(proj.getShooter() != null && proj.getShooter().equals(boostTarget)) {
				e.setDamage(e.getDamage() * (1.0 + dmgMod));
			}
		}
		
		if(victim.equals(reductionTarget)) {
			e.setDamage(e.getDamage() * (1.0 - dmgMod));
		}
	}
	
	@Override
	public String getInstructions() {
		return "Right-click with your bound item type";
	}

	@Override
	public String getDescription() {
		return "Grant your target &6" + AbilityUtils.doubleRoundToXDecimals(dmgMod * 100.0, 2) + "% &rmore damage for "
				+ (duration / 20) + " seconds. If used while crouching, grant &6"
				+ AbilityUtils.doubleRoundToXDecimals(dmgMod * 100.0, 2) + "% &rdamage reduction instead";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.dmgMod = 0.1 * currentLevel;
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
