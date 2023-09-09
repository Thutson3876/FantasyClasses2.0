package me.thutson3876.fantasyclasses.classes.highroller.statuses;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.events.RemoveStatusEvent;
import me.thutson3876.fantasyclasses.status.ApplyCause;
import me.thutson3876.fantasyclasses.status.DefaultStatusType;
import me.thutson3876.fantasyclasses.status.RemoveCause;
import me.thutson3876.fantasyclasses.status.StatusType;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class TrueBearing extends HighRollerStatus {

	//Attacks garauntee refilling crossbow in offhand
	//Gain 1 hunger and saturation every second

	private static final int DEFAULT_HUNGER_GAIN_AMT = 1;
	private static final float DEFAULT_SATURATION_GAIN_AMT = 1;
	
	public TrueBearing() {
		super("True Bearing", 20, (host, duration, stacks) -> {
			
			if(host instanceof Player) {
				AbilityUtils.addHunger(((Player)host), DEFAULT_HUNGER_GAIN_AMT);
				AbilityUtils.addSaturation(((Player)host), DEFAULT_SATURATION_GAIN_AMT);
			}
				 
			
		}, null);
	}
	
	@EventHandler
	public void onStatusRemoveEvent(RemoveStatusEvent e) {
		StatusType statusType = e.getStatus().getType();
		
		if(!e.getCause().equals(RemoveCause.ABILITY_PLAYER))
			return;
		
		if (!(e.getDispeller() instanceof LivingEntity))
			return;

		LivingEntity leHost = (LivingEntity) e.getDispeller();

		if (!statusManager.contains(leHost, this))
			return;
		
		if(statusType instanceof Blindsided || statusType instanceof Broadsided) {
			Player player = (Player) leHost;
			
			new BukkitRunnable() {

				@Override
				public void run() {
					DefaultStatusType.STEALTH.getType().apply(player, player, 1, 3 * 20, ApplyCause.PLAYER_ABILITY);
				}
				
			}.runTaskLater(FantasyClasses.getPlugin(), 1);
			;
		}
	}
	
}
