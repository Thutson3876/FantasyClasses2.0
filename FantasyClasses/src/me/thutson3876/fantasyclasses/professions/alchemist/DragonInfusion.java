package me.thutson3876.fantasyclasses.professions.alchemist;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;

public class DragonInfusion extends AbstractAbility {

	private float radius = 20.0f;
	
	public DragonInfusion(Player p) {
		super(p, Priority.LOW);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Dragonic Infusion";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.DRAGON_HEAD);
	}

	@EventHandler
	public void onLightningStrikeEvent(EntityDamageEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getEntityType().equals(EntityType.FROG))
			return;
		
		Entity victim = e.getEntity();
		if(!victim.getNearbyEntities(radius, radius, radius).contains(player))
			return;
		
		if(!victim.isDead())
			e.setDamage(100);
		
		ItemStack item = new ItemStack(Material.DRAGON_BREATH);
		Item droppedItem = (Item) victim.getWorld().spawnEntity(victim.getLocation(), EntityType.ITEM);
		
		droppedItem.setItemStack(item);
		droppedItem.setInvulnerable(true);
		
		this.onTrigger(false);
	}

	@Override
	public String getInstructions() {
		return "Strike down a frog with lightning";
	}

	@Override
	public String getDescription() {
		return "When &6Frogs &raround you are damaged by lightning, they produce &6Dragon's Breath";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {

	}

}
