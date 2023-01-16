package me.thutson3876.fantasyclasses.custommobs.boss.uthroes;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.custommobs.boss.MobAbility;

public class RemorselessWinter implements MobAbility {

	private final int durationInTicks = 10 * 20;
	private final int tickRate = 7;
	private final int radius = 4;
	
	private final int freezeAmt = 100;
	private final double dmg = 6.0;
	private final int duration = 20;
	private final int amp = 1;
	private final PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, duration, amp);
	private final PotionEffect fatigue = new PotionEffect(PotionEffectType.SLOW_DIGGING, duration, amp);
	
	private int counter = 0;
	
	@Override
	public String getName() {
		return "Remorseless Winter";
	}

	@Override
	public void run(Mob entity) {
		spawnSnowStorm(entity);		
	}
	
	private void spawnSnowStorm(Mob entity) {
		World world = entity.getWorld();
		
		counter = 0;
		new BukkitRunnable() {

			@Override
			public void run() {
				if(counter > durationInTicks) {
					this.cancel();
					return;
				}
				if(counter < durationInTicks / 2)
					world.playSound(entity, Sound.ITEM_ELYTRA_FLYING, 1.0f, 0.9f);
				
				world.spawnParticle(Particle.SNOWFLAKE, entity.getEyeLocation(), 40);
				/*for(Location l : sphere) {
					world.spawnParticle(Particle.SNOWFLAKE, l, 10);
				}*/
				for(Entity e : entity.getNearbyEntities(radius, radius, radius)){
					if(e instanceof LivingEntity && !(e instanceof Mob) && !e.isDead()) {
						LivingEntity ent = (LivingEntity) e;
						ent.addPotionEffect(fatigue);
						ent.addPotionEffect(slow);
						ent.damage(dmg, entity);
						ent.setFreezeTicks(ent.getFreezeTicks() + freezeAmt);
					}
				}
				
				counter += tickRate;
			}
			
		}.runTaskTimer(FantasyClasses.getPlugin(), 1, tickRate);
	}

}
