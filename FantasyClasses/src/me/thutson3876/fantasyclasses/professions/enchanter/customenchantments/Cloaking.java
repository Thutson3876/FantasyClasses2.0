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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.FantasyClasses;

public class Cloaking extends Enchantment implements Listener {
	//Armor - 20% chance to gain speed and stealth when hit
	private static Random rng = new Random();
	
	private double chance = 0.2;
	private int duration = 6 * 20;
	
	private PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, duration, 0);
	private PotionEffect invis = new PotionEffect(PotionEffectType.INVISIBILITY, duration, 0);
	
	public Cloaking(NamespacedKey key) {
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
		
		int cloakCount = 0;
		for(ItemStack armor : equipment.getArmorContents()) {
			if(armor != null && armor.containsEnchantment(this))
				cloakCount++;
		}
		
		if(rng.nextDouble() > cloakCount * chance)
			return;
		
		leVictim.addPotionEffect(speed);
		leVictim.addPotionEffect(invis);
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
		return "Cloaking";
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
