package me.thutson3876.fantasyclasses.professions.enchanter;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class Extraction extends AbstractAbility {
	
	//private double maxDistance = 5;
	//private int maxExpAmt = 160;
	
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
	
	/*@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if(!e.getPlayer().equals(player))
			return;
		
		if(e.getItem() == null || !e.getItem().getType().equals(Material.GLASS_BOTTLE))
			return;
		
		if(isOnCooldown())
			return;
		
		Player target = null;
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Entity rayTraceTarget = AbilityUtils.rayTraceTarget(player, maxDistance);
			
			if(rayTraceTarget instanceof Player)
				target = (Player) rayTraceTarget;

		}
		else {
			return;
		}
		
		if(target == null || !target.isSneaking()) {
			plugin.log("Extraction target null or not sneaking!");
			return;
		}
			
		
		if(target.getLocation().getBlock().getType() != Material.CAULDRON) {
			plugin.log("Extraction Target Block: " + target.getLocation().getBlock().getType());
			return;
		}
		
		ItemStack bottle = new ItemStack(Material.EXPERIENCE_BOTTLE);
		
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0f, 1.15f);
		
		player.getInventory().addItem(bottle);
		
		target.setLevel(target.getLevel() - 1);
		
		int replacementAmt = e.getItem().getAmount() - 1;
		ItemStack replacement = new ItemStack(Material.GLASS_BOTTLE, replacementAmt);
		if(replacementAmt <= 0)
			replacement = null;
		
		player.getInventory().setItem(e.getHand(), replacement);
		
		this.onTrigger(true);
	}*/

	@Override
	public String getInstructions() {
		return "Right-click a player while they are crouching in a cauldron";
	}

	@Override
	public String getDescription() {
		return "&4ERROR: NOT WORKING CURRENTLY \n&rStore up to experience from a player into an experience bottle";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		
	}

}
