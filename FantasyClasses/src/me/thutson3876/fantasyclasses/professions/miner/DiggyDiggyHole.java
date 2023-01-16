package me.thutson3876.fantasyclasses.professions.miner;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;

public class DiggyDiggyHole extends AbstractAbility implements Bindable {

	private int duration = 4 * 20;
	private Material type = null;
	private PotionEffect haste;
	
	public DiggyDiggyHole(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 20 * 20;
		this.displayName = "Diggy Diggy Hole";
		this.skillPointCost = 1;
		this.maximumLevel = 2;
		haste = new PotionEffect(PotionEffectType.FAST_DIGGING, duration, 1);

		this.createItemStack(Material.CRACKED_STONE_BRICKS);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if(isOnCooldown())
			return;
		
		if(!e.getPlayer().equals(player))
			return;
		
		if(e.getItem() == null)
			return;
		
		if(!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;
		
		if(!e.getItem().getType().equals(type))
			return;
		
		player.addPotionEffect(haste);
		
		this.onTrigger(true);
	}

	@Override
	public String getInstructions() {
		return "Right-click with bound item type";
	}

	@Override
	public String getDescription() {
		return "Gain a burst of speed and dig faster for &6" + (duration / 20) + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		duration = (4 * currentLevel) * 20;
		haste = new PotionEffect(PotionEffectType.FAST_DIGGING, duration, currentLevel);
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
