package me.thutson3876.fantasyclasses.classes.druid;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.Aura;
import me.thutson3876.fantasyclasses.util.geometry.Sphere;

public class Tranquility extends AbstractAbility implements Bindable {

	private Material boundType = null;
	private double healAmt = 1.5;
	private int duration = 6 * 20;
	private double maxDistance = 8.0;
	private int tickRate = 20;
	private HealingAura aura;
	
	public Tranquility(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30 * 20;
		this.displayName = "Tranquility";
		this.skillPointCost = 1;
		this.maximumLevel = 6;
		this.duration = 6 * 20;

		this.createItemStack(Material.ENCHANTED_GOLDEN_APPLE);		
	}

	@EventHandler
	public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {
		if(isOnCooldown())
			return;
		
		if (!e.getPlayer().equals(player))
			return;
			
		boolean correctType = false;
		
		if(e.getMainHandItem() != null) {
			if(e.getMainHandItem().getType().equals(boundType))
				correctType = true;
		}
		if(e.getOffHandItem() != null) {
			if(e.getOffHandItem().getType().equals(boundType))
				correctType = true;
		}
			
		if(!correctType)
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		e.setCancelled(true);
		aura.toggleAura();
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}
	
	@Override
	public String getInstructions() {
		return "Swap hands with bound item type";
	}

	@Override
	public String getDescription() {
		return "Cast a powerful healing spell centered on yourself that heals all nearby non-mob entities for &6" + healAmt + " &revery &6" + (tickRate / 20.0) + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		healAmt = 1.5 * currentLevel;
		tickRate = 25 - 5 * currentLevel;
		duration = 6 * 20;
		aura = new HealingAura(player, maxDistance, displayName, BarColor.GREEN, tickRate, duration);
		aura.setTickRate(tickRate);
	}

	@Override
	public Material getBoundType() {
		return boundType;
	}

	@Override
	public void setBoundType(Material type) {
		boundType = type;
	}
	
	private class HealingAura extends Aura {

		public HealingAura(Player p, double range, String name, BarColor color, int tickRate, long duration) {
			super(p, range, name, color, tickRate, duration);
		}

		@Override
		public void run() {
			List<LivingEntity> targets = new ArrayList<>();
			for(LivingEntity ent : AbilityUtils.getNearbyLivingEntities(player, range, range, range)) {
				if(ent instanceof Mob)
					continue;
				targets.add(ent);
			}
			targets.add(player);
			
			for(LivingEntity ent : targets) {
				AbilityUtils.heal(player, healAmt, ent);
			}
			
			player.getWorld().playSound(player.getLocation(), Sound.BLOCK_AMETHYST_CLUSTER_STEP, 0.6f, 1.2f);
			
			for(Location loc : Sphere.generateSphere(player.getLocation(), (int)range, true)) {
				player.getWorld().spawnParticle(Particle.COMPOSTER, loc, 4);
			}
			
			counterTick();
		}
		
	}
}
