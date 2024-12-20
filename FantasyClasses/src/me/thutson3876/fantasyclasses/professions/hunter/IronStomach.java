package me.thutson3876.fantasyclasses.professions.hunter;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class IronStomach extends AbstractAbility {

	List<PotionEffectType> effects = Arrays.asList(PotionEffectType.NAUSEA, PotionEffectType.HUNGER, PotionEffectType.POISON);
	
	public IronStomach(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Iron Stomach";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.IRON_INGOT);
	}

	@EventHandler
	public void onEntityPotionEffectEvent(EntityPotionEffectEvent e) {
		if(!e.getEntity().equals(player))
			return;
		
		if(!e.getAction().equals(Action.ADDED) && !e.getAction().equals(Action.CHANGED))
			return;
		
		if(effects.contains(e.getModifiedType())) {
			PotionEffect effect = e.getNewEffect();
			int amp = effect.getAmplifier();
			if(amp < 1)
				return;
			
			e.setCancelled(true);
			player.addPotionEffect(new PotionEffect(effect.getType(), effect.getDuration() / 2, amp - 1));
			this.onTrigger(true);
		}
		
		this.onTrigger(false);
	}

	@Override
	public String getInstructions() {
		return "Become afflicted with &dNausea&r, &dHunger&r, or &dPoison";
	}

	@Override
	public String getDescription() {
		return "Effects that impact your stomach are reduced by half";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		
	}

}
