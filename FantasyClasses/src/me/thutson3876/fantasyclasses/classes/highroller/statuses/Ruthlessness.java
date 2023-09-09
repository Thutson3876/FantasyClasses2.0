package me.thutson3876.fantasyclasses.classes.highroller.statuses;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.events.RemoveStatusEvent;
import me.thutson3876.fantasyclasses.status.RemoveCause;
import me.thutson3876.fantasyclasses.status.Status;
import me.thutson3876.fantasyclasses.status.StatusData;
import me.thutson3876.fantasyclasses.status.StatusType;

public class Ruthlessness extends HighRollerStatus {

	//Consumption of blindside increases the damage bonus of the next consumption of broadside by 10% stacking (and vice versa)
	//Gain resistance 1
	
	public Ruthlessness() {
		super("Ruthlessness", 99, null, (host, duration, stacks) -> {
			
			host.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) duration, 0));
			
		});
	}
	
	@EventHandler
	public void onStatusRemoveEvent(RemoveStatusEvent e) {
		StatusType statusType = e.getStatus().getType();
		
		if(!e.getCause().equals(RemoveCause.ABILITY_PLAYER))
			return;
		
		if (!(e.getDispeller() instanceof LivingEntity))
			return;

		LivingEntity leHost = (LivingEntity) e.getDispeller();

		Status eventStatus = e.getStatus();
		Status thisStatus = statusManager.get(leHost, this);
		
		if(thisStatus == null)
			return;
		
		int blindsideStacks = thisStatus.getData().getInt(0);
		int broadsideStacks = thisStatus.getData().getInt(1);
		int newStacks = eventStatus.getStacks();
		
		if(statusType instanceof Blindsided) {
			blindsideStacks++;
			if(broadsideStacks > 0) {
				newStacks += broadsideStacks;
				broadsideStacks = 0;
			}
				
		}
		else if(statusType instanceof Broadsided) {
			 broadsideStacks++;
			 if(blindsideStacks > 0) {
				 newStacks += blindsideStacks;
				 blindsideStacks = 0;
			 }
		}
		
		StatusData newThisData = thisStatus.getData();
		
		newThisData.setInt(0, blindsideStacks);
		newThisData.setInt(1, broadsideStacks);
		
		thisStatus.setData(newThisData);
		
		eventStatus.setStacks(newStacks);
	}
	
	
}
