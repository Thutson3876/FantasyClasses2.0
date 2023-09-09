package me.thutson3876.fantasyclasses.classes.seaguardian;

import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.events.CustomLivingEntityDamageEvent;
import me.thutson3876.fantasyclasses.events.DamageModifier;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class PolearmMastery extends AbstractAbility {

	private double dmg = 1.0;
	
	public PolearmMastery(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Polearm Mastery";
		this.skillPointCost = 1;
		this.maximumLevel = 3;

		this.createItemStack(Material.TRIDENT);
	}

	@EventHandler
	public void onCustomLivingEntityDamageEvent(CustomLivingEntityDamageEvent e) {
		if(e.isCancelled())
			return;
		
		if(!(e.getDamager() instanceof Trident))
			return;
		
		Trident trident = (Trident) e.getDamager();
		
		if(trident.getShooter() == null || !trident.getShooter().equals(player))
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		e.addModifier(new DamageModifier(displayName, Operation.ADD_NUMBER, dmg));
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Throw a trident";
	}

	@Override
	public String getDescription() {
		return "Your thrown tridents strike with precision, dealing &6" + AbilityUtils.doubleRoundToXDecimals(dmg, 2) + "&r bonus damage.";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		dmg = 1.0 * currentLevel;
	}

}
