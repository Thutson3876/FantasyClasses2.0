package me.thutson3876.fantasyclasses.custommobs.boss.engineer;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Mob;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.custommobs.FailedExperiment;
import me.thutson3876.fantasyclasses.custommobs.boss.MobAbility;

public class SummonStudents implements MobAbility {

	private final int delay = 20;
	private int spawnAmt = 1;
	private int count = 0;
	
	@Override
	public String getName() {
		return "Summon Students";
	}

	@Override
	public void run(Mob entity) {
		World world = entity.getWorld();
		
		count = 0;
		
		new BukkitRunnable() {

			@Override
			public void run() {
				if(entity == null || entity.isDead()) {
					this.cancel();
					return;
				}
				new FailedExperiment(entity.getLocation());

				world.playSound(entity.getLocation(), Sound.ENTITY_ZOMBIE_INFECT, 10.0f, 0.9f);
				count++;
				if(count >= spawnAmt) {
					this.cancel();
					return;
				}
			}
			
		}.runTaskTimer(FantasyClasses.getPlugin(), delay, delay);
		
		spawnAmt++;
	}

}
