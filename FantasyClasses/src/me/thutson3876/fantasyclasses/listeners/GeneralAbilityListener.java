package me.thutson3876.fantasyclasses.listeners;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.events.ApplyStatusEvent;
import me.thutson3876.fantasyclasses.events.RemoveStatusEvent;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.status.RemoveCause;
import me.thutson3876.fantasyclasses.status.Status;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;
import me.thutson3876.fantasyclasses.util.chat.ColorCode;

public class GeneralAbilityListener implements Listener {

	private static final FantasyClasses plugin = FantasyClasses.getPlugin();

	public GeneralAbilityListener() {
		plugin.registerEvents(this);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onAbilityTriggerEvent(AbilityTriggerEvent e) {
		// e.getFplayer().getPlayer().sendMessage(ChatUtils.chat("&6" +
		// e.getAbility().getName() + " &3triggered"));
		e.setCancelled(e.getFplayer().isViolatingArmorType());
	}

	// test code
	@EventHandler(priority = EventPriority.LOW)
	public void arrowVelocityTracker(EntityShootBowEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;

		if (!(e.getProjectile() instanceof Arrow))
			return;

		Player player = (Player) e.getEntity();
		Arrow arrow = (Arrow) e.getProjectile();

		FantasyPlayer fplayer = plugin.getPlayerManager().getPlayer(player);
		if (fplayer != null && !fplayer.hasArrowVelocityTracker())
			return;

		double velocityMagnitude = arrow.getVelocity().length();
		double damage = arrow.getDamage();

		player.sendMessage(ChatUtils.chat("&2Bow Charge: &f" + e.getForce()));
		player.sendMessage(
				ChatUtils.chat("&6Arrow Velocity: &f" + AbilityUtils.doubleRoundToXDecimals(velocityMagnitude, 3)));
		player.sendMessage(ChatUtils.chat("&4Arrow Damage: &f" + AbilityUtils.doubleRoundToXDecimals(damage, 3)));

	}

	@EventHandler(priority = EventPriority.LOW)
	public void onStatusApply(ApplyStatusEvent e) {
		if (e.isCancelled())
			return;

		Entity applier = e.getApplier();
		LivingEntity target = e.getTarget();
		Status status = e.getStatus();
		String statusName = status.getType().getName();
		if (status.getStacks() > 1)
			statusName += ": " + status.getStacks();

		if (applier instanceof Player) {
			FantasyPlayer fplayer = plugin.getPlayerManager().getPlayer((Player) applier);
			if (fplayer != null && fplayer.hasStatusMessages() && !applier.equals(target)) {
				String enemyName = "";
				if (target instanceof Player)
					enemyName = ((Player) target).getDisplayName();
				else
					enemyName = target.getType().toString();

				fplayer.getPlayer()
						.sendMessage(ChatUtils.chat(ColorCode.STATUS_APPLIED + statusName + ColorCode.DEFAULT_METER
								+ " applied to " + ColorCode.ENEMY + enemyName + ColorCode.DEFAULT_METER + ". Lasts "
								+ ColorCode.STATUS_DURATION
								+ AbilityUtils.doubleRoundToXDecimals(status.getRemainingDuration() / 20.0, 1)
								+ ColorCode.DEFAULT_METER + " seconds."));
			}
		}
		if (target instanceof Player) {
			FantasyPlayer fplayer = plugin.getPlayerManager().getPlayer((Player) target);
			if (fplayer != null && fplayer.hasStatusMessages()) {
				String applierName = "";
				if (target instanceof Player)
					applierName = ColorCode.ALLY + ((Player) applier).getDisplayName();
				else
					applierName = ColorCode.ENEMY + applier.getType().toString();

				fplayer.getPlayer().sendMessage(
						ChatUtils.chat(ColorCode.STATUS_RECEIVED + statusName + ColorCode.DEFAULT_METER + " received from " + applierName + ColorCode.DEFAULT_METER + ". Lasts " + ColorCode.STATUS_DURATION
								+ AbilityUtils.doubleRoundToXDecimals(status.getRemainingDuration() / 20.0, 1)
								+ ColorCode.DEFAULT_METER + " seconds."));
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onStatusRemove(RemoveStatusEvent e) {
		Entity dispeller = e.getDispeller();
		LivingEntity target = e.getTarget();
		Status status = e.getStatus();
		String statusName = status.getType().getName();
		RemoveCause cause = e.getCause();
		if (status.getStacks() > 1)
			statusName += ": " + status.getStacks();

		if (target instanceof Player) {
			FantasyPlayer fplayer = plugin.getPlayerManager().getPlayer((Player) target);
			if (fplayer != null && fplayer.hasStatusMessages()) {
				String dispellerName = "";
				if (dispeller instanceof Player)
					dispellerName = ColorCode.ALLY + ((Player) dispeller).getDisplayName();
				else if (dispeller != null)
					dispellerName = ColorCode.ENEMY + dispeller.getType().toString();

				if (cause.equals(RemoveCause.EXPIRED))
					fplayer.getPlayer().sendMessage(ChatUtils.chat(ColorCode.STATUS_RECEIVED + statusName + ColorCode.STATUS_EXPIRED + " has expired."));
				else if (!cause.equals(RemoveCause.DISCONNECT))
					fplayer.getPlayer().sendMessage(ChatUtils.chat(ColorCode.STATUS_RECEIVED +  statusName + ColorCode.STATUS_EXPIRED + " removed by "
							+ dispellerName + ColorCode.STATUS_EXPIRED + " due to " + ColorCode.STATUS_DISPEL_CAUSE + cause.toString() + ColorCode.STATUS_EXPIRED + "."));
			}
		}
	}
}
