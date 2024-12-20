package me.thutson3876.fantasyclasses.classes.witch;

import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.events.CustomLivingEntityDamageEvent;
import me.thutson3876.fantasyclasses.events.DamageModifier;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class WitchCurse extends AbstractAbility {

	private double dmgMod = 0.08;
	
	public WitchCurse(Player p) {
		super(p, Priority.LOW);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Witch's Curse";
		this.skillPointCost = 1;
		this.maximumLevel = 3;

		this.createItemStack(Material.LINGERING_POTION);		
	}

	@EventHandler
	public void onCustomLivingEntityDamageEvent(CustomLivingEntityDamageEvent e) {
		if(e.isCancelled())
			return;
		
		if(this.isOnCooldown())
			return;
		
		if(e.getDamager() == null)
			return;
		
		if(!e.getDamager().equals(player))
			return;
		
		e.addModifier(new DamageModifier("Witch's Curse", Operation.MULTIPLY_SCALAR_1, dmgMod));
		
		this.onTrigger(false);
	}

	@Override
	public String getInstructions() {
		return "Deal damage to a debuffed target";
	}

	@Override
	public String getDescription() {
		return "Deal &6" + AbilityUtils.doubleRoundToXDecimals(dmgMod * 100.0, 2) + "% &rmore damage to creature afflicted with a &dDebuff";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.dmgMod = 0.08 * currentLevel;
	}

}
