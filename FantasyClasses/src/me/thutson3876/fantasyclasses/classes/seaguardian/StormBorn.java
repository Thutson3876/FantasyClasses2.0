package me.thutson3876.fantasyclasses.classes.seaguardian;

import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.events.CustomLivingEntityDamageEvent;
import me.thutson3876.fantasyclasses.events.DamageModifier;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class StormBorn extends AbstractAbility {

	private static final double BASE_DAMAGE = 0.15;
	private double dmgMod = BASE_DAMAGE;
	
	public StormBorn(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Stormborn";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.COPPER_INGOT);
	}

	@EventHandler
	public void onEntityDamageEvent(CustomLivingEntityDamageEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getVictim().equals(player))
			return;
		
		if(!e.getCause().equals(DamageCause.LIGHTNING))
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		e.addModifier(new DamageModifier(displayName, Operation.MULTIPLY_SCALAR_1, -dmgMod));
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Take lightning damage";
	}

	@Override
	public String getDescription() {
		return "When you take lightning damage, it is reduced by &6" + AbilityUtils.doubleRoundToXDecimals(dmgMod * 100, 1) + "%";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		dmgMod = BASE_DAMAGE * currentLevel;
	}

}
