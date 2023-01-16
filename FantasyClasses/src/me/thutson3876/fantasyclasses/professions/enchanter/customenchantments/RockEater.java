package me.thutson3876.fantasyclasses.professions.enchanter.customenchantments;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.FantasyClasses;

public class RockEater extends Enchantment implements Listener {
	
	private static Random rng = new Random();
	private double chance = 0.1;
	
	public RockEater(NamespacedKey key) {
		super(key);
		
		FantasyClasses.getPlugin().registerEvents(this);
	}

	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e) {
		Player p = e.getPlayer();
		
		ItemStack item = p.getEquipment().getItemInMainHand();
		
		if(!item.containsEnchantment(this))
			return;
		
		if(p.isDead())
			return;
		
		if(p.getFoodLevel() >= 14)
			return;
		
		if(rng.nextDouble() < chance)
			p.setFoodLevel(p.getFoodLevel() + 1);
	}
	
	@Override
	public boolean canEnchantItem(ItemStack item) {
		if(item == null)
			return false;
		
		Material type = item.getType();
		
		return type.name().contains("SHOVEL") || type.name().contains("AXE") || type.name().contains("HOE");
	}

	@Override
	public boolean conflictsWith(Enchantment ench) {
		return Enchantments.CUSTOM.getEnchants().contains(ench);
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.TOOL;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public String getName() {
		return "Rock Eater";
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
