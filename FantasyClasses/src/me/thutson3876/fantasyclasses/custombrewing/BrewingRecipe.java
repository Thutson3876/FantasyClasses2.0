package me.thutson3876.fantasyclasses.custombrewing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public enum BrewingRecipe {

	RESISTANCE(Material.DIAMOND, Color.TEAL, new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 90 * 20, 0)),
	WITHER(Material.WITHER_ROSE, Color.BLACK, new PotionEffect(PotionEffectType.WITHER, 45 * 20, 0)),
	NAUSEA(Material.RED_MUSHROOM, Color.OLIVE, new PotionEffect(PotionEffectType.CONFUSION, 180 * 20, 0)),
	HASTE(Material.DIAMOND_BLOCK, Color.SILVER, new PotionEffect(PotionEffectType.FAST_DIGGING, 300 * 20, 0)),
	SATURATION(Material.GOLDEN_APPLE, Color.GREEN, new PotionEffect(PotionEffectType.SATURATION, 240 * 20, 0)),
	HUNGER(Material.ROTTEN_FLESH, Color.MAROON, new PotionEffect(PotionEffectType.HUNGER, 90 * 20, 0)),
	LUCK(Material.GOLD_INGOT, Color.YELLOW, new PotionEffect(PotionEffectType.LUCK, 360 * 20, 0)),
	UNLUCK(Material.COAL, Color.GRAY, new PotionEffect(PotionEffectType.UNLUCK, 360 * 20, 0)),
	GLOWING(Material.GLOW_BERRIES, Color.WHITE, new PotionEffect(PotionEffectType.GLOWING, 180 * 20, 0)),
	LEVITATION(Material.PHANTOM_MEMBRANE, Color.PURPLE, new PotionEffect(PotionEffectType.LEVITATION, 15 * 20, 0)),
	DARKNESS(Material.SCULK, Color.BLACK, new PotionEffect(PotionEffectType.DARKNESS, 90 * 20, 0)),
	
	REGENERATION(Material.GHAST_TEAR, Color.FUCHSIA, new PotionEffect(PotionEffectType.REGENERATION, 45 * 20, 0)),
	HEALING(Material.GLISTERING_MELON_SLICE, Color.RED, new PotionEffect(PotionEffectType.HEAL, 1 * 20, 0)),
	STRENGTH(Material.BLAZE_ROD, Color.MAROON, new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 180 * 20, 0)),
	SWIFTNESS(Material.SUGAR, Color.SILVER, new PotionEffect(PotionEffectType.SPEED, 180 * 20, 0)),
	NIGHT_VISION(Material.GOLDEN_CARROT, Color.NAVY, new PotionEffect(PotionEffectType.NIGHT_VISION, 180 * 20, 0)),
	INVISIBILITY(Material.ENDER_EYE, Color.WHITE, new PotionEffect(PotionEffectType.INVISIBILITY, 360 * 20, 0)),
	WATER_BREATHING(Material.PUFFERFISH, Color.AQUA, new PotionEffect(PotionEffectType.WATER_BREATHING, 180 * 20, 0)),
	LEAPING(Material.RABBIT_FOOT, Color.TEAL, new PotionEffect(PotionEffectType.JUMP, 180 * 20, 0)),
	SLOW_FALL(Material.PHANTOM_MEMBRANE, Color.WHITE, new PotionEffect(PotionEffectType.SLOW_FALLING, 120 * 20, 0)),
	FIRE_RESISTANCE(Material.MAGMA_CREAM, Color.ORANGE, new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 180 * 20, 0)),
	
	POISON(Material.SPIDER_EYE, Color.GREEN, new PotionEffect(PotionEffectType.POISON, 45 * 20, 0)),
	WEAKNESS(Material.FERMENTED_SPIDER_EYE, Color.NAVY, new PotionEffect(PotionEffectType.WEAKNESS, 90 * 20, 0)),
	HARM(Material.IRON_SWORD, Color.MAROON, new PotionEffect(PotionEffectType.HARM, 1 * 20, 1)),
	SLOWNESS(Material.SCUTE, Color.BLACK, new PotionEffect(PotionEffectType.SLOW, 90 * 20, 0));

	private Material ingredient;
	private ItemStack result;

	private BrewingRecipe(Material ingredient, Color color, PotionEffect... effects) {
		this.ingredient = ingredient;
		this.result = initResult(color, effects);
	}

	private ItemStack initResult(Color color, PotionEffect... effects) {
		ItemStack pot = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) pot.getItemMeta();
		for (PotionEffect effect : effects) {
			meta.addCustomEffect(effect, true);
		}
		meta.setColor(color);
		pot.setItemMeta(meta);

		return pot;
	}

	public static ItemStack getDrop(Collection<ItemStack> ingredients) {
		BrewingRecipe potentialRecipe = null;
		ItemStack potentialPotion = null;
		boolean isAwkward = false;
		for (ItemStack i : ingredients) {
			if (i.getType().equals(Material.POTION) || i.getType().equals(Material.SPLASH_POTION)) {
				PotionMeta potMeta = (PotionMeta) i.getItemMeta();
				if (potMeta.getBasePotionData().getType().equals(PotionType.UNCRAFTABLE)) {
					for (BrewingRecipe recipe : values()) {
						if (recipe.isMatching(i)) {
							potentialRecipe = recipe;
							potentialPotion = i;
							break;
						}
					}
				} else if (potMeta.getBasePotionData().getType().equals(PotionType.AWKWARD)) {
					isAwkward = true;
				}
			}

		}

		if (potentialRecipe != null) {
			ingredients.remove(potentialPotion);
			if(ingredients.size() != 1)
				return null;
			
			return potentialRecipe.checkCommons((ItemStack) ingredients.toArray()[0], potentialPotion);
		} else if (isAwkward) {
			Collection<Material> mats = new ArrayList<>();
			for (ItemStack i : ingredients) {
				mats.add(i.getType());
			}
			
			if(!mats.contains(Material.BLAZE_POWDER) || !mats.contains(Material.NETHER_WART))
				return null;
			
			for(BrewingRecipe recipe : values()) {
				if(mats.contains(recipe.ingredient)) {
					potentialRecipe = recipe;
					break;
				}
			}
			if(potentialRecipe == null)
				return null;
			
			return potentialRecipe.result;
		}
		
		
		return null;
	}

	public Material getIngredient() {
		return ingredient;
	}

	public ItemStack getResult() {
		return result;
	}

	public ItemStack ampUp(ItemStack item) {
		PotionMeta resultMeta = (PotionMeta) result.getItemMeta();
		PotionMeta itemMeta = (PotionMeta) item.getItemMeta();
		List<PotionEffect> newEffects = new ArrayList<>();
		for (PotionEffect effect : resultMeta.getCustomEffects()) {
			newEffects.add(new PotionEffect(effect.getType(), effect.getDuration(), effect.getAmplifier() + 1));
		}
		
		for (PotionEffect effect : newEffects) {
			itemMeta.addCustomEffect(effect, true);
		}
		item.setItemMeta(itemMeta);
		return item;
	}

	public ItemStack durationUp(ItemStack item) {
		PotionMeta resultMeta = (PotionMeta) result.getItemMeta();
		PotionMeta itemMeta = (PotionMeta) item.getItemMeta();
		List<PotionEffect> newEffects = new ArrayList<>();
		for (PotionEffect effect : resultMeta.getCustomEffects()) {
			newEffects.add(new PotionEffect(effect.getType(), effect.getDuration() * 3, effect.getAmplifier()));
		}
		
		for (PotionEffect effect : newEffects) {
			itemMeta.addCustomEffect(effect, true);
		}
		item.setItemMeta(itemMeta);
		return item;
	}

	public ItemStack makeSplash(ItemStack item) {
		ItemStack pot = new ItemStack(Material.SPLASH_POTION);
		PotionMeta resultMeta = (PotionMeta) result.getItemMeta();
		PotionMeta itemMeta = (PotionMeta) item.getItemMeta();
		List<PotionEffect> newEffects = new ArrayList<>();
		for (PotionEffect effect : resultMeta.getCustomEffects()) {
			newEffects.add(new PotionEffect(effect.getType(), effect.getDuration(), effect.getAmplifier()));
		}
		
		for (PotionEffect effect : newEffects) {
			itemMeta.addCustomEffect(effect, true);
		}
		pot.setItemMeta(itemMeta);
		return pot;
	}

	public ItemStack checkCommons(ItemStack ingredient, ItemStack currentItem) {
		if(!isMatching(currentItem))
			return null;

		Material ingredientType = ingredient.getType();
		if (ingredientType.equals(Material.GLOWSTONE)) {
			return ampUp(currentItem);
		} else if (ingredientType.equals(Material.REDSTONE_BLOCK)) {
			return durationUp(currentItem);
		} else if (ingredientType.equals(Material.TNT) && !currentItem.getType().equals(Material.SPLASH_POTION)) {
			return makeSplash(currentItem);
		}

		return null;
	}

	private boolean isMatching(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		if(!(meta instanceof PotionMeta))
			return false;
		
		PotionMeta potMeta = (PotionMeta) meta;
		
		PotionMeta thisPotMeta = (PotionMeta) this.getResult().getItemMeta();
		
		List<PotionEffect> effects = thisPotMeta.getCustomEffects();
		if(effects == null || effects.isEmpty())
			return false;
		
		if(!potMeta.getCustomEffects().containsAll(effects))
			return false;
		
		return true;
	}
}
