package me.thutson3876.fantasyclasses.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.custommobs.boss.SpawnCue;
import me.thutson3876.fantasyclasses.custommobs.boss.engineer.Engineer;
import me.thutson3876.fantasyclasses.custommobs.boss.skeletonlord.SkeletonLord;
import me.thutson3876.fantasyclasses.custommobs.boss.voidremnant.VoidRemnant;

public enum Schematic {

	VOID_REMNANT(Material.CONDUIT, () -> {
		Integer[][] schematicCoords = new Integer[][] { { 1, 0, 0 }, { -1, 0, 0 }, { 0, 0, 1 }, { 0, 0, -1 },
				{ 0, -1, 0 }, { 1, -1, 0 }, { -1, -1, 0 }, { 0, -1, 1 }, { 0, -1, -1 }, { 0, -2, 0 } };

		Map<Integer[], Material> schematic = new HashMap<>();
		for (int i = 0; i < schematicCoords.length; i++) {
			if (i < 4) {
				schematic.put(schematicCoords[i], Material.WITHER_SKELETON_SKULL);
				continue;
			}
			schematic.put(schematicCoords[i], Material.POLISHED_DEEPSLATE);
		}
		return schematic;
	}, (loc) -> {
		loc.getWorld().playSound(loc, Sound.ENTITY_ENDER_DRAGON_GROWL, 2.0f, 0.7f);
		loc.getWorld().spawnParticle(Particle.PORTAL, loc, 40);
		new BukkitRunnable() {

			@Override
			public void run() {
				loc.getWorld().createExplosion(loc, 6.0f);
				new VoidRemnant(loc);
			}

		}.runTaskLater(FantasyClasses.getPlugin(), 3 * 20);
	}), 
	
	ENGINEER(Material.ZOMBIE_HEAD, () -> {
		Map<Integer[], Material> schematic = new HashMap<>();
		Integer[][] schematicCoords = new Integer[][] { { 0, -1, 0 }, { 1, 1, 1 }, { -1, 1, 1 }, { 1, 1, -1 },
				{ -1, 1, -1 }, { 1, -1, 1 }, { -1, -1, 1 }, { 1, -1, -1 }, { -1, -1, -1 } };
	
		schematic.put(schematicCoords[0], Material.ANCIENT_DEBRIS);
		for (int i = 1; i < schematicCoords.length; i++) {
			schematic.put(schematicCoords[i], Material.TNT);
		}
	
		return schematic;
		}, (loc) -> {
			List<Location> locs = Sphere.generateCircle(loc, 3, true);
			World world = loc.getWorld();
	
			Particles.twirlingRing(loc, Particle.FLAME, 1.6, 12.6, 7);
	
			for (Location l : locs) {
				new BukkitRunnable() {
	
					@Override
					public void run() {
						world.playSound(l, Sound.ENTITY_CREEPER_PRIMED, 2.0f, 0.9f);
					}
	
				}.runTaskLater(FantasyClasses.getPlugin(), 7);
	
				new BukkitRunnable() {
	
					@Override
					public void run() {
						world.createExplosion(l, 4.0f, false, false);
					}
	
				}.runTaskLater(FantasyClasses.getPlugin(), 14);
			}
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					loc.getWorld().createExplosion(loc, 8.0f, false, false);
					new Engineer(loc);
				}

			}.runTaskLater(FantasyClasses.getPlugin(), 15 * locs.size());
	}), 
	SKELETON_LORD(Material.LIGHTNING_ROD, () -> {
		Map<Integer[], Material> schematic = new HashMap<>();
		Integer[][] schematicCoords = new Integer[][] { { 0, -1, 0 }, { 1, -1, 0 }, { -1, -1, 0 }, { 0, -1, 1 }, { 0, -1, -1 } };
	
		schematic.put(schematicCoords[0], Material.COPPER_BLOCK);
		for (int i = 1; i < schematicCoords.length; i++) {
			schematic.put(schematicCoords[i], Material.SKELETON_SKULL);
		}
	
		return schematic;
		}, (loc) -> {
			List<Location> locs = Sphere.generateCircle(loc, 3, true);
			World world = loc.getWorld();
	
			Particles.twirlingRing(loc, Particle.ELECTRIC_SPARK, 1.6, 12.6, 7);
	
			for (Location l : locs) {
				new BukkitRunnable() {
	
					@Override
					public void run() {
						world.playSound(l, Sound.ENTITY_SKELETON_HORSE_AMBIENT, 2.0f, 0.9f);
					}
	
				}.runTaskLater(FantasyClasses.getPlugin(), 7);
	
				new BukkitRunnable() {
	
					@Override
					public void run() {
						world.strikeLightning(l);
						for(LivingEntity le : AbilityUtils.getNearbyLivingEntities(l, 1.0, 2.0, 1.0)) {
							AbilityUtils.applyStackingPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, 30 * 20, 1), le, 9, 120 * 20);
						}
							
					}
	
				}.runTaskLater(FantasyClasses.getPlugin(), 14);
			}
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					world.strikeLightning(loc);
					for(LivingEntity le : AbilityUtils.getNearbyLivingEntities(loc, 1.0, 2.0, 1.0)) {
						AbilityUtils.applyStackingPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, 30 * 20, 1), le, 9, 120 * 20);
					}
					
					new SkeletonLord(loc);
				}

			}.runTaskLater(FantasyClasses.getPlugin(), 15 * locs.size());
	});

	private final Map<Integer[], Material> schematic;
	private final SpawnCue cue;
	private final Material placedType;

	private Schematic(Material placedType, SchematicGenerator gen, SpawnCue cue) {
		this.placedType = placedType;
		this.cue = cue;
		this.schematic = gen.generate();
	}

	public Material getPlacedType() {
		return this.placedType;
	}

	public boolean detect(Block placed) {
		int correctionCount = 0;
		Map<Integer[], Material> schematicMap = schematic;

		for (Integer[] coords : schematicMap.keySet()) {
			Material type = placed.getRelative(coords[0], coords[1], coords[2]).getType();
			if (type.equals(schematicMap.get(coords))) {
				correctionCount++;
				continue;
			}
			break;
		}

		if (correctionCount != schematicMap.size()) {
			return false;
		}

		return true;
	}

	public boolean detectAndRemove(Block placed) {
		int correctionCount = 0;
		Map<Integer[], Material> schematicMap = schematic;
		Set<Block> toRemove = new HashSet<>();
		toRemove.add(placed);

		for (Integer[] coords : schematicMap.keySet()) {
			Block b = placed.getRelative(coords[0], coords[1], coords[2]);
			toRemove.add(b);
			Material type = b.getType();
			if (type.equals(schematicMap.get(coords))) {
				correctionCount++;
				continue;
			}
			break;
		}

		if (correctionCount != schematicMap.size()) {
			return false;
		}

		for (Block b : toRemove) {
			b.setType(Material.AIR);
		}

		cue.run(placed.getLocation());

		return true;
	}

	private interface SchematicGenerator {
		Map<Integer[], Material> generate();
	}
	
	public static boolean isOpen(Location loc) {
		return (Sphere.generateDome(loc, 6, false).size() > 250);
	}
}
