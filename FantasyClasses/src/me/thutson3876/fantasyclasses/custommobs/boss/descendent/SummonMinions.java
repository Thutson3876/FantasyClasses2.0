package me.thutson3876.fantasyclasses.custommobs.boss.descendent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.custommobs.boss.AbstractBoss;
import me.thutson3876.fantasyclasses.custommobs.boss.BossAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class SummonMinions extends BossAbility {

	public SummonMinions(AbstractBoss boss) {
		super(boss);
	}

	private static List<EntityType> entityTypes = new ArrayList<>();
	private final int range = 20;
	private final int delay = 2 * 20;
	private final int summonAmt = 10;

	static {
		entityTypes.add(EntityType.SKELETON);
		entityTypes.add(EntityType.HUSK);
		entityTypes.add(EntityType.ZOMBIE);
	}

	@Override
	public String getName() {
		return "Summon Undead";
	}

	@Override
	public void run(Mob entity) {
		List<Player> nearbyPlayers = AbilityUtils.getNearbyPlayers(entity, range);
		List<Location> nearbyPlayerLocations = new ArrayList<>();
		List<Player> targets = new ArrayList<>();

		for (Player p : nearbyPlayers) {
			if (!p.isDead()) {
				Location loc = p.getLocation().add(0, 0.3, 0);
				loc.getWorld().playSound(loc, Sound.EVENT_RAID_HORN, 2.7f, 0.7f);
				loc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 15);
				nearbyPlayerLocations.add(loc);
				targets.add(p);
			}
		}

		Random rng = new Random();
		int size = nearbyPlayers.size();
		if (size < 1)
			return;

		Map<Location, Integer> targetSpawns = new HashMap<>();
		for (int i = 0; i < summonAmt; i++) {
			Location loc = nearbyPlayers.get(rng.nextInt(size)).getLocation();
			Integer amt = targetSpawns.get(loc);
			if (amt == null)
				amt = 0;

			targetSpawns.put(loc, amt + 1);
		}
		World world = entity.getWorld();

		new BukkitRunnable() {

			@Override
			public void run() {
				int index = 0;
				for (Entry<Location, Integer> entry : targetSpawns.entrySet()) {
					for (int i = 0; i < entry.getValue(); i++) {
						EntityType type = entityTypes.get(rng.nextInt(entityTypes.size()));
						Mob mob = (Mob) world.spawnEntity(entry.getKey(), type);
						setGear(mob);
						mob.setTarget(targets.get(index));
						boss.addMinion(mob);
					}
				}
			}

		}.runTaskLater(FantasyClasses.getPlugin(), delay);
	}

	private void setGear(Mob ent) {
		EntityEquipment equip = ent.getEquipment();

		ItemStack boots = new ItemStack(Material.IRON_BOOTS);
		ItemStack legs = new ItemStack(Material.IRON_LEGGINGS);
		ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE);
		ItemStack helm = new ItemStack(Material.IRON_HELMET);

		equip.setBoots(boots);
		equip.setBootsDropChance(0.0f);
		equip.setLeggings(legs);
		equip.setBootsDropChance(0.0f);
		equip.setChestplate(chest);
		equip.setChestplateDropChance(0.0f);
		equip.setHelmet(helm);
		equip.setHelmetDropChance(0.0f);
	}
}
