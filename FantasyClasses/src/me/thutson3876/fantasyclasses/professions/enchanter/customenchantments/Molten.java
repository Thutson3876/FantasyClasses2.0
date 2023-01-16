package me.thutson3876.fantasyclasses.professions.enchanter.customenchantments;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.FantasyClasses;

public class Molten extends Enchantment implements Listener {
	//Armor - 10% chance to cause the target to catch fire for 3 seconds when hit
	
	private static Random rng = new Random();
	
	private double chance = 0.1;
	private int duration = 3 * 20;
	
	public Molten(NamespacedKey key) {
		super(key);
		
		FantasyClasses.getPlugin().registerEvents(this);
	}

	@EventHandler
	public void onTookDamage(EntityDamageByEntityEvent e) {
		Entity victim = e.getEntity();
		
		if(!(victim instanceof LivingEntity))
			return;
		
		LivingEntity leVictim = (LivingEntity) victim;
		
		EntityEquipment equipment = leVictim.getEquipment();
		
		int moltenCount = 0;
		for(ItemStack armor : equipment.getArmorContents()) {
			if(armor != null && armor.containsEnchantment(this))
				moltenCount++;
		}
		
		if(rng.nextDouble() > moltenCount * chance)
			return;
		
		e.getDamager().setFireTicks(duration);
	}
	
	@Override
	public boolean canEnchantItem(ItemStack item) {
		if(item == null)
			return false;
		
		Material type = item.getType();
		
		return type.name().contains("BOOTS") || type.name().contains("LEGGINGS") || type.name().contains("CHESTPLATE") || type.name().contains("HELMET");
	}

	@Override
	public boolean conflictsWith(Enchantment ench) {
		if(ench.equals(Enchantment.THORNS))
			return true;
		
		return Enchantments.CUSTOM.getEnchants().contains(ench);
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.ARMOR;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public String getName() {
		return "Molten";
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
