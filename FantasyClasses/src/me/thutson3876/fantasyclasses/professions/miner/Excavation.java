package me.thutson3876.fantasyclasses.professions.miner;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.Cuboid;
import me.thutson3876.fantasyclasses.util.MaterialLists;

public class Excavation extends AbstractAbility {

	private static final List<Material> breakableMaterials = getMaterialList();

	public Excavation(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 8 * 20;
		this.displayName = "Excavation";
		this.skillPointCost = 1;
		this.maximumLevel = 3;

		this.createItemStack(Material.CRACKED_DEEPSLATE_BRICKS);
	}

	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e) {
		if(e.isCancelled())
			return;
		
		if (isOnCooldown())
			return;

		if (!e.getPlayer().equals(player))
			return;

		if (!player.isSneaking())
			return;

		if (!MaterialLists.PICKAXE.contains(player.getInventory().getItemInMainHand().getType()))
			return;

		this.onTrigger(breakArea(e));
	}

	@Override
	public String getInstructions() {
		return "Dig stone while crouching";
	}

	@Override
	public String getDescription() {
		return "Dig with impressive strength and carve out a 3x3 cube in stone. Has a cooldown of &6"
				+ (this.coolDowninTicks / 20) + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.coolDowninTicks = (12 - 4 * currentLevel) * 20 + 4;
	}

	private boolean breakArea(BlockBreakEvent e) {
		Block broken = e.getBlock();
		if (!breakableMaterials.contains(broken.getType()))
			return false;
		List<Block> surroundingBlocks = new LinkedList<>();
		Cuboid cube = Cuboid.createFromLocationRadius(broken.getLocation(), 1.0D);
		for (Block block : cube) {
			if (breakableMaterials.contains(block.getType()))
				surroundingBlocks.add(block);
		}
		if (surroundingBlocks.isEmpty())
			return false;
		
		ItemStack mainHand = player.getInventory().getItemInMainHand();
		ItemMeta meta = mainHand.getItemMeta();
		if(meta instanceof Damageable) {
			Damageable newMeta = (Damageable)meta;
			newMeta.setDamage(newMeta.getDamage() + surroundingBlocks.size());
			mainHand.setItemMeta(newMeta);
			player.getInventory().setItemInMainHand(mainHand);
		}
		
		for (Block b : surroundingBlocks)
			b.breakNaturally(mainHand);
		return true;
	}

	private static List<Material> getMaterialList() {
		List<Material> materials = new LinkedList<>();
		materials.add(Material.BLACKSTONE);
		materials.add(Material.COBBLESTONE);
		materials.add(Material.END_STONE);
		materials.add(Material.MOSSY_COBBLESTONE);
		materials.add(Material.SANDSTONE);
		materials.add(Material.STONE);
		materials.add(Material.NETHERRACK);
		materials.add(Material.BASALT);
		materials.add(Material.DIORITE);
		materials.add(Material.GRANITE);
		materials.add(Material.ANDESITE);
		materials.add(Material.COBBLED_DEEPSLATE);
		materials.add(Material.DEEPSLATE);
		materials.add(Material.TUFF);
		return materials;
	}
}
