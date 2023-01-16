package me.thutson3876.fantasyclasses.classes.ranger;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;

import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class EndlessTrickery extends AbstractAbility {

	private double addAmt = 0.2 * 20;

	public EndlessTrickery(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Endless Trickery";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.BLAZE_POWDER);
	}

	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		Projectile eventProjectile = e.getEntity();

		if(e.getHitEntity() == null)
			return;
		
		if (!(eventProjectile instanceof Arrow)) {
			return;
		}

		AbilityTriggerEvent thisEvent = this.callEvent();
		
		if(thisEvent.isCancelled())
			return;
		
		for(Ability a : this.fplayer.getClassAbilities()) {
			if(a instanceof QuiverOfTricks) {
				((QuiverOfTricks)a).addToRemainingDuration(addAmt);
				return;
			}
		}
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Land arrow shots while &6Quiver &6of &6Tricks &ris active";
	}

	@Override
	public String getDescription() {
		return "Your &6Quiver of Tricks &rduration is extended by &6"
				+ AbilityUtils.doubleRoundToXDecimals(addAmt / 20, 2)
				+ " &rseconds for each shot you land into a target";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		addAmt = 0.2 * this.currentLevel * 20;
	}

}
