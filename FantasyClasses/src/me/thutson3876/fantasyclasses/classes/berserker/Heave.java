package me.thutson3876.fantasyclasses.classes.berserker;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Heave extends AbstractAbility {

	private int duration = 3 * 20;
	private int remaining = 0;
	
	private PotionEffect slowFall = new PotionEffect(PotionEffectType.SLOW_FALLING, 3 * 20, 0);
	private LivingEntity target = null;
	private AbilityTriggerEvent thisEvent = null;
	
	private BossBar bar = null;
	
	private int taskId = -1;
	
	public Heave(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 16 * 20;
		this.displayName = "Heave";
		this.skillPointCost = 2;
		this.maximumLevel = 2;

		this.createItemStack(Material.PISTON);
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		if(this.isOnCooldown())
			return;
		
		if(!e.getDamager().equals(this.player))
			return;
		
		if(player.getAttackCooldown() < 1.0)
			return;
		
		if(!e.getCause().equals(DamageCause.ENTITY_ATTACK))
			return;
		
		if(!AbilityUtils.isCritical(player))
			return;
		
		if(!(e.getEntity() instanceof LivingEntity))
			return;
		
		LivingEntity le = (LivingEntity) e.getEntity();
		
		if(le.isDead())
			return;
		
		thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		if(!e.getEntity().equals(target)) {
			if(this.taskId == -1) {
				remaining = this.duration;
				this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new UplinkReducingTask(), 1L, 1L);
			}
			else {
				Bukkit.getScheduler().cancelTask(taskId);
				remaining = this.duration;
				this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new UplinkReducingTask(), 1L, 1L);
			}
			target = le;
			
			return;
		}
		else if(remaining > 0) {
			remaining = 0;
			if(this.thisEvent != null) {
				launch();
				this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
				this.thisEvent = null;
			}
		}
	}
	
	private void launch() {
		Vector newVelocity = new Vector(0, 1.5, 0);
		double speed = 1.0;
		
		newVelocity.add(player.getEyeLocation().getDirection().normalize().multiply(0.2));
		newVelocity.multiply(speed);
		
		player.getWorld().playSound(player, Sound.ENTITY_TURTLE_EGG_CRACK, 1.5f, 0.9f);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				if(target == null)
					return;
				
				target.setVelocity(newVelocity);
				target = null;
			}
			
		}.runTaskLater(plugin, 1);
		
		player.setVelocity(newVelocity);
		
		target.addPotionEffect(slowFall);
		player.addPotionEffect(slowFall);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}
	
	private void tick() {
		if (bar == null && remaining <= 0) {
			Bukkit.getScheduler().cancelTask(taskId);
			taskId = -1;
			this.target = null;
			return;
		}

		if (bar == null && remaining > 0) {
			bar = Bukkit.createBossBar(this.getName(), BarColor.WHITE, BarStyle.SOLID, new org.bukkit.boss.BarFlag[0]);
			bar.addPlayer(player);
			remaining--;
			return;
		}
		if (bar != null && remaining <= 0) {
			bar.setVisible(false);
			bar = null;
			Bukkit.getScheduler().cancelTask(taskId);
			taskId = -1;
			this.target = null;
			return;
		}
		if (bar != null && remaining > 0) {
			double value = remaining / (double)duration;
			value = Math.min(Math.max(0.0D, value), 1.0D);
			bar.setProgress(value);
			if (!bar.isVisible()) {
				bar.setVisible(true);
			}
			remaining--;
		}
	}

	@Override
	public String getInstructions() {
		return "Crit the same target twice in a row";
	}

	@Override
	public String getDescription() {
		return "Landing two subsequent critical strikes within &6" + this.duration / 20 + " &rseconds launches you and your target high into the sky, granting you both slow fall";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.duration = 3 * this.currentLevel * 20;
	}
	
	@Override
	public void deInit() {
		if(this.bar != null) {
			this.bar.setVisible(false);
			this.bar = null;
		}
		
		this.remaining = 0;
	}
	
	protected class UplinkReducingTask implements Runnable {
		public void run() {
			tick();
		}
	}

	
	
}
