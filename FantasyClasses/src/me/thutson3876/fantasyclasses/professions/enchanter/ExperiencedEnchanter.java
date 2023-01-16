package me.thutson3876.fantasyclasses.professions.enchanter;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerExpChangeEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class ExperiencedEnchanter extends AbstractAbility {

	private double expMod = 0.1;
	
	public ExperiencedEnchanter(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Experienced Enchanter";
		this.skillPointCost = 1;
		this.maximumLevel = 3;

		this.createItemStack(Material.EXPERIENCE_BOTTLE);
	}

	@EventHandler
	public void onPlayerExpChangeEvent(PlayerExpChangeEvent e) {
		if(!e.getPlayer().equals(player))
			return;
		
		if(e.getAmount() <= 0)
			return;
		
		e.setAmount((int) Math.round(((double)e.getAmount()) * (1 + this.expMod)));
	}
	
	@Override
	public String getInstructions() {
		return "Gain experience";
	}

	@Override
	public String getDescription() {
		return "Your prowess with enchanting allows you to gain &6" + AbilityUtils.doubleRoundToXDecimals(expMod * 100, 1) + "% &rmore experience from all sources";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.expMod = 0.1 * this.currentLevel;
	}

}
