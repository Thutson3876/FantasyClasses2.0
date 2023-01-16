package me.thutson3876.fantasyclasses.professions.enchanter.customenchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.FantasyClasses;

public class Blaze extends Enchantment implements Listener {
	//Bow - your arrows are fire balls instead
	
	private float yield = 2.0f;
	
	public Blaze(NamespacedKey key) {
		super(key);
		
		FantasyClasses.getPlugin().registerEvents(this);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityShootBowEvent(EntityShootBowEvent e) {
		if(e.isCancelled())
			return;
		
		if (!(e.getProjectile() instanceof AbstractArrow))
			return;

		LivingEntity shooter = e.getEntity();
		ItemStack bow = shooter.getEquipment().getItem(e.getHand());
		
		if(!bow.containsEnchantment(this))
			return;
		
		Fireball charge = (Fireball) shooter.getWorld().spawnEntity(shooter.getEyeLocation(), EntityType.SMALL_FIREBALL);
		charge.setShooter(shooter);
		charge.setDirection(shooter.getEyeLocation().getDirection());
		charge.setYield(yield);
		charge.setIsIncendiary(true);
		
		e.setProjectile(charge);
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
		return "Blaze";
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
