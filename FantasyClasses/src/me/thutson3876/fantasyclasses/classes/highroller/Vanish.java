package me.thutson3876.fantasyclasses.classes.highroller;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.status.ApplyCause;
import me.thutson3876.fantasyclasses.status.general.Stealth;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Vanish extends AbstractAbility implements Bindable {

	private Material type = null;
	private double duration = 12 * 20;
	
	public Vanish(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30 * 20;
		this.displayName = "Vanish";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.DISC_FRAGMENT_5);
	}

	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		if (isOnCooldown())
			return;

		if (!e.getPlayer().equals(player))
			return;

		if (!e.getItemDrop().getItemStack().getType().equals(type))
			return;

		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;

		e.setCancelled(true);

		Stealth buff = new Stealth();
		buff.apply(player, player, 1, duration, ApplyCause.PLAYER_ABILITY);

		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}
	
	@Override
	public String getInstructions() {
		return "Press your drop key while holding the bound item type";
	}

	@Override
	public String getDescription() {
		return "Apply &dStealth &rto yourself, causing you to become invisible and gain Speed for &6" + AbilityUtils.doubleRoundToXDecimals(duration / 20.0, 1) + " &rseconds. Damage dealt or taken cancels this effect";
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
