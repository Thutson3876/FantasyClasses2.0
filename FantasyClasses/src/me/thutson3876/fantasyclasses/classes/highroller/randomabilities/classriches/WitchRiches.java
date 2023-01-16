package me.thutson3876.fantasyclasses.classes.highroller.randomabilities.classriches;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.util.ChatUtils;
import me.thutson3876.fantasyclasses.util.Sphere;

public class WitchRiches extends AbstractClassRiches {

	public WitchRiches() {
		List<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(Material.NETHER_WART, 32));
		list.add(new ItemStack(Material.GLOWSTONE_DUST, 32));
		list.add(new ItemStack(Material.REDSTONE, 32));
		list.add(new ItemStack(Material.GLASS_BOTTLE, 16));
		list.add(new ItemStack(Material.CAULDRON));
		list.add(new ItemStack(Material.BLAZE_POWDER, 16));
		list.add(new ItemStack(Material.BLAZE_ROD, 8));
		list.add(new ItemStack(Material.WITHER_SKELETON_SKULL));
		
		this.riches = list;
	}
	
	@Override
	public void generateEvent(Player p) {
		p.getLocation().getBlock().setType(Material.WATER_CAULDRON);
		World world = p.getWorld();
		
		this.spawnRiches(p);
		
		for(Location loc : Sphere.generateSphere(p.getLocation(), 2, true)) {
			world.spawnEntity(loc, EntityType.WITCH);
		}
		
		p.sendMessage(ChatUtils.chat("&5Learn from the best, or die trying"));
	}

}
