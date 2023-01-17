package me.thutson3876.fantasyclasses.classes.berserker;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;

public class Leap extends AbstractAbility implements Bindable {

	private Material type = null;
	
	private double speed = 1.25;
	private double yBoost = 0.6;
	
	private boolean canDoubleJump = false;
	private boolean hasDoubleJumped = false;
	
	public Leap(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 8 * 20;
		this.displayName = "Leap";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.RABBIT_FOOT);
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if (!e.getPlayer().equals(player))
			return;

		if (!(e.getAction() == Action.RIGHT_CLICK_AIR))
			return;
		
		if(e.getItem() == null || !e.getItem().getType().equals(this.type))
			return;
		
		if(canDoubleJump && !hasDoubleJumped && isOnCooldown()) {
			Vector newVelocity = player.getEyeLocation().getDirection().normalize().multiply(0.2);
			newVelocity.add(new Vector(0, yBoost / 1.5, 0));
			player.setVelocity(newVelocity.multiply(speed));
			
			player.getWorld().playSound(player, Sound.ENTITY_ENDER_DRAGON_FLAP, 0.65f, 1.2f);
			hasDoubleJumped = true;
			return;
		}
		if(isOnCooldown())
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		Vector newVelocity = player.getEyeLocation().getDirection().normalize();
		newVelocity.add(new Vector(0, yBoost, 0));
		player.setVelocity(newVelocity.multiply(speed));
		
		player.getWorld().playSound(player, Sound.ENTITY_ENDER_DRAGON_FLAP, 0.8f, 0.75f);
		hasDoubleJumped = false;
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Right-click bound item type";
	}

	@Override
	public String getDescription() {
		return "Launch yourself in the direction you're facing";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		
	}
	
	public void setDoubleJump(boolean canDoubleJump) {
		this.canDoubleJump = canDoubleJump;
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
