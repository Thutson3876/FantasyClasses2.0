package me.thutson3876.fantasyclasses.classes.highroller.randomabilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.util.chat.ChatUtils;

public class DeathByDiamonds implements RandomAbility {

	@Override
	public void run(Player p) {
		Location loc = p.getLocation();
		loc.getBlock().getRelative(BlockFace.DOWN).setType(Material.POLISHED_BLACKSTONE_BRICKS);
		loc.getBlock().getRelative(BlockFace.NORTH).setType(Material.POLISHED_BLACKSTONE_WALL);
		loc.getBlock().getRelative(BlockFace.EAST).setType(Material.POLISHED_BLACKSTONE_WALL);
		loc.getBlock().getRelative(BlockFace.SOUTH).setType(Material.POLISHED_BLACKSTONE_WALL);
		loc.getBlock().getRelative(BlockFace.WEST).setType(Material.POLISHED_BLACKSTONE_WALL);
		
		loc.setY(loc.getY() + 7.0);
		loc.getBlock().setType(Material.AIR);
		Block b = loc.getBlock();
		b.setType(Material.DIAMOND_BLOCK);
		FallingBlock fb = loc.getWorld().spawnFallingBlock(loc, b.getBlockData());
		b.setType(Material.ANVIL);
		FallingBlock anvil = loc.getWorld().spawnFallingBlock(loc.subtract(0, 3, 0), b.getBlockData());
		
		fb.setHurtEntities(true);
		fb.setVelocity(new Vector(0, 1, 0));
		anvil.setHurtEntities(true);
		anvil.setVelocity(new Vector(0, 1, 0));
		
		b.setType(Material.AIR);
	
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 120, 4));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 4));
		
		p.sendMessage(ChatUtils.chat("&aYummy yummy carbon"));
	}

}
