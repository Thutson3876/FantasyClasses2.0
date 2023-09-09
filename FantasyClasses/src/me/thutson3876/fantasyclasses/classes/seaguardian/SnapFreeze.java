package me.thutson3876.fantasyclasses.classes.seaguardian;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.events.CustomLivingEntityDamageEvent;
import me.thutson3876.fantasyclasses.events.DamageModifier;
import me.thutson3876.fantasyclasses.util.particles.CustomParticle;

public class SnapFreeze extends AbstractAbility {

	//Deal damage equal to target freezeAmt / 20 and remove it
	
	double dmgMod = 2.0;
	
	public SnapFreeze(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0 * 20;
		this.displayName = "Snap Freeze";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.DIAMOND_SHOVEL);	
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(CustomLivingEntityDamageEvent e) {
		if(isOnCooldown())
			return;
		
		if(!e.getDamager().equals(player))
			return;
		
		if(player.getAttackCooldown() < 1.0)
			return;
		
		if(e.isCancelled())
			return;
		
		if(e.getInitialDamage() <= 0)
			return;
		
		
		Entity victim = e.getVictim();
		int freezeTicks = victim.getFreezeTicks();
		
		if(freezeTicks <= 0)
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		double bonusDmg = dmgMod * ((double)freezeTicks) / 20.0;
		
		e.addModifier(new DamageModifier(displayName, Operation.ADD_NUMBER, bonusDmg));
		
		victim.setFreezeTicks(0);
		
		victim.getWorld().playSound(victim, Sound.BLOCK_AMETHYST_CLUSTER_BREAK, 1.3f, 1.3f);
		new CustomParticle(Particle.SNOWFLAKE, 20, victim.getWidth(), victim.getHeight(), victim.getWidth(), 1.0, Color.WHITE);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Attack a freezing entity";
	}

	@Override
	public String getDescription() {
		return "Your attacks against freezing entities consume the freeze effect on them, dealing bonus damage based on how frozen they were";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
	}


	
	
}
