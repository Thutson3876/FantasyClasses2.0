package me.thutson3876.fantasyclasses.classes.ranger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;

public class QuiverOfTricks extends AbstractAbility implements Bindable {

	private Material type = null;

	private AbilityTriggerEvent thisEvent = null;
	
	private double remaining = 0;
	
	private int taskId;

	private BossBar bar = null;

	private int duration = 4 * 20;

	public QuiverOfTricks(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 20 * 20;
		this.displayName = "Quiver Of Tricks";
		this.skillPointCost = 2;
		this.maximumLevel = 2;

		this.createItemStack(Material.FIREWORK_ROCKET);
	}
	
	@EventHandler
	public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {
		if (e.isCancelled())
			return;

		if (isOnCooldown())
			return;

		if (!e.getPlayer().equals(player))
			return;

		boolean hasBoundType = false;
		if (e.getMainHandItem() != null) {
			hasBoundType = e.getMainHandItem().getType().equals(type);
		}
		if (!hasBoundType && e.getOffHandItem() != null) {
			hasBoundType = e.getOffHandItem().getType().equals(type);
		}
		if (!hasBoundType)
			return;
		if(remaining > 0)
			return;

		thisEvent = this.callEvent();

		if(thisEvent.isCancelled()) {
			thisEvent = null;
			return;
		}
		
		e.setCancelled(true);
		remaining = this.duration;
		init();
	}

	@EventHandler
	public void onEntityShootBowEvent(EntityShootBowEvent e) {
		if (!e.getEntity().equals(player))
			return;
		
		if(this.remaining <= 0)
			return;

		Entity proj = e.getProjectile();

		if (e.getProjectile() instanceof Arrow) {
			Arrow arrow = (Arrow) proj;
			AbstractFantasyClass clazz = this.getFantasyPlayer().getChosenClass();
			if (!(clazz instanceof Ranger))
				return;

			Ranger ranger = (Ranger) clazz;

			ranger.addTrickArrow(arrow);
		}

	}

	private void tick() {
		if (bar == null && remaining <= 0) {
			Bukkit.getScheduler().cancelTask(taskId);
			return;
		}

		if (bar == null && remaining > 0) {
			bar = Bukkit.createBossBar(this.getName(), BarColor.WHITE, BarStyle.SEGMENTED_12, new org.bukkit.boss.BarFlag[0]);
			bar.addPlayer(player);
			remaining--;
			return;
		}
		if (bar != null && remaining <= 0) {
			if(this.thisEvent != null) {
				this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
				this.thisEvent = null;
			}
			bar.setVisible(false);
			bar = null;
			Bukkit.getScheduler().cancelTask(taskId);
			return;
		}
		if (bar != null && remaining > 0) {
			double value = remaining / ((double)duration);
			value = Math.min(Math.max(0.0D, value), 1.0D);
			bar.setProgress(value);
			if (!bar.isVisible()) {
				bar.setVisible(true);
			}
			remaining--;
		}
	}
	
	@Override
	protected void init() {
		this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new UplinkReducingTask(), 1L, 1L);
		
		/*bar = Bukkit.createBossBar(this.getName(), BarColor.WHITE, BarStyle.SEGMENTED_12, new org.bukkit.boss.BarFlag[0]);
		bar.addPlayer(player);
		bar.setVisible(false);*/
	}
	
	@Override
	public void deInit() {
		if(this.bar != null) {
			this.bar.setVisible(false);
			this.bar = null;
		}
		
		this.remaining = 0;
	}

	@Override
	public String getInstructions() {
		return "Swap hands while holding bound item type";
	}

	@Override
	public String getDescription() {
		return "For the next &6" + this.duration / 20 + " &rseconds all of your shots are Trick Shots";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.duration = 4 * this.currentLevel * 20;
	}

	@Override
	public Material getBoundType() {
		return type;
	}

	@Override
	public void setBoundType(Material type) {
		this.type = type;
	}
	
	protected class UplinkReducingTask implements Runnable {
		
		@Override
		public void run() {
			tick();
		}
	}
	
	public void addToRemainingDuration(double addAmt) {
		if(this.remaining > 0)
			this.remaining += addAmt;
	}

}
