package me.thutson3876.fantasyclasses.custommobs.horde.iron;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.custommobs.horde.AbstractHordeWave;
import me.thutson3876.fantasyclasses.util.geometry.Sphere;

public class IronWave1 extends AbstractHordeWave {

	public IronWave1() {
		super(60);
		
		this.setDrops(generateDrops());
		
		this.setNextWave(new IronWave2());
	}

	@Override
	public void spawn(Location loc) {
		this.blockLoc = loc;
		
		loc.add(0, 0.3, 0);
		loc.getWorld().playSound(loc, Sound.EVENT_RAID_HORN, 2.7f, 0.7f);
		loc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 15);
		new BukkitRunnable(){

			@Override
			public void run() {
				horde = spawnEntities(Sphere.generateCircle(loc, 4, true), false, EntityType.STRAY, EntityType.HUSK, EntityType.CAVE_SPIDER);
			}
			
		}.runTaskLater(plugin, 3 * 20);
		
		
		
	}

	@Override
	protected List<ItemStack> generateDrops() {
		List<ItemStack> drops = new ArrayList<>();
		
		drops.add(new ItemStack(Material.CREEPER_HEAD, 2));
		
		return drops;
	}
	
}