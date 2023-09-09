package me.thutson3876.fantasyclasses.classes.highroller;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import me.thutson3876.fantasyclasses.status.ApplyCause;
import me.thutson3876.fantasyclasses.status.general.Strider;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Acrobat extends AbstractAbility implements Bindable {

	private Material type = null;

	private int duration = 2 * 20;
	public Acrobat(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 6 * 20;
		this.displayName = "Acrobat";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.FEATHER);
	}

	@EventHandler
	public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {
		if (isOnCooldown())
			return;

		if (!e.getPlayer().equals(player))
			return;

		if(!player.isSneaking())
			return;
		
		boolean correctType = false;

		if (e.getMainHandItem() != null) {
			if (e.getMainHandItem().getType().equals(type))
				correctType = true;
		}
		if (e.getOffHandItem() != null) {
			if (e.getOffHandItem().getType().equals(type))
				correctType = true;
		}

		if (!correctType)
			return;

		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;

		Strider striderStatus = new Strider();
		
		striderStatus.apply(player, player, 1, duration, ApplyCause.PLAYER_ABILITY);

		e.setCancelled(true);
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Swap hands with bound item type while crouching";
	}

	@Override
	public String getDescription() {
		return "Grant yourself speed and jump boost for &6"
				+ AbilityUtils.doubleRoundToXDecimals(((double) duration) / 20.0, 1) + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {

	}

	@Override
	public Material getBoundType() {
		return type;
	}

	@Override
	public void setBoundType(Material type) {
		this.type = type;
	}

}
