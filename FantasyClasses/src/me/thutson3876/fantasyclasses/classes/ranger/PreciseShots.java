package me.thutson3876.fantasyclasses.classes.ranger;

import org.bukkit.Material;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;

import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;

public class PreciseShots extends AbstractAbility {

	private double speedMult = 1.10;

	public PreciseShots(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Precise Shots";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.TARGET);
	}

	@EventHandler
	public void onEntityShootBowEvent(EntityShootBowEvent e) {
		if (!e.getEntity().equals(player))
			return;

		if (!(e.getProjectile() instanceof AbstractArrow))
			return;

		AbilityTriggerEvent thisEvent = this.callEvent();
		
		if(thisEvent.isCancelled())
			return;
		
		AbstractArrow arrow = (AbstractArrow) e.getProjectile();
		arrow.setVelocity(arrow.getVelocity().multiply(this.speedMult));

		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Shoot an arrow";
	}

	@Override
	public String getDescription() {
		return "Your expertise with a bow allows you to fire your arrows at &6" + AbilityUtils.doubleRoundToXDecimals((speedMult - 1.0) * 100.0, 2) + "% &rhigher velocity";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.speedMult = 1.0D + 0.1D * currentLevel;
	}

}
