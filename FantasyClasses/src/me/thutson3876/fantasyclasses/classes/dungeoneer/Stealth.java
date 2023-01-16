package me.thutson3876.fantasyclasses.classes.dungeoneer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;

public class Stealth extends AbstractAbility implements Bindable {

	private Material boundType = null;

	private PotionEffect invis;
	private PotionEffect speed;
	private PotionEffect jump;

	private int duration = 30 * 20;

	private boolean isOn = false;

	private BukkitRunnable task;

	public Stealth(Player p) {
		super(p);
	}

	@Override
	public void deInit() {
		resetTask();
		if(isOn)
			task.cancel();
		
		task.run();
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 24 * 20;
		this.displayName = "Stealth";
		this.skillPointCost = 3;
		this.maximumLevel = 1;

		this.createItemStack(Material.ENDER_PEARL);
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		if ((e.getEntity().equals(player) || e.getDamager().equals(player)) && isOn) {
			task.cancel();
			task.run();
		}
	}
	
	@EventHandler
	public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {
		if (isOnCooldown())
			return;

		boolean correctType = false;

		if (e.getMainHandItem() != null) {
			if (e.getMainHandItem().getType().equals(boundType))
				correctType = true;
		}
		if (e.getOffHandItem() != null) {
			if (e.getOffHandItem().getType().equals(boundType))
				correctType = true;
		}

		if (!correctType)
			return;

		if(isOn)
			task.cancel();
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		resetTask();
		isOn = true;
		task.runTaskLater(plugin, duration);
		addPotionEffects();
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	private void addPotionEffects() {
		for (Player p : Bukkit.getOnlinePlayers())
			p.hidePlayer(plugin, player);

		System.out.println(String.valueOf(player.getDisplayName()) + " is invisible");
		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 0.2f);
		player.addPotionEffect(invis);
		player.addPotionEffect(speed);
		player.addPotionEffect(jump);
	}

	private void resetTask() {
		this.task = new BukkitRunnable() {
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers())
					player.showPlayer(plugin, player);
				System.out.println(String.valueOf(player.getDisplayName()) + " is no longer invisible");
				player.removePotionEffect(Stealth.this.invis.getType());
				player.removePotionEffect(Stealth.this.speed.getType());
				player.removePotionEffect(Stealth.this.jump.getType());
				isOn = false;
			}
		};
	}

	@Override
	public String getInstructions() {
		return "Swap hands with the bound item type";
	}

	@Override
	public String getDescription() {
		return "Turn completely invisible and gain a burst of speed and jump height for &6" + (duration / 20)
				+ " &rseconds. Dealing or taking damage breaks this effect";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
	}

	@Override
	public Material getBoundType() {
		return this.boundType;
	}

	@Override
	public void setBoundType(Material type) {
		this.boundType = type;
	}

}
