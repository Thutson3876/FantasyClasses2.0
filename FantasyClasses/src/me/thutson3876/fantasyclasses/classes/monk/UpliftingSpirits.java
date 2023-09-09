package me.thutson3876.fantasyclasses.classes.monk;

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
import me.thutson3876.fantasyclasses.util.PotionList;
import me.thutson3876.fantasyclasses.util.particles.CustomParticle;
import me.thutson3876.fantasyclasses.util.particles.GeneralParticleEffects;

public class UpliftingSpirits extends AbstractAbility implements Bindable {

	private Material boundType = null;
	
	private boolean removesDebuffs = false;
	
	private double healAmt = 4.0;
	private int duration = 2 * 20;
	private PotionEffect slowFall = new PotionEffect(PotionEffectType.SLOW_FALLING, duration * 3, 0);
	private PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION, duration, 0);
	private double maxDistance = 10.0;

	public UpliftingSpirits(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 8 * 20;
		this.displayName = "Uplifting Spirits";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.SMALL_DRIPLEAF);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if (!e.getPlayer().equals(player))
			return;

		if (e.getItem() == null || !e.getItem().getType().equals(this.boundType))
			return;

		if (isOnCooldown())
			return;

		LivingEntity target = null;
		if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			Entity rayTraceTarget = AbilityUtils.rayTraceTarget(player, maxDistance);

			if (rayTraceTarget instanceof LivingEntity)
				target = (LivingEntity) rayTraceTarget;
			else
				target = player;
		} else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			target = player;
		} else {
			return;
		}

		AbilityTriggerEvent thisEvent = this.callEvent();
		
		if(thisEvent.isCancelled())
			return;
		
		AbilityUtils.heal(this.player, healAmt, target);
		GeneralParticleEffects.helix(target, new CustomParticle(Particle.SOUL), target.getWidth(), 2 * 6.3, 1000, 2, 0.1);
		target.addPotionEffect(slowFall);
		
		if(removesDebuffs) {
			for(PotionEffect effect : target.getActivePotionEffects()) {
				if(PotionList.DEBUFF.getPotList().contains(effect.getType())) {
					target.removePotionEffect(effect.getType());
					break;
				}
			}
			
			target.addPotionEffect(regen);
		}

		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Right-click with your bound item type";
	}

	@Override
	public String getDescription() {
		return "Restore &6" + healAmt + " &rhealth and Slowfall to your target. Targeting the ground heals yourself instead.";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		
	}

	@Override
	public Material getBoundType() {
		return this.boundType;
	}

	@Override
	public void setBoundType(Material type) {
		this.boundType = type;
	}

	public void setRemovesDebuffs(boolean removesDebuffs) {
		this.removesDebuffs = removesDebuffs;
	}
}
