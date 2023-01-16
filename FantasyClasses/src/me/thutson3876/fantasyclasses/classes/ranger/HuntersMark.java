package me.thutson3876.fantasyclasses.classes.ranger;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class HuntersMark extends AbstractAbility implements Bindable {

	private boolean isOn = false;

	private BossBar bar;

	private Arrow arrow = null;

	private Material type = null;

	private double dmgMod = 0.075;
	private int duration = 8 * 20;

	private List<LivingEntity> targets = new ArrayList<>();

	private int numOfTargets = 1;

	private PotionEffect glowing = new PotionEffect(PotionEffectType.GLOWING, duration, 1);

	public HuntersMark(Player p) {
		super(p, Priority.HIGH);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 12 * 20;
		this.displayName = "Hunter's Mark";
		this.skillPointCost = 1;
		this.maximumLevel = 2;
		bar = Bukkit.createBossBar(displayName, BarColor.GREEN, BarStyle.SOLID,
				new org.bukkit.boss.BarFlag[0]);
		
		this.createItemStack(Material.SKELETON_SKULL);
	}

	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		if (!e.getPlayer().equals(player))
			return;

		if (isOnCooldown())
			return;

		if (!e.getItemDrop().getItemStack().getType().equals(this.type))
			return;
		
		e.setCancelled(true);

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
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		Projectile eventProjectile = e.getEntity();
		if (!eventProjectile.equals(this.arrow))
			return;

		if (!(e.getHitEntity() instanceof LivingEntity))
			return;

		LivingEntity hitEntity = (LivingEntity) e.getHitEntity();
		
		if (hitEntity != null) {
			this.targets.add(hitEntity);

			hitEntity.addPotionEffect(glowing);

			List<LivingEntity> nearbyEnts = AbilityUtils.getNearbyLivingEntities(hitEntity, 3, 3, 3);

			for (int i = 0; i < numOfTargets - 1; i++) {
				if(i >= nearbyEnts.size())
					break;
				
				LivingEntity ent = nearbyEnts.get(i);
				if(ent.isDead() || ent.equals(hitEntity)) {
					numOfTargets++;
					continue;
				}
				
				targets.add(ent);
				ent.addPotionEffect(glowing);
			}

			new BukkitRunnable() {

				@Override
				public void run() {
					targets.clear();
				}

			}.runTaskLater(plugin, duration);
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
			AbilityTriggerEvent thisEvent = this.callEvent();
			
			if(thisEvent.isCancelled())
				return;
			
			this.isOn = false;
			this.bar.setVisible(false);
			
			this.arrow = (Arrow) proj;
			
			this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDamageEvent(EntityDamageByEntityEvent e) {
		if (targets.contains(e.getEntity()))
			e.setDamage(e.getDamage() * (1 + dmgMod));
	}
	
	@Override
	public String getInstructions() {
		return "Press your drop key on bound item type to toggle";
	}

	@Override
	public String getDescription() {
		return "Your next arrow shot will make the target take &6" + AbilityUtils.doubleRoundToXDecimals(dmgMod * 100, 1)
				+ "% &rmore damage from all sources and glow for &6" + duration / 20;
	}

	public void setTargetAmt(int amt) {
		this.numOfTargets = amt;
	}
	
	public int getTargetAmt() {
		return numOfTargets;
	}
	
	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	protected void init() {
		bar.addPlayer(player);
		bar.setVisible(false);
	}
	
	@Override
	public void applyLevelModifiers() {
		this.dmgMod = 0.075 * this.currentLevel;
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
