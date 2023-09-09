package me.thutson3876.fantasyclasses.classes.highroller.randomabilities.classriches;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.geometry.Sphere;

public abstract class AbstractClassRiches {

	protected List<ItemStack> riches = new ArrayList<>();
	
	public List<ItemStack> getFullRichesList() {
		return riches;
	}
	
	public List<ItemStack> generateRiches(){
		Random rng = new Random();
		List<ItemStack> list = riches;
		int size = list.size();
		for(int i = 0; i < size / 2; i++) {
			list.remove(rng.nextInt(list.size()));
		}
		
		return list;
	}
	
	protected void spawnRiches(Entity ent) {
		World world = ent.getWorld();
		
		for(ItemStack i : generateRiches()) {
			if(i == null || i.getType().equals(Material.AIR))
				continue;
			world.dropItemNaturally(ent.getLocation(), i);
		}
	}
	
	protected void explodeRiches(Entity ent) {
		Random rng = new Random();
		Location entLoc = ent.getLocation();
		World world = ent.getWorld();
		List<Location> sphere = Sphere.generateSphere(entLoc, 4, true);
		
		for(ItemStack i : generateRiches()) {
			if(i == null || i.getType().equals(Material.AIR))
				continue;
			Item item = (Item) world.dropItemNaturally(entLoc, i);
			item.setVelocity(AbilityUtils.getDifferentialVector(entLoc, sphere.get(rng.nextInt(sphere.size())).multiply(0.00000001)));
		}
	}
	
	public abstract void generateEvent(Player p);
}
