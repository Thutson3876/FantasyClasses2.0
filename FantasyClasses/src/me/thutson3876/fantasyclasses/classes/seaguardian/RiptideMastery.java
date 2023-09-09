package me.thutson3876.fantasyclasses.classes.seaguardian;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRiptideEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;

public class RiptideMastery extends AbstractAbility {

	private double lightningRange = 3.0;
	private int taskCounter = 0;
	private int ticksPerTick = 10;
	private int taskDuration = 3;

	public RiptideMastery(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 6 * 20;
		this.displayName = "Riptide Mastery";
		this.skillPointCost = 3;
		this.maximumLevel = 1;

		this.createItemStack(Material.END_ROD);
	}

	@EventHandler
	public void onPlayerRiptideEvent(PlayerRiptideEvent e) {
		if (!e.getPlayer().equals(player))
			return;

		if (isOnCooldown())
			return;

		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		lightningAura();
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Use a trident with riptide";
	}

	@Override
	public String getDescription() {
		return "Your riptides summon the aid of lightning to smite your foes";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {

	}

	private void lightningAura() {
		taskCounter = 0;
		new BukkitRunnable() {
			public void run() {
				if (taskCounter > taskDuration || !player.isRiptiding()) {
					taskCounter = 0;
					cancel();
					return;
				}
				for (Entity e : player.getNearbyEntities(lightningRange, lightningRange, lightningRange)) {
					if (player.hasLineOfSight(e))
						e.getWorld().strikeLightning(e.getLocation());
				}
				taskCounter++;
			}
		}.runTaskTimer(plugin, ticksPerTick, ticksPerTick);
	}
}
