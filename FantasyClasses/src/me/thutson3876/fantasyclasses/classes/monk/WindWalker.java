package me.thutson3876.fantasyclasses.classes.monk;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class WindWalker extends AbstractAbility {

	private double velocityMod = 2.0;

	private boolean canDoubleJump = false;
	private boolean hasDoubleJumped = false;
	private PotionEffect slowFall = new PotionEffect(PotionEffectType.SLOW_FALLING, 3 * 20, 0);
	
	public WindWalker(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 3 * 20;
		this.displayName = "Windwalker";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.IRON_BOOTS);
	}

	@EventHandler
	public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent e) {
		if (!e.getPlayer().equals(this.player))
			return;

		if (player.isSneaking())
			return;
		
		if (AbilityUtils.getHeightAboveGround(player) < 0.3)
			return;

		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		if (canDoubleJump && !hasDoubleJumped && isOnCooldown()) {
			player.addPotionEffect(slowFall);

			player.getWorld().playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 0.8f, 1.2f);
			hasDoubleJumped = true;
			return;
		}

		if (isOnCooldown())
			return;

		Vector dash = player.getEyeLocation().getDirection().multiply(velocityMod);
		dash.setY(0.5);
		player.setVelocity(dash);
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 0.5f, 1.1F);
		hasDoubleJumped = false;
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Crouch mid-air";
	}

	@Override
	public String getDescription() {
		return "Perform a horizontal dash mid-air in the direction you are facing";
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
}
