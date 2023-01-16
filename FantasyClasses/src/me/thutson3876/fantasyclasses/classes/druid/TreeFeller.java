package me.thutson3876.fantasyclasses.classes.druid;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;

public class TreeFeller extends AbstractAbility implements Bindable {

	private Material type = null;

	private static final int MAXDISTANCE = 4;

	private Block startingBlock = null;

	private static final Set<Material> logMaterials = new HashSet<>(Arrays.asList(new Material[] { Material.ACACIA_LOG,
			Material.BIRCH_LOG, Material.DARK_OAK_LOG, Material.JUNGLE_LOG, Material.OAK_LOG, Material.SPRUCE_LOG, Material.MANGROVE_LOG }));

	private static final Set<Material> leafMaterials = new HashSet<>(Arrays.asList(new Material[] {
			Material.ACACIA_LEAVES, Material.AZALEA_LEAVES, Material.BIRCH_LEAVES, Material.DARK_OAK_LEAVES,
			Material.FLOWERING_AZALEA_LEAVES, Material.JUNGLE_LEAVES, Material.OAK_LEAVES, Material.SPRUCE_LEAVES, Material.MANGROVE_LEAVES }));

	public TreeFeller(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 12 * 20;
		this.displayName = "Tree Feller";
		this.skillPointCost = 2;
		this.maximumLevel = 2;

		this.createItemStack(Material.DARK_OAK_SAPLING);
	}

	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e) {
		if(!e.getPlayer().equals(player))
			return;
		
		if(isOnCooldown())
			return;
		
		if(!player.isSneaking())
			return;
		
		if(player.getInventory().getItemInMainHand() == null || !player.getInventory().getItemInMainHand().getType().equals(type))
			return;
		
		if(!logMaterials.contains(e.getBlock().getType()))
			return;
		
		this.startingBlock = e.getBlock();
		
		AbilityTriggerEvent thisEvent = this.callEvent();
	    breakTree(this.startingBlock);
	    this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());

	}
	
	@Override
	public String getInstructions() {
		return "Chop down a tree with the bound item type while crouching";
	}

	@Override
	public String getDescription() {
		return "You gain the ability to fell a tree in a single strike. This ability has a cooldown of &6"
				+ this.coolDowninTicks / 20 + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.coolDowninTicks = (18 - 8 * this.currentLevel) * 20;
	}

	@Override
	public Material getBoundType() {
		return type;
	}

	@Override
	public void setBoundType(Material type) {
		this.type = type;
	}

	private void breakTree(Block tree) {
		if (Math.abs(this.startingBlock.getX() - tree.getX()) > MAXDISTANCE
				|| Math.abs(this.startingBlock.getZ() - tree.getZ()) > MAXDISTANCE)
			return;
		if (!logMaterials.contains(tree.getType()) && !leafMaterials.contains(tree.getType()))
			return;
		tree.breakNaturally(player.getInventory().getItemInMainHand());
		Bukkit.getPluginManager().callEvent((Event)new BlockBreakEvent(tree, player));
		
		byte b;
		int i;
		BlockFace[] arrayOfBlockFace;
		for (i = (arrayOfBlockFace = BlockFace.values()).length, b = 0; b < i;) {
			BlockFace face = arrayOfBlockFace[b];
			breakTree(tree.getRelative(face));
			breakTree(tree.getRelative(face).getRelative(BlockFace.UP));
			b++;
		}
	}
}
