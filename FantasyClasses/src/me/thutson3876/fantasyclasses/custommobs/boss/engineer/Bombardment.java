package me.thutson3876.fantasyclasses.custommobs.boss.engineer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.custommobs.boss.MobAbility;
import me.thutson3876.fantasyclasses.util.Sphere;

public class Bombardment implements MobAbility {

	private final int radiusInterval = 1;
	private final int tickRate = 30;
	private final int fuseTicks = 20;
	
	private int castAmount = 1;
	private int count = 1;
	
	@Override
	public String getName() {
		return "Bombardment";
	}

	@Override
	public void run(Mob entity) {
		List<Integer> casts = new ArrayList<>();
		
		for(int i = 0; i < castAmount; i++) {
			casts.add((i + 1) * radiusInterval);
		}
		
		Collections.shuffle(casts);
		
		World world = entity.getLocation().getWorld();
		
		world.playSound(entity.getLocation(), Sound.ENTITY_TNT_PRIMED, 5.0f, 0.5f);
		
		int reduceAmt = 12 - casts.size() / 3;
		
		count = 0;
		
		new BukkitRunnable() {

			@Override
			public void run() {
				if(entity == null || entity.isDead()) {
					this.cancel();
					return;
				}
				
				for(Location loc : Sphere.generateCircle(entity.getLocation(), casts.get(count), true)) {
					if(count % reduceAmt == 1)
						continue;
					
					Vector launchVector = loc.toVector().subtract(entity.getLocation().toVector()).multiply(0.07).add(new Vector(0, 0.93, 0));
					TNTPrimed tnt = (TNTPrimed) world.spawnEntity(entity.getEyeLocation().add(0, 0.4, 0), EntityType.PRIMED_TNT);
					tnt.setFuseTicks(tickRate + fuseTicks + (int)(launchVector.length() / 2));
					tnt.setYield(3.0f);
					tnt.setSource(entity);
					tnt.setVelocity(launchVector);
				}

				world.playSound(entity.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 5.0f, 1.3f);
				
				count++;
				if(count >= casts.size()) {
					this.cancel();
					return;
				}
			}
			
		}.runTaskTimer(FantasyClasses.getPlugin(), tickRate, tickRate);
		
		if(castAmount < 5)
			castAmount++;
	}

}
