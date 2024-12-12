package me.thutson3876.fantasyclasses.professions.enchanter.customenchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Quickening extends Enchantment implements Listener {

	private static PotionEffect haste = new PotionEffect(PotionEffectType.HASTE, 10 * 20, 0);
	
	private NamespacedKey key;
	
	public Quickening(NamespacedKey key) {
		super();
		this.key = key;
		
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
		
		AbilityUtils.applyStackingPotionEffect(haste, p, 4, 10 * 20);
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
		return "Quickening";
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

	@Override
	public NamespacedKey getKey() {
		// TODO Auto-generated method stub
		return key;
	}

	@Override
	public String getTranslationKey() {
		// TODO Auto-generated method stub
		return key.getNamespace();
	}

}
