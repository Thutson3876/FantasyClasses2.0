package me.thutson3876.fantasyclasses.classes.ranger;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;

public class Disengage extends AbstractAbility implements Bindable {

	private static final double yBoost = 0.375;

	private static final double velocityAmp = 0.95;

	private Material type = null;

	public Disengage(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 8 * 20;
		this.displayName = "Disengage";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.BIRCH_DOOR);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if (!e.getPlayer().equals(player))
			return;

		if (isOnCooldown())
			return;

		if (player.isSneaking())
			return;

		if (!e.getAction().equals(Action.LEFT_CLICK_AIR) && !e.getAction().equals(Action.LEFT_CLICK_BLOCK))
			return;

		ItemStack item = e.getItem();
		if (item == null)
			return;

		if (!item.getType().equals(type))
			return;

		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;

		player.setVelocity(
				player.getEyeLocation().getDirection().multiply(-velocityAmp).add(new Vector(0.0D, yBoost, 0.0D)));
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0f, 0.8f);
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Left-click with bound item type while not crouching";
	}

	@Override
	public String getDescription() {
		return "Leap backwards at immense speed";
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
