package me.thutson3876.fantasyclasses.classes.witch;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.particles.CustomParticle;
import me.thutson3876.fantasyclasses.util.particles.GeneralParticleEffects;

public class MistyStep extends AbstractAbility implements Bindable {

	private Material boundType = null;
	private double maxDistance = 12.0;
	
	public MistyStep(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 13 * 20;
		this.displayName = "Misty Step";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.ENDER_PEARL);	
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if(!e.getPlayer().equals(player))
			return;
		
		if(e.getItem() == null || !e.getItem().getType().equals(this.boundType))
			return;
		
		if(isOnCooldown())
			return;
		
		LivingEntity target = null;
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			Entity rayTraceTarget = AbilityUtils.rayTraceTarget(player, maxDistance);
			
			if(rayTraceTarget instanceof LivingEntity)
				target = (LivingEntity) rayTraceTarget;
		}
		else if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

		}
		else {
			return;
		}
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		
		if(target != null) {
			GeneralParticleEffects.helix(target, new CustomParticle(Particle.END_ROD, 2, 0, 0, 0, 0, null), target.getWidth(), 2 * 6.3, 30, 2, 0.1);
			
			player.teleport(target);
			
			this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
		}
		
	}

	@Override
	public String getInstructions() {
		return "Right-click with your bound item type, while aiming at another player";
	}

	@Override
	public String getDescription() {
		return "Teleport to a nearby player. This ability has a cooldown of &6" + coolDowninTicks + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.coolDowninTicks = 21 - 8 * currentLevel;
	}

	@Override
	public Material getBoundType() {
		return boundType;
	}

	@Override
	public void setBoundType(Material type) {
		boundType = type;
	}
	
}
