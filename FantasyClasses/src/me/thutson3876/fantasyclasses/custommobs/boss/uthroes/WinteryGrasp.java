package me.thutson3876.fantasyclasses.custommobs.boss.uthroes;

import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.Snowball;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.custommobs.boss.MobAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class WinteryGrasp implements MobAbility {

	@Override
	public String getName() {
		return "Wintery Grasp";
	}

	@Override
	public void run(Mob entity) {
		entity.getWorld().playSound(entity, Sound.ENTITY_SNOW_GOLEM_AMBIENT, 5.0f, 0.8f);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				double addedHeight = 0;
				if(entity instanceof PolarBear)
					addedHeight = 1.0;
				
				entity.getWorld().playSound(entity, Sound.ENTITY_SNOW_GOLEM_SHOOT, 5.0f, 0.8f);
				Vector velocity = AbilityUtils.getVectorBetween2Points(entity.getLocation().add(0, addedHeight, 0), entity.getTarget().getEyeLocation(), 1.0);
				Snowball snowball = (Snowball) entity.getWorld().spawnEntity(entity.getEyeLocation().add(velocity.multiply(0.4)).add(0, addedHeight, 0), EntityType.SNOWBALL);
				snowball.setVelocity(velocity.multiply(1.0));
				snowball.setShooter(entity);
			}
			
		};
		
	}

}
