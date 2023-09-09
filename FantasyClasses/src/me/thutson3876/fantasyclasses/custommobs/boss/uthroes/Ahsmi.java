package me.thutson3876.fantasyclasses.custommobs.boss.uthroes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.collectible.Collectible;
import me.thutson3876.fantasyclasses.custommobs.boss.AbstractBoss;
import me.thutson3876.fantasyclasses.custommobs.horde.Horde;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Ahsmi extends AbstractBoss {

	private final int freezeAmt = 180;
	private final LivingEntity rider;
	private final int abilityDelay = 4 * 20;
	private final PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, 5 * 20, 3);

	private static final List<EntityType> safeEntityTypes;

	static {
		List<EntityType> list = new ArrayList<>();
		list.add(EntityType.ZOMBIE);
		list.add(EntityType.SKELETON);
		list.add(EntityType.STRAY);
		list.add(EntityType.POLAR_BEAR);

		safeEntityTypes = list;
	}

	public Ahsmi(Location loc, LivingEntity rider) {
		super(loc, "&bAhsmi");

		((PolarBear) ent).setAdult();
		this.rider = rider;
		ent.setCustomNameVisible(false);
		if (rider == null) {
			this.setBossBar("&bAhsmi", BarColor.BLUE, BarStyle.SOLID, new BarFlag[0]);
			
			abilities.add(new SummonBrood());
			abilities.add(new WinteryGrasp());
			abilities.add(new FrozenPrison());
		}

		Collection<ItemStack> loot = new HashSet<>();

		Random rng = new Random();
		int extraDrops = 64 - rng.nextInt(40);

		loot.add(new ItemStack(Material.COD, extraDrops));
		loot.add(new ItemStack(Material.SALMON, 64 - extraDrops));
		loot.add(new ItemStack(Material.ENDER_EYE, 12));
		loot.add(new ItemStack(Material.DRAGON_BREATH, 1));
		loot.add(new ItemStack(Material.SHULKER_BOX, 1));
		loot.add(Collectible.generateProfessionResetDrop());

		for(Horde horde : Horde.values()) {
			if(rng.nextDouble() < horde.getDropRate() * 4) {
				loot.add(horde.generateDrop());
				break;
			}	
		}
		
		this.setDrops(loot);

		new BukkitRunnable() {

			@Override
			public void run() {
				startAbilityTick();
			}

		}.runTaskLater(plugin, abilityDelay);

		// nullify freezing
		new BukkitRunnable() {

			@Override
			public void run() {
				if (ent == null || ent.isDead()) {
					this.cancel();
					return;
				}

				ent.setFreezeTicks(0);
			}

		}.runTaskTimer(plugin, 1, 1);

	}

	@Override
	protected void applyDefaults() {
		this.setMaxHealth(500);
		this.setAttackDamage(30);
		this.setSkillExpReward(30);
		// this.setMoveSpeed(0.16f);
	}

	@Override
	public String getMetadataTag() {
		return "ahsmi";
	}

	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		if(!(e.getEntity() instanceof Snowball) || e.isCancelled() || !e.getEntity().getShooter().equals(ent)) {
			return;
		}
		
		Entity target = e.getHitEntity();
		if(target == null)
			return;
		
		target.setVelocity(AbilityUtils.getVectorBetween2Points(target.getLocation(), ent.getLocation(), 0.25));
		
		if(target instanceof LivingEntity) {
			LivingEntity livingTarget = (LivingEntity) target;
			livingTarget.addPotionEffect(slowness);
			livingTarget.setFreezeTicks(livingTarget.getFreezeTicks() + freezeAmt);
			livingTarget.damage(16.0, ent);
		}
		
		ent.getWorld().playSound(ent.getLocation(), Sound.BLOCK_CONDUIT_AMBIENT, 10.0f, 0.8f);
		target.getWorld().playSound(target.getLocation(), Sound.ENTITY_FISHING_BOBBER_RETRIEVE, 10.0f, 0.5f);
	}
	
	@Override
	protected void targeted(EntityTargetEvent e) {
		ent.getWorld().playSound(ent, Sound.ENTITY_POLAR_BEAR_WARNING, 5.0f, 0.8f);
	}

	@Override
	protected void healed(EntityRegainHealthEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void tookDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager().equals(rider)) {
			e.setCancelled(true);
			return;
		}
	}

	@Override
	protected void dealtDamage(EntityDamageByEntityEvent e) {
		e.getEntity().setFreezeTicks(e.getEntity().getFreezeTicks() + freezeAmt);
	}

	@EventHandler
	public void onEntityDeathEvent(EntityDeathEvent e) {
		if(e.getEntity().equals(ent)) {
			if(drops != null && !drops.isEmpty()) {
				for(ItemStack i : drops) {
					e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), i);
				}
			}
			died(e);
			
			HandlerList.unregisterAll(this);
			
			if(task != null && (Bukkit.getScheduler().isCurrentlyRunning(task.getTaskId()) || Bukkit.getScheduler().isQueued(task.getTaskId())))
				task.cancel();
		}
		
		if (e.getEntity().equals(rider)) {
			this.setBossBar("&bAhsmi", BarColor.BLUE, BarStyle.SOLID, new BarFlag[0]);
			
			abilities.add(new SummonBrood());
			abilities.add(new WinteryGrasp());
			abilities.add(new FrozenPrison());
			return;
		}

		if (!e.getEntityType().equals(EntityType.POLAR_BEAR))
			return;
		PolarBear victim = (PolarBear) e.getEntity();
		if (victim.getLocation().distance(ent.getLocation()) > 20.0)
			return;

		if (victim.isAdult())
			return;

		World world = ent.getWorld();
		world.playSound(victim.getLocation(), Sound.ENTITY_WOLF_WHINE, 1.6f, 0.9f);

		for (LivingEntity l : AbilityUtils.getNearbyLivingEntities(ent, 20.0, 20.0, 20.0)) {
			if (l.isDead() || safeEntityTypes.contains(l.getType()))
				continue;

			AbilityUtils.applyStackingPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, 15 * 20, 1), l, 9,
					90 * 20);
		}

		world.playSound(ent, Sound.ENTITY_WOLF_GROWL, 5.0f, 0.8f);
		world.playSound(ent, Sound.ENTITY_GOAT_SCREAMING_PREPARE_RAM, 8.0f, 0.5f);
	}

	@Override
	protected void died(EntityDeathEvent e) {
		ent.getWorld().playSound(ent.getLocation(), Sound.ENTITY_GOAT_SCREAMING_PREPARE_RAM, 12.0f, 0.5f);
		if (rider == null) {
			Uthroes.spawnUthroes(e);
		}
		else if (!rider.isDead()) {
			rider.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60 * 20, 1));
			rider.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60 * 20, 2));
			rider.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60 * 20, 4));
		}
	}

	@Override
	protected EntityType getEntityType() {
		return EntityType.POLAR_BEAR;
	}

}
