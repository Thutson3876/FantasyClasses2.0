package me.thutson3876.fantasyclasses.professions.alchemist;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent.Action;
import org.bukkit.potion.PotionEffect;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.PotionList;

public class Immunology extends AbstractAbility {

	private boolean increaseBuffs = false;
	private boolean justTriggered = false;
	
	public Immunology(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 2 * 20;
		this.displayName = "Immunology";
		this.skillPointCost = 3;
		this.maximumLevel = 2;
		
		this.createItemStack(Material.TOTEM_OF_UNDYING);
	}

	@EventHandler
	public void onEntityPotionEffectEvent(EntityPotionEffectEvent e) {
		if(justTriggered)
			return;
		
		if(isOnCooldown())
			return;
		
		if(e.isCancelled())
			return;
		
		if(!e.getEntity().equals(player))
			return;
		
		if(!e.getAction().equals(Action.ADDED) && !e.getAction().equals(Action.CHANGED))
			return;
		
		if(PotionList.DEBUFF.getPotList().contains(e.getModifiedType())) {
			PotionEffect effect = e.getNewEffect();
			int amp = effect.getAmplifier();
			if(amp < 1)
				return;
			
			e.setCancelled(true);
			justTriggered = true;
			player.addPotionEffect(new PotionEffect(effect.getType(), effect.getDuration(), amp - 1));
			justTriggered = false;
			this.onTrigger(true);
		}
		else if(increaseBuffs){
			PotionEffect effect = e.getNewEffect();
			e.setCancelled(true);
			justTriggered = true;
			player.addPotionEffect(new PotionEffect(effect.getType(), effect.getDuration(), effect.getAmplifier() + 1));
			justTriggered = false;
			
			this.onTrigger(true);
		}
	}
	
	@Override
	public String getInstructions() {
		return "Gain a potion effect";
	}

	@Override
	public String getDescription() {
		return "Reduces the potency of debuffs you receive. At second level, increases the potency of buffs";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		if(currentLevel > 1) {
			increaseBuffs = true;
		}
	}

}
