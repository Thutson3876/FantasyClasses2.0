package me.thutson3876.fantasyclasses.classes.druid;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Stampede extends AbstractAbility implements Bindable {

	private Material type = null;
	private PotionEffect speed;
	private int duration = 8 * 20;
	private int amp = 0;

	public Stampede(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 40 * 20;
		this.displayName = "Stampede";
		this.skillPointCost = 1;
		this.maximumLevel = 3;
		speed = new PotionEffect(PotionEffectType.SPEED, duration, amp);

		this.createItemStack(Material.SADDLE);
	}

	@EventHandler
	public void onPlayerToggleSprintEvent(PlayerToggleSprintEvent e) {
		if (!e.getPlayer().equals(player))
			return;

		if (isOnCooldown())
			return;
		
		if(!e.isSprinting())
			return;
		
		if(!Bindable.isHolding(player, type))
			return;

		List<LivingEntity> targets = AbilityUtils.getNearbyPlayerPets(player, 15.0);
		targets.add(player);
		
		for(LivingEntity ent : targets) {
			ent.addPotionEffect(speed);
		}
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_RAVAGER_ROAR, 1.2f, 0.9f);
		player.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, player.getLocation(), amp);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Sprint while holding bound item type";
	}

	@Override
	public String getDescription() {
		return "Grant a great burst of speed &6" + (amp + 1) + " &rto yourself and your pets within 15 meters";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		amp = currentLevel - 1;
		speed = new PotionEffect(PotionEffectType.SPEED, duration, amp);
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
