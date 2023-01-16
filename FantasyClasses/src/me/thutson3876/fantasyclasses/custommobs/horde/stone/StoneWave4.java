package me.thutson3876.fantasyclasses.custommobs.horde.stone;

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
import me.thutson3876.fantasyclasses.util.Sphere;

public class StoneWave4 extends AbstractHordeWave {

	public StoneWave4() {
		super(150);
		
		this.setDrops(generateDrops());
		
		this.setNextWave(new StoneWave5());
	}

	@Override
	public void spawn(Location loc) {
		this.blockLoc = loc;
		
		List<Location> spawnLocs = Sphere.generateCircle(loc, 3, true);
		for(int i = 0; i < 4; i++)
			spawnLocs.addAll(spawnLocs);
		
		loc.add(0, 0.3, 0);
		loc.getWorld().playSound(loc, Sound.EVENT_RAID_HORN, 2.7f, 0.7f);
		loc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 15);
		new BukkitRunnable(){

			@Override
			public void run() {
				horde = spawnEntities(spawnLocs, true, EntityType.SKELETON, EntityType.ZOMBIE);
			}
			
		}.runTaskLater(plugin, 3 * 20);
		
		
	}

	@Override
	protected List<ItemStack> generateDrops() {
		List<ItemStack> drops = new ArrayList<>();
		
		
		
		drops.add(new ItemStack(Material.DIAMOND, 24));
		
		return drops;
	}
	
}
