package me.thutson3876.fantasyclasses.classes.monk;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.Particles;

public class ChiExpulsion extends AbstractAbility implements Bindable {

	private Material type = null;

	private int remaining = 0;

	private AbilityTriggerEvent thisEvent = null;

	private BossBar bar = null;

	private int taskId = -1;

	private int duration = 6 * 20;
	private double radius = 5.0;
	private int tickRate = 2;
	private double dmgConversion = 1.00;
	private double storedDamage = 0.0;

	public ChiExpulsion(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 12 * 20;
		this.displayName = "Chi Expulsion";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.TNT_MINECART);
	}

	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		if (isOnCooldown())
			return;

		if (!e.getPlayer().equals(player))
			return;

		if (!e.getItemDrop().getItemStack().getType().equals(type))
			return;

		thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;

		e.setCancelled(true);

		if (this.taskId == -1) {
			remaining = this.duration;
			this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new UplinkReducingTask(), 1L, 1L);
		} else {
			Bukkit.getScheduler().cancelTask(taskId);
			remaining = this.duration;
			this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new UplinkReducingTask(), 1L, 1L);
		}
		
		Particles.helix(player, Particle.SNEEZE, 0.8, 12.554, 5, 0.12);
		player.getWorld().playSound(player, Sound.ENTITY_PHANTOM_AMBIENT, 1.3f, 1.2f);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if (e.isCancelled())
			return;

		if (this.isOnCooldown())
			return;

		if (this.remaining <= 0)
			return;

		if (!e.getDamager().equals(this.player))
			return;

		if (!e.getCause().equals(DamageCause.ENTITY_ATTACK))
			return;

		if (!(e.getEntity() instanceof LivingEntity))
			return;

		if (e.getEntity().isDead())
			return;

		storedDamage += e.getDamage();
	}

	private void tick() {
		if (bar == null && remaining <= 0) {
			Bukkit.getScheduler().cancelTask(taskId);
			thisEvent = null;
			taskId = -1;
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
			
			expulsion();
			
			if (thisEvent != null)
				this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());

			this.thisEvent = null;
			Bukkit.getScheduler().cancelTask(taskId);
			taskId = -1;
			return;
		}
		if (bar != null && remaining > 0) {
			double value = remaining / (double) duration;
			value = Math.min(Math.max(0.0D, value), 1.0D);
			bar.setProgress(value);
			if (!bar.isVisible()) {
				bar.setVisible(true);
			}
			
			if(remaining % 20 == 0)
				Particles.helix(player, Particle.SNEEZE, 0.8, 12.554, 5, 0.12);
			
			remaining--;
		}
	}
	
	private void expulsion() {
		if(this.storedDamage <= 0)
			return;
		
		player.getWorld().playSound(player, Sound.ENTITY_PHANTOM_AMBIENT, 1.3f, 1.2f);
		Particles.explosion(player.getLocation().add(0, 1, 0), Particle.EXPLOSION_NORMAL, radius, tickRate);
		
		AbstractFantasyClass clazz = this.getFantasyPlayer().getChosenClass();
		if (!(clazz instanceof Monk))
			return;

		Monk monk = (Monk) clazz;

		boolean hasFriendlyFire = fplayer.hasFriendlyFire();
		boolean isHealer = monk.isHealer();
		
		new BukkitRunnable() {

			@Override
			public void run() {
				
				
				for(Entity ent : player.getNearbyEntities(radius, radius, radius)) {
					if(!(ent instanceof LivingEntity) || ent.equals(player) || ent.isDead())
						continue;
					
					if (ent instanceof Player) {
						if (isHealer) {
							AbilityUtils.heal(player, storedDamage, (Player) ent);
							continue;
						} else if (!hasFriendlyFire) {
							continue;
						}
					}
					
					((LivingEntity)ent).damage(storedDamage, player);
				}
				
				player.getWorld().playSound(player, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.3f, 1.2f);
				storedDamage = 0;
			}
			
		}.runTaskLater(plugin, ((int)radius) * tickRate - tickRate);
	}

	@Override
	public String getInstructions() {
		return "Drop bound item type";
	}

	@Override
	public String getDescription() {
		return "Harness the Chi of those around you, storing the "
				+ AbilityUtils.doubleRoundToXDecimals(dmgConversion * 100, duration)
				+ "damage you deal, and releasing it upon all nearby creatures after &6" + duration / 20 + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {

	}

	@Override
	public void deInit() {
		if (this.bar != null) {
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

	@Override
	public Material getBoundType() {
		return type;
	}

	@Override
	public void setBoundType(Material type) {
		this.type = type;
	}

}
