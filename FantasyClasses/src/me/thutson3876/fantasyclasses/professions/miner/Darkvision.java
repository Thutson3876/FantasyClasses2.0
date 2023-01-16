package me.thutson3876.fantasyclasses.professions.miner;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;

public class Darkvision extends AbstractAbility implements Bindable {

	private boolean hasSpeed = false;
	private int duration = 8 * 20;
	private Material type = null;
	private PotionEffect vision;
	private PotionEffect speed;
	
	public Darkvision(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 20 * 20;
		this.displayName = "Darkvision";
		this.skillPointCost = 1;
		this.maximumLevel = 2;
		vision = new PotionEffect(PotionEffectType.NIGHT_VISION, duration, 1);
		speed = new PotionEffect(PotionEffectType.SPEED, duration, 0);

		this.createItemStack(Material.ENDER_EYE);
	}

	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		if(isOnCooldown())
			return;
		
		if(!e.getPlayer().equals(player))
			return;
		
		if(!e.getItemDrop().getItemStack().getType().equals(type))
			return;
		
		e.setCancelled(true);
		
		if(hasSpeed)
			player.addPotionEffect(speed);
		
		player.addPotionEffect(vision);
		
		this.onTrigger(true);
	}
	
	@Override
	public String getInstructions() {
		return "Drop bound item type";
	}

	@Override
	public String getDescription() {
		return "Gain Night Vision for &6" + this.duration / 20 + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.duration = 8 * this.currentLevel * 20;
		
		this.vision = new PotionEffect(PotionEffectType.NIGHT_VISION, duration, 1);
		this.speed = new PotionEffect(PotionEffectType.SPEED, duration, 0);
	}

	@Override
	public Material getBoundType() {
		return type;
	}

	@Override
	public void setBoundType(Material type) {
		this.type = type;
	}
	
	public void setHasSpeed(boolean bool) {
		this.hasSpeed = bool;
	}

}
