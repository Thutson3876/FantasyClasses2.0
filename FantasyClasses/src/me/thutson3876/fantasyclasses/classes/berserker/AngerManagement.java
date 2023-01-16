package me.thutson3876.fantasyclasses.classes.berserker;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class AngerManagement extends AbstractAbility {

	private double procChance = 0.1;
	private int duration = 3 * 20;

	public AngerManagement(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Anger Management";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.EXPERIENCE_BOTTLE);
	}

	@EventHandler
	public void onDamageEvent(EntityDamageByEntityEvent e) {
		if (!e.getEntity().equals(player))
			return;

		if (e.isCancelled())
			return;

		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		Random rng = new Random();
		
		if(rng.nextDouble() > this.procChance)
			return;
		
		AbstractFantasyClass clazz = this.getFantasyPlayer().getChosenClass();
		if (!(clazz instanceof Berserker))
			return;

		Berserker berserker = (Berserker) clazz;

		berserker.setEnrageDurationRemaining(this.duration);
	}

	@Override
	public String getInstructions() {
		return "Take damage from an entity";
	}

	@Override
	public String getDescription() {
		return "Upon taking damage, you have a &6" + AbilityUtils.doubleRoundToXDecimals(procChance * 100, 1)
				+ "% &rto become &6Enraged &rfor &6" + duration / 20 + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.procChance = 0.1 * this.currentLevel;
	}

}
