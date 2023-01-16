package me.thutson3876.fantasyclasses.classes.highroller.randomabilities.classriches;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.collectible.Collectible;
import me.thutson3876.fantasyclasses.util.ChatUtils;
import me.thutson3876.fantasyclasses.util.MaterialLists;
import me.thutson3876.fantasyclasses.util.Sphere;

public class DruidRiches extends AbstractClassRiches {

	public DruidRiches() {
		List<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(Material.POTATO, 32));
		list.add(new ItemStack(Material.CARROT, 32));
		list.add(new ItemStack(Material.EGG, 16));
		list.add(new ItemStack(Material.APPLE, 32));
		list.add(new ItemStack(Material.WHEAT_SEEDS, 32));
		list.add(new ItemStack(Material.WHEAT, 64));
		list.add(new ItemStack(Material.COOKED_BEEF, 16));
		list.add(new ItemStack(Material.PORKCHOP, 16));
		list.add(new ItemStack(Material.GOLDEN_APPLE, 4));
		list.add(new ItemStack(Material.GOLDEN_CARROT, 8));
		
		this.riches = list;
	}
	
	@Override
	public void generateEvent(Player p) {
		Random rng = new Random();
		List<Material> leaves = MaterialLists.LEAVES.getMaterials();
		for(Location loc : Sphere.generateSphere(p.getLocation(), 5, true)) {
			loc.getBlock().setType(leaves.get(rng.nextInt(leaves.size())));
		}
		
		new BukkitRunnable() {

			@Override
			public void run() {
				spawnRiches(p);
				p.sendMessage(ChatUtils.chat(Collectible.DRUIDIC_INSCRIPTION.getRandomLore()));
			}
			
		}.runTaskLater(FantasyClasses.getPlugin(), 10);
	}

}
