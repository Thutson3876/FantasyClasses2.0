package me.thutson3876.fantasyclasses.classes.combat;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.MaterialLists;

public class Reaper extends AbstractAbility {

	private static final double DAMAGE_MOD_PER_LEVEL = 0.05;
	private static final double HEALTH_THRESHOLD = 0.25;
	
	private double damageMod = DAMAGE_MOD_PER_LEVEL;
	
	public Reaper(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Reaper";
		this.skillPointCost = 1;
		this.maximumLevel = 6;
		
		this.createItemStack(Material.WITHER_SKELETON_SKULL);
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageByEntityEvent e) {
		if(this.isOnCooldown())
			return;
		
		if(e.isCancelled())
			return;
		
		if(!e.getDamager().equals(this.player))
			return;

		if(player.getAttackCooldown() < 1.0)
			return;
		
		if(!(e.getEntity() instanceof LivingEntity))
			return;
		
		LivingEntity ent = (LivingEntity) e.getEntity();
		
		if(!e.getCause().equals(DamageCause.ENTITY_ATTACK))
			return;
		
		if(!(MaterialLists.HOE.getMaterials().contains(player.getInventory().getItemInMainHand().getType())))
			return;
		
		if(ent.getHealth() > ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.25)
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		e.setDamage(e.getDamage() * (1.0 + damageMod));
		
		ent.getWorld().playSound(ent, Sound.ENTITY_WITHER_AMBIENT, 1.0f, 0.7f);
		ent.getWorld().spawnParticle(Particle.SOUL, ent.getEyeLocation(), 5 * currentLevel);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}
	
	
	@Override
	public String getInstructions() {
		return "Damage a low health target";
	}

	@Override
	public String getDescription() {
		return "Deal &6" + AbilityUtils.doubleRoundToXDecimals(damageMod * 100.0, 2) + "% &rmore damage to targets under &6" + AbilityUtils.doubleRoundToXDecimals(HEALTH_THRESHOLD * 100.0, 2) + "% &rhealth";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		this.damageMod = DAMAGE_MOD_PER_LEVEL * currentLevel;
	}

}
