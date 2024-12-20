package me.thutson3876.fantasyclasses.professions.hunter;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class PaleoDiet extends AbstractAbility {

	private int bonusFood = 1;
	
	public PaleoDiet(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Refined Palate";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.COOKED_COD);
	}

	@EventHandler
	public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
		if(!e.getPlayer().equals(player))
			return;
		
		player.setFoodLevel(player.getFoodLevel() + bonusFood);
		player.setSaturation(player.getSaturation() + (bonusFood * 2));
		player.setExhaustion(0);
		
		this.onTrigger(false);
	}

	@Override
	public String getInstructions() {
		return "Eat food";
	}

	@Override
	public String getDescription() {
		return "Eating food restores an extra &6" + AbilityUtils.doubleRoundToXDecimals((double)bonusFood / 2.0, 1) + " &rhunger, and clears exhaustion";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		bonusFood = 1 * currentLevel;
	}

}
