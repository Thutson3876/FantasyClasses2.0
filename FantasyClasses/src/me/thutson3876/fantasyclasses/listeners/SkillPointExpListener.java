package me.thutson3876.fantasyclasses.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.util.ChatUtils;
import me.thutson3876.fantasyclasses.util.NoExpDrop;

public class SkillPointExpListener implements Listener {

	private static Map<EntityType, Integer> entityTypeExpDrop = new HashMap<>();
	private static Map<Material, Integer> blockTypeExpDrop = new HashMap<>();
	private static Map<Material, Integer> smeltTypeExpDrop = new HashMap<>();
	private static final Random rng = new Random();

	// Entity Killed
	private static final FantasyClasses plugin = FantasyClasses.getPlugin();

	private Map<LivingEntity, HashSet<Player>> trackedEntities = new HashMap<>();

	public SkillPointExpListener() {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	static {
		// Entity Types
		entityTypeExpDrop.put(EntityType.ZOMBIE, 1);
		entityTypeExpDrop.put(EntityType.HUSK, 1);
		entityTypeExpDrop.put(EntityType.DROWNED, 2);
		entityTypeExpDrop.put(EntityType.SKELETON, 2);
		entityTypeExpDrop.put(EntityType.STRAY, 3);
		entityTypeExpDrop.put(EntityType.SPIDER, 1);
		entityTypeExpDrop.put(EntityType.CAVE_SPIDER, 2);
		entityTypeExpDrop.put(EntityType.CREEPER, 3);
		entityTypeExpDrop.put(EntityType.ZOMBIE_VILLAGER, 1);
		entityTypeExpDrop.put(EntityType.PHANTOM, 3);
		entityTypeExpDrop.put(EntityType.SLIME, 1);
		entityTypeExpDrop.put(EntityType.MAGMA_CUBE, 1);

		entityTypeExpDrop.put(EntityType.PIGLIN, 3);
		entityTypeExpDrop.put(EntityType.ZOMBIFIED_PIGLIN, 0);
		entityTypeExpDrop.put(EntityType.HOGLIN, 4);
		entityTypeExpDrop.put(EntityType.ZOGLIN, 5);
		entityTypeExpDrop.put(EntityType.PILLAGER, 1);
		entityTypeExpDrop.put(EntityType.VINDICATOR, 5);
		entityTypeExpDrop.put(EntityType.GUARDIAN, 4);
		entityTypeExpDrop.put(EntityType.GHAST, 4);
		entityTypeExpDrop.put(EntityType.BLAZE, 5);
		entityTypeExpDrop.put(EntityType.WITHER_SKELETON, 5);
		entityTypeExpDrop.put(EntityType.WITCH, 4);
		entityTypeExpDrop.put(EntityType.RAVAGER, 8);
		entityTypeExpDrop.put(EntityType.PIGLIN_BRUTE, 8);
		entityTypeExpDrop.put(EntityType.ENDERMAN, 4);
		entityTypeExpDrop.put(EntityType.POLAR_BEAR, 4);

		entityTypeExpDrop.put(EntityType.EVOKER, 12);
		entityTypeExpDrop.put(EntityType.ILLUSIONER, 8);
		entityTypeExpDrop.put(EntityType.ELDER_GUARDIAN, 20);
		entityTypeExpDrop.put(EntityType.WARDEN, 20);

		entityTypeExpDrop.put(EntityType.WITHER, 30);

		// Block Types
		blockTypeExpDrop.put(Material.ANCIENT_DEBRIS, 8);
		blockTypeExpDrop.put(Material.EMERALD_ORE, 8);
		blockTypeExpDrop.put(Material.DEEPSLATE_EMERALD_ORE, 8);
		blockTypeExpDrop.put(Material.DIAMOND_ORE, 6);
		blockTypeExpDrop.put(Material.DEEPSLATE_DIAMOND_ORE, 6);
		blockTypeExpDrop.put(Material.LAPIS_ORE, 2);
		blockTypeExpDrop.put(Material.DEEPSLATE_LAPIS_ORE, 2);
		blockTypeExpDrop.put(Material.REDSTONE_ORE, 1);
		blockTypeExpDrop.put(Material.DEEPSLATE_REDSTONE_ORE, 1);

		blockTypeExpDrop.put(Material.IRON_ORE, 1);
		blockTypeExpDrop.put(Material.DEEPSLATE_IRON_ORE, 1);
		blockTypeExpDrop.put(Material.GOLD_ORE, 3);
		blockTypeExpDrop.put(Material.DEEPSLATE_GOLD_ORE, 3);
		// Smelt Types
		smeltTypeExpDrop.put(Material.IRON_INGOT, 4);
		smeltTypeExpDrop.put(Material.GOLD_INGOT, 8);
		smeltTypeExpDrop.put(Material.NETHERITE_SCRAP, 16);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Mob) {
			if (!(e.getEntity() instanceof LivingEntity))
				return;

			LivingEntity victim = (LivingEntity) e.getEntity();
			if (victim == null)
				return;
			
			//if(victim.getSpawnCategory().equals())

			Entity damager = e.getDamager();
			if (damager instanceof Tameable) {
				damager = (Entity) ((Tameable) damager).getOwner();
				if (damager == null)
					return;
			}
			else if(damager instanceof Projectile) {
				damager = (Entity) ((Projectile)damager).getShooter();
			}

			if (!(damager instanceof Player))
				return;

			Player player = (Player) damager;

			addEntity(victim, player);
			if (e.getFinalDamage() < victim.getHealth()) {
				new BukkitRunnable() {

					@Override
					public void run() {
						if(victim.isDead() && trackedEntities.containsKey(victim)) {
							entityDeath(victim);
						}
							
					}
					
				}.runTaskLater(plugin, 3);
			} else {
				entityDeath(victim);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDeathEvent(EntityDeathEvent e) {
		entityDeath(e.getEntity());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreakEvent(BlockBreakEvent e) {
		if (rng.nextDouble() < 0.01 && !e.getBlock().isPassable())
			plugin.getPlayerManager().getPlayer(e.getPlayer()).addSkillExp(1);

		if (e.getBlock() instanceof Ageable) {
			if(rng.nextDouble() < 0.2) {
				Ageable agedBlock = (Ageable) e.getBlock();
				if (agedBlock.getAge() >= agedBlock.getMaximumAge())
					plugin.getPlayerManager().getPlayer(e.getPlayer()).addSkillExp(1);
			}
		}

		if (e.getExpToDrop() == 0)
			return;

		Integer expToDrop = blockTypeExpDrop.get(e.getBlock().getType());
		if (expToDrop == null)
			return;

		plugin.getPlayerManager().getPlayer(e.getPlayer()).addSkillExp(expToDrop);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerFishEvent(PlayerFishEvent e) {
		if (e.getState().equals(State.CAUGHT_FISH))
			plugin.getPlayerManager().getPlayer(e.getPlayer()).addSkillExp(3);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityBreedEvent(EntityBreedEvent e) {
		if (e.getBreeder() instanceof Player) {
			plugin.getPlayerManager().getPlayer((Player) e.getBreeder()).addSkillExp(1);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onFurnaceExtractEvent(FurnaceExtractEvent e) {
		Integer exp = smeltTypeExpDrop.get(e.getItemType());
		int expToDrop = 0;
		Random rng = new Random();
		if (exp != null) {
			for(int i = 0; i < e.getItemAmount(); i++) {
				if(rng.nextDouble() < 0.125)
					expToDrop += exp;
			}
			
			plugin.getPlayerManager().getPlayer(e.getPlayer()).addSkillExp(expToDrop);
		}
	}
	
	@EventHandler
	public void onMobSpawnEvent(CreatureSpawnEvent e) {
		if(e.getSpawnReason().equals(SpawnReason.SPAWNER)) {
			e.getEntity().setMetadata("noexpdrop", new NoExpDrop());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerAdvancementDoneEvent(PlayerAdvancementDoneEvent e) {
		if (checkAdvancementValidity(e.getAdvancement())) {
			FantasyPlayer fplayer = plugin.getPlayerManager().getPlayer(e.getPlayer());
			fplayer.addSkillExp(15);
			fplayer.getPlayer().sendMessage(ChatUtils.chat("&6Congratulations on completing an advancement!"));
		}
	}
	
	private void addEntity(LivingEntity ent, Player... players) {
		if (trackedEntities.containsKey(ent)) {
			List<Player> trackedPlayers = new ArrayList<>();
			trackedPlayers.addAll(trackedEntities.get(ent));
			trackedEntities.get(ent).addAll(Arrays.asList(players));
		} else {
			HashSet<Player> playerSet = new HashSet<>(Arrays.asList(players));

			trackedEntities.put(ent, playerSet);
		}
	}

	private void entityDeath(Entity ent) {
		Integer expDrop = entityTypeExpDrop.get(ent.getType());
		if (expDrop == null)
			return;
		HashSet<Player> players = trackedEntities.remove(ent);
		
		if(ent.hasMetadata("noexpdrop"))
			return;
		
		expDrop += plugin.getMobManager().getExpDrop((LivingEntity) ent);
		
		if (players != null) {
			for (Player p : players) {
				if(plugin.getPlayerManager().getPlayer(p) != null)
					plugin.getPlayerManager().getPlayer(p).addSkillExp(expDrop);
			}
		}
	}
	
	private static boolean checkAdvancementValidity(Advancement adv) {
		Collection<String> criteriaList = adv.getCriteria();
		for (String criteria : criteriaList) {
			if (criteria.equalsIgnoreCase("has_the_recipe")) {
				return false;
			}
		}
		return true;
	}

	public static int getExpReward(Material type) {
		Integer val = blockTypeExpDrop.get(type);
		if (val != null)
			return val;

		val = smeltTypeExpDrop.get(type);
		if (val != null)
			return val;

		return 0;
	}

	public static int getExpReward(EntityType type) {
		Integer val = entityTypeExpDrop.get(type);
		if (val != null)
			return val;

		return 0;
	}
}
