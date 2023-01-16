package me.thutson3876.fantasyclasses.classes.berserker;

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
import me.thutson3876.fantasyclasses.util.Particles;

public class DemoralizingShout extends AbstractAbility implements Bindable {

	private Material type = null;
	private int duration = 5 * 20;
	private double range = 8.0;
	private PotionEffect weak = new PotionEffect(PotionEffectType.WEAKNESS, duration, 0);
	private PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, duration, 0);
	
	public DemoralizingShout(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 12 * 20;
		this.displayName = "Demoralizing Shout";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.SKELETON_SKULL);
	}

	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		if (isOnCooldown())
			return;

		if (!e.getPlayer().equals(player))
			return;

		if (!e.getItemDrop().getItemStack().getType().equals(type))
			return;

		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;

		e.setCancelled(true);

		for(LivingEntity le : AbilityUtils.getNearbyLivingEntities(player, range, range, range)) {
			if(!this.fplayer.hasFriendlyFire() && le instanceof Player)
				continue;
			
			le.addPotionEffect(weak);
			le.addPotionEffect(slow);
		}

		player.getWorld().playSound(player, Sound.ENTITY_WARDEN_ROAR, 1.5f, 1.2f);
		Particles.pulsingCircle(player.getLocation().add(0, 1.0, 0), range, 0.25, 2, Particle.CRIT_MAGIC);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}
	
	@Override
	public String getInstructions() {
		return "Drop bound item type";
	}

	@Override
	public String getDescription() {
		return "Unleash a savage roar that applies Weakness and Slowness to all creatures within &6" + range + " &rblocks";
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
		return type;
	}

	@Override
	public void setBoundType(Material type) {
		this.type = type;
	}

}
