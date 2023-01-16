package me.thutson3876.fantasyclasses.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.classes.witch.WitchBrewRecipe;
import me.thutson3876.fantasyclasses.events.HealEvent;

public class AbilityUtils {

	private static final ItemStack BLOOD_VIAL;
	private static final ItemStack WITCHES_BREW;

	static {
		ItemStack bloodTemp = new ItemStack(Material.POTION);
		PotionMeta bloodMeta = (PotionMeta) bloodTemp.getItemMeta();
		bloodMeta.setColor(Color.RED);
		bloodMeta.setDisplayName(ChatUtils.chat("&4Blood Vial"));
		bloodTemp.setItemMeta(bloodMeta);

		BLOOD_VIAL = bloodTemp;

		ItemStack witchTemp = new ItemStack(Material.SPLASH_POTION);
		PotionMeta witchMeta = (PotionMeta) witchTemp.getItemMeta();
		witchMeta.setColor(Color.PURPLE);
		witchMeta.setDisplayName(ChatUtils.chat("&6Witch's Brew"));
		List<String> lore = new ArrayList<>();
		lore.add("Ingredients: ");
		witchTemp.setItemMeta(witchMeta);

		WITCHES_BREW = witchTemp;
	}
	
	public static ItemStack setDisplayName(String name, ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatUtils.chat(name));
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static ItemStack setLore(List<String> lore, ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static ItemStack createItem(String displayName, List<String> lore, Material material, int stackSize) {
		ItemStack item = new ItemStack(material, stackSize);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatUtils.chat(displayName));
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static Location getMidpoint(Location l1, Location l2) {
		double x = (l1.getX() + l2.getX()) / 2.0;
		double y = (l1.getY() + l2.getY()) / 2.0;
		double z = (l1.getZ() + l2.getZ()) / 2.0;
		return new Location(l1.getWorld(), x, y, z);
	}
	
	public static LivingEntity getLivingTarget(LivingEntity ent, double range) {
		for (Entity e : getNearbyLivingEntities(ent, range, range, range)) {
			if (e.getLocation().distance(ent.getEyeLocation()) < 0.1) {
				return (LivingEntity) e;
			}
		}

		return null;
	}

	public static LivingEntity rayTraceTarget(LivingEntity source, double maxDistance) {
		Predicate<Entity> notSource = (e) -> !e.equals(source) && e instanceof LivingEntity;
		RayTraceResult result = source.getWorld().rayTraceEntities(source.getEyeLocation(), source.getEyeLocation().getDirection(), maxDistance, notSource);
		
		if(result == null || result.getHitEntity() == null)
			return null;
		
		return (LivingEntity) result.getHitEntity();
	}
	
	public static List<LivingEntity> getEntitiesInLineOfSight(LivingEntity entity, double range){
		List<LivingEntity> targets = new ArrayList<>();
		
		for(Entity e : entity.getNearbyEntities(range, range, range)) {
			if(entity.hasLineOfSight(e) && e instanceof LivingEntity)
				targets.add((LivingEntity)e);
		}
		
		return targets;
	}
	
	public static List<LivingEntity> onlyLiving(List<Entity> entities) {
		List<LivingEntity> result = new ArrayList<>();
		for (Entity ent : entities) {
			if (ent instanceof LivingEntity)
				result.add((LivingEntity) ent);
		}

		return result;
	}

	public static List<LivingEntity> getNearbyLivingEntities(Entity ent, double x, double y, double z) {
		List<LivingEntity> livingEntities = new ArrayList<>();
		for (Entity e : ent.getNearbyEntities(x, y, z)) {
			if (e instanceof LivingEntity)
				livingEntities.add((LivingEntity) e);
		}

		return livingEntities;
	}
	
	public static List<LivingEntity> getNearbyLivingEntities(Location loc, double x, double y, double z) {
		List<LivingEntity> livingEntities = new ArrayList<>();
		for (Entity e : loc.getWorld().getNearbyEntities(loc, x, y, z)) {
			if (e instanceof LivingEntity)
				livingEntities.add((LivingEntity) e);
		}

		return livingEntities;
	}
	
	public static float getAngleOfPlayerEyeDirectionAndLocation(Player p, Location loc) {
		Vector eyeDirection = p.getEyeLocation().getDirection();
		return eyeDirection.angle(p.getLocation().toVector());
	}

	public static LivingEntity getNearestLivingEntity(Location loc, List<LivingEntity> entities) {
		if (entities.isEmpty())
			return null;

		LivingEntity nearest = entities.get(0);
		double distance = 99999;
		for (LivingEntity ent : entities) {
			double temp = ent.getLocation().distance(loc);
			if (temp > distance) {
				distance = temp;
				nearest = ent;
			}
		}

		return nearest;
	}

	public static BlockFace getBlockFace(Player player, int range) {
		List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, range);
		if (lastTwoTargetBlocks.size() != 2 || !lastTwoTargetBlocks.get(1).getType().isOccluding())
			return null;
		Block targetBlock = lastTwoTargetBlocks.get(1);
		Block adjacentBlock = lastTwoTargetBlocks.get(0);
		return targetBlock.getFace(adjacentBlock);
	}

	public static Vector getDifferentialVector(Location from, Location to) {
		return new Vector((to.getX() - from.getX()), to.getY() - from.getY(), (to.getZ() - from.getZ()));
	}

	public static void randomSpreadGeneration(Location start, Material fillType, int spreadChance, int decreasePerTick,
			boolean replaceAll) {
		Block b = start.getBlock();
		b.setType(fillType);
		Random rng = new Random();
		int i = 1;
		int roll = rng.nextInt(100) + 10;
		while (roll < (spreadChance / i) + 10) {
			spreadChance -= decreasePerTick;
			Block next = null;
			do {
				next = b.getRelative(BlockFace.values()[rng.nextInt(BlockFace.values().length)]);
			} while (!replaceAll && !next.isPassable());

			randomSpreadGeneration(next.getLocation(), fillType, spreadChance, decreasePerTick, replaceAll);
			i++;
		}
		return;
	}
	
	public static List<Player> getNearbyPlayers(Entity ent, double range){
		List<Player> list = new ArrayList<>();
		for(Entity e : ent.getNearbyEntities(range, range, range)) {
			if(e instanceof Player)
				list.add((Player)e);
		}
		
		return list;
	}

	public static void heal(LivingEntity source, double amt, LivingEntity target) {
		if (target == null || target.isDead())
			return;

		double maxhp = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		if (target.hasPotionEffect(PotionEffectType.UNLUCK) || target.hasPotionEffect(PotionEffectType.WITHER))
			amt *= 0.5;

		HealEvent event = new HealEvent(source, amt, target);
		Bukkit.getPluginManager().callEvent(event);
		
		double newhp = target.getHealth() + event.getHealAmt();
		if (newhp > maxhp)
			newhp = maxhp;

		target.setHealth(newhp);
		target.getWorld().spawnParticle(Particle.COMPOSTER, target.getLocation().add(0, target.getHeight() / 2.0, 0), 8);
		target.getWorld().playSound(target.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 5.0f, 1.1f);
	}

	public static void setMaxHealth(Entity e, double amt, Operation op) {
		if (!(e instanceof LivingEntity))
			return;

		LivingEntity ent = (LivingEntity) e;
		AttributeModifier mod = new AttributeModifier("maxhealth", amt, op);
		ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(mod);
	}

	public static void setMaxHealth(LivingEntity e, AttributeModifier mod) {
		if (e.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().contains(mod))
			return;

		e.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(mod);
	}

	public static void setAttackDamage(LivingEntity e, AttributeModifier mod) {
		if(e.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) == null)
			return;
		
		if (e.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getModifiers().contains(mod))
			return;

		e.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).addModifier(mod);
	}

	public static void setAttackDamage(Entity e, double amt, Operation op) {
		if (!(e instanceof LivingEntity))
			return;

		LivingEntity ent = (LivingEntity) e;
		AttributeModifier mod = new AttributeModifier("atkdamage", amt, op);
		ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).addModifier(mod);
	}

	// Measures the distance between player and all nearby entities and selects the
	// closest (ignoring y distance)
	public static Entity closestEntity(Player p, Entity[] nearbyEntities, double minHeight) {
		Entity closest = null;
		double distanceToPlayer = 1000; // Specifically the distance from the player without taking the y value into
										// account (only differences between X and Z)

		for (Entity e : nearbyEntities) {
			if (e == null) {
				return null;
			}
			Location entityLoc = e.getLocation();
			Location playerLoc = p.getLocation();

			if (playerLoc.getY() - entityLoc.getY() > minHeight) {
				double xDiffSqrd = (playerLoc.getX() - entityLoc.getX()) * (playerLoc.getX() - entityLoc.getX());
				double zDiffSqrd = (playerLoc.getZ() - entityLoc.getZ()) * (playerLoc.getZ() - entityLoc.getZ());

				// distance formula
				if (distanceToPlayer > Math.sqrt(xDiffSqrd + zDiffSqrd)) {
					closest = e;
				}
			}
		}

		return closest;
	}

	public static boolean isHarvestable(BlockData data) {
		if (!(data instanceof Ageable))
			return false;
		Ageable ageable = (Ageable) data;
		if (ageable.getAge() < ageable.getMaximumAge())
			return false;

		return true;
	}

	public static List<Location> generateRectangle(Location center, double xLength, double zLength) {
		List<Location> locations = new LinkedList<>();
		double tempX = -(xLength / 2);
		double tempZ = -(zLength / 2);
		for (int i = 0; i < xLength; i++) {
			tempX += i;

			Location temp = new Location(center.getWorld(), tempX, center.getY(), center.getZ());
			locations.add(temp);
			temp = new Location(center.getWorld(), tempX, center.getY(), center.getZ() + tempZ);
			locations.add(temp);
		}
		for (int i = 0; i < xLength; i++) {
			tempZ += i;

			Location temp = new Location(center.getWorld(), center.getX() - tempX, center.getY(), tempZ);
			if (!locations.contains(temp)) {
				locations.add(temp);
			}
			temp = new Location(center.getWorld(), center.getX(), center.getY(), tempZ);
			if (!locations.contains(temp)) {
				locations.add(temp);
			}
		}

		return locations;
	}

	public static Entity closestEntityFromList(List<Entity> list, Entity entity) {
		if (list == null) {
			return null;
		}
		if (list.isEmpty()) {
			return null;
		}

		Entity closest = null;
		double distance = 10000;
		double tempDistance = 1000;
		for (Entity e : list) {
			tempDistance = e.getLocation().distance(entity.getLocation());
			if (tempDistance < distance) {
				distance = tempDistance;
				closest = e;
			}
		}

		return closest;
	}

	public static List<Entity> getEntitiesInAngle(Player p, double maxAngle, double maxDistance) {
		Vector dirToDestination;
		Vector playerDirection;
		double angle;
		List<Entity> enemies = p.getNearbyEntities(maxDistance, maxDistance, maxDistance);
		List<Entity> targets = new LinkedList<>();

		playerDirection = p.getEyeLocation().getDirection();

		for (Entity e : enemies) {
			if (p.hasLineOfSight(e)) {
				dirToDestination = e.getLocation().toVector().subtract(p.getEyeLocation().toVector());
				angle = dirToDestination.angle(playerDirection);

				if (angle < maxAngle && angle > -maxAngle) {
					targets.add(e);
				}
			}
		}

		return targets;
	}
	
	public static boolean isInWaterOrRain(Entity ent) {
		double y = ent.getLocation().getY();
		boolean isInRain = ent.getWorld().getHighestBlockAt(ent.getLocation()).getY() < y && !ent.getWorld().isClearWeather();
		
		
		return ent.isInWater() || isInRain;
	}

	public static List<Entity> getEntitiesInAngle(Player p, double maxAngle, double maxDistance, double offset) {
		Vector dirToDestination;
		Vector playerDirection;
		double angle;
		List<Entity> enemies = p.getNearbyEntities(maxDistance, maxDistance, maxDistance);
		List<Entity> targets = new LinkedList<>();

		playerDirection = p.getEyeLocation().getDirection();
		playerDirection.setX(playerDirection.getX() - offset);
		playerDirection.setZ(playerDirection.getZ() - offset);

		for (Entity e : enemies) {
			if (p.hasLineOfSight(e)) {
				dirToDestination = e.getLocation().toVector().subtract(p.getEyeLocation().toVector());
				angle = dirToDestination.angle(playerDirection);

				if (angle < maxAngle && angle > -maxAngle) {
					targets.add(e);
				}
			}
		}

		return targets;
	}

	// Filters any non-living entities out of an array of entities
	public static Entity[] onlyLiving(Entity[] entities) {
		ArrayList<Entity> entitiesAsList = new ArrayList<>();
		for (Entity e : entities) {
			if (e instanceof LivingEntity) {
				entitiesAsList.add(e);
			}
		}

		return entitiesAsList.toArray(new Entity[1]);
	}

	public static Vector getVectorBetween2Points(Location loc1, Location loc2, double distanceScaling) {
		Vector returnVector = loc2.toVector().subtract(loc1.toVector()).normalize();
		double distance = loc2.distance(loc1);
		if (distanceScaling != 0 && distance != 0)
			returnVector.multiply(distanceScaling * distance);

		return returnVector;
	}

	public static void moveToward(Entity entity, Location to, double speed) {
		entity.setVelocity(to.subtract(entity.getLocation()).toVector().normalize().multiply(speed));
	}

	public static void moveToward(Entity entity, Location to, double speed, double yMod) {
		Vector velocity = to.subtract(entity.getLocation()).toVector().normalize().multiply(speed);
		velocity.setY(velocity.getY() * yMod);
		entity.setVelocity(velocity);
	}

	public static void moveTowardPlusY(Entity entity, Location to, double speed, double bonusY) {
		Vector velocity = to.subtract(entity.getLocation()).toVector().normalize().multiply(speed);
		velocity.setY(velocity.getY() + bonusY);
		entity.setVelocity(velocity);
	}

	public static Block[] getBlocksAroundPlayer(Player p, int radius) {
		ArrayList<Block> blockList = new ArrayList<>();
		Location playerLoc = p.getLocation();
		Block temp = null;

		for (Location loc : Sphere.generateSphere(playerLoc, radius, false)) {
			temp = loc.getBlock();
			blockList.add(temp);
		}

		return blockList.toArray(new Block[1]);
	}

	public static double getHeightAboveGround(Entity e) {
		if (e.isOnGround()) {
			return 0;
		}

		Location eLoc = e.getLocation();
		Block currentBlock = eLoc.getBlock();
		Block nextBlock;

		for (int i = 0; i < 2048; i++) {
			nextBlock = currentBlock.getRelative(BlockFace.DOWN);
			if (!nextBlock.isPassable()) {
				return eLoc.distance(currentBlock.getLocation());
			}
			currentBlock = nextBlock;
		}

		return -999;
	}

	public static boolean isCritical(Player p) {
		return p.getVelocity().getY() < -0.1;
	}

	public static boolean hasArmor(Player p) {
		ItemStack[] armorContents = p.getInventory().getArmorContents();
		// System.out.println(p.getDisplayName() + " armor contents: " +
		// armorContents.toString());
		for (int i = 0; i < armorContents.length; i++) {
			if (armorContents[i] != null) {
				if (MaterialLists.ARMOR.getMaterials().contains(armorContents[i].getType()))
					return true;
			}
		}
		return false;
	}

	public static List<LivingEntity> getNearbyPlayerPets(Player p, double distance) {
		List<LivingEntity> pets = new ArrayList<>();
		for (Entity e : p.getNearbyEntities(distance, distance, distance)) {
			if (e instanceof Tameable) {
				if (((Tameable) e).getOwner() == null)
					continue;

				if (((Tameable) e).getOwner().equals(p))
					pets.add((LivingEntity) e);
			}
		}

		return pets;
	}

	public static void applyStackingPotionEffect(PotionEffect effect, LivingEntity entity, int maxAmp,
			int maxDurationInTicks) {
		if (entity.isDead())
			return;

		if (!entity.hasPotionEffect(effect.getType())) {
			entity.addPotionEffect(effect);
			return;
		}

		PotionEffect currentEffect = entity.getPotionEffect(effect.getType());
		int effectAmp = effect.getAmplifier();
		if(effectAmp == 0)
			effectAmp = 1;
		
		int newAmp = currentEffect.getAmplifier() + effectAmp;
		if (newAmp > maxAmp)
			newAmp = maxAmp;
		int newDuration = currentEffect.getDuration() + effect.getDuration();
		if (newDuration > maxDurationInTicks)
			newDuration = maxDurationInTicks;

		entity.addPotionEffect(new PotionEffect(effect.getType(), newDuration, newAmp));
	}

	public static double doubleRoundToXDecimals(double val, int decimals) {
		String format = "###.";
		for (int i = 0; i < decimals; i++) {
			format += "#";
		}
		DecimalFormat df2 = new DecimalFormat(format);
		return Double.valueOf(df2.format(val));
	}

	public static boolean isBloodVial(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		if (!item.getType().equals(Material.POTION))
			return false;

		if (!(meta instanceof PotionMeta))
			return false;

		PotionMeta pm = (PotionMeta) meta;
		PotionMeta bloodMeta = (PotionMeta) BLOOD_VIAL.getItemMeta();

		if (!pm.getColor().equals(bloodMeta.getColor()))
			return false;

		if (!pm.getDisplayName().equalsIgnoreCase(bloodMeta.getDisplayName()))
			return false;

		return true;
	}

	public static ItemStack generateRandomWitchesBrew() {
		Random rng = new Random();
		WitchBrewRecipe[] recipes = WitchBrewRecipe.values();
		int i = rng.nextInt(recipes.length);
		System.out.println(i);
		ItemStack item = recipes[i].getResult();
		System.out.println("Generated witch brew: " + recipes[i].name());
		System.out.println(item.getItemMeta().getDisplayName());
		return item;
	}

	public static ItemStack getBloodVial() {
		return BLOOD_VIAL;
	}

	public static ItemStack getWitchesBrew() {
		return WITCHES_BREW;
	}
}
