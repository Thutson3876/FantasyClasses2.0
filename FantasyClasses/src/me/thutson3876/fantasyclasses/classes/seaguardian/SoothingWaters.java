package me.thutson3876.fantasyclasses.classes.seaguardian;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.particles.CustomParticle;
import me.thutson3876.fantasyclasses.util.particles.GeneralParticleEffects;

public class SoothingWaters extends AbstractAbility implements Bindable {

	private double maxDistance = 8.0;
	private int duration = 4 * 20;
	private int amp = 0;
	
	private Material type = null;
	
	public SoothingWaters(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 18 * 20;
		this.displayName = "Soothing Waters";
		this.skillPointCost = 1;
		this.maximumLevel = 3;

		this.createItemStack(Material.WATER_BUCKET);
	}

	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		if(isOnCooldown())
			return;
		
		if(!e.getPlayer().equals(player))
			return;
		
		if(!e.getItemDrop().getItemStack().getType().equals(type))
			return;
		
		e.setCancelled(true);
		
		LivingEntity target = null;
		target = AbilityUtils.rayTraceTarget(player, maxDistance);
		int newAmp = amp;
		if(target == null) {
			target = player;
		}
		else {
			newAmp += 2;
		}
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, duration, newAmp));
		player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, duration, amp));
		GeneralParticleEffects.helix(target, new CustomParticle(Particle.DOLPHIN), target.getWidth(), 2 * 6.3, duration, 2, 0.1);
		target.getWorld().playSound(target, Sound.ENTITY_DOLPHIN_PLAY, 3.0f, 1.05f);
		target.getWorld().playSound(target, Sound.ENTITY_DOLPHIN_SPLASH, 1.0f, 1.05f);
		target.getWorld().spawnParticle(Particle.NAUTILUS, target.getLocation(), 8);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Press your drop key while holding the bound item type";
	}

	@Override
	public String getDescription() {
		return "Grant your target (or yourself) a short burst of speed and regeneration &6" + (amp + 1) + " &r. When used on another target, healing is increased";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		amp = currentLevel - 1;
		this.coolDowninTicks = (20 - 2 * currentLevel) * 20;
	}

	@Override
	public Material getBoundType() {
		return type;
	}

	@Override
	public void setBoundType(Material type) {
		this.type = type;
	}

}
