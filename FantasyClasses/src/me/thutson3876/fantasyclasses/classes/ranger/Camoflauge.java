package me.thutson3876.fantasyclasses.classes.ranger;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;

public class Camoflauge extends AbstractAbility implements Bindable {

	private int duration = 4 * 20;
	
	private static final List<PotionEffectType> POTION_EFFECTS;
	
	private boolean isActive = false;
	
	private Material type = null;
	
	static {
		List<PotionEffectType> effects = new ArrayList<>();
		
		effects.add(PotionEffectType.INVISIBILITY);
		effects.add(PotionEffectType.SPEED);
		effects.add(PotionEffectType.JUMP);
		effects.add(PotionEffectType.REGENERATION);
		
		POTION_EFFECTS = effects;
	}
	
	public Camoflauge(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 20 * 20;
		this.displayName = "Camoflauge";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.OAK_LEAVES);
	}

	@EventHandler
	public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {
		
		if(e.isCancelled())
			return;
		
		if(isOnCooldown())
			return;
		
		if(!e.getPlayer().equals(player))
			return;
		
		boolean hasBoundType = false;
		if(e.getMainHandItem() != null) {
			hasBoundType = e.getMainHandItem().getType().equals(type);
		}
		if(!hasBoundType && e.getOffHandItem() != null) {
			hasBoundType = e.getOffHandItem().getType().equals(type);
		}
		if(!hasBoundType)
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		if(thisEvent.isCancelled())
			return;
		
		e.setCancelled(true);
		addPotionEffects();
		
		isActive = true;
		
		new BukkitRunnable() {

			@Override
			public void run() {
				isActive = false;
			}
			
		}.runTaskLater(plugin, duration);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(!e.getEntity().equals(player))
			return;
		
		if(!isActive)
			return;
		
		if(player != null && !player.isDead()) {
			for(PotionEffectType effect : POTION_EFFECTS) {
				player.removePotionEffect(effect);
			}
		}
	}

	@Override
	public String getInstructions() {
		return "Swap hands while holding bound item type";
	}

	@Override
	public String getDescription() {
		return "Blend in with your surroundings gaining invisibility and speed for &6" + (duration / 20) + " &rseconds. Receiving attack damage while active removes all buffs";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		duration = (4 * currentLevel) * 20;
	}

	private void addPotionEffects() {
	    player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, this.duration, 0));
	    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, this.duration, 0));
	  }

	public int getDuration() {
		return this.duration;
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
