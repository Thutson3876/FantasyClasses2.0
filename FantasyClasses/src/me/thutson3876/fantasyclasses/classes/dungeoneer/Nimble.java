package me.thutson3876.fantasyclasses.classes.dungeoneer;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;

public class Nimble extends AbstractAbility {

	private Random rng = new Random();
	private double dodgeChance = 0.02;
	
	public Nimble(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 20;
		this.displayName = "Nimble Movement";
		this.skillPointCost = 1;
		this.maximumLevel = 6;

		this.createItemStack(Material.FEATHER);
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(!e.getEntity().equals(player))
			return;
		
		if(player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
			if(rng.nextDouble() < dodgeChance) {
				e.setCancelled(true);
				
				AbilityTriggerEvent thisEvent = this.callEvent();
				
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SHULKER_HURT_CLOSED, 1.0f, 1.3f);
				if(e.getDamager() instanceof Player)
					((Player)e.getDamager()).sendMessage(ChatUtils.chat("&4" + player.getDisplayName() + " &6dodged your attack!"));
				
				
				this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
			}
		}
	}

	@Override
	public String getInstructions() {
		return "Take damage while invisible";
	}

	@Override
	public String getDescription() {
		return "While you are invisible, you have a &6" + AbilityUtils.doubleRoundToXDecimals(dodgeChance * 100, 2) + "% &rchance to dodge an attack and take no damage from it";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		dodgeChance = 0.02 * currentLevel;
	}

}
