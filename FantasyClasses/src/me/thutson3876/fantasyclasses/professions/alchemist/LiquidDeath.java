package me.thutson3876.fantasyclasses.professions.alchemist;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class LiquidDeath extends AbstractAbility {

	private double dmg = 3.0;
	private PotionEffect wither = null;
	
	public LiquidDeath(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 16;
		this.displayName = "Liquid Death";
		this.skillPointCost = 1;
		this.maximumLevel = 7;

		this.createItemStack(Material.WITHER_SKELETON_SKULL);
	}

	@EventHandler
	public void onPotionSplashEvent(PotionSplashEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getEntity().getShooter().equals(player))
			return;
		
		boolean isHarm = false;
		for(PotionEffect effect : e.getPotion().getEffects()) {
			if(effect.getType().equals(PotionEffectType.HARM)) {
				isHarm = true;
				break;
			}
		}
		
		if(!isHarm)
			return;
		
		for(LivingEntity le : e.getAffectedEntities()) {
			le.damage(dmg, player);
			if(wither != null)
				le.addPotionEffect(wither);
		}
		
		this.onTrigger(true);
	}

	@Override
	public String getInstructions() {
		return "Throw a potion of harming";
	}

	@Override
	public String getDescription() {
		return "Your potions of harming deal &6" + dmg + " &rextra damage. Max level also applies the withering";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		dmg = 3.0 * currentLevel;
		if(currentLevel == getMaxLevel()) 
			wither = new PotionEffect(PotionEffectType.WITHER, 5 * 20, 1);
	}

}
