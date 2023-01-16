package me.thutson3876.fantasyclasses.custommobs.boss.voidremnant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.scheduler.BukkitRunnable;
import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.custommobs.boss.MobAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class WitheringRain implements MobAbility {

	private final int range = 20;
	private final int tickRate = 10;
	private final int rainAmt = 6;
	
	@Override
	public String getName() {
		return "Withering Rain";
	}

	@Override
	public void run(Mob entity) {
		int delay = tickRate;
		World world = entity.getWorld();
		List<Entity> targets = new ArrayList<>();
		for(Entity e : entity.getNearbyEntities(range, range, range)) {
			if(e instanceof Player)
				targets.add(e);
		}
		
		if(targets == null || targets.isEmpty())
			return;
		
		int targetsLength = targets.size();
		Random rng = new Random();
		
		for(int i = 0; i < rainAmt; i++) {
			new BukkitRunnable() {

				@Override
				public void run() {
					Entity e = targets.get(rng.nextInt(targetsLength));
					world.playSound(entity.getLocation(), Sound.ENTITY_WITHER_SHOOT, 5.0f, 1.0f);
					WitherSkull skull = (WitherSkull) world.spawnEntity(e.getLocation().add(0, 5.0, 0), EntityType.WITHER_SKULL);
					skull.setCharged(true);
					skull.setShooter(entity);
					skull.setDirection(AbilityUtils.getDifferentialVector(skull.getLocation(), e.getLocation()));
				}
				
			}.runTaskLater(FantasyClasses.getPlugin(), delay);
			
			delay += tickRate;
		}
	}

}
