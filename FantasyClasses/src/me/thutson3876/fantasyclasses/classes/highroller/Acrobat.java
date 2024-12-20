package me.thutson3876.fantasyclasses.classes.highroller;

import org.bukkit.Input;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInputEvent;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Acrobat extends AbstractAbility {

	private double yBoost = 0.3;
	private double velocityMod = 0.8;
	
	public Acrobat(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 4 * 20;
		this.displayName = "Acrobat";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.FEATHER);
	}

	@EventHandler
	public void onPlayerInputEvent(PlayerInputEvent e) {
		if (!e.getPlayer().equals(player))
			return;
		
		if (isOnCooldown())
			return;

		Input input = e.getInput();
		
		if(!e.getInput().isSneak())
			return;

		if (AbilityUtils.getHeightAboveGround(player) < 0.3)
			return;
		
		if(player.getLocation().getBlock().getType().equals(Material.WATER))
			return;
		
		Vector directionVector = player.getEyeLocation().getDirection();
		
		double left = input.isLeft() ? 1.5708 : 0;
		double right = input.isRight() ? 4.71239 : 0;
		double forward = input.isForward() ? 0 : 0;
		double backward = input.isBackward() ? 3.14159 : 0;
		
		double angle = left + right + forward + backward;
		directionVector.rotateAroundY(angle);

		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;

		Vector dash = directionVector.multiply(velocityMod);
		dash.setY(yBoost);
		player.setVelocity(dash);
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 0.5f, 1.1F);
		
		//FantasyClasses.getPlugin().log("Acrobat Angle: " + angle);

		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Crouch mid-air";
	}

	@Override
	public String getDescription() {
		return "Perform a horizontal dash mid-air in the direction you are moving.";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {

	}

}
