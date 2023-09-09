package me.thutson3876.fantasyclasses.classes.highroller;

import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.Broadsided;
import me.thutson3876.fantasyclasses.events.CustomLivingEntityDamageEvent;
import me.thutson3876.fantasyclasses.events.DamageModifier;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class PreciseStrikes extends AbstractAbility {

	//sweeping edge damage gets increased by 30/60% when target has Broadsided
	
	private static final double dmgModPerLevel = 0.3;
	private double dmgMod = dmgModPerLevel;
	
	public PreciseStrikes(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Precise Strikes";
		this.skillPointCost = 1;
		this.maximumLevel = 2;
		
		this.createItemStack(Material.DIAMOND_SWORD);
	}
	
	@EventHandler
	public void onDamageEvent(CustomLivingEntityDamageEvent e) {
		if(!e.getDamager().equals(player))
			return;
			
		if(!e.getCause().equals(DamageCause.ENTITY_SWEEP_ATTACK))
			return;
		
		if(plugin.getStatusManager().get(e.getVictim(), Broadsided.class) == null)
			return;
		
		e.addModifier(new DamageModifier("Precise Strikes", Operation.ADD_NUMBER, e.getInitialDamage() * dmgMod));
	}

	@Override
	public String getInstructions() {
		return "Hit a target with sweeping strikes";
	}

	@Override
	public String getDescription() {
		return "Your sweeping strikes that damage a target with &dBroadsided &rdeal &6" + AbilityUtils.doubleRoundToXDecimals(dmgMod * 100.0, 1) + "% &rmore damage";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.dmgMod = dmgModPerLevel * this.currentLevel;
	}
	
	
}
