package me.thutson3876.fantasyclasses.professions.fisherman;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class RefinedPalate extends AbstractAbility {

	private int bonusFood = 1;
	
	private static final List<Material> FISHIES = fishies();
	
	public RefinedPalate(Player p) {
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
		
		if(!FISHIES.contains(e.getItem().getType()))
			return;
		
		player.setFoodLevel(player.getFoodLevel() + bonusFood);
		player.setSaturation(player.getSaturation() + (bonusFood * 2));
		
		this.onTrigger(true);
	}

	@Override
	public String getInstructions() {
		return "Eat fish";
	}

	@Override
	public String getDescription() {
		return "Eating fish restores an extra &6" + AbilityUtils.doubleRoundToXDecimals((double)bonusFood / 2.0, 1) + " &rhunger";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		bonusFood = 1 * currentLevel;
	}
	
	private static List<Material> fishies() {
	    List<Material> fishies = new LinkedList<>();
	    fishies.add(Material.COOKED_COD);
	    fishies.add(Material.COOKED_SALMON);
	    fishies.add(Material.TROPICAL_FISH);
	    fishies.add(Material.PUFFERFISH);
	    fishies.add(Material.COD);
	    fishies.add(Material.SALMON);
	    return fishies;
	  }

}
