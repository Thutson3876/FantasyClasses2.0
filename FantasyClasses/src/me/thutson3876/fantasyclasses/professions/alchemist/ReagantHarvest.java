package me.thutson3876.fantasyclasses.professions.alchemist;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.MaterialLists;

public class ReagantHarvest extends AbstractAbility {

	private float dropChance = 0.1f;

	public ReagantHarvest(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Reagant Harvest";
		this.skillPointCost = 1;
		this.maximumLevel = 5;

		this.createItemStack(Material.BLAZE_ROD);
	}

	@EventHandler
	public void onEntityDeathEvent(EntityDeathEvent e) {
		if(e.getEntity().getKiller() == null || !e.getEntity().getKiller().equals(player))
			return;
		
		List<ItemStack> drops = e.getDrops();
		List<ItemStack> extraDrops = new ArrayList<>();
		Random rng = new Random();

		for (Material mat : MaterialLists.ALCHEMICAL_INGREDIENT.getMaterials()) {
			for (ItemStack item : drops) {
				if (item.getType().equals(mat)) {
					if (rng.nextFloat() < this.dropChance) {
						extraDrops.add(new ItemStack(mat));
					}
				}
			}
		}

		for (ItemStack i : extraDrops) {
			e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), i);
		}
		this.onTrigger(!extraDrops.isEmpty());
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getPlayer().equals(this.player))
			return;
		
		Block block = e.getBlock();
		BlockData data = block.getBlockData();
		Material type = block.getType();
		if(!MaterialLists.ALCHEMICAL_INGREDIENT.contains(type))
			return;

		if (!(data instanceof Ageable)) {
			return;
		}
		Ageable ageable = (Ageable) data;
		if (ageable.getAge() < ageable.getMaximumAge()) {
			return;
		}
		Random rng = new Random();
		if(e.isDropItems() && rng.nextDouble() < this.dropChance) {
			e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(type));
			this.onTrigger(true);
		}
	}

	@Override
	public String getName() {
		return "Reagent Harvest";
	}

	@Override
	public String getInstructions() {
		return "Obtain alchemical reagants by any natural means";
	}

	@Override
	public String getDescription() {
		return "When harvesting reagants for alchemy, you have a &6" + AbilityUtils.doubleRoundToXDecimals(0.1 * currentLevel * 100, 2)
				+ "% &rto obtain an additional reagant";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return currentLevel > 0;
	}

	@Override
	public void applyLevelModifiers() {
		this.dropChance = 0.1f * this.currentLevel;
	}

}
