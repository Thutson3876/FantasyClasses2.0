package me.thutson3876.fantasyclasses.classes.combat;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Parry extends AbstractAbility {

	private int duration = 2;
	private Entity target = null;
	
	public Parry(Player p) {
		super(p, Priority.HIGHEST);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 20;
		this.displayName = "Parry";
		this.skillPointCost = 3;
		this.maximumLevel = 1;
		
		this.createItemStack(Material.GOLDEN_SWORD);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		if(e.getDamager().equals(this.player)) {
			if(isOnCooldown())
				return;
			
			if(player.getAttackCooldown() < 1.0)
				return;
			
			BukkitRunnable task = new BukkitRunnable() {

				@Override
				public void run() {
					target = null;
				}
				
			};
			
			AbilityTriggerEvent thisEvent = this.callEvent();
			target = e.getEntity();
			task.runTaskLater(plugin, duration);
			
			this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
			return;
		}
		else if(e.getEntity().equals(this.player)) {
			if(target == null)
				return;
			
			if(!e.getDamager().equals(target))
				return;
			
			List<Entity> entitiesInSight = AbilityUtils.getEntitiesInAngle(player, 1.4, 6.0);
			if(!entitiesInSight.contains(e.getDamager()))
				return;
			
			e.setDamage(0);
			e.setCancelled(true);
			
			player.getWorld().playSound(player, Sound.BLOCK_ANVIL_FALL, 4.5f, 1.6f);
			
			if(e.getDamager() instanceof Player) {
				((Player)e.getDamager()).sendMessage("&7Your attack was &6PARRIED");
			}
			
		}
	}

	@Override
	public String getInstructions() {
		return "Attack an entity as they are attacking you";
	}

	@Override
	public String getDescription() {
		return "Nullifies damage taken if you attack at the same time";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
	}

}
