package me.thutson3876.fantasyclasses.professions.miner;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.classes.dungeoneer.Shadowmeld;

public class DungeonDelver extends AbstractAbility {

	boolean isDisabled = false;

	private BukkitTask task = null;
	
	private int maxLightLevel = 8;

	private List<PotionEffect> effects = new ArrayList<>();

	public DungeonDelver(Player p) {
		super(p);

		effects.add(new PotionEffect(PotionEffectType.SPEED, 40, 0));
		effects.add(new PotionEffect(PotionEffectType.NIGHT_VISION, 240, 0));
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 20;
		this.displayName = "Dungeon Delver";
		this.skillPointCost = 3;
		this.maximumLevel = 1;

		this.createItemStack(Material.ENDER_EYE);
	}

	@Override
	public void deInit() {
		isDisabled = true;
		if(task != null && (Bukkit.getScheduler().isQueued(task.getTaskId()) || Bukkit.getScheduler().isCurrentlyRunning(task.getTaskId())))
			task.cancel();
	}

	@Override
	public String getInstructions() {
		return "Be underground in a light level below " + (maxLightLevel + 1);
	}

	@Override
	public String getDescription() {
		return "While in darkness underground, gain darkvision, speed, and strength per level respectively";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		deInit();

		if(task != null && (Bukkit.getScheduler().isQueued(task.getTaskId()) || Bukkit.getScheduler().isCurrentlyRunning(task.getTaskId())))
			task.cancel();
		task = new BukkitRunnable() {
			public void run() {
				isDisabled = false;
				
				new BukkitRunnable() {

					public void run() {
						if (player == null)
							deInit();

						if (isDisabled) {
							this.cancel();
							return;
						}

						if (player.isDead())
							return;

						Location loc = player.getLocation();
						if (loc.getBlock().getLightLevel() > maxLightLevel
								|| loc.getWorld().getHighestBlockAt(loc).getY() < loc.getY() || loc.getWorld().getEnvironment().equals(Environment.NETHER)) {
							player.removePotionEffect(PotionEffectType.NIGHT_VISION);
							return;
						}
						applyPotionEffects();
					}

				}.runTaskTimer(plugin, coolDowninTicks, coolDowninTicks);
			}
		}.runTaskLater(plugin, 30);

	}

	private void applyPotionEffects() {
		List<Ability> abils = getFantasyPlayer().getClassAbilities();
		for (int i = 0; i < abils.size(); i++) {
			if (abils.get(i).getName().equalsIgnoreCase("shadowmeld")) {
				for (PotionEffectType type : Shadowmeld.getPotionEffects()) {
					player.addPotionEffect(new PotionEffect(type, 40, abils.get(i).getCurrentLevel() - 1));
				}
				break;
			}
		}
		player.addPotionEffects(effects);
	}

}
