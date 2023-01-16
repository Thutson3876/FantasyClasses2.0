package me.thutson3876.fantasyclasses.custommobs.boss.engineer;

import java.util.List;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.custommobs.boss.MobAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class FireworkLauncher implements MobAbility {

	private final int delay = 20;
	private final int range = 15;
	private int fwAmt = 1;
	private int count = 0;
	
	@Override
	public String getName() {
		return "Firework Launcher";
	}

	@Override
	public void run(Mob entity) {
		World world = entity.getWorld();
		
		world.playSound(entity.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 10.0f, 0.7f);
		
		List<LivingEntity> nearbyEntities = AbilityUtils.getNearbyLivingEntities(entity, range, range, range);
		count = 0;
		
		new BukkitRunnable() {

			@Override
			public void run() {
				if(entity == null || entity.isDead()) {
					this.cancel();
					return;
				}
				
				Random rng = new Random();
				Location spawnLoc = entity.getEyeLocation().add(0, 2.0, 0);
				Firework fw = (Firework) world.spawnEntity(spawnLoc, EntityType.FIREWORK);
				FireworkMeta fwMeta = fw.getFireworkMeta();
				fwMeta.addEffect(FireworkEffect.builder().withColor(Color.fromBGR(rng.nextInt(255), rng.nextInt(255), rng.nextInt(255))).flicker(rng.nextBoolean()).trail(rng.nextBoolean()).with(Type.values()[rng.nextInt(Type.values().length)]).build());
				fw.setFireworkMeta(fwMeta);
				fw.setShooter(entity);
				Location target = nearbyEntities.get(rng.nextInt(nearbyEntities.size())).getLocation();
				
				new BukkitRunnable() {

					@Override
					public void run() {
						if(fw == null || fw.isDead()) {
							this.cancel();
							return;
						}
							
						fw.setVelocity(fw.getLocation().toVector().subtract(target.toVector()));
					}
					
				}.runTaskTimer(FantasyClasses.getPlugin(), 1, 1);
				
				
				count++;
				if(count >= fwAmt) {
					this.cancel();
					return;
				}
				
				world.playSound(entity.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 10.0f, 0.7f);
			}
			
		}.runTaskTimer(FantasyClasses.getPlugin(), delay, delay);
		
		fwAmt++;
	}

}
