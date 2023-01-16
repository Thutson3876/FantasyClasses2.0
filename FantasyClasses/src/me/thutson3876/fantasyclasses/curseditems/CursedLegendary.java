package me.thutson3876.fantasyclasses.curseditems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.MaterialLists;

public enum CursedLegendary {

	LIGHTNING_RANGE("lightning", 
			(event, owner) -> {
				if(!(event instanceof ProjectileHitEvent))
					return;
				
				ProjectileHitEvent e = (ProjectileHitEvent)event;
				
				if(e.isCancelled())
					return;
				
				Projectile proj = e.getEntity();
				if(proj.isVisualFire())
					return;
				
				if(proj instanceof AbstractArrow && ((AbstractArrow)proj).getPierceLevel() > 0) {
					return;
				}
				
				if(!proj.getShooter().equals(owner))
					return;
				
				Random rng = new Random();
				if(rng.nextBoolean())
					return;
				
				if(e.getHitEntity() != null) {
					new BukkitRunnable() {

						@Override
						public void run() {
							e.getEntity().getWorld().strikeLightning(e.getHitEntity().getLocation());
						}
						
					}.runTaskLater(FantasyClasses.getPlugin(), 5);
				}
				
			}, MaterialLists.RANGE_WEAPON.getMaterials(), 
			"Summons a lightning bolt to strike your target (50% chance)"), 
	RISING_RANGE("rising", (event, owner)->{
		if(!(event instanceof ProjectileHitEvent))
			return;
		
		ProjectileHitEvent e = (ProjectileHitEvent)event;
		
		if(e.isCancelled())
			return;
		
		Projectile proj = e.getEntity();
		
		if(!proj.getShooter().equals(owner))
			return;
		
		if(e.getHitEntity() instanceof LivingEntity)
			((LivingEntity)e.getHitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 2 * 20, 0));
		
	}, MaterialLists.RANGE_WEAPON.getMaterials(), 
			"Causes your target to float for a short time"), 
	GRASPING_RANGE("grasping", (event, owner)->{
		if(!(event instanceof ProjectileHitEvent))
			return;
		
		ProjectileHitEvent e = (ProjectileHitEvent)event;
		
		if(e.isCancelled())
			return;
		
		Projectile proj = e.getEntity();
		
		if(!proj.getShooter().equals(owner))
			return;
		
		if(e.getHitEntity() instanceof LivingEntity) {
			LivingEntity target = (LivingEntity) e.getHitEntity();
			new BukkitRunnable() {

				@Override
				public void run() {
					target.setVelocity(AbilityUtils.getVectorBetween2Points(target.getLocation(), owner.getEyeLocation(), 0.25));
				}
				
			}.runTaskLater(FantasyClasses.getPlugin(), 1);
		}
	}, MaterialLists.RANGE_WEAPON.getMaterials(), 
			"Pulls your target towards you"),
	UNENDING_STRENGTH_RANGE("unending strength", (event, owner)->{
		if(!(event instanceof ProjectileHitEvent))
			return;
		
		ProjectileHitEvent e = (ProjectileHitEvent)event;
		
		if(e.isCancelled())
			return;
		
		Projectile proj = e.getEntity();
		
		if(!proj.getShooter().equals(owner))
			return;
		
		if(e.getHitEntity() instanceof LivingEntity) {
			AbilityUtils.applyStackingPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2 * 20, 0), owner, 4, 3 * 20);
		}
	}, MaterialLists.RANGE_WEAPON.getMaterials(), 
			"Grants you stacking strength for each arrow landed into a target"),
	LIGHTNING_MELEE("lightning", (event, owner)->{
		if(!(event instanceof EntityDamageByEntityEvent))
			return;
		
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
		
		Random rng = new Random();
		if(!rng.nextBoolean())
			return;
		
		if(e.isCancelled() || e.getDamage() < 0.5)
			return;
		
		if(owner instanceof Player) {
			if(((Player)owner).getAttackCooldown() < 1.0) {
				return;
			}
		}
		
		if(e.getDamager().equals(owner)) {
			new BukkitRunnable() {

				@Override
				public void run() {
					e.getEntity().getWorld().strikeLightning(e.getEntity().getLocation());
				}
				
			}.runTaskLater(FantasyClasses.getPlugin(), 5);
		}
			
		
	}, MaterialLists.MELEE_WEAPON.getMaterials(), 
			"Summons a lightning bolt to strike your target (50% chance)"), 
	FREEZING_MELEE("freezing", (event, owner)->{
		if(!(event instanceof EntityDamageByEntityEvent))
			return;
		
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
		
		if(e.isCancelled() || e.getDamage() < 0.5)
			return;
		
		if(owner instanceof Player) {
			if(((Player)owner).getAttackCooldown() < 1.0) {
				return;
			}
		}
		
		if(!e.getDamager().equals(owner))
			return;
		
		if(e.getEntity() instanceof LivingEntity) {
			LivingEntity l = (LivingEntity)e.getEntity();
			l.setFreezeTicks(l.getFreezeTicks() + 80);
		}
		
	}, MaterialLists.MELEE_WEAPON.getMaterials(), 
			"Freeze your target on hit"), 
	LEECHING_MELEE("leeching", (event, owner)->{
				if(!(event instanceof EntityDamageByEntityEvent))
					return;
				
				EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
				
				if(e.isCancelled() || e.getDamage() < 0.5)
					return;
				
				if(owner instanceof Player) {
					if(((Player)owner).getAttackCooldown() < 1.0) {
						return;
					}
				}
				
				if(e.getDamager().equals(owner))
					AbilityUtils.heal(owner, e.getDamage() / 5.0, owner);
				
			}, MaterialLists.MELEE_WEAPON.getMaterials(), 
			"Heals you for 20% the damage you deal"),
	ENSNARING_MELEE("ensnaring", (event, owner)->{
		if(!(event instanceof EntityDamageByEntityEvent))
			return;
		
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
		
		if(e.isCancelled() || e.getDamage() < 0.5)
			return;
		
		if(owner instanceof Player) {
			if(((Player)owner).getAttackCooldown() < 1.0) {
				return;
			}
		}
		
		if(!e.getDamager().equals(owner))
			return;
		
		if(e.getEntity() instanceof LivingEntity)
			((LivingEntity)e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2 * 20, 1));
		
	}, MaterialLists.MELEE_WEAPON.getMaterials(), 
			"Slows your target on hit"),
	SHATTERING_MELEE("shattering", (event, owner)->{
		if(!(event instanceof EntityDamageByEntityEvent))
			return;
		
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
		
		if(e.isCancelled() || e.getDamage() < 0.5)
			return;
		
		if(owner instanceof Player) {
			if(((Player)owner).getAttackCooldown() < 1.0) {
				return;
			}
		}
		
		if(!e.getDamager().equals(owner))
			return;
		
		if(e.getEntity() instanceof LivingEntity) {
			int armorAmt = ((LivingEntity)e.getEntity()).getEquipment().getArmorContents().length;
			e.setDamage(e.getDamage() * (1 + (armorAmt * 0.05)));
		}
			
	}, MaterialLists.MELEE_WEAPON.getMaterials(), 
			"Deals 5% more damage for each piece of armor your target is wearing"), 
	FREEZING_ARMOR("freezing", (event, owner)->{
		
		if(!(event instanceof EntityDamageByEntityEvent))
			return;
		
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
		
		if(e.isCancelled() || e.getDamage() < 0.5)
			return;
		
		if(owner instanceof Player) {
			if(((Player)owner).getAttackCooldown() < 1.0) {
				return;
			}
		}
		
		if(!e.getEntity().equals(owner))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof LivingEntity))
			return;
		
		((LivingEntity)damager).setFreezeTicks(((LivingEntity)damager).getFreezeTicks() + 30);
		
	}, MaterialLists.ARMOR.getMaterials(), 
			"Freezes anyone who strikes you"),
	FLAMING_ARMOR("flaming", (event, owner)->{
		
		if(!(event instanceof EntityDamageByEntityEvent))
			return;
		
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
		
		if(e.isCancelled() || e.getDamage() < 0.5)
			return;
		
		if(!e.getEntity().equals(owner))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof LivingEntity))
			return;
		
		((LivingEntity)damager).setFireTicks(((LivingEntity)damager).getFireTicks() + 10);
		
	}, MaterialLists.ARMOR.getMaterials(), 
			"Sets aflame anyone who strikes you"),
	ENFEEBLEMENT_ARMOR("enfeeblement", (event, owner)->{
		
		if(!(event instanceof EntityDamageByEntityEvent))
			return;
		
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
		
		if(e.isCancelled() || e.getDamage() < 0.5)
			return;
		
		if(!e.getEntity().equals(owner))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof LivingEntity))
			return;
		
		((LivingEntity)damager).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 4 * 20, 0));
		
	}, MaterialLists.ARMOR.getMaterials(), 
			"Weakens anyone who strikes you"),
	UNENDING_STRENGTH_ARMOR("unending strength", (event, owner)->{
		
		if(!(event instanceof EntityDamageByEntityEvent))
			return;
		
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
		
		if(e.isCancelled() || e.getDamage() < 0.5)
			return;
		
		if(!e.getEntity().equals(owner))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof LivingEntity))
			return;
		
		AbilityUtils.applyStackingPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 15, 0), owner, 5, 10 * 20);
		
	}, MaterialLists.ARMOR.getMaterials(), 
			"Strengthens you for each strike you suffer in a short time"),
	RECKLESSNESS_ARMOR("recklessness", (event, owner)->{
		
		if(!(event instanceof EntityDamageByEntityEvent))
			return;
		
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
		
		if(e.isCancelled() || e.getDamage() < 0.5)
			return;
		
		if(!e.getEntity().equals(owner) && !e.getDamager().equals(owner))
			return;
		
		e.setDamage(e.getDamage() * 1.05);
		
	}, MaterialLists.ARMOR.getMaterials(), 
			"Increases the damage you take and deal by 5%"),
	FORTITUDE_ARMOR("fortitude", (event, owner)->{
		if(!(event instanceof EntityDamageByEntityEvent))
			return;
		
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
		
		if(e.isCancelled() || e.getDamage() < 0.5)
			return;
		
		if(!e.getEntity().equals(owner))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof LivingEntity))
			return;
		
		e.setDamage(e.getDamage() * 0.975);
	}, MaterialLists.ARMOR.getMaterials(), 
			"Reduces the damage you take and deal by 2.5%"), 
	RESILIENCE_ARMOR("resilience", (event, owner)->{
		if(!(event instanceof EntityDamageByEntityEvent))
			return;
		
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
		
		if(e.isCancelled() || e.getDamage() < 0.5)
			return;
		
		if(!e.getEntity().equals(owner))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof LivingEntity))
			return;
		
		if(e.getDamage() > 1.5)
			e.setDamage(e.getDamage() - 0.4);
		
	}, MaterialLists.ARMOR.getMaterials(), 
			"Reduces each instance of damage you take by 0.4"), 
	DAMPENING_ARMOR("dampening", (event, owner)->{
		if(!(event instanceof EntityDamageByEntityEvent))
			return;
		
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
		
		if(e.isCancelled() || e.getDamage() < 0.5)
			return;
		
		if(!e.getEntity().equals(owner))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof LivingEntity))
			return;
		
		if(e.getDamage() > 10.0) {
			double reductionAmt = 0.01 * e.getFinalDamage();
			if(reductionAmt > 0.15)
				reductionAmt = 0.15;
			
			e.setDamage(e.getDamage() * (1 - reductionAmt));
		}	
		
	}, MaterialLists.ARMOR.getMaterials(), 
			"Reduces the damage of heavy blows dealt to you"), 
	CONDUCTIVITY_ARMOR("conductivity", (event, owner)->{
		if(!(event instanceof EntityDamageEvent))
			return;
		
		EntityDamageEvent e = (EntityDamageEvent) event;
		
		if(e.isCancelled() || e.getDamage() < 0.5)
			return;
		
		if(!e.getEntity().equals(owner))
			return;
			
		if(e.getCause().equals(DamageCause.LIGHTNING))
			e.setDamage(e.getDamage() * 0.7);
			
	}, MaterialLists.ARMOR.getMaterials(), 
			"Reduces lightning damage by 30%"), 
	THERMAL_ARMOR("thermal", (event, owner)->{
		if(!(event instanceof EntityDamageEvent))
			return;
		
		EntityDamageEvent e = (EntityDamageEvent) event;
		
		if(e.isCancelled() || e.getDamage() < 0.5)
			return;
		
		if(!e.getEntity().equals(owner))
			return;
			
		if(owner.getFreezeTicks() < 21)
			return;
		
		owner.setFreezeTicks(owner.getFreezeTicks() - 20);
			
	}, MaterialLists.ARMOR.getMaterials(), 
			"Reduces freeze duration whenever you suffer freeze damage"), 
	CHILLED_ARMOR("chilled", (event, owner)->{
		if(!(event instanceof EntityDamageEvent))
			return;
		
		EntityDamageEvent e = (EntityDamageEvent) event;
		
		if(e.isCancelled() || e.getDamage() < 0.5)
			return;
		
		if(!e.getEntity().equals(owner))
			return;
			
		owner.setFireTicks(owner.getFireTicks() - 10);
			
	}, MaterialLists.ARMOR.getMaterials(), 
			"Reduces fire duration whenever you suffer fire damage"), 
	BLASTPROOFING_ARMOR("blastproofing", (event, owner)->{
		if(!(event instanceof EntityDamageEvent))
			return;
		
		EntityDamageEvent e = (EntityDamageEvent) event;
		
		if(e.isCancelled() || e.getDamage() < 0.5)
			return;
		
		if(!e.getEntity().equals(owner))
			return;
			
		if(e.getCause().equals(DamageCause.BLOCK_EXPLOSION) || e.getCause().equals(DamageCause.ENTITY_EXPLOSION))
			e.setDamage(e.getDamage() * 0.85);
			
	}, MaterialLists.ARMOR.getMaterials(), 
			"Reduces explosion damage you suffer by 15%"), 
	LIGHTNING_FISHING("lightning", (event, owner)->{
		
		if(!(event instanceof PlayerFishEvent))
			return;
		
		PlayerFishEvent e = (PlayerFishEvent) event;
		
		if(e.isCancelled())
			return;
		
		if(!e.getPlayer().equals(owner))
			return;
		
		if(!e.getState().equals(State.REEL_IN))
			return;
		
		if(e.getCaught() instanceof LivingEntity)
			owner.getWorld().strikeLightning(e.getCaught().getLocation());
		
	}, MaterialLists.FISHING.getMaterials(), 
			"Summon a lightning bolt to strike your fished up target");
	
	private static final List<Enchantment> CURSES;
	
	private final String TAG;
	private final List<Material> VIABLE_TYPES;
	private final CurseAction ACTION;
	
	static {
		List<Enchantment> list = new ArrayList<>();
		
		list.add(Enchantment.BINDING_CURSE);
		list.add(Enchantment.VANISHING_CURSE);
		
		CURSES = list;
	}
	
	private CursedLegendary(String tag, CurseAction action, List<Material> viableTypes, String description) {
		this.TAG = tag;
		this.VIABLE_TYPES = viableTypes;
		this.ACTION = action;
	}
	
	public final String getTag() {
		return this.TAG;
	}
	
	public final List<Material> getViableTypes(){
		return this.VIABLE_TYPES;
	}
	
	public static void runAction(ItemStack item, LivingEntity owner, Event e) {
		String name = item.getItemMeta().getDisplayName().toLowerCase();
		
		for(CursedLegendary cl : values()) {
			if(name.contains(cl.getTag()) && cl.getViableTypes().contains(item.getType())) {
				cl.ACTION.action(e, owner);
				break; //Prevents multiple curse effects on one item
			}
		}
	}
	
	public static boolean isCursed(ItemStack item) {
		if(item == null)
			return false;
		
		boolean isCursed = false;
		
		for(Enchantment e : CURSES) {
			if(item.containsEnchantment(e)) {
				isCursed = true;
				break;
			}
		}
		
		return isCursed;
	}
}
