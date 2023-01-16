package me.thutson3876.fantasyclasses.classes.combat;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;

public class Rage extends AbstractAbility {

	private int duration = 8 * 20;
	private PotionEffect absorb;
	private PotionEffect resist;
	private PotionEffect strength;
	
	public Rage(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 18 * 20;
		this.displayName = "Rage";
		this.skillPointCost = 2;
		this.maximumLevel = 3;
		
		this.createItemStack(Material.CRACKED_DEEPSLATE_BRICKS);
	}
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageByEntityEvent e) {
		if(this.isOnCooldown())
			return;
		
		if(e.isCancelled())
			return;
		
		if(!e.getEntity().equals(this.player))
			return;
		
		if(player.getHealth() > player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.7) {
			return;
		}
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		player.addPotionEffect(absorb);
		if(currentLevel > 1)
			player.addPotionEffect(resist);
		if(currentLevel > 2)
			player.addPotionEffect(strength);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Take damage from an entity while under 70% health";
	}

	@Override
	public String getDescription() {
		return "Become enraged after receiving damage and gain a potion effect per level. 1: Absorption 2: Resistance 3: Strength";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.absorb = new PotionEffect(PotionEffectType.ABSORPTION, duration, 0);
		this.resist = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, 0);
		this.strength = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, duration, 0);
	}

}
