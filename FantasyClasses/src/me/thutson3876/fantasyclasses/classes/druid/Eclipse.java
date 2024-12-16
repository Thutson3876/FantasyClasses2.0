package me.thutson3876.fantasyclasses.classes.druid;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Eclipse extends AbstractAbility {

	private double saturationChance = 0.12;
	private double shootingStarChance = 0.12;
	
	public Eclipse(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Eclipse";
		this.skillPointCost = 1;
		this.maximumLevel = 3;

		this.createItemStack(Material.CLOCK);	
	}

	@EventHandler
	public void onFoodLevelChangeEvent(FoodLevelChangeEvent e) {
		if(!e.getEntity().equals(player))
			return;
		
		if(!isDay())
			return;
		
		//plugin.log("Player Hunger: " + player.getFoodLevel() + " | New Hunger: " + e.getFoodLevel());
		if(player.getFoodLevel() < e.getFoodLevel())
			return;
		
		Random rng = new Random();
		
		if(rng.nextDouble() > saturationChance)
			return;
		
		e.setCancelled(true);
		player.setExhaustion(0);
		player.setSaturation(player.getSaturation() + 30);
		
		this.onTrigger(false);
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(!e.getDamager().equals(this.player))
			return;
		
		if(isDay())
			return;
		
		Random rng = new Random();
		
		if(rng.nextDouble() > shootingStarChance)
			return;
		
		AbstractFantasyClass clazz = this.getFantasyPlayer().getChosenClass();
		if (!(clazz instanceof Druid))
			return;

		Druid druid = (Druid) clazz;
		
		druid.spawnShootingStar(e.getEntity());
		
		this.onTrigger(false);
	}
	
	private boolean isDay() {
		long time = player.getWorld().getTime();
		
		return time < 12300 || time > 23850;
	}
	
	@Override
	public String getInstructions() {
		return "Exist.";
	}

	@Override
	public String getDescription() {
		return "During the &eDay&r, you have are &6" + AbilityUtils.doubleRoundToXDecimals(saturationChance * 100, 2) + "% &rless hungry. During &9Night&r, your attacks have a &6" + AbilityUtils.doubleRoundToXDecimals(shootingStarChance * 100, 2) + "% &rchance to summon a &6Shooting Star";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		saturationChance = 0.12 * currentLevel;
		shootingStarChance = 0.12 * currentLevel;
	}

}
