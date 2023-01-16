package me.thutson3876.fantasyclasses.classes.seaguardian;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class PolearmMastery extends AbstractAbility {

	private double dmg = 0.4;
	
	public PolearmMastery(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Polearm Mastery";
		this.skillPointCost = 1;
		this.maximumLevel = 6;

		this.createItemStack(Material.TRIDENT);
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		if(!(e.getDamager() instanceof Trident))
			return;
		
		Trident trident = (Trident) e.getDamager();
		
		if(trident.getShooter() == null || !trident.getShooter().equals(player))
			return;
		
		e.setDamage(e.getDamage() + (dmg * (AbilityUtils.isInWaterOrRain(player) ? 2 : 1)));
		
		this.onTrigger(true);
	}

	@Override
	public String getInstructions() {
		return "Throw a trident";
	}

	@Override
	public String getDescription() {
		return "Your thrown tridents strike with precision, dealing &6" + AbilityUtils.doubleRoundToXDecimals(dmg, 2) + " &r bonus damage. This extra damage is doubled if you are in water";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		dmg = 0.4 * currentLevel;
	}

}
