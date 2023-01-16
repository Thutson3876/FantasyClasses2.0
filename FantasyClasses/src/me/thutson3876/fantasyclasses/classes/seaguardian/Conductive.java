package me.thutson3876.fantasyclasses.classes.seaguardian;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Conductive extends AbstractAbility {

	private double dmgMod = 0.12;
	
	public Conductive(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Conductive";
		this.skillPointCost = 1;
		this.maximumLevel = 4;

		this.createItemStack(Material.COPPER_INGOT);
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getEntity().equals(player))
			return;
		
		if(!e.getCause().equals(DamageCause.LIGHTNING))
			return;
		
		e.setDamage(e.getDamage() * (1 - dmgMod));
		
		this.onTrigger(true);
	}

	@Override
	public String getInstructions() {
		return "Take lightning damage";
	}

	@Override
	public String getDescription() {
		return "When you take lightning damage, it is reduced by &6" + AbilityUtils.doubleRoundToXDecimals(dmgMod * 100, 1) + "%";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		dmgMod = 0.12 * currentLevel;
	}

}
