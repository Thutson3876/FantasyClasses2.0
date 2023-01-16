package me.thutson3876.fantasyclasses.classes.druid;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.MaterialLists;

public class Regrowth extends AbstractAbility {

	public Regrowth(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Regrowth";
		this.skillPointCost = 3;
		this.maximumLevel = 1;

		this.createItemStack(Material.COMPOSTER);
	}

	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e) {
		if (!e.getPlayer().equals(player))
			return;

		Block block = e.getBlock();
		Material type = block.getType();

		if (!MaterialLists.CROP.contains(type))
			return;

		if (!AbilityUtils.isHarvestable(block.getBlockData()))
			return;

		AbilityTriggerEvent thisEvent = this.callEvent();
		
		new BukkitRunnable() {
			public void run() {
				if (block.getType().equals(Material.AIR) && block.getRelative(BlockFace.DOWN).getType().equals(Material.FARMLAND))
					block.setType(type);
			}
		}.runTaskLater(plugin, 5L);
		
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Harvest a crop";
	}

	@Override
	public String getDescription() {
		return "When you harvest a crop, it automatically replants itself";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
	}

}
