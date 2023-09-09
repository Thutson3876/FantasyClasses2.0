package me.thutson3876.fantasyclasses.custommobs.boss.descendent;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.custommobs.boss.AbstractBoss;
import me.thutson3876.fantasyclasses.custommobs.boss.BossAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class BoundedReality extends BossAbility {

	// Deal 15 damage + 5 per alive minion to all players more than 10 blocks away from boss
	
	private int radius = 10;
	private double damage = 15.0;
	private double bonusDmg = 5.0;
	
	private double currentRadii = 0.0;
	public BoundedReality(AbstractBoss boss) {
		super(boss);
	}

	@Override
	public String getName() {
		return "Bounded Reality";
	}

	@Override
	public void run(Mob entity) {
		currentRadii = 0.0;
		World world = entity.getWorld();
		
		new BukkitRunnable() {

			@Override
			public void run() {
				if(currentRadii < 6.3) {
					double x = radius * Math.cos(currentRadii);
			        double z = radius * Math.sin(currentRadii);
			        Location center = entity.getEyeLocation();
			        Location current1 = new Location(world, center.getX() + x, center.getY(), center.getZ() + z);
			        Location current2 = new Location(world, center.getX() - x, center.getY(), center.getZ() - z);
			        
			        world.spawnParticle(Particle.SMOKE_LARGE, current1, 1);
			        world.playEffect(current1, Effect.END_PORTAL_FRAME_FILL, 4, 2);
			        
			        world.spawnParticle(Particle.SMOKE_LARGE, current2, 1);
			        world.playEffect(current2, Effect.END_PORTAL_FRAME_FILL, 4, 2);
			        
					currentRadii += 0.1;
					return;
				}
				
				this.cancel();
				
				List<Player> nearbyEntities = AbilityUtils.getNearbyPlayers(entity, 40);
				double dmg = calculateDamage();
				
				for(Player e : nearbyEntities) {
					if(!e.isDead() && e.getLocation().distance(entity.getEyeLocation()) > radius + 0.5) {
						e.damage(dmg, entity);
						e.playSound(e.getLocation().add(0, 1.5, 0), Sound.ENTITY_ENDERMAN_SCREAM, 1.2f, 0.9f);
					}
				}
				
			}
	    	
	    }.runTaskTimer(FantasyClasses.getPlugin(), 1, 1);
		
	}
	
	private double calculateDamage() {
		return damage + (bonusDmg * boss.getMinions().size());
	}

}
