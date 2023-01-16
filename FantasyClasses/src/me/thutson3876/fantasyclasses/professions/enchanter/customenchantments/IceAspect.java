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
import me.thutson3876.fantasyclasses.FantasyClasses;

public class IceAspect extends Enchantment implements Listener {
	//Weapon - applies freeze for 3 * 20 ticks
	private int freezeTicks = 3 * 20;
	
	public IceAspect(NamespacedKey key) {
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
		
		leVictim.setFreezeTicks(leVictim.getFreezeTicks() + freezeTicks);
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
		if(enchant.equals(Enchantment.FIRE_ASPECT))
			return true;
		
		return Enchantments.CUSTOM.getEnchants().contains(enchant);
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.WEAPON;
	}

	@Override
	public int getMaxLevel() {
		return 2;
	}

	@Override
	public String getName() {
		return "Ice Aspect";
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
