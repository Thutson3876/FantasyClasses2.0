package me.thutson3876.fantasyclasses.professions.fisherman;

import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;

public class Turboat extends AbstractAbility implements Bindable {

	private Material type = null;
	
	private double speed = 2.0;
	
	public Turboat(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 8 * 20;
		this.displayName = "Turboat";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.DARK_OAK_BOAT);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if (isOnCooldown())
			return;

		if (!e.getPlayer().equals(player))
			return;

		if (!(e.getAction() == Action.RIGHT_CLICK_AIR))
			return;
		
		Entity vehicle = this.player.getVehicle();
		if(!(vehicle instanceof Boat))
			return;
		
		Boat boat = (Boat) vehicle;
		
		if (e.getItem() == null || !e.getItem().getType().equals(this.type))
			return;
	
		Vector newVelocity = player.getEyeLocation().getDirection().normalize();
		boat.setVelocity(newVelocity.multiply(speed));
		
		this.onTrigger(true);
	}
	
	@Override
	public String getInstructions() {
		return "Right-click bound item type";
	}

	@Override
	public String getDescription() {
		return "Boost your boat in the direction you're facing. This has a cooldown of &6" + this.coolDowninTicks / 20 + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.coolDowninTicks = (12 - 4 * this.currentLevel) * 20;
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
