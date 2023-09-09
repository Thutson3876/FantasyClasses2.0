package me.thutson3876.fantasyclasses.classes.highroller;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Feint extends AbstractAbility {

	private static final double REDUCTION_PER_LEVEL = 0.15;
	
	private int minDuration = 3 * 20;
	private double dmgReduction = REDUCTION_PER_LEVEL;
	
	private boolean isOn = false;
	private int counter = minDuration;
	private boolean off = false;

	public Feint(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0 * 20;
		this.displayName = "Feint";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.CHAINMAIL_CHESTPLATE);
	}
	
	@Override
	public void init() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if(off)
					this.cancel();
				
				counter -= 10;
				
				if(counter <= 0) {
					isOn = true;
				}
			}
			
		};
	}
	
	@Override
	public void deInit() {
		off = true;
	}
	
	@EventHandler
	public void onDamageEvent(EntityDamageEvent e) {
		if(!e.getEntity().equals(player))
			return;
		
		if(e.isCancelled())
			return;
		
		counter = minDuration;
		
		if(!isOn) {
			return;
		}
		
		e.setDamage(e.getDamage() * (1 - this.dmgReduction));
		isOn = false;
	}

	@Override
	public String getInstructions() {
		return "Don't take damage for some time";
	}

	@Override
	public String getDescription() {
		return "When you haven't taken damage for at least &6" + AbilityUtils.doubleRoundToXDecimals(minDuration / 20.0, 1)
				+ " &rseconds, the next instance of damage you take is reduced by &6"
				+ AbilityUtils.doubleRoundToXDecimals(dmgReduction * 100.0, 1) + "%";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		dmgReduction = REDUCTION_PER_LEVEL * this.currentLevel;
	}

}
