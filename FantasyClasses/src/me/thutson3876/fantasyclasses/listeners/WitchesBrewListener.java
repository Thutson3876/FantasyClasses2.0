package me.thutson3876.fantasyclasses.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.classes.witch.WitchBrewRecipe;

public class WitchesBrewListener implements Listener {
	private static final FantasyClasses plugin = FantasyClasses.getPlugin();

	public WitchesBrewListener() {
		plugin.registerEvents(this);
	}
	
	@EventHandler
	public void onPotionSplashEvent(PotionSplashEvent e) {
		Block hit = e.getHitBlock();
		Entity target = e.getHitEntity();
		Location loc = null;
		if(hit == null) {
			if(target != null)
				loc = target.getLocation();
			
			else
				loc = e.getPotion().getLocation();
		}
		else {
			loc = hit.getRelative(e.getHitBlockFace()).getLocation();
		}
		
		ItemStack potion = e.getPotion().getItem();
		WitchBrewRecipe recipe = WitchBrewRecipe.getMatching(potion);
		if(recipe == null)
			return;
		
		recipe.runAction(e, loc);
	}
}
