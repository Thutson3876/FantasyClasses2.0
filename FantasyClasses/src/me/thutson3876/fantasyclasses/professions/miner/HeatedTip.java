package me.thutson3876.fantasyclasses.professions.miner;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.listeners.SkillPointExpListener;

public class HeatedTip extends AbstractAbility implements Bindable {

	private static final Map<Material, Material> SMELTABLES = smeltables();

	private Material type = null;

	public HeatedTip(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Heated Tip";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.BLAZE_POWDER);
	}

	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e) {
		if(e.isCancelled())
			return;
		
		if (!e.getPlayer().equals(player))
			return;

		ItemStack item = player.getInventory().getItemInMainHand();
		if (item == null)
			return;
		if (!item.getType().equals(type))
			return;

		this.onTrigger(smeltItem(e));
	}

	@Override
	public String getInstructions() {
		return "Mine a smeltable block with the bound item type";
	}

	@Override
	public String getDescription() {
		return "Blocks you mine are automatically smelted. Nether Quartz now drops additional experience";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
	}

	private boolean smeltItem(BlockBreakEvent e) {
		Block block = e.getBlock();

		if (block.getType().equals(Material.NETHER_QUARTZ_ORE)) {
			if (e.getExpToDrop() > 0) {
				e.setExpToDrop(e.getExpToDrop() + 1);
			}
		}

		Collection<ItemStack> drops = block.getDrops(e.getPlayer().getInventory().getItemInMainHand(), player);
		if (drops == null || drops.isEmpty())
			return false;
		List<ItemStack> toRemove = new LinkedList<>();
		for (ItemStack item : drops) {
			if (SMELTABLES.containsKey(item.getType()))
				toRemove.add(item);
		}
		if (toRemove.isEmpty())
			return false;
		for (ItemStack item : toRemove) {
			drops.remove(item);
			drops.add(new ItemStack(SMELTABLES.get(item.getType()), item.getAmount()));
			fplayer.addSkillExp(SkillPointExpListener.getExpReward(item.getType()) / 2);
		}
		for (ItemStack item : drops)
			block.getWorld().dropItemNaturally(block.getLocation(), item);
		e.setDropItems(false);
		return true;
	}

	private static Map<Material, Material> smeltables() {
		Map<Material, Material> materials = new HashMap<>();
		materials.put(Material.COBBLESTONE, Material.STONE);
		materials.put(Material.RAW_COPPER, Material.COPPER_INGOT);
		materials.put(Material.RAW_IRON, Material.IRON_INGOT);
		materials.put(Material.RAW_GOLD, Material.GOLD_INGOT);
		materials.put(Material.COBBLED_DEEPSLATE, Material.DEEPSLATE);
		materials.put(Material.GILDED_BLACKSTONE, Material.GOLD_INGOT);
		materials.put(Material.SAND, Material.GLASS);
		materials.put(Material.RED_SAND, Material.GLASS);
		materials.put(Material.SANDSTONE, Material.SMOOTH_SANDSTONE);
		materials.put(Material.RED_SANDSTONE, Material.SMOOTH_RED_SANDSTONE);
		materials.put(Material.BASALT, Material.SMOOTH_BASALT);
		materials.put(Material.NETHER_GOLD_ORE, Material.GOLD_INGOT);
		materials.put(Material.SANDSTONE, Material.SMOOTH_SANDSTONE);
		materials.put(Material.CLAY, Material.TERRACOTTA);
		materials.put(Material.CLAY_BALL, Material.TERRACOTTA);
		materials.put(Material.CACTUS, Material.GREEN_DYE);
		materials.put(Material.ACACIA_LOG, Material.CHARCOAL);
		materials.put(Material.BIRCH_LOG, Material.CHARCOAL);
		materials.put(Material.DARK_OAK_LOG, Material.CHARCOAL);
		materials.put(Material.JUNGLE_LOG, Material.CHARCOAL);
		materials.put(Material.OAK_LOG, Material.CHARCOAL);
		materials.put(Material.SPRUCE_LOG, Material.CHARCOAL);
		materials.put(Material.STRIPPED_ACACIA_LOG, Material.CHARCOAL);
		materials.put(Material.STRIPPED_BIRCH_LOG, Material.CHARCOAL);
		materials.put(Material.STRIPPED_DARK_OAK_LOG, Material.CHARCOAL);
		materials.put(Material.STRIPPED_JUNGLE_LOG, Material.CHARCOAL);
		materials.put(Material.STRIPPED_OAK_LOG, Material.CHARCOAL);
		materials.put(Material.STRIPPED_SPRUCE_LOG, Material.CHARCOAL);
		materials.put(Material.WET_SPONGE, Material.SPONGE);
		materials.put(Material.SEA_PICKLE, Material.LIME_DYE);
		return materials;
	}

	@Override
	public Material getBoundType() {
		return type;
	}

	@Override
	public void setBoundType(Material type) {
		this.type = type;
	}
}
