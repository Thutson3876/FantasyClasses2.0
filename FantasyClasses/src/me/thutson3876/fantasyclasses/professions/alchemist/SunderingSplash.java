package me.thutson3876.fantasyclasses.professions.alchemist;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PotionSplashEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;

public class SunderingSplash extends AbstractAbility {

	private float power = 1.0f;
	
	public SunderingSplash(Player p) {
		super(p, Priority.LOW);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Sundering Splash";
		this.skillPointCost = 1;
		this.maximumLevel = 5;

		this.createItemStack(Material.CREEPER_HEAD);
	}

	@EventHandler
	public void onPotionSplashEvent(PotionSplashEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getEntity().getShooter().equals(player))
			return;
		
		e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), power);
		
		this.onTrigger(true);
	}

	@Override
	public String getInstructions() {
		return "Throw a splash potion";
	}

	@Override
	public String getDescription() {
		return "Your splash potions explode upon impact with a force of &6" + power * 1000 + " &rnewtons";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		power = (0.5f * currentLevel);
	}

}
