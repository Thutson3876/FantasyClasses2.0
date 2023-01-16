package me.thutson3876.fantasyclasses.custommobs.boss.voidremnant;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.WitherSkull;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.custommobs.boss.MobAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Barrage implements MobAbility {

	private int numberOfProjectiles = 9;
	private int count = 0;
	private Vector launchVector = null;
	private int animationTimeInTicks = 2 * 20;
	
	@Override
	public String getName() {
		return "Barrage";
	}

	@Override
	public void run(Mob ent) {
		LivingEntity target = ent.getTarget();
		Location targetLoc = target.getEyeLocation();
		
		launchVector = AbilityUtils.getVectorBetween2Points(ent.getLocation(), targetLoc, 2.0);
		World world = ent.getWorld();
		spawnWitherSkull(world, ent, launchVector);
		double rotationRadians = (2 * 6.28319) / numberOfProjectiles;
		long timeBetweenProjectiles = Math.round(animationTimeInTicks / numberOfProjectiles);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				if(ent.isDead() || count > numberOfProjectiles) {
					count = 0;
					launchVector = null;
					cancel();
					return;
				}
					
				
				launchVector.rotateAroundY(rotationRadians);
				spawnWitherSkull(world, ent, launchVector);
				count++;
			}
			
		}.runTaskTimer(FantasyClasses.getPlugin(), 1, timeBetweenProjectiles);
		
	}
	
	private void spawnWitherSkull(World world, Mob ent, Vector target) {
		Location spawnLoc = ent.getLocation();
		WitherSkull skull = (WitherSkull) world.spawnEntity(spawnLoc.add(launchVector.multiply(0.2)), EntityType.WITHER_SKULL);
		skull.setShooter(ent);
		skull.setCharged(true);
		skull.setDirection(target.multiply(2.0));
		skull.setVelocity(target.multiply(2.0));
		
		world.playSound(spawnLoc, Sound.ENTITY_WITHER_SHOOT, 1.0f, 1.0f);
	}

}
