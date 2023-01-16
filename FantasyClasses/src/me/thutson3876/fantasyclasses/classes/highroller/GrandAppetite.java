package me.thutson3876.fantasyclasses.classes.highroller;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;

public class GrandAppetite extends AbstractAbility {

	private final Random rng = new Random();
	private int dc = 19;
	
	public GrandAppetite(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 16 * 20;
		this.displayName = "Grand Appetite";
		this.skillPointCost = 1;
		this.maximumLevel = 3;

		this.createItemStack(Material.COOKED_BEEF);
	}

	@EventHandler
	public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
		if(!e.getPlayer().equals(player))
			return;
		
		if(isOnCooldown())
			return;
		
		int roll = rng.nextInt(20);
		if (roll >= dc) {
			AbilityTriggerEvent thisEvent = this.callEvent();
			player.setExhaustion(0);
			player.setSaturation(player.getSaturation() + 12);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1.2F, 0.8F);
			this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
		} else if (roll == 0) {
			AbilityTriggerEvent thisEvent = this.callEvent();
			e.setCancelled(true);
			player.damage(3.0);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_COW_HURT, 1.2F, 0.8F);
			this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());

		}
	}

	@Override
	public String getInstructions() {
		return "Eat food";
	}

	@Override
	public String getDescription() {
		return "Roll a d20 whenever eat food. On a roll above a &6" 
				+ this.dc + "&r you feel extra replenished. On a natural one, choke on your food instead";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		dc = 20 - currentLevel;
	}

}
