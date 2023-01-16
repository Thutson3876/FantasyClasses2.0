package me.thutson3876.fantasyclasses.classes.combat;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.MaterialLists;

public class AxeWielder extends AbstractAbility {

	private double dmgMod = 0.5;
	
	public AxeWielder(Player p) {
		super(p, Priority.HIGH);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 16;
		this.displayName = "Axe Wielder";
		this.skillPointCost = 1;
		this.maximumLevel = 6;
		
		this.createItemStack(Material.IRON_AXE);
		
	}
	
	@EventHandler
	public void onDamageEvent(EntityDamageByEntityEvent e) {
		
		if(e.isCancelled())
			return;
		
		if(!e.getDamager().equals(this.player))
			return;
		
		if(player.getAttackCooldown() < 1.0)
			return;
		
		if(!e.getCause().equals(DamageCause.ENTITY_ATTACK))
			return;
		
		if(!MaterialLists.AXE.getMaterials().contains(player.getInventory().getItemInMainHand().getType()))
			return;
		
		if(AbilityUtils.isCritical(player)) {
			AbilityTriggerEvent thisEvent = this.callEvent();

			e.setDamage(e.getDamage() + dmgMod);
			player.getWorld().playSound(e.getEntity().getLocation(), Sound.BLOCK_GLASS_BREAK, 1.1f, 0.6f);
			this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
		}
	}

	@Override
	public String getInstructions() {
		return "Critically hit with an axe";
	}

	@Override
	public String getDescription() {
		return "When you critically hit a foe with an axe, deal &6" + AbilityUtils.doubleRoundToXDecimals(dmgMod, 2) + "&r more damage";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		this.dmgMod = (0.5 * currentLevel);	
	}

}
