package me.thutson3876.fantasyclasses.professions.hunter;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class Expedition extends AbstractAbility {

	List<Statistic> validReasons = Arrays.asList(Statistic.HORSE_ONE_CM, Statistic.BOAT_ONE_CM, Statistic.SPRINT_ONE_CM, Statistic.WALK_ONE_CM,
			Statistic.STRIDER_ONE_CM, Statistic.SWIM_ONE_CM, Statistic.CROUCH_ONE_CM, Statistic.CLIMB_ONE_CM);
	
	int divisor = 100;
	int amtToGive = 1;
	ItemStack emeralds;
	
	public Expedition(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Expedition";
		this.skillPointCost = 1;
		this.maximumLevel = 1;
		emeralds = new ItemStack(Material.EMERALD, amtToGive);

		this.createItemStack(Material.EMERALD);
	}

	@EventHandler
	public void onPlayerStatisticIncrementEvent(PlayerStatisticIncrementEvent e) {
		if(!e.getPlayer().equals(player))
			return;
		
		if(!validReasons.contains(e.getStatistic()))
			return;
		
		if(e.getNewValue() % divisor != 0)
			return;
		
		player.getInventory().addItem(emeralds);
		player.playSound(player, Sound.ENTITY_ALLAY_ITEM_TAKEN, 1.2f, 1.1f);
		
		this.onTrigger(true);
	}

	@Override
	public String getInstructions() {
		return "Travel far";
	}

	@Override
	public String getDescription() {
		return "Whenever you travel &6100 meters&r, gain &610 &remeralds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		emeralds = new ItemStack(Material.EMERALD, amtToGive);
	}

}
