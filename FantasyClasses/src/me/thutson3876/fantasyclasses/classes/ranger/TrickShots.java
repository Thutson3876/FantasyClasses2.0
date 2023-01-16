package me.thutson3876.fantasyclasses.classes.ranger;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.ChatUtils;

public class TrickShots extends AbstractAbility {

	private boolean isOn = false;

	private double doubleTriggerChance = 0.00;

	private BossBar bar;

	public TrickShots(Player p) {
		super(p, Priority.LOW);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 8 * 20;
		this.displayName = "Trick Shots";
		this.skillPointCost = 1;
		this.maximumLevel = 1;
		bar = Bukkit.createBossBar(displayName, BarColor.WHITE, BarStyle.SOLID,
				new org.bukkit.boss.BarFlag[0]);

		this.createItemStack(Material.TNT);
	}

	@EventHandler
	public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent e) {
		if (!e.getPlayer().equals(player))
			return;
		
		if (player.isSneaking())
			return;

		if (isOnCooldown())
			return;
		
		bar.addPlayer(player);
		
		if (this.isOn) {
			this.isOn = false;
			this.bar.setVisible(false);
		} else {
			this.isOn = true;
			this.bar.setVisible(true);
		}
	}

	@EventHandler
	public void onEntityShootBowEvent(EntityShootBowEvent e) {
		if (!e.getEntity().equals(player))
			return;

		if (isOnCooldown())
			return;
		if (!this.isOn)
			return;

		Entity proj = e.getProjectile();

		if (proj instanceof Arrow) {
			this.isOn = false;
			this.bar.setVisible(false);

			Arrow arrow = (Arrow) proj;
			AbstractFantasyClass clazz = this.getFantasyPlayer().getChosenClass();
			if (!(clazz instanceof Ranger))
				return;

			AbilityTriggerEvent thisEvent = this.callEvent();

			if (thisEvent.isCancelled())
				return;

			Ranger ranger = (Ranger) clazz;

			ranger.addTrickArrow(arrow);

			this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
		}
	}

	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		Projectile eventProjectile = e.getEntity();

		if (!(eventProjectile instanceof Arrow)) {
			return;
		}

		Arrow arrow = (Arrow) eventProjectile;
		if(this.getFantasyPlayer() == null)
			return;
		
		AbstractFantasyClass clazz = this.getFantasyPlayer().getChosenClass();
		if (!(clazz instanceof Ranger))
			return;

		Ranger ranger = (Ranger) clazz;

		if (e.getHitBlock() != null) {
			ranger.removeTrickArrow(arrow);
			return;
		}
		
		if(e.isCancelled())
			return;
		
		Random rng = new Random();
		Entity hitEntity = e.getHitEntity();
		

		TrickShot[] trickShots = TrickShot.getRandomOrder();
		int triggerAmt = 1;

		if (hitEntity != null && ranger.getTrickArrows().contains(arrow)) {
			if (rng.nextDouble() < doubleTriggerChance)
				triggerAmt++;

			final int triggerAmtFinal = triggerAmt;

			new BukkitRunnable() {

				@Override
				public void run() {
					for (int i = 0; i < triggerAmtFinal; i++) {
						player.sendMessage(ChatUtils.chat("&cTrick Shot Triggered: &f&o" + trickShots[i].name()));
						player.playSound(player, Sound.BLOCK_SHULKER_BOX_CLOSE, 1.0f, 2.0f);
						trickShots[i].run(arrow, hitEntity);
						
					}
				}

			}.runTaskLater(plugin, 10);
		}

	}

	@Override
	public String getInstructions() {
		return "Crouch to toggle";
	}

	@Override
	public String getDescription() {
		return "Your next shot has chance to be explosive, summon a rain of arrows, ricochet to nearby targets, or grasp all nearby targets and slow them, upon hitting a target. Has a cooldown of &6"
				+ this.coolDowninTicks / 20 + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	protected void init() {
		
	}
	
	@Override
	public void applyLevelModifiers() {
		
	}

	public void setDoubleTriggerChance(double chance) {
		this.doubleTriggerChance = chance;
	}
}
