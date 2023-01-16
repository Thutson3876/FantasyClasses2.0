package me.thutson3876.fantasyclasses.professions.alchemist;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PotionSplashEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class PotentSplash extends AbstractAbility {

	private double intensity = 0.2;
	
	public PotentSplash(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 16;
		this.displayName = "Potent Splash";
		this.skillPointCost = 1;
		this.maximumLevel = 5;

		this.createItemStack(Material.SPLASH_POTION);
	}

	@EventHandler
	public void onPotionSplashEvent(PotionSplashEvent e) {
		if(!e.getEntity().getShooter().equals(player))
			return;
		
		for(LivingEntity ent : e.getAffectedEntities()) {
			e.setIntensity(ent, 1.0 + intensity);
		}
		
		e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 0.5f, 0.6f);
		
		this.onTrigger(true);
	}
	
	@Override
	public String getInstructions() {
		return "Throw a splash potion";
	}

	@Override
	public String getDescription() {
		return "Your splash potions are &6" + AbilityUtils.doubleRoundToXDecimals(intensity * 100, 2) + "% &rmore potent";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		intensity = 0.2 * currentLevel;
	}

}
