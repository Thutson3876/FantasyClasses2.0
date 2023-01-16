package me.thutson3876.fantasyclasses.classes.highroller.randomabilities.classriches;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.collectible.Collectible;
import me.thutson3876.fantasyclasses.custommobs.UndeadMiner;
import me.thutson3876.fantasyclasses.util.ChatUtils;
import me.thutson3876.fantasyclasses.util.Sphere;

public class DungeoneerRiches extends AbstractClassRiches {

	public DungeoneerRiches() {
		List<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(Material.COAL_ORE, 12));
		list.add(new ItemStack(Material.COPPER_ORE, 12));
		list.add(new ItemStack(Material.GOLD_ORE, 8));
		list.add(new ItemStack(Material.IRON_ORE, 8));
		list.add(new ItemStack(Material.LAPIS_ORE, 8));
		list.add(new ItemStack(Material.REDSTONE_ORE, 8));
		list.add(new ItemStack(Material.EMERALD_ORE, 8));
		list.add(new ItemStack(Material.DIAMOND_ORE, 4));
		
		this.riches = list;
	}
	
	@Override
	public void generateEvent(Player p) {
		
		for(Location loc : Sphere.generateSphere(p.getLocation(), 5, true)) {
			loc.getBlock().setType(Material.DEEPSLATE);
		}
		
		World world = p.getWorld();
		Location loc = p.getLocation();
		world.playSound(loc, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.2f, 0.4f);
		world.spawnParticle(Particle.PORTAL, loc, 20);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				spawnRiches(p);
				p.sendMessage(ChatUtils.chat(Collectible.MINING_SCHEMATICS.getRandomLore()));
				new UndeadMiner(loc);
			}
			
		}.runTaskLater(FantasyClasses.getPlugin(), 20);
	}

}
