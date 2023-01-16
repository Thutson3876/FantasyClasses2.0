package me.thutson3876.fantasyclasses.classes.druid;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class NaturesBlessing extends AbstractAbility {
	
	private BukkitTask task = null;
	private BukkitRunnable runnable = null;
	
	public NaturesBlessing(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 10 * 20;
		this.displayName = "Nature's Blessing";
		this.skillPointCost = 1;
		this.maximumLevel = 6;

		this.createItemStack(Material.BIG_DRIPLEAF);
	}

	@Override
	public String getInstructions() {
		return "Passive";
	}

	@Override
	public String getDescription() {
		return "Heals you for half a heart every &6" + (coolDowninTicks / 20) + " &rseconds. If you are at full health, an injured nearby pet will be healed instead";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		coolDowninTicks = (11 - currentLevel) * 20;
		if(this.currentLevel <= 0 && task != null) {
			if((Bukkit.getScheduler().isCurrentlyRunning(task.getTaskId()) || Bukkit.getScheduler().isQueued(task.getTaskId())))
				task.cancel();
			
			return;
		}
		
		if(task == null) {
			this.runnable = new BukkitRunnable() {

				@Override
				public void run() {
					if(player.getHealth() >= player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
						for(LivingEntity e : AbilityUtils.getNearbyPlayerPets(player, 15.0)) {
							if(e.getHealth() >= e.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
								continue;

							else {
								AbilityUtils.heal(player, 1.0, e);
								return;
							}
						}
					}
					else {
						AbilityUtils.heal(player, 1.0, player);
						return;
					}
				}
			};
			
			task = runnable.runTaskTimer(plugin, coolDowninTicks, coolDowninTicks);
		}
		else if((Bukkit.getScheduler().isCurrentlyRunning(task.getTaskId()) || Bukkit.getScheduler().isQueued(task.getTaskId()))) {
			task.cancel();
			
			this.runnable = new BukkitRunnable() {

				@Override
				public void run() {
					if(player.getHealth() >= player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
						for(LivingEntity e : AbilityUtils.getNearbyPlayerPets(player, 15.0)) {
							if(e.getHealth() >= e.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
								continue;

							else {
								AbilityUtils.heal(player, 1.0, e);
								return;
							}
						}
					}
					else {
						AbilityUtils.heal(player, 1.0, player);
						return;
					}
				}
			};
			
			task = runnable.runTaskTimer(plugin, coolDowninTicks, coolDowninTicks);
		}
			
	}

}
