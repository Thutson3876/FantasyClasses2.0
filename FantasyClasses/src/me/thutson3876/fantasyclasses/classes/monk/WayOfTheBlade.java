package me.thutson3876.fantasyclasses.classes.monk;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class WayOfTheBlade extends AbstractAbility {
	
	private double unarmedConversion = 0.3;
	
	public WayOfTheBlade(Player p) {
		super(p, Priority.HIGH);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Way Of The Blade";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.IRON_SWORD);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(!e.getDamager().equals(this.player))
			return;
		
		
		ItemStack mainHand = player.getInventory().getItemInMainHand();
		if ((mainHand == null
				|| mainHand.getType().equals(Material.AIR) || mainHand.getType().equals(Material.STICK)))
			return;
		
		AbstractFantasyClass clazz = this.getFantasyPlayer().getChosenClass();
		if (!(clazz instanceof Monk))
			return;

		Monk monk = (Monk) clazz;
		
		double damageMod = monk.getUnarmedDmgMod();
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		if(thisEvent.isCancelled())
			return;
		
		e.setDamage(e.getDamage() + (damageMod * (1 + this.unarmedConversion)));
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Attack with a weapon";
	}

	@Override
	public String getDescription() {
		return "Your weapon attacks deal bonus damage equal to &6" + AbilityUtils.doubleRoundToXDecimals(unarmedConversion * 100, coolDowninTicks) + "% &rof your &6Open &6Palm &rmodifier";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		this.unarmedConversion = 0.3 * this.currentLevel;
	}

}
