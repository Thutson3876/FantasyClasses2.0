package me.thutson3876.fantasyclasses.classes.witch;

import org.bukkit.Location;
import org.bukkit.event.entity.PotionSplashEvent;

public interface WitchesBrewAction {

	public void run(PotionSplashEvent event, Location loc);
}
