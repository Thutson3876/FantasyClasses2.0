package me.thutson3876.fantasyclasses.classes.highroller.randomabilities.classriches;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.collectible.Collectible;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;

public class MonkRiches extends AbstractClassRiches {

	public MonkRiches() {
		List<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(Material.LECTERN, 12));
		list.add(new ItemStack(Material.BOOKSHELF, 16));
		list.add(new ItemStack(Material.BOOK, 32));
		list.add(new ItemStack(Material.BAMBOO, 32));
		list.add(new ItemStack(Material.BAMBOO_SAPLING, 8));
		list.add(new ItemStack(Material.BEEHIVE, 1));
		list.add(new ItemStack(Material.HONEY_BOTTLE, 16));
		list.add(new ItemStack(Material.HONEYCOMB, 32));
		
		this.riches = list;
	}
	
	@Override
	public void generateEvent(Player p) {
		this.spawnRiches(p);
		p.setVelocity(new Vector(0, 1, 0));
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 0.5f, 1F);
		p.sendMessage(ChatUtils.chat(Collectible.ANCIENT_TECHNIQUE.getRandomLore()));
	}

}
