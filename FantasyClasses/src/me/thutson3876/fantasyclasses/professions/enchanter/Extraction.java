package me.thutson3876.fantasyclasses.professions.enchanter;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Extraction extends AbstractAbility {
	
	private double maxDistance = 5;
	private int maxExpAmt = 160;
	
	public Extraction(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Extraction";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.EXPERIENCE_BOTTLE);
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if(!e.getPlayer().equals(player))
			return;
		
		if(e.getItem() == null || !e.getItem().getType().equals(Material.GLASS_BOTTLE))
			return;
		
		if(isOnCooldown())
			return;
		
		Player target = null;
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			Entity rayTraceTarget = AbilityUtils.rayTraceTarget(player, maxDistance);
			
			if(rayTraceTarget instanceof Player)
				target = (Player) rayTraceTarget;
		}
		else {
			return;
		}
		
		if(target == null || !target.isSneaking())
			return;
		
		if(target.getLocation().getBlock().getType() != Material.CAULDRON)
			return;
		
		int amtToStore = Math.min(target.getTotalExperience(), maxExpAmt);
		
		ItemStack bottle = new ItemStack(Material.EXPERIENCE_BOTTLE);
		ItemMeta meta = bottle.getItemMeta();
		
		List<String> lore = new ArrayList<String>();
		lore.add("XP: " + amtToStore);
		meta.setLore(lore);
		
		bottle.setItemMeta(meta);
		
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0f, 1.15f);
		
		player.getInventory().addItem(bottle);
		
		target.setTotalExperience(target.getTotalExperience() - amtToStore);
		
		int replacementAmt = e.getItem().getAmount() - 1;
		ItemStack replacement = new ItemStack(Material.GLASS_BOTTLE, replacementAmt);
		if(replacementAmt <= 0)
			replacement = null;
		
		player.getInventory().setItem(e.getHand(), replacement);
		
		this.onTrigger(true);
	}

	@Override
	public String getInstructions() {
		return "Right-click a player while they are crouching in a cauldron";
	}

	@Override
	public String getDescription() {
		return "Store up to &6" + maxExpAmt + " &rexperience from a player into an experience bottle";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		
	}

}
