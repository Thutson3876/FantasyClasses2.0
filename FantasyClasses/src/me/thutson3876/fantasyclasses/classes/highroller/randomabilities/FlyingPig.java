package me.thutson3876.fantasyclasses.classes.highroller.randomabilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.util.ChatUtils;

public class FlyingPig implements RandomAbility {

	private static final int MAX_DURATION = 20;

	private int count = MAX_DURATION;

	@Override
	public void run(Player p) {
		Block b = p.getTargetBlockExact(50);
		Location loc;
		if (b == null) {
			loc = p.getLocation();
		} else {
			loc = b.getLocation();
		}

		Pig pig = (Pig) p.getWorld().spawnEntity(loc, EntityType.PIG);
		pig.setGravity(false);
		pig.setVelocity(new Vector(1, 1, 1));

		p.sendMessage(ChatUtils.chat("&aWhen pigs fly... they'll rain porkchops?"));
		
		World world = pig.getWorld();
		new BukkitRunnable() {

			@Override
			public void run() {
				if (pig == null || pig.isDead()) {
					this.cancel();
					return;
				}
				if (count <= 1) {
					world.createExplosion(pig.getLocation(), 4.0f);
					this.cancel();
					return;
				}
				
				//porkExplosion(pig);
				world.dropItemNaturally(pig.getLocation(), new ItemStack(Material.COOKED_PORKCHOP, 4));
				count--;
				
			}
		}.runTaskTimer(FantasyClasses.getPlugin(), 20, 20);
	}
	
	/*private static void porkExplosion(Entity ent) {
		Location entLoc = ent.getLocation();
		World world = ent.getWorld();
		for(Location loc : Sphere.generateSphere(entLoc, 3, true)) {
			Item item = (Item) world.dropItemNaturally(entLoc, new ItemStack(Material.COOKED_PORKCHOP));
			item.setVelocity(AbilityUtils.getDifferentialVector(entLoc, loc).multiply(0.3));
		}
	}*/
}
