package me.thutson3876.fantasyclasses.custommobs.boss.uthroes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.collectible.Collectible;
import me.thutson3876.fantasyclasses.custommobs.boss.AbstractBoss;
import me.thutson3876.fantasyclasses.custommobs.horde.Horde;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.NoExpDrop;
import me.thutson3876.fantasyclasses.util.Particles;

public class Uthroes extends AbstractBoss {

	private final PolarBear ahsmi;
	private final PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, 5 * 20, 3);

	private static final List<EntityType> safeEntityTypes;

	private final int freezeAmt = 160;

	static {
		List<EntityType> list = new ArrayList<>();
		list.add(EntityType.ZOMBIE);
		list.add(EntityType.SKELETON);
		list.add(EntityType.STRAY);
		list.add(EntityType.POLAR_BEAR);

		safeEntityTypes = list;
	}

	public Uthroes(Location loc) {
		super(loc, "&bUthroes");
		this.setBossBar("&bUthroes", BarColor.BLUE, BarStyle.SOLID, new BarFlag[0]);

		((Ageable) ent).setAdult();

		Collection<ItemStack> loot = new HashSet<>();

		loot.add(new ItemStack(Material.CONDUIT));
		loot.add(new ItemStack(Material.FROSTED_ICE, 64));
		loot.add(new ItemStack(Material.BLUE_ICE, 64));
		loot.add(new ItemStack(Material.DRAGON_BREATH, 2));
		loot.add(new ItemStack(Material.SHULKER_BOX, 2));
		for(int i = 0; i < 3; i++)
			loot.add(Collectible.generateProfessionResetDrop());

		Random rng = new Random();
		for(Horde horde : Horde.values()) {
			if(rng.nextDouble() < horde.getDropRate() * 2) {
				loot.add(horde.generateDrop());
				break;
			}	
		}
		
		this.setDrops(loot);

		this.setGear();

		ahsmi = (PolarBear) (new Ahsmi(loc, ent)).getEntity();
		ahsmi.addPassenger(ent);

		abilities.add(new RemorselessWinter());
		abilities.add(new WinteryGrasp());
		abilities.add(new SummonUndead());
		abilities.add(new LeechingGrasp());
		//abilities.add(new PlagueUnleashed());

		this.startAbilityTick();

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
		this.setMaxHealth(400);
		this.setAttackDamage(35);
		this.setSkillExpReward(120);
		this.setMoveSpeed(0.3f);
	}

	@Override
	protected void targeted(EntityTargetEvent e) {
		if (e.getTarget() instanceof LivingEntity)
			ahsmi.setTarget((LivingEntity) e.getTarget());
	}

	@Override
	protected void dealtDamage(EntityDamageByEntityEvent e) {
		if (e.getFinalDamage() < 1.0 || !(e.getEntity() instanceof LivingEntity))
			return;

		AbilityUtils.heal(this.ent, e.getFinalDamage(), ahsmi);
		e.getEntity().setFreezeTicks(e.getEntity().getFreezeTicks() + freezeAmt);
	}

	@Override
	protected void tookDamage(EntityDamageByEntityEvent e) {

	}

	@Override
	protected void tookDamage(EntityDamageEvent e) {
		if (e.getCause().equals(DamageCause.FREEZE))
			e.setCancelled(true);

		ent.setFreezeTicks(0);
	}

	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		if (!(e.getEntity() instanceof Snowball) || e.isCancelled() || !e.getEntity().getShooter().equals(ent)) {
			return;
		}

		Entity target = e.getHitEntity();
		if (target == null)
			return;

		target.setVelocity(AbilityUtils.getVectorBetween2Points(target.getLocation(), ent.getLocation(), 0.25));

		if (target instanceof LivingEntity) {
			LivingEntity livingTarget = (LivingEntity) target;
			livingTarget.addPotionEffect(slowness);
			livingTarget.setFreezeTicks(livingTarget.getFreezeTicks() + freezeAmt);
			livingTarget.damage(16.0, ent);
		}

		ent.getWorld().playSound(ent.getLocation(), Sound.BLOCK_CONDUIT_AMBIENT, 10.0f, 0.8f);
		target.getWorld().playSound(target.getLocation(), Sound.ENTITY_FISHING_BOBBER_RETRIEVE, 10.0f, 0.5f);
	}

	@Override
	public String getMetadataTag() {
		return "uthroes";
	}

	private void setGear() {
		EntityEquipment equip = ent.getEquipment();

		ItemStack boots = new ItemStack(Material.NETHERITE_BOOTS);
		boots.addEnchantment(Enchantment.BINDING_CURSE, 1);
		boots.addEnchantment(Enchantment.DURABILITY, 3);
		boots = AbilityUtils.setDisplayName("&bThermal Netherite Boots", boots);
		ItemStack legs = new ItemStack(Material.NETHERITE_LEGGINGS);
		legs.addEnchantment(Enchantment.BINDING_CURSE, 1);
		legs.addEnchantment(Enchantment.DURABILITY, 3);
		legs = AbilityUtils.setDisplayName("&bThermal Netherite Leggings", legs);
		ItemStack chest = new ItemStack(Material.NETHERITE_CHESTPLATE);
		chest.addEnchantment(Enchantment.BINDING_CURSE, 1);
		chest.addEnchantment(Enchantment.DURABILITY, 3);
		chest = AbilityUtils.setDisplayName("&bThermal Netherite Chestplate", chest);
		ItemStack helm = new ItemStack(Material.NETHERITE_HELMET);
		helm.addEnchantment(Enchantment.BINDING_CURSE, 1);
		helm.addEnchantment(Enchantment.DURABILITY, 3);
		helm = AbilityUtils.setDisplayName("&bThermal Netherite Helmet", helm);

		equip.setBoots(boots);
		equip.setBootsDropChance(0.1f);
		equip.setLeggings(legs);
		equip.setBootsDropChance(0.1f);
		equip.setChestplate(chest);
		equip.setChestplateDropChance(0.1f);
		equip.setHelmet(helm);
		equip.setHelmetDropChance(0.1f);

		equip.setItemInMainHand(new ItemStack(Material.TRIDENT));
		equip.setItemInMainHandDropChance(1.0f);
	}

	@Override
	protected EntityType getEntityType() {
		return EntityType.DROWNED;
	}

	@EventHandler
	public void onTargetEvent(EntityTargetEvent e) {
		if (e.getTarget() == null)
			return;

		if (safeEntityTypes.contains(e.getEntityType()) && safeEntityTypes.contains(e.getTarget().getType()))
			e.setCancelled(true);
	}

	public static void spawnUthroes(EntityDeathEvent e) {
		LivingEntity dead = e.getEntity();
		World world = dead.getWorld();
		Location loc = dead.getLocation();

		if (!(dead instanceof Mob))
			return;
		
		List<LivingEntity> nearby = AbilityUtils.getNearbyLivingEntities(loc, 12.0, 12.0, 12.0);
		int polarCount = 0;
		for(LivingEntity l : nearby) {
			if(l instanceof PolarBear)
				polarCount++;
		}
		int finalCount = polarCount;
		
		new BukkitRunnable() {

			@Override
			public void run() {
				(new LeechingGrasp()).run((Mob) dead);
				new BukkitRunnable() {

					@Override
					public void run() {
						for(LivingEntity l : AbilityUtils.getNearbyLivingEntities(loc, 12.0, 12.0, 12.0)) {
							if(l instanceof PolarBear)
								l.damage(100.0);
						}
						
						if(finalCount < 8)
							return;
						
						world.strikeLightning(loc);
						
						new BukkitRunnable() {
							
							@Override
							public void run() {
								world.playSound(loc, Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 5.5f, 0.9f);
								world.playSound(loc, Sound.ENTITY_ZOMBIE_AMBIENT, 1.0f, 0.9f);
								loc.getBlock().setType(Material.RESPAWN_ANCHOR);
								loc.getBlock().setMetadata("spawnuthroes", new NoExpDrop());
								
								new BukkitRunnable() {

									@Override
									public void run() {
										if(!loc.getBlock().getType().equals(Material.RESPAWN_ANCHOR)) {
											this.cancel();
											return;
										}
										
										world.playSound(loc, Sound.BLOCK_CONDUIT_AMBIENT, 1.5f, 0.9f);
										Particles.helix(loc, Particle.NAUTILUS, 1.6, 2 * 6.3, 5, 3);
									}
									
								}.runTaskTimer(plugin, 1, 40);
								
								new BukkitRunnable() {
									
									@Override
									public void run() {
										if(loc.getBlock().hasMetadata("spawnuthroes"))
											loc.getBlock().setType(Material.AIR);
									}
								}.runTaskLater(plugin, 60 * 20);
							}
							
						}.runTaskLater(plugin, 6);
						
					}
					
				}.runTaskLater(plugin, 3);
			}

		}.runTaskLater(plugin, 3 * 20);
		
		

	}

}
