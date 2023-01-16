package me.thutson3876.fantasyclasses.classes.seaguardian;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;

public class GlacialSmite extends AbstractAbility {

	private int freezeAmt = 20;
	private int duration = 2 * 15;
	private int amp = 0;
	private PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, duration, amp);
	private PotionEffect fatigue = new PotionEffect(PotionEffectType.SLOW_DIGGING, duration, amp);
	
	public GlacialSmite(Player p) {
		super(p, Priority.HIGH);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0 * 20;
		this.displayName = "Glacial Smite";
		this.skillPointCost = 1;
		this.maximumLevel = 6;

		this.createItemStack(Material.ICE);	
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(isOnCooldown())
			return;
		
		if(!e.getDamager().equals(player))
			return;
		
		if(player.getAttackCooldown() < 1.0)
			return;
		
		Entity victim = e.getEntity();
		victim.setFreezeTicks(victim.getFreezeTicks() + freezeAmt);
		
		if(victim instanceof LivingEntity)
			applyPotionEffects((LivingEntity)victim);
		
		this.onTrigger(true);
	}

	@Override
	public String getInstructions() {
		return "Attack an entity";
	}

	@Override
	public String getDescription() {
		return "Your attacks chill your targets applying freeze, slowness, and mining fatigue for &6" + duration / 20 + " &r seconds.";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		duration = (currentLevel) * 10;
		
		freezeAmt = (20 * currentLevel);
		
		slow = new PotionEffect(PotionEffectType.SLOW, duration, amp);
		fatigue = new PotionEffect(PotionEffectType.SLOW_DIGGING, duration, amp);
	}

	private void applyPotionEffects(LivingEntity ent) {
		if(ent.isDead())
			return;
		
		ent.addPotionEffect(slow);
		ent.addPotionEffect(fatigue);
	}
	
}
