package me.thutson3876.fantasyclasses.classes.witch;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class NoBroomNeeded extends AbstractAbility implements Bindable {

	private boolean isGliding = false;

	private Material boundType = null;

	public NoBroomNeeded(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 10 * 20;
		this.displayName = "No Broom Needed";
		this.skillPointCost = 2;
		this.maximumLevel = 3;

		this.createItemStack(Material.ELYTRA);
	}

	@EventHandler
	public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent e) {
		if (!e.getPlayer().equals(player))
			return;

		if (isOnCooldown())
			return;

		if (AbilityUtils.getHeightAboveGround(player) < 0.6)
			return;

		if (!e.isSneaking())
			return;

		if ((player.getInventory().getItemInMainHand() == null
				|| !player.getInventory().getItemInMainHand().getType().equals(this.boundType))
				&& (player.getInventory().getItemInOffHand() == null
						|| !player.getInventory().getItemInOffHand().getType().equals(this.boundType)))
			return;

		e.setCancelled(true);

		player.setVelocity(player.getVelocity().add(new Vector(0, 2, 0)));

		isGliding = true;
		player.setGliding(true);
		
		this.onTrigger(true);
	}
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		if (!e.getPlayer().equals(player))
			return;

		if (!isGliding)
			return;

		if (AbilityUtils.getHeightAboveGround(player) < 0.3) {
			isGliding = false;
			player.setGliding(false);
		}
	}
	
	@EventHandler
	public void onEntityToggleGlideEvent(EntityToggleGlideEvent e) {
		if (!e.getEntity().equals(player))
			return;

		if (e.isGliding() && !isGliding) {
			e.setCancelled(false);
		} else {
			e.setCancelled(true);
		}
	}

	@Override
	public String getInstructions() {
		return "Crouch while mid-air with bound item type in hand";
	}

	@Override
	public String getDescription() {
		return "Launch yourself high into the air and glide to your destination safely. Without the need of a broom. Has a cooldown of &6"
				+ this.coolDowninTicks / 20 + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.coolDowninTicks = (90 - 15 * this.currentLevel) * 20;
	}

	@Override
	public Material getBoundType() {
		return this.boundType;
	}

	@Override
	public void setBoundType(Material type) {
		this.boundType = type;
	}

}
