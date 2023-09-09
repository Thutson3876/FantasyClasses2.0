package me.thutson3876.fantasyclasses.classes.seaguardian;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
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
import me.thutson3876.fantasyclasses.util.PotionList;
import me.thutson3876.fantasyclasses.util.particles.CustomParticle;
import me.thutson3876.fantasyclasses.util.particles.GeneralParticleEffects;

public class Cleanse extends AbstractAbility implements Bindable {

	private static final List<PotionEffectType> DEBUFFS = PotionList.DEBUFF.getPotList();

	private double healAmt = 3.0;
	private double maxDistance = 8.0;
	private Material type = null;
	
	public Cleanse(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 16 * 20;
		this.displayName = "Cleanse";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.MILK_BUCKET);	
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if(isOnCooldown())
			return;
		
		if(!e.getPlayer().equals(player))
			return;
		
		if(e.getItem() == null || !e.getItem().getType().equals(this.type))
			return;
		
		LivingEntity target = null;
		double toHeal = healAmt;
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			target = AbilityUtils.rayTraceTarget(player, maxDistance);
			if(target == null)
				target = player;
			else
				toHeal += healAmt * 0.5;
		}
		else if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			target = player;
		}
		else {
			return;
		}
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		for(int i = 0; i < 2; i++) {
			for(PotionEffect effect : target.getActivePotionEffects()) {
				if(DEBUFFS.contains(effect.getType())) {
					target.removePotionEffect(effect.getType());
					break;
				}
			}
		}
			
		AbilityUtils.heal(player, toHeal, target);
		GeneralParticleEffects.helix(target, new CustomParticle(Particle.NAUTILUS), target.getWidth(), 2 * 6.3, 1000, 2, 0.1);
		target.getWorld().playSound(target, Sound.ITEM_TOTEM_USE, 1.1f, 1.3f);
		target.getWorld().spawnParticle(Particle.NAUTILUS, target.getLocation(), 8);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Right-click with the bound item type";
	}

	@Override
	public String getDescription() {
		return "Cleanse the impurities from your target, removing all debuffs. Heals for &6" + AbilityUtils.doubleRoundToXDecimals(healAmt, 1) + "&r. Heals for more when used on another target";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		healAmt = 3.0 * this.currentLevel;
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
