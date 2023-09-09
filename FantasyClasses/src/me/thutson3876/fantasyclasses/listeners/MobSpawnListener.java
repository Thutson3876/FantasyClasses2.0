package me.thutson3876.fantasyclasses.listeners;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PolarBear;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.custommobs.DrownedMiner;
import me.thutson3876.fantasyclasses.custommobs.FailedExperiment;
import me.thutson3876.fantasyclasses.custommobs.LostGuardian;
import me.thutson3876.fantasyclasses.custommobs.Parasite;
import me.thutson3876.fantasyclasses.custommobs.UndeadMiner;
import me.thutson3876.fantasyclasses.custommobs.boss.TargetDummy;
import me.thutson3876.fantasyclasses.custommobs.boss.engineer.Engineer;
import me.thutson3876.fantasyclasses.custommobs.boss.skeletonlord.SkeletonLord;
import me.thutson3876.fantasyclasses.custommobs.boss.uthroes.Ahsmi;
import me.thutson3876.fantasyclasses.custommobs.boss.uthroes.Uthroes;
import me.thutson3876.fantasyclasses.custommobs.boss.voidremnant.VoidRemnant;
import me.thutson3876.fantasyclasses.custommobs.horde.Horde;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;
import me.thutson3876.fantasyclasses.util.geometry.Schematic;

public class MobSpawnListener implements Listener {
	private static final FantasyClasses plugin = FantasyClasses.getPlugin();

	public MobSpawnListener() {
		plugin.registerEvents(this);
	}
	
	@EventHandler
	public void onMobSpawnEvent(CreatureSpawnEvent e) {
		if(e.getSpawnReason().equals(SpawnReason.SPAWNER) || e.getSpawnReason().equals(SpawnReason.CUSTOM))
			return;
		
		Random rng = new Random();
		Location loc = e.getLocation();
		double chance = rng.nextDouble();
		if(e.getEntityType().equals(EntityType.ZOMBIE)) {
			if(chance < 0.07) {
				if(loc.getY() < -30) {
					e.setCancelled(true);
					loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_AMBIENT, 2.5f, 0.8f);
					new UndeadMiner(loc);
				}
				else if(loc.getY() < 0) {
					e.setCancelled(true);
					loc.getWorld().playSound(loc, Sound.ENTITY_CREEPER_PRIMED, 3.0f, 0.5f);
					new FailedExperiment(loc);
				}
			}
		}
		else if(e.getEntityType().equals(EntityType.SKELETON)) {
			if(chance < 0.07) {
				if(loc.getY() < -15) {
					loc.getWorld().playSound(loc, Sound.ENTITY_DONKEY_HURT, 1.9f, 0.7f);
					new Parasite(loc);
					new Parasite(loc);
				}
			}
		}
		else if(e.getEntityType().equals(EntityType.DROWNED)) {
			
			if(chance < 0.035) {
				e.setCancelled(true);
				loc.getWorld().playSound(loc, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 3.0f, 1.2f);
				new LostGuardian(loc);
			}
			else if(chance < 0.08) {
				e.setCancelled(true);
				loc.getWorld().playSound(loc, Sound.ENTITY_PARROT_IMITATE_DROWNED, 3.0f, 0.5f);
				new DrownedMiner(loc);
			}
		}
		else if(e.getEntityType().equals(EntityType.POLAR_BEAR)) {
			if(chance < 0.08) {
				e.setCancelled(true);
				new Ahsmi(loc, null);
				for(int i = 0; i < 2; i++) {
					PolarBear bear = (PolarBear) loc.getWorld().spawnEntity(loc, EntityType.POLAR_BEAR);
					bear.setBaby();
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e) {
		Location loc = e.getBlock().getLocation();
		Block block = e.getBlock();
		if(e.getPlayer() == null)
			return;
		
		if(block.getType().equals(Material.RESPAWN_ANCHOR)) {
			if(block.hasMetadata("spawnuthroes")) {
				e.setDropItems(false);
				
				loc.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_AMBIENT, 5.0f, 0.7f);
				loc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 60);
				
				new BukkitRunnable() {

					@Override
					public void run() {
						loc.getWorld().strikeLightningEffect(loc);
						new Uthroes(loc);
					}
					
				}.runTaskLater(plugin, 3 * 20);
			}
		}
		
		//Test Code
		
		//Remove this before launching
		if(block.getType().equals(Material.MYCELIUM)) {
			if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.SLIME_BALL)){
				loc.getWorld().playSound(loc, Sound.ENTITY_SLIME_SQUISH, 1.5f, 2.0f);
				new TargetDummy(loc);
			}
			else if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BEETROOT)){
				loc.getWorld().playSound(loc, Sound.ENTITY_SILVERFISH_AMBIENT, 5f, 1.3f);
				new Parasite(loc);
			}
			else if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.NETHERITE_PICKAXE)){
				loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_SKELETON_AMBIENT, 5.0f, 0.8f);
				new UndeadMiner(loc);
			}
			else if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.DARK_PRISMARINE)){
				loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_SKELETON_AMBIENT, 5.0f, 0.8f);
				new DrownedMiner(loc);
			}
			else if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.PRISMARINE)){
				loc.getWorld().playSound(loc, Sound.ENTITY_GUARDIAN_AMBIENT, 5.0f, 0.8f);
				new LostGuardian(loc);
			}
			else if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.ROTTEN_FLESH)){
				loc.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_AMBIENT, 5.0f, 2.0f);
				new FailedExperiment(loc);
			}
			else if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WITHER_SKELETON_SKULL)){
				loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_SPAWN, 1.5f, 2.0f);
				new VoidRemnant(loc);
			}
			else if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.TNT)){
				loc.getWorld().playSound(loc, Sound.ENTITY_PARROT_IMITATE_ZOMBIE, 1.5f, 1.0f);
				new Engineer(loc);
			}
			else if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.ICE)){
				loc.getWorld().playSound(loc, Sound.ENTITY_HOGLIN_CONVERTED_TO_ZOMBIFIED, 2.5f, 0.8f);
				new Uthroes(loc);
			}
			else if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.SNOW)){
				loc.getWorld().playSound(loc, Sound.ENTITY_POLAR_BEAR_AMBIENT, 2.5f, 0.8f);
				new Ahsmi(loc, null);
			}
			else if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BONE)){
				loc.getWorld().playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 5.0f, 0.8f);
				new SkeletonLord(loc);
			}
			
		}
		
	}
	
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent e) {
		Block placed = e.getBlockPlaced();
		
		Horde horde = Horde.getHordeDrop(e.getItemInHand());
		Location loc = e.getBlock().getLocation();
		
		if(horde != null) {
			if(loc.getWorld().getHighestBlockYAt(loc) == loc.getY()) {
				horde.startWave(loc);
			}
			else {
				loc.getWorld().playSound(loc, Sound.ENTITY_VILLAGER_NO, 3.0f, 0.9f);
				e.getPlayer().setCooldown(e.getItemInHand().getType(), 20);
				e.setCancelled(true);
			}
			
			return;
		}
		
		for(Schematic s : Schematic.values()) {
			if(s.getPlacedType().equals(placed.getType())) {
				if(s.detect(placed)) {
					if(Schematic.isOpen(loc))
						s.detectAndRemove(placed);
					else {
						loc.getWorld().playEffect(loc, Effect.ELECTRIC_SPARK, 5);
						e.getPlayer().sendMessage(ChatUtils.chat("&8The ritual seems stifled by its surroundings..."));
					}
				}
				
				
			}
				
		}
	}
}
