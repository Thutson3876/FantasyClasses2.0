package me.thutson3876.fantasyclasses.classes.monk;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class WindWeaver extends AbstractAbility {

	private double minDot = 0.7;
	private double distance = 4.0;
	private int jumpDistance = 5;
	private double velocity = 1.5;
	private double offset = 0.0;
	private double yBoost = 0.15;

	public WindWeaver(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 1 * 20;
		this.displayName = "Windweaver";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.PHANTOM_MEMBRANE);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if (!e.getPlayer().equals(this.player))
			return;

		if (isOnCooldown())
			return;

		if (player.isSneaking())
			return;

		if (!e.getAction().equals(Action.LEFT_CLICK_AIR) && !e.getAction().equals(Action.LEFT_CLICK_BLOCK))
			return;

		ItemStack mainHand = player.getInventory().getItemInMainHand();
		if (!(mainHand == null || mainHand.getType().equals(Material.AIR) || mainHand.getType().equals(Material.STICK)))
			return;

		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;

		AbstractFantasyClass clazz = fplayer.getChosenClass();
		if (!(clazz instanceof Monk))
			return;

		Monk monk = (Monk) clazz;

		double damage = monk.getUnarmedDmgMod();
		boolean hasFriendlyFire = fplayer.hasFriendlyFire();
		boolean isHealer = monk.isHealer();

		List<Entity> enemies = AbilityUtils.getEntitiesInAngle(player, minDot, distance, offset);

		for (Entity ent : enemies) {
			if (ent instanceof Player) {
				if (isHealer) {
					AbilityUtils.heal(player, damage, (Player) ent);
					continue;
				} else if (!hasFriendlyFire) {
					continue;
				}
			}

			ent.setVelocity(ent.getVelocity().add(new Vector(0, yBoost, 0)).multiply(0.6)
					.add(player.getEyeLocation().getDirection().multiply(velocity)));

			if (ent instanceof LivingEntity) {
				((LivingEntity) ent).damage(damage, player);
			}
		}
		for (Entity ent : enemies) {
			if (ent instanceof Player) {
				AbilityUtils.heal(player, damage, (Player) ent);
			}
		}

		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PHANTOM_FLAP, 0.7f, 1F);
		Block b = player.getTargetBlockExact(jumpDistance);

		if (b != null) {
			player.setVelocity(player.getEyeLocation().getDirection().multiply(-1.15));

			this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
			return;
		}

		if (enemies.isEmpty()) {
			return;
		}

		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Punch the air or a block with your empty hand";
	}

	@Override
	public String getDescription() {
		return "Send out a blast of wind to shoot your foes away or launch yourself while not wearing armor";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
	}

}
