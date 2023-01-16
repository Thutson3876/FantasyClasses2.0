package me.thutson3876.fantasyclasses.classes.witch;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;

public class FireballWand extends AbstractAbility implements Bindable {

	private Material boundType = null;
	private double velocity = 1.12;
	private float yield = 0.7f;
	
	public FireballWand(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 8 * 20;
		this.displayName = "Fireball Wand";
		this.skillPointCost = 1;
		this.maximumLevel = 5;

		this.createItemStack(Material.BLAZE_ROD);	
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if(!e.getPlayer().equals(player))
			return;
		
		if(e.getItem() == null || !e.getItem().getType().equals(this.boundType))
			return;
		
		if(!e.getAction().equals(Action.RIGHT_CLICK_AIR))
			return;
		
		if(isOnCooldown())
			return;
		
		launchFireball();
		
		this.onTrigger(true);
	}

	@Override
	public String getInstructions() {
		return "Right-click with bound item type";
	}

	@Override
	public String getDescription() {
		return "Launch a fireball that explodes with a radius of &6" + yield + " &rmeters";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		yield = (0.5f * currentLevel);
		velocity = 1.0 + 0.12 * currentLevel;
	}

	@Override
	public Material getBoundType() {
		return boundType;
	}

	@Override
	public void setBoundType(Material type) {
		boundType = type;
	}
	
	private void launchFireball() {
		Location spawnAt = player.getEyeLocation().toVector().add(player.getEyeLocation().getDirection()).toLocation(player.getWorld());
		
		Fireball f = (Fireball)player.getWorld().spawnEntity(spawnAt, EntityType.FIREBALL);
		f.setShooter(player);
		f.setYield(yield);
		f.setDirection(player.getEyeLocation().getDirection().multiply(velocity));
		
		player.getWorld().playSound(spawnAt, Sound.ITEM_FIRECHARGE_USE, 1.0f, 1F);
	}
}
