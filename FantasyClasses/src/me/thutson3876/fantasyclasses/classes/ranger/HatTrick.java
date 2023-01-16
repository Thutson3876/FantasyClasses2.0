package me.thutson3876.fantasyclasses.classes.ranger;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class HatTrick extends AbstractAbility {

	private double procChance = 0.05;
	
	public HatTrick(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Hat Trick";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.LEATHER_HELMET);
	}
	
	@EventHandler
	public void onEntityShootBowEvent(EntityShootBowEvent e) {
		if(!player.equals(e.getEntity()))
			return;
		
		Random rng = new Random();
		if(rng.nextDouble() > this.procChance)
			return;
		
		AbstractFantasyClass clazz = this.getFantasyPlayer().getChosenClass();
		if(!(clazz instanceof Ranger))
			return;
		
		Ranger ranger = (Ranger) clazz;
		
		if(!(e.getProjectile() instanceof Arrow))
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		if(thisEvent.isCancelled())
			return;
		
		ranger.addTrickArrow((Arrow)e.getProjectile());
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Shoot an arrow at an entity";
	}

	@Override
	public String getDescription() {
		return "Your arrows have a &6" + AbilityUtils.doubleRoundToXDecimals(procChance * 100, 2) + "% &rchance to be trick arrows";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.procChance = 0.05 * this.currentLevel;
	}

}
