package me.thutson3876.fantasyclasses.classes.seaguardian;

import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.events.CustomLivingEntityDamageEvent;
import me.thutson3876.fantasyclasses.events.DamageModifier;
import me.thutson3876.fantasyclasses.util.math.MathUtils;

public class ProtectorsDuty extends AbstractAbility {

	private double dmgMod = 0.05;
	private double radius = 8;

	public ProtectorsDuty(Player p) {
		super(p, Priority.HIGH);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "A Protector's Duty";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.SHIELD);
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(CustomLivingEntityDamageEvent e) {
		if (e.isCancelled())
			return;
		
		if(!(e.getVictim() instanceof Player))
			return;
		
		if(e.getVictim().getLocation().distance(player.getLocation()) > radius)
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		e.addModifier(new DamageModifier(displayName, Operation.MULTIPLY_SCALAR_1, -dmgMod));
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Have friends";
	}

	@Override
	public String getDescription() {
		return "You and your nearby allies take &6" + MathUtils.convertToPercent(dmgMod, 1)
				+ "% &rless damage";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		dmgMod = 0.05 * currentLevel;
	}
}
