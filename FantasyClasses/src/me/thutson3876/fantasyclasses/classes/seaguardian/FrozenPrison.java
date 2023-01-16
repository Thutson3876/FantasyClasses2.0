package me.thutson3876.fantasyclasses.classes.seaguardian;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.util.Sphere;

public class FrozenPrison extends AbstractAbility implements Bindable {

	private int duration = 6 * 20;
	private int radius = 3;
	
	private Material type = null;
	
	public FrozenPrison(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 24 * 20;
		this.displayName = "Frozen Prison";
		this.skillPointCost = 1;
		this.maximumLevel = 4;

		this.createItemStack(Material.BLUE_ICE);	
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if(isOnCooldown())
			return;
		
		if(!e.getPlayer().equals(player))
			return;
		
		if(e.getItem() == null || !e.getItem().getType().equals(this.type))
			return;
		
		if(!player.isSneaking())
			return;
		
		Location spawnAt = player.getTargetBlock(null, 16).getLocation();
		List<Location> iceball = Sphere.generateSphere(spawnAt, radius, true);
		spawnAt.getWorld().playSound(spawnAt, Sound.BLOCK_GLASS_PLACE, 1.4f, 1.5f);
		
		for(int i = 0; i < iceball.size(); i++) {
			iceball.get(i).getBlock().setType(Material.PACKED_ICE);
		}
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for(Location l : iceball) {
					if(l.getBlock().getType().equals(Material.PACKED_ICE)) {
						l.getBlock().setType(Material.AIR);
					}
				}
				
				player.getWorld().playSound(spawnAt, Sound.BLOCK_GLASS_BREAK, 1.2f, 1F);
			}
		}.runTaskLater(plugin, duration);
		
		this.onTrigger(true);
	}

	@Override
	public String getInstructions() {
		return "Right-click with the bound item type while crouching";
	}

	@Override
	public String getDescription() {
		return "Summon a sphere of ice around your target that lasts for &6" + duration / 20 + " &rseconds. This sphere has a radius of &6" + radius + " &rblocks";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		radius = (2 * currentLevel);
		duration = (2 + (2 * currentLevel)) * 20;
	}

	@Override
	public Material getBoundType() {
		return type;
	}

	@Override
	public void setBoundType(Material type) {
		this.type = type;
	}

}
