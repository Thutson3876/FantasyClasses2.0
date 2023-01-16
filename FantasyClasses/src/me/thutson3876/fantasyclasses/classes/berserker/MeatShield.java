package me.thutson3876.fantasyclasses.classes.berserker;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class MeatShield extends AbstractAbility {

	private Random rng = new Random();
	private double procChance = 0.1;
	private int maxAmp = 4;
	private int maxDuration = 20 * 20;
	private PotionEffect absorb = new PotionEffect(PotionEffectType.ABSORPTION, 5 * 20, 0);
	
	public MeatShield(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Meat Shield";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.BEEF);
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		if(this.isOnCooldown())
			return;
		
		if(rng.nextDouble() > this.procChance)
			return;
		
		if(!e.getDamager().equals(this.player))
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		AbilityUtils.applyStackingPotionEffect(absorb, player, maxAmp, maxDuration);
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}
	
	@Override
	public String getInstructions() {
		return "Deal damage to an entity";
	}

	@Override
	public String getDescription() {
		return "When you deal damage, you have a &6" + AbilityUtils.doubleRoundToXDecimals(procChance * 100, 1) + "% &rchance to gain Absorption hearts. This effect may stack";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.procChance = 0.1 * this.currentLevel;
	}

}
