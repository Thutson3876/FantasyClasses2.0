package me.thutson3876.fantasyclasses.classes.combat;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.MaterialLists;

public class HeavyBlow extends AbstractAbility {

	private double dmgMod = 0.25;
	
	public HeavyBlow(Player p) {
		super(p, Priority.HIGH);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Heavy Blow";
		this.skillPointCost = 1;
		this.maximumLevel = 6;

		this.createItemStack(Material.CHIPPED_ANVIL);	
	}
	
	@EventHandler
	public void onDamageEvent(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamager().equals(player))
			return;
		
		if(player.getAttackCooldown() < 1.0)
			return;
		
		if(!e.getCause().equals(DamageCause.ENTITY_ATTACK))
			return;
		
		if(!MaterialLists.AXE.contains(player.getInventory().getItemInMainHand().getType()))
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		e.setDamage(e.getDamage() + dmgMod);
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Attack an entity with an axe";
	}

	@Override
	public String getDescription() {
		return "Your refined skill with the blade allows you to deal &6" + dmgMod + " &rmore damage with an axe";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		dmgMod = 0.25 * currentLevel;
	}

}
