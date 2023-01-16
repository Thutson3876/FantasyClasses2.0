package me.thutson3876.fantasyclasses.custommobs.boss.voidremnant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.custommobs.CustomMob;
import me.thutson3876.fantasyclasses.custommobs.boss.MobAbility;

public class SummonTar implements MobAbility {

	private static List<CustomMob> entityTypes = new ArrayList<>();
	private int range = 20;
	private int delay = 3 * 20;

	static {
		entityTypes.add(CustomMob.UNDEAD_MINER);
		entityTypes.add(CustomMob.PARASITE);
	}

	@Override
	public String getName() {
		return "Summon Tar";
	}

	@Override
	public void run(Mob entity) {
		Set<Location> nearbyPlayerLocations = new HashSet<>();

		for (Entity ent : entity.getNearbyEntities(range, range, range)) {
			if (ent instanceof Player) {
				if (!ent.isDead()) {
					Location loc = ent.getLocation();
					nearbyPlayerLocations.add(loc);
					loc.getWorld().playSound(loc, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.2f, 0.4f);
					loc.getWorld().spawnParticle(Particle.PORTAL, loc, 20);
				}

			}
		}

		new BukkitRunnable() {

			@Override
			public void run() {
				Random rng = new Random();

				for (Location loc : nearbyPlayerLocations) {
					CustomMob mob = entityTypes.get(rng.nextInt(entityTypes.size()));
					mob.newMob(loc);
					mob.newMob(loc);
				}
			}

		}.runTaskLater(FantasyClasses.getPlugin(), delay);
	}
}
