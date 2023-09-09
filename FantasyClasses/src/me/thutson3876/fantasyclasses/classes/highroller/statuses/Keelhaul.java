package me.thutson3876.fantasyclasses.classes.highroller.statuses;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.events.RemoveStatusEvent;
import me.thutson3876.fantasyclasses.status.ApplyCause;
import me.thutson3876.fantasyclasses.status.RemoveCause;
import me.thutson3876.fantasyclasses.status.StatusType;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.metadatavalue.NoExplodeBlocks;

public class Keelhaul extends HighRollerStatus {

	public Keelhaul() {
		super("Keelhaul", 99, null, null);
	}

	// crossbow shot hits that consume Blindsided launch tnt at the target (mortar
	// style)
	// explosion damage applies debuff Blindsided

	@EventHandler
	public void onStatusRemoveEvent(RemoveStatusEvent e) {
		StatusType statusType = e.getStatus().getType();

		if (!e.getCause().equals(RemoveCause.ABILITY_PLAYER))
			return;

		if (!(e.getDispeller() instanceof LivingEntity))
			return;

		LivingEntity leHost = (LivingEntity) e.getDispeller();

		if(leHost instanceof Player)
			((Player)leHost).sendMessage("Keelhauled");
		
		if (!statusManager.contains(leHost, this))
			return;

		if (!leHost.equals(e.getDispeller()))
			return;

		if (statusType instanceof Blindsided) {
			Location victimLoc = e.getTarget().getLocation();

			TNTPrimed tnt = (TNTPrimed) leHost.getWorld().spawnEntity(leHost.getEyeLocation().add(0, 0.3, 0),
					EntityType.PRIMED_TNT);

			tnt.setSource(leHost);
			tnt.setVelocity(AbilityUtils.getVectorBetween2Points(leHost.getLocation(), victimLoc, 0.3)
					.add(new Vector(0, 2, 0)).normalize());
			tnt.setYield(3.0f);
			tnt.setFuseTicks(30);
			tnt.setGravity(true);
			tnt.setMetadata("noexplodeblocks", new NoExplodeBlocks());
		}
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof TNTPrimed) {
			TNTPrimed tnt = (TNTPrimed) e.getDamager();
			Entity source = tnt.getSource();

			if (source != null) {
				if (!(source instanceof LivingEntity))
					return;

				LivingEntity leHost = (LivingEntity) source;

				if (!statusManager.contains(leHost, this))
					return;

				if (e.getEntity() instanceof LivingEntity) {
					Blindsided debuff = new Blindsided();
					debuff.apply(leHost, leHost, 1, Blindsided.getBaseDuration(), ApplyCause.PLAYER_ABILITY);
				}
			}

		}
	}

}
