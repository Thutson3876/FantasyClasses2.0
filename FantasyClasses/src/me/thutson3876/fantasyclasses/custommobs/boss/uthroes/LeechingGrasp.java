package me.thutson3876.fantasyclasses.custommobs.boss.uthroes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.custommobs.boss.MobAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.Particles;

public class LeechingGrasp implements MobAbility {

	private final int range = 16;
	private final int delay = 2 * 20;
	private final double damage = 30.0;
	
	private final PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, 5 * 20, 3);
	
	@Override
	public String getName() {
		return "Leeching Grasp";
	}

	@Override
	public void run(Mob entity) {
		List<LivingEntity> targets = getEntitiesInLineOfSight(entity);
		//World world = entity.getWorld();
		
		for(LivingEntity e : targets) {
			//Location loc = entity.getEyeLocation();
			Location targetLoc = e.getEyeLocation();
			if(e instanceof Player)
				((Player)e).playSound(targetLoc, Sound.BLOCK_CONDUIT_AMBIENT, 5.0f, 0.9f);
			
			Particles.drawLine(entity.getEyeLocation(), targetLoc, Particle.SONIC_BOOM, 0.2);
			
		}
		
		new BukkitRunnable() {

			@Override
			public void run() {
				if(entity == null || entity.isDead())
					return;
				
				entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_CONDUIT_AMBIENT, 10.0f, 0.8f);
				for(LivingEntity e : getEntitiesInLineOfSight(entity)) {
					e.damage(damage, entity);
					AbilityUtils.heal(entity, damage * 2.0, entity);
					onHit(entity, e);
				}
				
			}
			
		}.runTaskLater(FantasyClasses.getPlugin(), delay);
		
	}
	
	private void onHit(LivingEntity ent, LivingEntity target) {
		target.setVelocity(AbilityUtils.getVectorBetween2Points(target.getLocation(), ent.getLocation(), 0.45));
		Particles.drawLine(ent.getEyeLocation(), target.getEyeLocation(), Particle.SONIC_BOOM, 0.2);
		target.addPotionEffect(slowness);
	}
	
	private List<LivingEntity> getEntitiesInLineOfSight(LivingEntity entity){
		List<LivingEntity> targets = new ArrayList<>();
		
		for(Entity e : entity.getNearbyEntities(range, range, range)) {
			if(entity.hasLineOfSight(e) && e instanceof LivingEntity)
				targets.add((LivingEntity)e);
		}
		
		return targets;
	}

}
