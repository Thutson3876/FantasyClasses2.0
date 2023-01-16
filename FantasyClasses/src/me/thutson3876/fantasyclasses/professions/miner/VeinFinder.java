package me.thutson3876.fantasyclasses.professions.miner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.Sphere;

public class VeinFinder extends AbstractAbility {

	private static final Map<Material, Material> refinedProducts = generateProductMap();
	private static final Map<Material, Material> refinedDeepslateProducts = generateDeepslateProductMap();
	
	private int range = 20;
	
	public VeinFinder(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 12 * 20;
		this.displayName = "Vein Finder";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.DEEPSLATE_EMERALD_ORE);		
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if(isOnCooldown())
			return;
		
		if(!e.getPlayer().equals(player))
			return;
		
		if(!player.isSneaking())
			return;
		
		if(!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;
		
		if(e.getItem() == null)
			return;
		
		this.onTrigger(getNearestOre(e.getItem(), e.getHand()));
	}
	
	@Override
	public String getInstructions() {
		return "Right-click while holding a refined mining product while crouching";
	}

	@Override
	public String getDescription() {
		return "Consume a refined mining item, such as a diamond or iron ingot, in order to see where the nearest vein of that type is. Has a cooldown of &6" + (this.coolDowninTicks / 20) + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
	}

	private boolean getNearestOre(ItemStack item, EquipmentSlot hand) {
		Material oreType = refinedProducts.get(item.getType());
		boolean isDeepslate = false;
		if(oreType == null) {
			oreType = refinedDeepslateProducts.get(item.getType());
			isDeepslate = true;
			if(oreType == null)
				return false;
		}
		
		Location loc = player.getLocation();
		List<Location> blockLocs = Sphere.generateSphereIncludingBlocks(loc, range, false);
		Location veinLoc = loc.clone().add(1000, 1000, 1000);
		Location startLoc = loc.clone().add(1000, 1000, 1000);
		double distance = 9999;
		for(Location l : blockLocs) {
			if(l.getBlock().getType().equals(oreType)) {
				double newDistance = player.getLocation().distance(veinLoc);
				if(veinLoc == null) {
					veinLoc = l;
				}
				else if(newDistance < distance) {
					veinLoc = l;
					distance = newDistance;
				}
				
			}
		}
		
		if(veinLoc == null && !isDeepslate) {
			oreType = refinedDeepslateProducts.get(item.getType());
			for(Location l : blockLocs) {
				if(l.getBlock().getType().equals(oreType)) {
					veinLoc = l;
					break;
				}
			}
		}
		
		if(veinLoc == startLoc) {
			player.playSound(loc, Sound.ENTITY_ENDER_EYE_DEATH, 1.0f, 1.1f);
			return false;
		}
		
		item.setAmount(item.getAmount() - 1);
		player.getInventory().setItem(hand, item);
		
		player.playSound(loc, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1.0f, 1.3f);
		
		EnderSignal eye = (EnderSignal) player.getWorld().spawnEntity(player.getLocation(), EntityType.ENDER_SIGNAL);
		eye.setDropItem(false);
		eye.setTargetLocation(veinLoc);
		eye.setGlowing(true);
		
		return true;
	}
	
	private static Map<Material, Material> generateProductMap() {
		Map<Material, Material> products = new HashMap<>();
		products.put(Material.DIAMOND, Material.DIAMOND_ORE);
		products.put(Material.COPPER_INGOT, Material.COPPER_ORE);
		products.put(Material.EMERALD, Material.EMERALD_ORE);
		products.put(Material.IRON_INGOT, Material.IRON_ORE);
		products.put(Material.GOLD_INGOT, Material.GOLD_ORE);
		products.put(Material.REDSTONE, Material.REDSTONE_ORE);
		products.put(Material.LAPIS_LAZULI, Material.LAPIS_ORE);
		products.put(Material.COAL, Material.COAL_ORE);
		
		return products;
	}
	private static Map<Material, Material> generateDeepslateProductMap() {
		Map<Material, Material> products = new HashMap<>();
		products.put(Material.DIAMOND, Material.DEEPSLATE_DIAMOND_ORE);
		products.put(Material.COPPER_INGOT, Material.DEEPSLATE_COPPER_ORE);
		products.put(Material.EMERALD, Material.DEEPSLATE_EMERALD_ORE);
		products.put(Material.IRON_INGOT, Material.DEEPSLATE_IRON_ORE);
		products.put(Material.GOLD_INGOT, Material.DEEPSLATE_GOLD_ORE);
		products.put(Material.REDSTONE, Material.DEEPSLATE_REDSTONE_ORE);
		products.put(Material.LAPIS_LAZULI, Material.DEEPSLATE_LAPIS_ORE);
		products.put(Material.NETHERITE_SCRAP, Material.ANCIENT_DEBRIS);
		products.put(Material.QUARTZ, Material.NETHER_QUARTZ_ORE);
		products.put(Material.COAL, Material.DEEPSLATE_COAL_ORE);
		
		return products;
	}
}
