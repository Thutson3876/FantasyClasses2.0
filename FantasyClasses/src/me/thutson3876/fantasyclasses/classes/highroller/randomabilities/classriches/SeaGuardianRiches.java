package me.thutson3876.fantasyclasses.classes.highroller.randomabilities.classriches;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.collectible.Collectible;
import me.thutson3876.fantasyclasses.custommobs.LostGuardian;
import me.thutson3876.fantasyclasses.util.ChatUtils;
import me.thutson3876.fantasyclasses.util.Sphere;

public class SeaGuardianRiches extends AbstractClassRiches {

	public SeaGuardianRiches() {
		List<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(Material.PRISMARINE, 32));
		list.add(new ItemStack(Material.PRISMARINE_SHARD, 64));
		list.add(new ItemStack(Material.PRISMARINE_BRICKS, 32));
		list.add(new ItemStack(Material.DARK_PRISMARINE, 32));
		list.add(new ItemStack(Material.PRISMARINE_CRYSTALS, 48));
		list.add(new ItemStack(Material.SEA_LANTERN, 16));
		list.add(new ItemStack(Material.SPONGE, 1));
		list.add(new ItemStack(Material.NAUTILUS_SHELL, 4));
		list.add(new ItemStack(Material.HEART_OF_THE_SEA, 1));
		
		this.riches = list;
	}
	
	@Override
	public void generateEvent(Player p) {
		for(Location loc : Sphere.generateSphere(p.getLocation(), 4, false)) {
			loc.getBlock().setType(Material.WATER);
		}
		
		new LostGuardian(p.getLocation());
		
		this.spawnRiches(p);
		p.sendMessage(ChatUtils.chat(Collectible.ETCHED_GLASS.getRandomLore()));
	}

}
