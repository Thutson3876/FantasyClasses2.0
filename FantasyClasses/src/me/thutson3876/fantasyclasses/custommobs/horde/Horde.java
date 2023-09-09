package me.thutson3876.fantasyclasses.custommobs.horde;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.thutson3876.fantasyclasses.custommobs.horde.stone.StoneWave1;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;

public enum Horde {
	
	STONE(Material.SMOOTH_STONE, 0.008, new StoneWave1());
	
	private final Material mat;
	
	private final double dropRate;
	
	private final HordeWave wave;
	
	private Horde(Material mat, double dropRate, HordeWave wave) {
		this.mat = mat;
		this.dropRate = dropRate;
		this.wave = wave;
	}
	
	public void startWave(Location loc) {
		wave.spawn(loc);
	}
	
	public Material getMaterial() {
		return this.mat;
	}
	
	public double getDropRate() {
		return this.dropRate;
	}
	
	public ItemStack generateDrop() {
		ItemStack item = new ItemStack(this.mat);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatUtils.chat("&4Horde Originator"));
		List<String> lore = new ArrayList<>();
		lore.add("Place on ground to start Horde invasion!");
		lore.add("Warning: Do not place near residences");
		meta.setLore(lore);
		
		
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static boolean isHordeDrop(ItemStack item) {
		boolean isDrop = false;
		
		
		
		return isDrop;
	}

	public static Horde getHordeDrop(ItemStack itemInHand) {
		ItemMeta meta = itemInHand.getItemMeta();
		Horde horde = null;
		for(Horde h : values()) {
			if(itemInHand.getType().equals(h.getMaterial())) {
				horde = h;
				break;
			}
		}
		
		if(horde == null)
			return null;
		
		if(meta.getLore().contains("Place on ground to start Horde invasion!") && meta.getLore().contains("Warning: Do not place near residences"))
			return horde;
		
		return null;
	}
	
}
