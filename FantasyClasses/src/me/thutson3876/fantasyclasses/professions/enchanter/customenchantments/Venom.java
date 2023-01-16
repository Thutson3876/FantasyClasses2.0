package me.thutson3876.fantasyclasses.professions.enchanter.customenchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Venom extends Enchantment implements Listener {

	private static PotionEffect poison = new PotionEffect(PotionEffectType.POISON, 4 * 20, 0);
	
	public Venom(NamespacedKey key) {
		super(key);
		
		FantasyClasses.getPlugin().registerEvents(this);
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		Entity damager = e.getDamager();
		if(!(damager instanceof LivingEntity))
			return;
		
		LivingEntity leDamager = (LivingEntity) damager;
		
		ItemStack mainHand = leDamager.getEquipment().getItemInMainHand();
		
		if(!mainHand.containsEnchantment(this))
			return;
		
		Entity victim = e.getEntity();
		if(!(victim instanceof LivingEntity))
			return;
		
		LivingEntity leVictim = (LivingEntity) victim;
		
		if(leVictim.isDead())
			return;
		
		AbilityUtils.applyStackingPotionEffect(poison, leVictim, 4, 5 * 20);
	}
	
	@Override
	public boolean canEnchantItem(ItemStack item) {
		if(item == null)
			return false;
		
		Material type = item.getType();
		
		return type.name().contains("SWORD") || type.name().contains("AXE") || type.name().contains("TRIDENT") || type.name().contains("HOE");
	}

	@Override
	public boolean conflictsWith(Enchantment enchant) {
		if(enchant.equals(Enchantment.SWEEPING_EDGE))
			return true;
		
		return Enchantments.CUSTOM.getEnchants().contains(enchant);
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.WEAPON;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public String getName() {
		return "Venom";
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
