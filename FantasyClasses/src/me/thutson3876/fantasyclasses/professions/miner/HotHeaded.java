package me.thutson3876.fantasyclasses.professions.miner;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.DamageType;

public class HotHeaded extends AbstractAbility {

	private double dmgReduction = 0.05;
	
	public HotHeaded(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Hot Headed";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.MAGMA_BLOCK);
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(!e.getEntity().equals(player))
			return;
		
		if(!DamageType.FIRE.contains(e.getCause()))
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		e.setDamage(e.getDamage() * (1 - dmgReduction));
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Take fire damage";
	}

	@Override
	public String getDescription() {
		return "Gain &6" + (AbilityUtils.doubleRoundToXDecimals(dmgReduction, 1) * 100) + "% &rdamage reduction against fire damage";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		dmgReduction = 0.05 * currentLevel;
	}

}
