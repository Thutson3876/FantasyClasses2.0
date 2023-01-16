package me.thutson3876.fantasyclasses.classes.berserker;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class IgnorePain extends AbstractAbility implements Bindable {

	private Material type = null;
	
	private boolean isOn = false;
	
	private int duration = 4 * 20;
	private double dmgReduction = 0.2;
	
	public IgnorePain(Player p) {
		super(p, Priority.HIGH);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 12 * 20;
		this.displayName = "Ignore Pain";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.DIAMOND_CHESTPLATE);
	}
	
	@EventHandler
	public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {
		if(e.isCancelled())
			return;
		
		if(isOnCooldown())
			return;
		
		if(!e.getPlayer().equals(player))
			return;
		
		boolean hasBoundType = false;
		if(e.getMainHandItem() != null) {
			hasBoundType = e.getMainHandItem().getType().equals(type);
		}
		if(!hasBoundType && e.getOffHandItem() != null) {
			hasBoundType = e.getOffHandItem().getType().equals(type);
		}
		if(!hasBoundType)
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		if(thisEvent.isCancelled())
			return;
		
		e.setCancelled(true);
		
		isOn = true;
		
		new BukkitRunnable() {

			@Override
			public void run() {
				player.getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1.0f, 0.9f);
				isOn = false;
			}
			
		}.runTaskLater(plugin, duration);
		
		player.getWorld().playSound(player.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1.0f, 0.9f);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
		
	}
	
	@EventHandler
	public void onDamageEvent(EntityDamageEvent e) {
		if(!e.getEntity().equals(player))
			return;
		
		if(!isOn)
			return;
		
		e.setDamage(e.getDamage() * (1 - this.dmgReduction));
	}
	
	@Override
	public String getInstructions() {
		return "Swap hands with bound item-type";
	}

	@Override
	public String getDescription() {
		return "Reduces damage taken by &6" + AbilityUtils.doubleRoundToXDecimals(dmgReduction * 100, 1) + "% &rfor " + this.duration / 20 + " seconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		dmgReduction = 0.2 * this.currentLevel;
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
