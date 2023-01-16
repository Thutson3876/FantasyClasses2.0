package me.thutson3876.fantasyclasses.custommobs;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.custommobs.boss.Boss;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.ChatUtils;

public abstract class AbstractCustomMob implements Listener {

	protected static final FantasyClasses plugin = FantasyClasses.getPlugin();

	private BukkitTask task = null;

	protected Collection<ItemStack> drops = new ArrayList<>();

	private int skillExpReward = 0;

	protected final LivingEntity ent;
	protected double maxHealth;

	protected AbstractCustomMob(Location loc) {
		this.ent = (LivingEntity) loc.getWorld().spawnEntity(loc, this.getEntityType());
		applyDefaults();
		ent.setMetadata(getMetadataTag(), new FixedMetadataValue(plugin, this));
		plugin.registerEvents(this);
		plugin.getMobManager().addMob(this);
	}

	protected abstract EntityType getEntityType();

	protected void applyDefaults() {

	}

	public void deInit() {

	}

	public LivingEntity getEntity() {
		return ent;
	}

	public void setMaxHealth(double newMax) {
		ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newMax);
		maxHealth = newMax;
		AbilityUtils.heal(ent, newMax, ent);
	}

	public void setAttackDamage(double newDmg) {
		ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(newDmg);
	}

	public void setMoveSpeed(float newSpeed) {
		ent.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(newSpeed);
	}

	public void giveItems(ItemStack[] armorContents, ItemStack mainHand, float dropChance) {
		EntityEquipment equipment = ent.getEquipment();
		if (equipment == null)
			return;
		if (armorContents != null)
			equipment.setArmorContents(armorContents);

		if (mainHand != null)
			equipment.setItemInMainHand(mainHand);

		equipment.setItemInMainHandDropChance(dropChance);
	}

	public void setDrops(Collection<ItemStack> drops) {
		this.drops = drops;
	}

	public Collection<ItemStack> getDrops() {
		return drops;
	}

	public void setTask(BukkitTask task) {
		this.task = task;
	}

	public BukkitTask getTask() {
		return task;
	}

	public int getSkillExpReward() {
		return skillExpReward;
	}

	public void setSkillExpReward(int amt) {
		this.skillExpReward = amt;
	}

	public double getMaxHealth() {
		return this.maxHealth;
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageByEntityEvent e) {
		if (e.getDamager().equals(ent)) {
			dealtDamage(e);
		} else if (e.getEntity().equals(ent)) {
			tookDamage(e);

			// Prevents damage knockback when hit
			if (this instanceof Boss) {
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run() {
						ent.setVelocity(new Vector());
					}
				}, 1L);

				((Boss) this).resetCount();
			}
		}
	}

	@EventHandler
	public void onDamageEvent(EntityDamageEvent e) {
		if (e.isCancelled() || !e.getEntity().equals(ent))
			return;

		if (e.getCause().equals(DamageCause.FALL) || e.getCause().equals(DamageCause.CRAMMING)
				|| e.getCause().equals(DamageCause.DROWNING) || e.getCause().equals(DamageCause.LAVA)
				|| e.getCause().equals(DamageCause.HOT_FLOOR))
			e.setCancelled(true);
	}

	@EventHandler
	public void onTransformationEvent(EntityTransformEvent e) {
		if (e.getEntity().equals(ent))
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDeathEvent(EntityDeathEvent e) {
		if (e.getEntity().equals(ent)) {
			if (!ent.hasMetadata("noexpdrop")) {
				for (Entity entity : ent.getNearbyEntities(20, 20, 20)) {
					if (entity instanceof Player)
						((Player) entity).sendMessage(ChatUtils.chat("&3Loading drops..."));
				}

				new BukkitRunnable() {

					@Override
					public void run() {
						if (drops != null && !drops.isEmpty()) {
							for (ItemStack i : drops) {
								if(i == null || i.getType().isAir())
									continue;
								
								e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), i);
							}

							for (Entity entity : ent.getNearbyEntities(20, 20, 20)) {
								if (entity instanceof Player)
									((Player) entity).sendMessage(ChatUtils.chat("&6Drops loaded!"));
							}
						}
					}

				}.runTaskLater(plugin, 5 * 20);
			}

			died(e);

			HandlerList.unregisterAll(this);

			if (task != null && (Bukkit.getScheduler().isCurrentlyRunning(task.getTaskId())
					|| Bukkit.getScheduler().isQueued(task.getTaskId())))
				task.cancel();
		}

	}

	@EventHandler
	public void onTarget(EntityTargetEvent e) {
		if (e.getEntity().equals(ent))
			targeted(e);
	}

	@EventHandler
	public void onHeal(EntityRegainHealthEvent e) {
		if (e.getEntity().equals(ent))
			healed(e);
	}

	public abstract String getMetadataTag();

	protected abstract void targeted(EntityTargetEvent e);

	protected abstract void healed(EntityRegainHealthEvent e);

	protected abstract void tookDamage(EntityDamageByEntityEvent e);

	protected abstract void tookDamage(EntityDamageEvent e);

	protected abstract void dealtDamage(EntityDamageByEntityEvent e);

	protected abstract void died(EntityDeathEvent e);

}
