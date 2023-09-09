package me.thutson3876.fantasyclasses.playermanagement.currency;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.thutson3876.fantasyclasses.util.chat.ChatUtils;

public enum MarketCategory {

	LEAVES, DIRT, WOOD, STONE, AQUATIC, ICE, NETHER, END, DECORATION, MISC;

	static {
		// Leaves
		LEAVES.stock.add(new Purchasable(Material.ACACIA_LEAVES, 5));
		LEAVES.stock.add(new Purchasable(Material.AZALEA_LEAVES, 5));
		LEAVES.stock.add(new Purchasable(Material.BIRCH_LEAVES, 5));
		LEAVES.stock.add(new Purchasable(Material.DARK_OAK_LEAVES, 5));
		LEAVES.stock.add(new Purchasable(Material.FLOWERING_AZALEA_LEAVES, 10));
		LEAVES.stock.add(new Purchasable(Material.JUNGLE_LEAVES, 5));
		LEAVES.stock.add(new Purchasable(Material.OAK_LEAVES, 5));
		LEAVES.stock.add(new Purchasable(Material.SPRUCE_LEAVES, 5));
		LEAVES.stock.add(new Purchasable(Material.MANGROVE_LEAVES, 10));
		
		LEAVES.item = LEAVES.generateCategoryItem(Material.OAK_LEAVES);
		LEAVES.generateStockItems();
		// Dirt
		DIRT.stock.add(new Purchasable(Material.DIRT, 5));
		DIRT.stock.add(new Purchasable(Material.MUD, 10));
		DIRT.stock.add(new Purchasable(Material.CLAY, 15));
		DIRT.stock.add(new Purchasable(Material.COARSE_DIRT, 10));
		DIRT.stock.add(new Purchasable(Material.ROOTED_DIRT, 10));
		DIRT.stock.add(new Purchasable(Material.MYCELIUM, 10));
		DIRT.stock.add(new Purchasable(Material.PODZOL, 10));
		DIRT.stock.add(new Purchasable(Material.GRASS_BLOCK, 10));
		DIRT.stock.add(new Purchasable(Material.GRAVEL, 5));
		DIRT.stock.add(new Purchasable(Material.SAND, 5));
		DIRT.stock.add(new Purchasable(Material.RED_SAND, 5));
		
		DIRT.item = DIRT.generateCategoryItem(Material.DIRT);
		DIRT.generateStockItems();
		// Wood
		WOOD.stock.add(new Purchasable(Material.ACACIA_LOG, 10));
		WOOD.stock.add(new Purchasable(Material.BIRCH_LOG, 10));
		WOOD.stock.add(new Purchasable(Material.OAK_LOG, 10));
		WOOD.stock.add(new Purchasable(Material.DARK_OAK_LOG, 10));
		WOOD.stock.add(new Purchasable(Material.JUNGLE_LOG, 10));
		WOOD.stock.add(new Purchasable(Material.MANGROVE_LOG, 10));
		WOOD.stock.add(new Purchasable(Material.SPRUCE_LOG, 10));
		
		WOOD.item = WOOD.generateCategoryItem(Material.OAK_LOG);
		WOOD.generateStockItems();
		// Stone
		STONE.stock.add(new Purchasable(Material.STONE, 5));
		STONE.stock.add(new Purchasable(Material.SANDSTONE, 5));
		STONE.stock.add(new Purchasable(Material.RED_SANDSTONE, 5));
		STONE.stock.add(new Purchasable(Material.TERRACOTTA, 5));
		STONE.stock.add(new Purchasable(Material.GRANITE, 5));
		STONE.stock.add(new Purchasable(Material.ANDESITE, 5));
		STONE.stock.add(new Purchasable(Material.DIORITE, 5));
		STONE.stock.add(new Purchasable(Material.TUFF, 5));
		STONE.stock.add(new Purchasable(Material.BASALT, 5));
		STONE.stock.add(new Purchasable(Material.DEEPSLATE, 5));
		STONE.stock.add(new Purchasable(Material.BONE_BLOCK, 10));
		STONE.stock.add(new Purchasable(Material.OBSIDIAN, 30));
		
		STONE.item = STONE.generateCategoryItem(Material.STONE);
		STONE.generateStockItems();
		// Aquatic
		AQUATIC.stock.add(new Purchasable(Material.PRISMARINE, 10));
		AQUATIC.stock.add(new Purchasable(Material.DARK_PRISMARINE, 10));
		AQUATIC.stock.add(new Purchasable(Material.SEA_LANTERN, 30));
		
		AQUATIC.item = AQUATIC.generateCategoryItem(Material.SEA_LANTERN);
		AQUATIC.generateStockItems();
		// End
		END.stock.add(new Purchasable(Material.END_STONE, 5));
		END.stock.add(new Purchasable(Material.END_ROD, 10));
		END.stock.add(new Purchasable(Material.PURPUR_BLOCK, 10));
		END.stock.add(new Purchasable(Material.CHORUS_PLANT, 10));
		END.stock.add(new Purchasable(Material.CHORUS_FLOWER, 10));
		END.stock.add(new Purchasable(Material.ENDER_PEARL, 10));
		END.stock.add(new Purchasable(Material.CHORUS_FRUIT, 10));
		
		END.item = END.generateCategoryItem(Material.ENDER_PEARL);
		END.generateStockItems();
		// Nether
		NETHER.stock.add(new Purchasable(Material.NETHERRACK, 5));
		NETHER.stock.add(new Purchasable(Material.NETHER_BRICKS, 10));
		NETHER.stock.add(new Purchasable(Material.MAGMA_BLOCK, 10));
		NETHER.stock.add(new Purchasable(Material.NETHER_QUARTZ_ORE, 30));
		NETHER.stock.add(new Purchasable(Material.QUARTZ_BLOCK, 10));
		NETHER.stock.add(new Purchasable(Material.WARPED_FUNGUS, 15));
		NETHER.stock.add(new Purchasable(Material.CRIMSON_FUNGUS, 15));
		
		NETHER.item = NETHER.generateCategoryItem(Material.NETHER_BRICKS);
		NETHER.generateStockItems();
		// Misc
		MISC.stock.add(new Purchasable(Material.STRING, 2));
		MISC.stock.add(new Purchasable(Material.COBWEB, 3));
		MISC.stock.add(new Purchasable(Material.LEATHER, 5));
		MISC.stock.add(new Purchasable(Material.GLASS, 10));
		MISC.stock.add(new Purchasable(Material.SLIME_BALL, 10));
		MISC.stock.add(new Purchasable(Material.AMETHYST_CLUSTER, 500));
		MISC.stock.add(new Purchasable(Material.NAUTILUS_SHELL, 500));
		MISC.stock.add(new Purchasable(Material.TRIDENT, 1000));
		
		MISC.item = MISC.generateCategoryItem(Material.TRIDENT);
		MISC.generateStockItems();
	}

	private List<Purchasable> stock = new ArrayList<>();
	private ItemStack item = new ItemStack(Material.STONE);
	private List<ItemStack> stockItems = new ArrayList<>();

	public List<Purchasable> getStock() {
		return stock;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public List<ItemStack> getStockItems() {
		return stockItems;
	}
	
	private ItemStack generateCategoryItem(Material material) {
		ItemStack i = new ItemStack(material);
		
		ItemMeta meta = i.getItemMeta();
		
		meta.setDisplayName(ChatUtils.chat("&c" + this.toString()));
		
		i.setItemMeta(meta);
		
		return i;
	}
	
	private List<ItemStack> generateStockItems() {
		List<ItemStack> items = new ArrayList<>();
		
		for(Purchasable merch : this.stock) {
			items.add(generateStockItem(merch.getMaterial(), merch.getPrice()));
		}
		
		this.stockItems = items;
		
		return items;
	}
	
	private ItemStack generateStockItem(Material material, int cost) {
		ItemStack i = new ItemStack(material);
		
		ItemMeta meta = i.getItemMeta();
		
		meta.setDisplayName(ChatUtils.chat("&6" + material.toString()));
		
		List<String> lore = new ArrayList<>();
		lore.add(ChatUtils.chat("Cost: &6" + cost));
		
		meta.setLore(lore);
		
		i.setItemMeta(meta);
		
		return i;
	}
}
