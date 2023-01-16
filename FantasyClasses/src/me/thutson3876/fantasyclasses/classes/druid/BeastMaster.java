package me.thutson3876.fantasyclasses.classes.druid;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class BeastMaster extends AbstractAbility {

	private static final double HEALTH_MODIFIER_PER_LEVEL = 0.1;
	private static final double DAMAGE_MODIFIER_PER_LEVEL = 0.1;
	private double healthMod = HEALTH_MODIFIER_PER_LEVEL;
	private double damageMod = DAMAGE_MODIFIER_PER_LEVEL;

	public BeastMaster(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 20;
		this.displayName = "Beast Master";
		this.skillPointCost = 1;
		this.maximumLevel = 3;

		this.createItemStack(Material.BONE);
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Tameable) {
			Tameable tamed = (Tameable) e.getEntity();
			if (tamed.getOwner() != null && tamed.getOwner().equals(player)) {
				AbilityTriggerEvent thisEvent = this.callEvent();
				e.setDamage(e.getDamage() * (1.0 - healthMod));
				this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
			}

		}
		if (e.getDamager() instanceof Tameable) {
			Tameable tamed = (Tameable) e.getDamager();
			if (tamed.getOwner() != null && tamed.getOwner().equals(player)) {
				AbilityTriggerEvent thisEvent = this.callEvent();
				e.setDamage(e.getDamage() * (1.0 + damageMod));
				this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
			}
		}
	}

	@Override
	public String getInstructions() {
		return "Tame an animal";
	}

	@Override
	public String getDescription() {
		return "Your tamed animals take &6" + AbilityUtils.doubleRoundToXDecimals(healthMod * 100, 1)
				+ "% &rless damage and deal &6" + AbilityUtils.doubleRoundToXDecimals(damageMod * 100, 1)
				+ "% &rmore damage";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		healthMod = HEALTH_MODIFIER_PER_LEVEL * currentLevel;
		damageMod = DAMAGE_MODIFIER_PER_LEVEL * currentLevel;
	}

}
