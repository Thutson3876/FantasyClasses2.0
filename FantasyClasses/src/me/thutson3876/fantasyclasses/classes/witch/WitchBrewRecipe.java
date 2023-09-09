package me.thutson3876.fantasyclasses.classes.witch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;
import me.thutson3876.fantasyclasses.util.geometry.Sphere;

public enum WitchBrewRecipe {

	GLOWSTONE((event, loc) -> {
		for (Location l : Sphere.generateSphere(loc, 3, false)) {
			l.getBlock().setType(Material.GLOWSTONE);
		}
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_SAND, Material.GLOWSTONE),
	COW((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.COW);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.LEATHER),
	MOOSHROOM((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.MUSHROOM_COW);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_SAND, Material.RED_MUSHROOM_BLOCK),
	BAT((event, loc) -> {
		for(int i = 0; i < 5; i++) {
			loc.getWorld().spawnEntity(loc, EntityType.BAT);
		}
		
		for(LivingEntity ent : event.getAffectedEntities()) {
			ent.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 15 * 20, 3));
			ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 15 * 20, 0));
		}
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.COAL_BLOCK),
	SNOW_GOLEM((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.SNOWMAN);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_SAND, Material.SNOW_BLOCK),
	SHEEP((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.SHEEP);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.WHITE_WOOL),
	RABBIT((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.RABBIT);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.RABBIT_HIDE),
	KILLER_RABBIT((event, loc) -> {
		Rabbit r = (Rabbit) loc.getWorld().spawnEntity(loc, EntityType.RABBIT);
		r.setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_LANTERN, Material.RABBIT_FOOT),
	SQUID((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.SQUID);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.INK_SAC),
	GLOW_SQUID((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.GLOW_SQUID);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.GLOW_INK_SAC),
	TURTLE((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.SQUID);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_SAND, Material.TURTLE_EGG),
	PANDA((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.PANDA);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_SAND, Material.BAMBOO),
	ZOMBIE((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_SAND,
			Material.ROTTEN_FLESH),
	SPIDER((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.SPIDER);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_SAND, Material.STRING),
	SKELETON((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.SKELETON);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_SAND, Material.BONE),
	SLIME((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.SLIME);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_SAND, Material.SLIME_BALL),
	PHANTOM((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.PHANTOM);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_SAND,
			Material.PHANTOM_MEMBRANE),
	GHAST((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.GHAST);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_SAND, Material.GHAST_TEAR),
	ENDERMAN((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.ENDERMAN);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_LANTERN,Material.ENDER_PEARL),
	MAGMA_CUBE((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.MAGMA_CUBE);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_SAND,
			Material.MAGMA_CREAM),
	BLAZE((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.BLAZE);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_SAND, Material.BLAZE_ROD),
	VILLAGER((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_LANTERN, Material.EMERALD),
	EVOKER((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.EVOKER);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_LANTERN,
			Material.TOTEM_OF_UNDYING),
	VEX((event, loc) -> {
		for(int i = 0; i < 4; i++) {
			loc.getWorld().spawnEntity(loc, EntityType.VEX);
		}
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_SAND, Material.IRON_SWORD),
	RAVAGER((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.RAVAGER);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_LANTERN, Material.SADDLE),
	SHULKER((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.SHULKER);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_LANTERN,
			Material.SHULKER_SHELL),
	WITHER((event, loc) -> {
		for(Location l : Sphere.generateSphere(loc, 4, true)) {
			WitherSkull skull = (WitherSkull) l.getWorld().spawnEntity(l, EntityType.WITHER_SKULL);
			skull.setCharged(true);
			skull.setVelocity(AbilityUtils.getDifferentialVector(l, loc).normalize().multiply(1.0));
		}
		
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_LANTERN, Material.WITHER_SKELETON_SKULL),
	TNT((event, loc) -> {
		World world = loc.getWorld();
		world.createExplosion(loc, 4.0f);
		Firework firework = (Firework) world.spawnEntity(loc, EntityType.FIREWORK);
		FireworkMeta meta = firework.getFireworkMeta();

		Type type = Type.CREEPER;
		Color c = Color.GREEN;

		FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(c).withFade(c).with(type).trail(true)
				.build();

		meta.addEffect(effect);
		meta.setPower(0);
		firework.setFireworkMeta(meta);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_SAND, Material.GUNPOWDER),
	GROWTH((event, loc) -> {
		for (Location l : Sphere.generateSphere(loc, 5, false)) {
			Block b = l.getBlock();
			BlockData data = b.getBlockData();
			if (!(data instanceof Ageable))
				return;

			Ageable ageable = (Ageable) data;
			int maxAge = ageable.getMaximumAge();
			if (ageable.getAge() < maxAge)
				return;
			int newAge = 2 + ageable.getAge();
			if (newAge > maxAge)
				newAge = maxAge;

			ageable.setAge(newAge);
			b.setBlockData(ageable);
		}
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.BONE_MEAL),
	CHICKEN((event, loc) -> {
		loc.getWorld().spawnEntity(loc, EntityType.CHICKEN);
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.EGG),
	LEECH((event, loc) -> {
		for(LivingEntity e : event.getAffectedEntities()) {
			e.damage(30 * event.getIntensity(e));
		}
		ProjectileSource source = event.getPotion().getShooter();
		if(source instanceof LivingEntity)
			AbilityUtils.heal(null, 20, (LivingEntity)source);
		
	}, Material.NETHER_WART, Material.REDSTONE, Material.FERMENTED_SPIDER_EYE, Material.SOUL_LANTERN, Material.BEETROOT);

	private ItemStack result;
	private Collection<Material> ingredients;
	private WitchesBrewAction action;

	private WitchBrewRecipe(WitchesBrewAction action, Material... ingredients) {
		ItemStack result = AbilityUtils.getWitchesBrew();
		PotionMeta meta = (PotionMeta) result.getItemMeta();

		this.ingredients = Arrays.asList(ingredients);
		meta.setDisplayName(serializeIngredients(Arrays.asList(ingredients)));

		result.setItemMeta(meta);

		this.result = result;
		this.action = action;
	}

	public Collection<Material> getIngredients() {
		return this.ingredients;
	}

	public ItemStack getResult() {
		return this.result;
	}

	public void runAction(PotionSplashEvent event, Location loc) {
		this.action.run(event, loc);
	}

	public static boolean isPerfect(Collection<Material> mats) {
		if (mats == null)
			return false;

		for (WitchBrewRecipe recipe : values()) {
			if (mats.containsAll(recipe.ingredients)) {
				return true;
			}
		}

		return false;
	}

	public static WitchBrewRecipe getMatching(ItemStack item) {
		if (item == null)
			return null;

		ItemMeta meta = item.getItemMeta();

		if (!(meta instanceof PotionMeta))
			return null;

		PotionMeta potMeta = (PotionMeta) meta;
		PotionMeta defaultMeta = (PotionMeta) AbilityUtils.getWitchesBrew().getItemMeta();
		if (!defaultMeta.getColor().equals(potMeta.getColor()))
			return null;

		Collection<Material> ingredients = deserializeIngredients(potMeta.getDisplayName());
		if (ingredients == null || ingredients.isEmpty())
			return null;

		for (WitchBrewRecipe recipe : values()) {
			if (ingredients.containsAll(recipe.ingredients)) {
				return recipe;
			}
		}

		return null;
	}

	public String serializeIngredients() {
		if (ingredients == null || ingredients.isEmpty())
			return " ";

		String name = "&6";
		List<Material> list = new ArrayList<>(ingredients);
		for (int i = 0; i < list.size(); i++) {
			name += list.get(i).name();

			if (!(i == list.size() - 1)) {
				name += "&5, &6";
			}
		}

		return ChatUtils.chat(name);
	}

	public static String serializeIngredients(Collection<Material> ingredients) {
		if (ingredients == null || ingredients.isEmpty())
			return " ";

		String name = "&6";
		List<Material> list = new ArrayList<>(ingredients);
		for (int i = 0; i < list.size(); i++) {
			name += list.get(i).name();

			if (!(i == list.size() - 1)) {
				name += "&5, &6";
			}
		}

		return ChatUtils.chat(name);
	}

	public static Collection<Material> deserializeIngredients(String ingredients) {
		if (ingredients == null)
			return null;

		Collection<Material> mats = new ArrayList<>();
		String name = ChatColor.stripColor(ingredients);

		List<String> names = Arrays.asList(name.split(", "));

		for (String s : names) {
			mats.add(Material.getMaterial(s));
		}
		return mats;
	}
	
	public static ItemStack getRandom() {
		Random rng = new Random();
		WitchBrewRecipe recipe = values()[rng.nextInt(values().length)];
		ItemStack item = recipe.getResult();
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(serializeIngredients(recipe.ingredients));
		item.setItemMeta(meta);
		
		return item;
	}
}
