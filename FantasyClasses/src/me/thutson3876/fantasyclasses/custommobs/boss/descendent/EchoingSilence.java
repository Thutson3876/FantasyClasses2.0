package me.thutson3876.fantasyclasses.custommobs.boss.descendent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.custommobs.boss.AbstractBoss;
import me.thutson3876.fantasyclasses.custommobs.boss.BossAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class EchoingSilence extends BossAbility {

	// Deal 7 damage to all players within 3 blocks of a minion

	// track how many damage circles a player is in when abil triggers
	private Map<Player, Integer> damageToDeal = new HashMap<>();

	private double dmg = 7.0;
	private double radius = 3.0;

	private double currentRadii = 0.0;

	public EchoingSilence(AbstractBoss boss) {
		super(boss);
	}

	@Override
	public String getName() {
		return "Echoing Silence";
	}

	@Override
	public void run(Mob entity) {
		currentRadii = 0.0;
		damageToDeal.clear();
		World world = entity.getWorld();

		new BukkitRunnable() {

			@Override
			public void run() {
				if (currentRadii < 6.3) {

					for (Mob m : boss.getMinions()) {
						double x = radius * Math.cos(currentRadii);
						double z = radius * Math.sin(currentRadii);
						Location center = m.getEyeLocation();
						Location current1 = new Location(world, center.getX() + x, center.getY(), center.getZ() + z);

						world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, current1, 1);
						world.playEffect(current1, Effect.END_PORTAL_FRAME_FILL, 1, 1);
					}

					currentRadii += 0.1;
					return;
				}

				this.cancel();
				List<Player> nearbyPlayers = AbilityUtils.getNearbyPlayers(entity, radius);
				for(Player p : nearbyPlayers) {
					damageToDeal.put(p, 0);
					Location pLoc = p.getLocation();
					for(Mob m : boss.getMinions()) {
						if(pLoc.distance(m.getLocation()) < radius) {
							damageToDeal.put(p, damageToDeal.get(p) + 1);
						}
					}
				}
				
				for(Entry<Player, Integer> entry : damageToDeal.entrySet()) {
					if(entry.getValue() > 0)
						entry.getKey().damage(entry.getValue() * dmg, entity);
				}
					
			}

		}.runTaskTimer(FantasyClasses.getPlugin(), 1, 1);
	}

}
