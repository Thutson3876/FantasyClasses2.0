package me.thutson3876.fantasyclasses.classes.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.metadatavalue.NoExpDrop;

public class RaiseDead extends AbstractAbility implements Bindable {

	private Material type = null;
	private List<EntityType> mobTypes = new ArrayList<>();
	private List<Creature> undead = new ArrayList<>();
	private int maxAmt = 1;
	private int duration = 8 * 20;
	private PotionEffect speed = null;

	public RaiseDead(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 20 * 20;
		this.displayName = "Raise Dead";
		this.skillPointCost = 2;
		this.maximumLevel = 4;

		this.createItemStack(Material.SKELETON_SKULL);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (!e.getPlayer().equals(player))
			return;

		if (e.getItem() == null || !e.getItem().getType().equals(this.type))
			return;

		if (!e.getAction().equals(Action.RIGHT_CLICK_AIR))
			return;

		if (isOnCooldown())
			return;

		AbilityTriggerEvent thisEvent = this.callEvent();
		spawnDead();
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
		
	}

	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent e) {
		if (e.isCancelled())
			return;

		if (e.getDamager().equals(player) && e.getEntity() instanceof LivingEntity) {
			if (undead.contains(e.getEntity())) {
				e.setCancelled(true);
				return;
			}

			for (Creature c : undead) {
				c.setTarget((LivingEntity) e.getEntity());
			}
		} /*
			 * else if (undead.contains(e.getDamager())) { for (Ability abil :
			 * fplayer.getClassAbilities()) { if (abil instanceof LifeRip) { ((LifeRip)
			 * abil).trigger(); break; } } return false; }
			 */

		if (e.getEntity().equals(player) && e.getDamager() instanceof LivingEntity) {
			for (Creature c : undead) {
				c.setTarget((LivingEntity) e.getDamager());
			}
		}
	}

	@EventHandler
	public void onTarget(EntityTargetEvent e) {
		if (e.isCancelled())
			return;

		if (!undead.contains(e.getEntity()))
			return;

		if (e.getTarget() == null || mobTypes.contains(e.getTarget().getType()) || e.getTarget().equals(player)) {
			List<LivingEntity> nearbyEntities = AbilityUtils.getNearbyLivingEntities(player, 15, 15, 15);
			if (nearbyEntities == null) {
				e.setTarget(null);
				return;
			}
			nearbyEntities.removeAll(undead);
			if (nearbyEntities.isEmpty()) {
				e.setTarget(null);
				return;
			}

			e.setTarget(AbilityUtils.getNearestLivingEntity(player.getLocation(), nearbyEntities));
		}
	}

	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		if (!e.getPlayer().equals(player))
			return;

		for (Creature c : undead) {
			if (e.getTo().distance(c.getLocation()) > 20.0) {
				c.teleport(e.getTo());
			}
		}
	}
	
	@Override
	public String getInstructions() {
		return "Right-click with bound item type in hand";
	}

	@Override
	public String getDescription() {
		return "Summon &6" + maxAmt + " &rundead minions to aid you in battle for &6" + (duration / 20) + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.mobTypes.add(EntityType.ZOMBIE);
		this.mobTypes.add(EntityType.HUSK);
		maxAmt = currentLevel;
		duration = (4 + 4 * currentLevel) * 20;
		if (this.currentLevel == 3) {
			mobTypes.add(EntityType.SKELETON);
			mobTypes.add(EntityType.STRAY);
		}
		if (this.currentLevel == 4) {
			mobTypes.add(EntityType.ZOMBIFIED_PIGLIN);
			mobTypes.add(EntityType.WITHER_SKELETON);
			speed = new PotionEffect(PotionEffectType.SPEED, 4 * 20, 1);
		}
	}

	@Override
	public Material getBoundType() {
		return type;
	}

	@Override
	public void setBoundType(Material type) {
		this.type = type;
	}

	private void spawnDead() {
		World world = player.getWorld();
		Location spawn = player.getEyeLocation();
		List<LivingEntity> nearbyEntities = AbilityUtils.getNearbyLivingEntities(player, 15, 15, 15);
		Random rng = new Random();

		for (int i = 0; i < maxAmt; i++) {
			Creature ent = (Creature) world.spawnEntity(spawn, mobTypes.get(rng.nextInt(mobTypes.size())));
			ent.setMetadata("noexpdrop", new NoExpDrop());
			undead.add(ent);
			((Creature) ent).setTarget(AbilityUtils.getNearestLivingEntity(player.getLocation(), nearbyEntities));
			if (speed != null)
				ent.addPotionEffect(speed);

		}

		world.spawnParticle(Particle.SOUL, player.getLocation(), maxAmt);
		new BukkitRunnable() {

			@Override
			public void run() {
				for (Creature c : undead) {
					if (!c.isDead()) {
						c.getWorld().playSound(c.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 0.8f, 0.9f);
						c.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, c.getLocation(), 4);
						c.remove();
					}
				}
				undead.clear();
			}

		}.runTaskLater(plugin, duration);
	}

}
