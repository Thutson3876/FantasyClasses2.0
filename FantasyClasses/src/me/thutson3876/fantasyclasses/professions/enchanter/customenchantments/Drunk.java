package me.thutson3876.fantasyclasses.professions.enchanter.customenchantments;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.FantasyClasses;

public class Drunk extends Enchantment implements Listener {
	//Armor - 20% chance when taking damage: reduce the damage by 20%, but gain nausea and slowness for 3 seconds
	
	private static Random rng = new Random();
	
	private double chance = 0.2;
	private int duration = 5 * 20;
	private double reduction = 0.2;
	
	private PotionEffect nausea = new PotionEffect(PotionEffectType.CONFUSION, duration, 0);
	private PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, duration, 0);
	
	public Drunk(NamespacedKey key) {
		super(key);
		
		FantasyClasses.getPlugin().registerEvents(this);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onTookDamage(EntityDamageByEntityEvent e) {
		Entity victim = e.getEntity();
		
		if(!(victim instanceof LivingEntity))
			return;
		
		LivingEntity leVictim = (LivingEntity) victim;
		
		EntityEquipment equipment = leVictim.getEquipment();
		
		int drunkCount = 0;
		for(ItemStack armor : equipment.getArmorContents()) {
			if(armor != null && armor.containsEnchantment(this))
				drunkCount++;
		}
		
		if(rng.nextDouble() > drunkCount * chance)
			return;
		
		if(leVictim.isDead())
			return;
		
		e.setDamage(e.getDamage() * (1 - reduction));
		leVictim.addPotionEffect(nausea);
		leVictim.addPotionEffect(slow);
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
		if(ench.equals(Enchantment.THORNS) || ench.equals(Enchantment.PROTECTION_ENVIRONMENTAL))
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
		return "Drunk";
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
