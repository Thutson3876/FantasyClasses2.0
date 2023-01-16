package me.thutson3876.fantasyclasses.classes.monk;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class BeerBelly extends AbstractAbility {

	double conversion = 0.08;
	
	public BeerBelly(Player p) {
		super(p, Priority.LOW);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Beer Belly";
		this.skillPointCost = 1;
		this.maximumLevel = 4;

		this.createItemStack(Material.HONEYCOMB_BLOCK);
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getEntity().equals(player))
			return;
		
		if(AbilityUtils.hasArmor(player))
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		double dmg = e.getDamage();
		player.setExhaustion((float) (player.getExhaustion() + (dmg * conversion * 2)));
		e.setDamage(dmg * (1 - conversion));
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Wear no armor";
	}

	@Override
	public String getDescription() {
		return "Convert &6" + AbilityUtils.doubleRoundToXDecimals(conversion * 100, 1) + "% &rof damage taken into exhaustion while not wearing armor";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		conversion = 0.08 * currentLevel;
	}

}
