package me.thutson3876.fantasyclasses.professions.miner;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.MaterialLists;

public class BuiltToLast extends AbstractAbility {

	private static Random rng = new Random();
	private double chance = 0.1;
	
	public BuiltToLast(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Built To Last";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.NETHERITE_PICKAXE);
	}

	@EventHandler
	public void onPlayerItemDamageEvent(PlayerItemDamageEvent e) {
		if(!e.getPlayer().equals(player))
			return;
		
		if(isOnCooldown())
			return;
		
		if(!MaterialLists.PICKAXE.contains(e.getItem().getType()))
			return;
		
		if(rng.nextDouble() <= chance) {
			e.setDamage(0);
			this.onTrigger(true);
		}
	}

	@Override
	public String getInstructions() {
		return "Use a pickaxe";
	}

	@Override
	public String getDescription() {
		return "Reduce the durability consumption of your pickaxes by &6" + (AbilityUtils.doubleRoundToXDecimals(chance, 1) * 100 + "%");
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		chance = 0.1 * currentLevel;
	}

}
