package me.thutson3876.fantasyclasses.custommobs.horde.iron;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.custommobs.horde.AbstractHordeWave;
import me.thutson3876.fantasyclasses.util.Sphere;

public class IronWave5 extends AbstractHordeWave {

	public IronWave5() {
		super(180);
		
		this.setDrops(generateDrops());
		
		this.setNextWave(null);
	}

	@Override
	public void spawn(Location loc) {
		this.blockLoc = loc;
		
		List<Location> spawnLocs = Sphere.generateCircle(loc, 3, true);
		for(int i = 0; i < 1; i++)
			spawnLocs.addAll(spawnLocs);
		
		World world = loc.getWorld();
		loc.add(0, 0.3, 0);
		loc.getWorld().playSound(loc, Sound.EVENT_RAID_HORN, 2.7f, 0.7f);
		loc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 15);
		new BukkitRunnable(){

			@Override
			public void run() {
				for(Location l : spawnLocs) {
					 horde.addAll(AbstractHordeWave.spawnEntityStack((Mob)world.spawnEntity(l, EntityType.IRON_GOLEM), 6, EntityType.STRAY));
				}
				
			}
			
		}.runTaskLater(plugin, 3 * 20);
	}

	@Override
	protected List<ItemStack> generateDrops() {
		List<ItemStack> drops = new ArrayList<>();
		
		drops.add(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 3));
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.getLocation().distance(blockLoc) <= 30) {
				ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
				SkullMeta sm = (SkullMeta) item.getItemMeta();
				sm.setOwningPlayer(p);
				item.setItemMeta(sm);
				drops.add(item);
			}
		}
		
		return drops;
	}
	
}
