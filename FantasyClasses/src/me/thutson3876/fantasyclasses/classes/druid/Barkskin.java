package me.thutson3876.fantasyclasses.classes.druid;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Barkskin extends AbstractAbility {

	private double dmgReduction = 0.20;
	private boolean isOn = false;
	private int durationInTicks = 1 * 20;
	
	public Barkskin(Player p) {
		super(p, Priority.LOW);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 12 * 20;
		this.displayName = "Barkskin";
		this.skillPointCost = 1;
		this.maximumLevel = 6;

		this.createItemStack(Material.OAK_WOOD);	
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(!e.getEntity().equals(player))
			return;
		
		if(isOnCooldown()) {
			if(isOn)
				e.setDamage((1 - dmgReduction) * e.getDamage());
			
			return;
		}
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		e.setDamage((1 - dmgReduction) * e.getDamage());
		
		isOn = true;
		
		new BukkitRunnable() {

			@Override
			public void run() {
				isOn = false;
				
			}
			
		}.runTaskLater(plugin, durationInTicks);
		
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}
	
	@Override
	public String getInstructions() {
		return "Take damage from an entity";
	}

	@Override
	public String getDescription() {
		return "Take &6" + AbilityUtils.doubleRoundToXDecimals(dmgReduction * 100, 1) + "% &rless damage for 1 second";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		dmgReduction = 0.20 * currentLevel;
	}

}
