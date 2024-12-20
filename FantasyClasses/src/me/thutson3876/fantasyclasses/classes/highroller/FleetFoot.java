package me.thutson3876.fantasyclasses.classes.highroller;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;

public class FleetFoot extends AbstractAbility {

	private int duration = 4 * 20;
	private int amp = 0;
	
	public FleetFoot(Player p) {
		super(p, Priority.LOW);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Fleet Foot";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.RABBIT_FOOT);
	}

	@EventHandler
	public void onAbilityTriggerEvent(AbilityTriggerEvent e) {
		if(!e.getFplayer().equals(fplayer))
			return;
		
		if(!(e.getAbility() instanceof Acrobat))
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		if(thisEvent.isCancelled())
			return;
		
		e.getFplayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, amp));
		
		//this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Cast &6Acrobat";
	}

	@Override
	public String getDescription() {
		return "&6Acrobat &rapplies &dSpeed &6" + currentLevel + " &rfor a short duration";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		amp = currentLevel - 1;
	}

}
