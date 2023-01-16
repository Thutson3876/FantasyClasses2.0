package me.thutson3876.fantasyclasses.professions.enchanter.customenchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Crippling extends Enchantment implements Listener {
	//Weapon - landing a crit applies weakness to target
	
	private static PotionEffect weak = new PotionEffect(PotionEffectType.WEAKNESS, 3 * 20, 0);
	
	public Crippling(NamespacedKey key) {
		super(key);
		
		FantasyClasses.getPlugin().registerEvents(this);
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;
		
		Player player = (Player) damager;
		
		ItemStack mainHand = player.getEquipment().getItemInMainHand();
		
		if(!mainHand.containsEnchantment(this))
			return;
		
		Entity victim = e.getEntity();
		if(!(victim instanceof LivingEntity))
			return;
		
		if(!AbilityUtils.isCritical(player))
			return;
		
		LivingEntity leVictim = (LivingEntity) victim;
		
		if(leVictim.isDead())
			return;
		
		leVictim.addPotionEffect(weak);
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
		if(enchant.equals(Enchantment.DAMAGE_ALL))
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
		return "Crippling";
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
