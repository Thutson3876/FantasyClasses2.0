package me.thutson3876.fantasyclasses.classes.ranger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.util.AbilityUtils;

public enum TrickShot {

	EXPLOSIVE((arrow, hitEntity) -> {
		ProjectileSource shooter = arrow.getShooter();
		if (shooter instanceof Entity)
			arrow.getWorld().createExplosion(hitEntity.getLocation(), 1.0F, false, false, (Entity) shooter);
		else
			arrow.getWorld().createExplosion(hitEntity.getLocation(), 1.0F, false, false);
	}), RAIN((arrow, hitEntity) -> {
		Location entityLoc = hitEntity.getLocation();

		int spawnAmt = 11;

		int maxXChange = 4;
		int maxYChange = 1;
		int maxZChange = 4;

		double x = entityLoc.getX();
		double y = entityLoc.getY() + 7.0;
		double z = entityLoc.getZ();

		List<Location> spawnLocations = new ArrayList<>();

		for (int i = 0; i < spawnAmt; i++) {
			Location loc = new Location(arrow.getWorld(), x + randomDoubleChange(maxXChange),
					y + randomDoubleChange(maxYChange), z + randomDoubleChange(maxZChange));
			spawnLocations.add(loc);
		}

		World world = entityLoc.getWorld();

		for (Location loc : spawnLocations) {
			Arrow arr = world.spawnArrow(loc, new Vector(0, -0.91, 0), 1.0f, 6);
			arr.setShooter(arrow.getShooter());
			arr.setDamage(4.0);
			arr.setPickupStatus(PickupStatus.CREATIVE_ONLY);
		}
	}), FOLLOW_UP((arrow, hitEntity) -> {
		Location entityLoc = hitEntity.getLocation().add(0, 1, 0);
		Random rng = new Random();
		World world = arrow.getWorld();
		double speed = 1.5;

		int spawnAmt = 4;
		double range = 4.0;
		
		int maxXChange = 6;
		int maxYChange = 1;
		int maxZChange = 6;
		
		double x = entityLoc.getX();
		double y = entityLoc.getY() + hitEntity.getHeight();
		double z = entityLoc.getZ();

		List<LivingEntity> targets = AbilityUtils.getNearbyLivingEntities(entityLoc, range, range, range);
		targets.remove(arrow.getShooter());
		if (targets.isEmpty() && hitEntity instanceof LivingEntity)
			targets.add((LivingEntity) hitEntity);

		if (targets.isEmpty())
			return;

		List<Location> spawnLocations = new ArrayList<>();
		for (int i = 0; i < spawnAmt; i++) {
			Location loc = new Location(arrow.getWorld(), x + randomDoubleChange(maxXChange),
					y + randomDoubleChange(maxYChange), z + randomDoubleChange(maxZChange));
			spawnLocations.add(loc);
		}

		for (Location spawnLoc : spawnLocations) {
			Arrow newArrow = world.spawnArrow(spawnLoc, AbilityUtils.getVectorBetween2Points(spawnLoc,
					targets.get(rng.nextInt(targets.size())).getLocation(), speed), 1.0f, 6);

			newArrow.setShooter(arrow.getShooter());
			newArrow.setPierceLevel(spawnAmt);
			newArrow.setDamage(4.0);
			newArrow.setPickupStatus(PickupStatus.CREATIVE_ONLY);
		}

	}), GRASPING((arrow, hitEntity) -> {
		Location entityLoc = hitEntity.getLocation();

		double range = 3.5;

		int duration = 6 * 20;
		int amp = 3;

		PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, duration, amp);

		List<LivingEntity> targets = AbilityUtils.getNearbyLivingEntities(entityLoc, range, range, range);

		if (hitEntity instanceof LivingEntity)
			((LivingEntity) hitEntity).addPotionEffect(slowness);

		for (LivingEntity le : targets) {
			if (le.isDead() || le.equals(hitEntity))
				continue;

			le.setVelocity(AbilityUtils.getVectorBetween2Points(le.getLocation(), entityLoc, 0.5));
			le.addPotionEffect(slowness);
		}
	});

	private final TrickShotAction ACTION;

	private TrickShot(TrickShotAction action) {
		this.ACTION = action;
	}

	public void run(Arrow arrow, Entity hitEntity) {
		this.ACTION.run(arrow, hitEntity);
	}

	public static TrickShot getRandom() {
		Random rng = new Random();

		return values()[rng.nextInt(values().length)];
	}

	private static double randomDoubleChange(double maxChange) {
		Random rng = new Random();
		return ((rng.nextDouble() * maxChange * 2) - maxChange) * rng.nextDouble();
	}

	public static TrickShot[] getRandomOrder() {
		List<TrickShot> list = Arrays.asList(values());

		Collections.shuffle(list);

		return list.toArray(values());
	}
}
