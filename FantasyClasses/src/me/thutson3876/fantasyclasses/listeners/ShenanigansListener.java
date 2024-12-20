package me.thutson3876.fantasyclasses.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.FantasyClasses;

public class ShenanigansListener implements Listener {

	private static final FantasyClasses plugin = FantasyClasses.getPlugin();
	
	private static final PotionEffect nausea = new PotionEffect(PotionEffectType.NAUSEA, 10 * 20, 0);
	
	public ShenanigansListener() {
		plugin.registerEvents(this);
	}
	
	@EventHandler
	public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
		if(!e.getItem().getType().equals(Material.HONEY_BOTTLE))
			return;
		
		ItemMeta meta = e.getItem().getItemMeta();
		
		if(meta == null)
			return;
		
		if(!meta.getDisplayName().toLowerCase().contains("mead"))
			return;
		
		e.getPlayer().addPotionEffect(nausea);
	}
	
	
}
