package me.thutson3876.fantasyclasses.classes.highroller.randomabilities.classriches;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;
import me.thutson3876.fantasyclasses.util.geometry.Sphere;

public class CombatRiches extends AbstractClassRiches {

	private static List<EntityType> entityList = new ArrayList<>();
	
	static {
		entityList.add(EntityType.ZOMBIE);
		entityList.add(EntityType.SKELETON);
		entityList.add(EntityType.SPIDER);
		entityList.add(EntityType.CREEPER);
	}
	
	
	public CombatRiches() {
		List<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(Material.BOW));
		list.add(new ItemStack(Material.CROSSBOW));
		list.add(new ItemStack(Material.TNT, 12));
		list.add(new ItemStack(Material.DIAMOND_AXE));
		list.add(new ItemStack(Material.DIAMOND_SWORD));
		list.add(new ItemStack(Material.DIAMOND_CHESTPLATE));
		list.add(new ItemStack(Material.DIAMOND_BOOTS));
		list.add(new ItemStack(Material.DIAMOND_HELMET));
		list.add(new ItemStack(Material.DIAMOND_LEGGINGS));
		
		this.riches = list;
	}
	
	@Override
	public void generateEvent(Player p) {
		World world = p.getWorld();
		Random rng = new Random();
		
		for(Location loc : Sphere.generateSphere(p.getLocation(), 3, true)) {
			world.spawnEntity(loc, entityList.get(rng.nextInt(entityList.size())));
		}
		
		new BukkitRunnable() {

			@Override
			public void run() {
				spawnRiches(p);
				p.sendMessage(ChatUtils.chat("&7FIGHT FOR GLORY AND RICHES!!!"));
			}
			
		}.runTaskLater(FantasyClasses.getPlugin(), 8 * 20);
	}

}
