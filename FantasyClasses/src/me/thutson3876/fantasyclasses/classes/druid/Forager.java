package me.thutson3876.fantasyclasses.classes.druid;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.MaterialLists;

public class Forager extends AbstractAbility {

	private double chanceOnBreak = 0.1;
	private double goldenChance = 0.001;
	private static final List<Material> FOODS;
	private static final List<Material> LEAVES;

	static {
		FOODS = MaterialLists.DRUID_FOOD.getMaterials();
		LEAVES = MaterialLists.LEAVES.getMaterials();
	}

	public Forager(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Forager";
		this.skillPointCost = 1;
		this.maximumLevel = 5;

		this.createItemStack(Material.OAK_LEAVES);
	}

	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e) {
		Player p = e.getPlayer();

		if (!p.equals(player))
			return;

		if (!LEAVES.contains(e.getBlock().getType()))
			return;

		Random rng = new Random();
		double rolled = rng.nextDouble();
		if (rolled < this.chanceOnBreak) {
			if (rolled < this.goldenChance) {
				spawnGolden(e.getBlock().getLocation());
			} else {
				spawnFood(e.getBlock().getLocation());
			}
			AbilityTriggerEvent thisEvent = this.callEvent();
			this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
		}
	}

	@Override
	public String getInstructions() {
		return "Break leaves";
	}

	@Override
	public String getDescription() {
		return "Leaves have a &6" + AbilityUtils.doubleRoundToXDecimals(chanceOnBreak * 100, 2) + "% &rchance to drop fresh food, and a &6" + goldenChance * 100
				+ "% &rchance to drop a golden apple";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		chanceOnBreak = 0.1 * currentLevel;
		goldenChance = 0.001 * currentLevel;
	}

	private void spawnFood(Location loc) {
		Random rng = new Random();
		Material type = FOODS.get(rng.nextInt(FOODS.size()));
		ItemStack food = new ItemStack(type);
		loc.getWorld().dropItemNaturally(loc, food);
	}

	private void spawnGolden(Location loc) {
		loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.GOLDEN_CARROT));
	}

}
