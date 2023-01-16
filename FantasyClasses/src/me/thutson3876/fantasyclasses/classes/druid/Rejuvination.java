package me.thutson3876.fantasyclasses.classes.druid;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.Particles;

public class Rejuvination extends AbstractAbility implements Bindable {

	private Material boundType = null;
	private double healAmt = 1.0;
	private int duration = 6 * 20;
	private PotionEffect regen = null;
	private double maxDistance = 8.0;
	
	public Rejuvination(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 10 * 20;
		this.displayName = "Rejuvination";
		this.skillPointCost = 1;
		this.maximumLevel = 6;

		this.createItemStack(Material.SMALL_DRIPLEAF);		
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
			else
				target = player;
		}
		else if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			target = player;
		}
		else {
			return;
		}
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		AbilityUtils.heal(this.player, healAmt, target);
		Particles.helix(target, Particle.COMPOSTER, target.getWidth(), 2 * 6.3, 2, 0.1);
		if(regen != null)
			target.addPotionEffect(regen);
			
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Right-click with your bound item type";
	}

	@Override
	public String getDescription() {
		return "Restore &6" + healAmt + " &rhealth to your target. Targeting the ground heals yourself instead. At max level, also applies regeneration";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		healAmt = 1.0 * currentLevel;
		if(currentLevel >= maximumLevel)
			regen = new PotionEffect(PotionEffectType.REGENERATION, duration, 0);
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
