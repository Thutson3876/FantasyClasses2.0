package me.thutson3876.fantasyclasses.classes.seaguardian;

import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.events.CustomLivingEntityDamageEvent;
import me.thutson3876.fantasyclasses.events.DamageModifier;
import me.thutson3876.fantasyclasses.util.math.MathUtils;

public class IceboundFortitude extends AbstractAbility {

	private static final double DMG_MOD_PER_LEVEL = 0.05;
	private double dmgMod = DMG_MOD_PER_LEVEL;
	
	public IceboundFortitude(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0 * 20;
		this.displayName = "Icebound Fortitude";
		this.skillPointCost = 1;
		this.maximumLevel = 3;

		this.createItemStack(Material.PACKED_ICE);
	}
	
	@EventHandler
	public void onCustomLivingEntityDamageEvent(CustomLivingEntityDamageEvent e) {
		if(e.isCancelled())
			return;
		
		if(this.isOnCooldown())
			return;
		
		if(!e.getVictim().equals(player)) 
			return;
		
		if(e.getDamager() == null || e.getDamager().getFreezeTicks() <= 0)
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		e.addModifier(new DamageModifier(displayName, Operation.MULTIPLY_SCALAR_1, -dmgMod));
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Take damage from a freezing entity";
	}

	@Override
	public String getDescription() {
		return "When you take damage from a freezing entity, it is reduced by &6" + MathUtils.convertToPercent(dmgMod, 1);
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.dmgMod = DMG_MOD_PER_LEVEL * this.currentLevel;
	}

}
