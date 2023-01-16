package me.thutson3876.fantasyclasses.professions.enchanter.customenchantments;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.FantasyClasses;

public class Transposition extends Enchantment implements Listener {
	//Bow - 50% chance to swap your position with your target upon arrow hitting entity
	private static Random rng = new Random();
	private double chance = 0.5;
	Map<AbstractArrow, LivingEntity> arrowMap = new HashMap<>();
	
	public Transposition(NamespacedKey key) {
		super(key);
		
		FantasyClasses.getPlugin().registerEvents(this);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityShootBowEvent(EntityShootBowEvent e) {
		if(e.isCancelled())
			return;
		
		if (!(e.getProjectile() instanceof AbstractArrow))
			return;

		LivingEntity shooter = e.getEntity();
		ItemStack bow = e.getBow();
		
		if(!bow.containsEnchantment(this))
			return;
		
		AbstractArrow arrow = (AbstractArrow) e.getProjectile();

		arrowMap.put(arrow, shooter);
	}
	
	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		Projectile eventProjectile = e.getEntity();
		if (!this.arrowMap.containsKey(eventProjectile))
			return;

		LivingEntity shooter = arrowMap.remove(eventProjectile);
		Location shooterLoc = shooter.getLocation();
		
		Entity hitEntity = e.getHitEntity();

		if (hitEntity == null) {
			return;
		}
		
		Location targetLoc = hitEntity.getLocation();
		
		if(rng.nextDouble() > chance)
			return;
		
		shooter.teleport(targetLoc);
		hitEntity.teleport(shooterLoc);
	}
	
	@Override
	public boolean canEnchantItem(ItemStack item) {
		if(item == null)
			return false;
		
		Material type = item.getType();
		
		return type.name().contains("BOW");
	}

	@Override
	public boolean conflictsWith(Enchantment ench) {
		if(ench.equals(Enchantment.PIERCING))
			return true;
		
		return Enchantments.CUSTOM.getEnchants().contains(ench);
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.BOW;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public String getName() {
		return "Transposition";
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

	@Override
	public boolean isCursed() {
		return false;
	}

	@Override
	public boolean isTreasure() {
		return true;
	}
}
