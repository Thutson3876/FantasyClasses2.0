package me.thutson3876.fantasyclasses.classes.druid;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class BirdSinger extends AbstractAbility implements Bindable {
	
	private Material type = null;
	private double range = 10.0;
	
	public BirdSinger(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Bird Singer";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.WHEAT_SEEDS);
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		if(isOnCooldown())
			return;
		
		if(!e.getDamager().equals(player))
			return;
		
		if (!(player.getInventory().getItemInOffHand().getType().equals(type) || player.getInventory().getItemInMainHand().getType().equals(type)))
				return;
		
		List<Entity> parrots = new ArrayList<>();
		for(Entity ent : player.getNearbyEntities(range, range, range)){
			if(ent instanceof Parrot) {
				if(player.equals(((Parrot)ent).getOwner())) {
					ent.getWorld().playSound(ent.getLocation(), Sound.ENTITY_PARROT_IMITATE_EVOKER, 2.0f, 1.3f);
					parrots.add(ent);
				}
			}
		}
		
		if(parrots.isEmpty())
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		Entity target = e.getEntity();
		World world = target.getWorld();
		
		new BukkitRunnable() {

			@Override
			public void run() {
				Location targetLoc = target.getLocation().add(0, target.getHeight() / 1.8, 0);
				for(Entity parrot : parrots) {
					Arrow arrow = world.spawnArrow(parrot.getLocation().add(0, parrot.getHeight() + 0.05, 0), AbilityUtils.getVectorBetween2Points(parrot.getLocation(), targetLoc, 0.3), 1.6f, 5);
					arrow.setShooter(player);
					arrow.setDamage(5.0);
					arrow.setCritical(true);
					arrow.setPickupStatus(PickupStatus.CREATIVE_ONLY);
				}
			}
			
		}.runTaskLater(plugin, 10);
		
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Attack an entity with tamed parrots near you";
	}

	@Override
	public String getDescription() {
		return "When you strike an entity while holding the bound item type, your parrots each throw a sharp stick they found at the target";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		
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
