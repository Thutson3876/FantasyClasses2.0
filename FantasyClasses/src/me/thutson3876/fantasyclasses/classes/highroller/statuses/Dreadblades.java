package me.thutson3876.fantasyclasses.classes.highroller.statuses;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.status.ApplyCause;
import me.thutson3876.fantasyclasses.status.Status;
import me.thutson3876.fantasyclasses.status.general.Leech;

public class Dreadblades extends HighRollerStatus {

	//Gain 20% lifesteal
	//Haste 4
	
	private static final int STACK_AMT = 2;
	//private static final double RIPOSTE_PERCENT = 1.00;
	
	//private double storedDamage = 0.0;
	//used to deal damage equal to damage taken
	
	public Dreadblades() {
		super("Dreadblades", 99, null, (host, duration, stacks) -> {
			host.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, (int) duration, 3));
			
			Leech leech = new Leech();
			
			Status status = leech.apply(host, host, stacks, duration, ApplyCause.PLAYER_ABILITY);
			
			if(status != null)
				status.setStacks(STACK_AMT);
		});
	}
	
	/*@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		if(e.getEntity().equals(this.host)) {
			storedDamage = e.getFinalDamage();
		}
		else if(e.getDamager().equals(this.host)) {
			e.setDamage(e.getDamage() + (RIPOSTE_PERCENT * storedDamage));
			storedDamage = 0.0;
		}
	}*/

}
