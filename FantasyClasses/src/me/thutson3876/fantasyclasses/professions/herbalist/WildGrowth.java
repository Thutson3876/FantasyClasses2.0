package me.thutson3876.fantasyclasses.professions.herbalist;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockGrowEvent;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class WildGrowth extends AbstractAbility {
	
	private double radius = 50;
	
	public WildGrowth(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Wild Growth";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.BAMBOO_SAPLING);	
	}

	@EventHandler
	public void onPlayerInteractEvent(BlockGrowEvent e) {
		
		Block b = e.getBlock();
		
		if(b == null)
			return;
		
		Ageable ageable = (Ageable) b.getBlockData();

		if(ageable == null)
			return;
		
		if(!AbilityUtils.getNearbyLivingEntities(b.getLocation(), radius, radius, radius).contains(player))
			return;
		
		ageable.setAge(ageable.getAge() + 2);
		
		this.onTrigger(false);
	}

	@Override
	public String getInstructions() {
		return "Stand near plants";
	}

	@Override
	public String getDescription() {
		return "Plants around you grow at an increased rate";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {

	}

}
