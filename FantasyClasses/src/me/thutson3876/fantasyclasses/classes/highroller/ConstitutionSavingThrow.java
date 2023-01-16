package me.thutson3876.fantasyclasses.classes.highroller;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;

public class ConstitutionSavingThrow extends AbstractAbility {

	private final Random rng = new Random();
	private int dc = 19;
	
	public ConstitutionSavingThrow(Player p) {
		super(p, Priority.LOW);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Constitution Saving Throw";
		this.skillPointCost = 1;
		this.maximumLevel = 3;

		this.createItemStack(Material.GHAST_TEAR);
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getEntity().equals(player))
			return;
		
		if(isOnCooldown())
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		int roll = rng.nextInt(20);
		World world = player.getWorld();
		if (roll >= dc) {
			e.setDamage(e.getDamage() / 2);
			
			world.playSound(player, Sound.BLOCK_CAKE_ADD_CANDLE, 5.0f, 1.0f);
		}
		else if(roll == 0) {
			e.setDamage(e.getDamage() * 2.0);
			
			world.playSound(player, Sound.BLOCK_GLASS_BREAK, 2.0f, 1.0f);
		}
		
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());

	}

	@Override
	public String getInstructions() {
		return "Take damage";
	}

	@Override
	public String getDescription() {
		return "Roll a d20 when you take damage. On a roll above a &6" + this.dc + "&r take 50% reduced damage. On a natural one, take double instead";
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
