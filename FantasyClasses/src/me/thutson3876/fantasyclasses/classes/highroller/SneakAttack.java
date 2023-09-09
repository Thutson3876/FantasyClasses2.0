package me.thutson3876.fantasyclasses.classes.highroller;

import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.ApplyStatusEvent;
import me.thutson3876.fantasyclasses.events.CustomLivingEntityDamageEvent;
import me.thutson3876.fantasyclasses.events.DamageModifier;
import me.thutson3876.fantasyclasses.events.RemoveStatusEvent;
import me.thutson3876.fantasyclasses.status.general.Stealth;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class SneakAttack extends AbstractAbility {

	private boolean isOn = false;
	private double dmgMod = 0.25;
	
	public SneakAttack(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Sneak Attack";
		this.skillPointCost = 1;
		this.maximumLevel = 2;
		
		this.createItemStack(Material.ENDER_PEARL);
	}
	
	@EventHandler
	public void onStealthEvent(ApplyStatusEvent e) {
			if(!e.getTarget().equals(player))
				return;
			
			if(e.getStatus().getType() instanceof Stealth) {
				isOn = true;
			}
	}
	
	@EventHandler
	public void onUnstealthEvent(RemoveStatusEvent e) {
			if(!e.getTarget().equals(player))
				return;
			
			if(e.getStatus().getType() instanceof Stealth) {
				isOn = false;
			}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onCustomLivingEntityDamageEvent(CustomLivingEntityDamageEvent e) {
		if(!isOn)
			return;
		
		if(!AbilityUtils.isTrueCause(player, e.getDamager()))
			return;
		
		e.addModifier(new DamageModifier("Sneak Attack", Operation.ADD_SCALAR, dmgMod));
	}
	
	@Override
	public String getInstructions() {
		return "Attack while &dStealthed";
	}

	@Override
	public String getDescription() {
		return "&dStealth &rcauses your next attack to deal &6" + AbilityUtils.doubleRoundToXDecimals(100 * dmgMod, coolDowninTicks) + "% &radditional damage";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		dmgMod = 0.25 * this.currentLevel;
	}

}
