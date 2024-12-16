package me.thutson3876.fantasyclasses.classes.druid;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class SurvivalInstincts extends AbstractAbility {

	private double dmgReduction = 0.8;
	private int duration = 5 * 20;
	private PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, duration, 1);
	private PotionEffect strength = new PotionEffect(PotionEffectType.STRENGTH, duration, 1);
	private PotionEffect haste = new PotionEffect(PotionEffectType.HASTE, duration, 1);
	private PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION, duration, 1);
	
	public SurvivalInstincts(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30 * 20;
		this.displayName = "Survival Instincts";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.SKELETON_SKULL);
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(!e.getEntity().equals(player))
			return;
		
		if(isOnCooldown())
			return;
		
		double health = player.getHealth();
		
		if(e.getFinalDamage() >= health)
			return;
		
		if(health - e.getFinalDamage() > AbilityUtils.getMaxHealth(player) * 0.3)
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		if(thisEvent.isCancelled())
			return;
		
		e.setDamage(e.getDamage() * (1.0 - dmgReduction));
		
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.2f, 1.0f);
		player.getWorld().spawnParticle(Particle.FALLING_LAVA, player.getLocation(), 4);
		
		player.addPotionEffect(speed);
		player.addPotionEffect(strength);
		player.addPotionEffect(haste);
		player.addPotionEffect(regen);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Take damage at 30% health";
	}

	@Override
	public String getDescription() {
		return "Upon being put to less than 30% of your max health, reduce the amount taken by &6" + AbilityUtils.doubleRoundToXDecimals(dmgReduction * 100, 1) + "% &rgain &dspeed&r, &dstrength&r, and &dhaste &rfor &6" + (duration / 20) + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
	}

}
