package me.thutson3876.fantasyclasses.classes.monk;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class UnarmoredDexterity extends AbstractAbility {

	private double damageMod = 0.15;
	
	public UnarmoredDexterity(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Unarmored Dexterity";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.SHIELD);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamageEvent(EntityDamageEvent e) {
		if(!e.getEntity().equals(this.player))
			return;
			
		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		e.setDamage(e.getDamage() * (1 - damageMod));
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Wear no armor";
	}

	@Override
	public String getDescription() {
		return "Take &6" + AbilityUtils.doubleRoundToXDecimals(damageMod * 100, 1) + "% &rless damage while wearing no armor";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		damageMod = (0.15 * currentLevel);
	}

}
