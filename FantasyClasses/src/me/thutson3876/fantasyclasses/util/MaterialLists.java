package me.thutson3876.fantasyclasses.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

public enum MaterialLists {
	
	MELEE_WEAPON, RANGE_WEAPON, SWORD, AXE, HOE, PICKAXE, SHIELD, FISHING, EXOTIC_WEAPON, TOOL, ARMOR,
	HELMET, CHESTPLATE, LEGGINGS, BOOTS,
	POTION, ALCHEMICAL_INGREDIENT, DRUID_FOOD, CROP, LEAVES, ICE;
	
	private List<Material> matList = new ArrayList<>();
	
	static {
		generateLists();
	}
	
	private static void generateLists() {
		//Melee Weapon
		MELEE_WEAPON.matList.add(Material.IRON_AXE);
		MELEE_WEAPON.matList.add(Material.IRON_SWORD);
		MELEE_WEAPON.matList.add(Material.WOODEN_AXE);
		MELEE_WEAPON.matList.add(Material.WOODEN_SWORD);
		MELEE_WEAPON.matList.add(Material.STONE_AXE);
		MELEE_WEAPON.matList.add(Material.STONE_SWORD);
		MELEE_WEAPON.matList.add(Material.GOLDEN_AXE);
		MELEE_WEAPON.matList.add(Material.GOLDEN_SWORD);
		MELEE_WEAPON.matList.add(Material.DIAMOND_AXE);
		MELEE_WEAPON.matList.add(Material.DIAMOND_SWORD);
		MELEE_WEAPON.matList.add(Material.NETHERITE_AXE);
		MELEE_WEAPON.matList.add(Material.NETHERITE_SWORD);
		MELEE_WEAPON.matList.add(Material.TRIDENT);
		//Range Weapon
		RANGE_WEAPON.matList.add(Material.BOW);
		RANGE_WEAPON.matList.add(Material.CROSSBOW);
		RANGE_WEAPON.matList.add(Material.TRIDENT);
		//Sword
		SWORD.matList.add(Material.IRON_SWORD);
		SWORD.matList.add(Material.WOODEN_SWORD);
		SWORD.matList.add(Material.GOLDEN_SWORD);
		SWORD.matList.add(Material.STONE_SWORD);
		SWORD.matList.add(Material.DIAMOND_SWORD);
		SWORD.matList.add(Material.NETHERITE_SWORD);
		//Axe
		AXE.matList.add(Material.IRON_AXE);
		AXE.matList.add(Material.WOODEN_AXE);
		AXE.matList.add(Material.STONE_AXE);
		AXE.matList.add(Material.GOLDEN_AXE);
		AXE.matList.add(Material.DIAMOND_AXE);
		AXE.matList.add(Material.NETHERITE_AXE);
		//Hoe
		HOE.matList.add(Material.IRON_HOE);
		HOE.matList.add(Material.GOLDEN_HOE);
		HOE.matList.add(Material.WOODEN_HOE);
		HOE.matList.add(Material.STONE_HOE);
		HOE.matList.add(Material.NETHERITE_HOE);
		HOE.matList.add(Material.DIAMOND_HOE);
		//Pickaxe
		PICKAXE.matList.add(Material.IRON_PICKAXE);
		PICKAXE.matList.add(Material.WOODEN_PICKAXE);
		PICKAXE.matList.add(Material.STONE_PICKAXE);
		PICKAXE.matList.add(Material.GOLDEN_PICKAXE);
		PICKAXE.matList.add(Material.DIAMOND_PICKAXE);
		PICKAXE.matList.add(Material.NETHERITE_PICKAXE);
		//Shield
		SHIELD.matList.add(Material.SHIELD);
		//Fishing
		FISHING.matList.add(Material.FISHING_ROD);
		//Exotic
		EXOTIC_WEAPON.matList.add(Material.TRIDENT);
		EXOTIC_WEAPON.matList.add(Material.IRON_HOE);
		EXOTIC_WEAPON.matList.add(Material.GOLDEN_HOE);
		EXOTIC_WEAPON.matList.add(Material.WOODEN_HOE);
		EXOTIC_WEAPON.matList.add(Material.STONE_HOE);
		EXOTIC_WEAPON.matList.add(Material.NETHERITE_HOE);
		EXOTIC_WEAPON.matList.add(Material.DIAMOND_HOE);
		//Tool
		TOOL.matList.addAll(MELEE_WEAPON.matList);
		TOOL.matList.addAll(EXOTIC_WEAPON.matList);
		TOOL.matList.addAll(PICKAXE.matList);
		TOOL.matList.add(Material.FISHING_ROD);
		//Armor
		ARMOR.matList.add(Material.LEATHER_HELMET);
		ARMOR.matList.add(Material.LEATHER_CHESTPLATE);
		ARMOR.matList.add(Material.LEATHER_LEGGINGS);
		ARMOR.matList.add(Material.LEATHER_BOOTS);
		ARMOR.matList.add(Material.IRON_HELMET);
		ARMOR.matList.add(Material.IRON_CHESTPLATE);
		ARMOR.matList.add(Material.IRON_LEGGINGS);
		ARMOR.matList.add(Material.IRON_BOOTS);
		ARMOR.matList.add(Material.GOLDEN_HELMET);
		ARMOR.matList.add(Material.GOLDEN_CHESTPLATE);
		ARMOR.matList.add(Material.GOLDEN_LEGGINGS);
		ARMOR.matList.add(Material.GOLDEN_BOOTS);
		ARMOR.matList.add(Material.DIAMOND_HELMET);
		ARMOR.matList.add(Material.DIAMOND_CHESTPLATE);
		ARMOR.matList.add(Material.DIAMOND_LEGGINGS);
		ARMOR.matList.add(Material.DIAMOND_BOOTS);
		ARMOR.matList.add(Material.NETHERITE_HELMET);
		ARMOR.matList.add(Material.NETHERITE_CHESTPLATE);
		ARMOR.matList.add(Material.NETHERITE_LEGGINGS);
		ARMOR.matList.add(Material.NETHERITE_BOOTS);
		ARMOR.matList.add(Material.TURTLE_HELMET);
		//Helmet
		HELMET.matList.add(Material.LEATHER_HELMET);
		HELMET.matList.add(Material.CHAINMAIL_HELMET);
		HELMET.matList.add(Material.GOLDEN_HELMET);
		HELMET.matList.add(Material.IRON_HELMET);
		HELMET.matList.add(Material.DIAMOND_HELMET);
		HELMET.matList.add(Material.NETHERITE_HELMET);
		//Chestplate
		CHESTPLATE.matList.add(Material.LEATHER_CHESTPLATE);
		CHESTPLATE.matList.add(Material.CHAINMAIL_CHESTPLATE);
		CHESTPLATE.matList.add(Material.GOLDEN_CHESTPLATE);
		CHESTPLATE.matList.add(Material.IRON_CHESTPLATE);
		CHESTPLATE.matList.add(Material.DIAMOND_CHESTPLATE);
		CHESTPLATE.matList.add(Material.NETHERITE_CHESTPLATE);
		//Leggings
		LEGGINGS.matList.add(Material.LEATHER_LEGGINGS);
		LEGGINGS.matList.add(Material.CHAINMAIL_LEGGINGS);
		LEGGINGS.matList.add(Material.GOLDEN_LEGGINGS);
		LEGGINGS.matList.add(Material.IRON_LEGGINGS);
		LEGGINGS.matList.add(Material.DIAMOND_LEGGINGS);
		LEGGINGS.matList.add(Material.NETHERITE_LEGGINGS);
		//Boots
		BOOTS.matList.add(Material.LEATHER_BOOTS);
		BOOTS.matList.add(Material.CHAINMAIL_BOOTS);
		BOOTS.matList.add(Material.GOLDEN_BOOTS);
		BOOTS.matList.add(Material.IRON_BOOTS);
		BOOTS.matList.add(Material.DIAMOND_BOOTS);
		BOOTS.matList.add(Material.NETHERITE_BOOTS);
		//Potion
		POTION.matList.add(Material.POTION);
		POTION.matList.add(Material.LINGERING_POTION);
		POTION.matList.add(Material.SPLASH_POTION);
		//Alchemical Ingredient
		ALCHEMICAL_INGREDIENT.matList.add(Material.SPIDER_EYE);
		ALCHEMICAL_INGREDIENT.matList.add(Material.NETHER_WART);
		ALCHEMICAL_INGREDIENT.matList.add(Material.BLAZE_ROD);
		ALCHEMICAL_INGREDIENT.matList.add(Material.BLAZE_POWDER);
		ALCHEMICAL_INGREDIENT.matList.add(Material.GUNPOWDER);
		ALCHEMICAL_INGREDIENT.matList.add(Material.GLOWSTONE_DUST);
		ALCHEMICAL_INGREDIENT.matList.add(Material.SUGAR);
		ALCHEMICAL_INGREDIENT.matList.add(Material.REDSTONE);
		ALCHEMICAL_INGREDIENT.matList.add(Material.MELON_SLICE);
		ALCHEMICAL_INGREDIENT.matList.add(Material.GLISTERING_MELON_SLICE);
		ALCHEMICAL_INGREDIENT.matList.add(Material.PHANTOM_MEMBRANE);
		ALCHEMICAL_INGREDIENT.matList.add(Material.RABBIT_FOOT);
		ALCHEMICAL_INGREDIENT.matList.add(Material.FERMENTED_SPIDER_EYE);
		ALCHEMICAL_INGREDIENT.matList.add(Material.GHAST_TEAR);
		ALCHEMICAL_INGREDIENT.matList.add(Material.CARROT);
		ALCHEMICAL_INGREDIENT.matList.add(Material.GOLDEN_CARROT);
		ALCHEMICAL_INGREDIENT.matList.add(Material.PUFFERFISH);
		ALCHEMICAL_INGREDIENT.matList.add(Material.MAGMA_CREAM);
		ALCHEMICAL_INGREDIENT.matList.add(Material.SCUTE);
		//Druid Food
		DRUID_FOOD.matList.add(Material.APPLE);
		DRUID_FOOD.matList.add(Material.CARROT);
		DRUID_FOOD.matList.add(Material.POTATO);
		DRUID_FOOD.matList.add(Material.BEETROOT);
		DRUID_FOOD.matList.add(Material.SWEET_BERRIES);
		DRUID_FOOD.matList.add(Material.GLOW_BERRIES);
		//Crop
		CROP.matList.add(Material.POTATOES);
	    CROP.matList.add(Material.CARROTS);
	    CROP.matList.add(Material.WHEAT);
	    CROP.matList.add(Material.BEETROOTS);
	    CROP.matList.add(Material.COCOA_BEANS);
	    CROP.matList.add(Material.MELON_STEM);
	    CROP.matList.add(Material.ATTACHED_MELON_STEM);
	    CROP.matList.add(Material.PUMPKIN_STEM);
	    CROP.matList.add(Material.ATTACHED_PUMPKIN_STEM);
		//Leaves
		LEAVES.matList.add(Material.ACACIA_LEAVES);
		LEAVES.matList.add(Material.AZALEA_LEAVES);
		LEAVES.matList.add(Material.BIRCH_LEAVES);
		LEAVES.matList.add(Material.DARK_OAK_LEAVES);
		LEAVES.matList.add(Material.FLOWERING_AZALEA_LEAVES);
		LEAVES.matList.add(Material.JUNGLE_LEAVES);
		LEAVES.matList.add(Material.OAK_LEAVES);
		LEAVES.matList.add(Material.SPRUCE_LEAVES);
		LEAVES.matList.add(Material.MANGROVE_LEAVES);
		//Ice
		ICE.matList.add(Material.ICE);
		ICE.matList.add(Material.BLUE_ICE);
		ICE.matList.add(Material.FROSTED_ICE);
		ICE.matList.add(Material.PACKED_ICE);
	}
	
	public List<Material> getMaterials(){
		return this.matList;
	}
	
	public boolean contains(Material mat) {
		return this.matList.contains(mat);
	}
}
