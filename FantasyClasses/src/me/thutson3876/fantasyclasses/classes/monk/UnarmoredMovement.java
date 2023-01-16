package me.thutson3876.fantasyclasses.classes.monk;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class UnarmoredMovement extends AbstractAbility {

	private float speedMod = 0.014f;

	public UnarmoredMovement(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Unarmored Movement";
		this.skillPointCost = 1;
		this.maximumLevel = 5;

		this.createItemStack(Material.RABBIT_FOOT);
	}

	@EventHandler
	public void InventoryCloseEvent(InventoryCloseEvent e) {
		if(!e.getPlayer().equals(this.player))
			return;
		
		if (!AbilityUtils.hasArmor(player)) {
			player.setWalkSpeed(0.2f + this.speedMod);
		} else {
			player.setWalkSpeed(0.2f);
		}
	}

	@Override
	public String getInstructions() {
		return "Wear no armor";
	}

	@Override
	public String getDescription() {
		return "While wearing no armor you move &6" + 7 * currentLevel + "% &rfaster";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		speedMod = 0.014f * currentLevel;
	}
	
	@Override
	public void deInit() {
		player.setWalkSpeed(0.2f);
	}
}
