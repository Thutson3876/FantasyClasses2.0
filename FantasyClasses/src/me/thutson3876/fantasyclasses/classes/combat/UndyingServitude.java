package me.thutson3876.fantasyclasses.classes.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.MaterialLists;
import me.thutson3876.fantasyclasses.util.NoExpDrop;

public class UndyingServitude extends AbstractAbility {

	private static List<EntityType> entityTypes = new ArrayList<>();
	private final int DELAY = 2 * 20;

	private List<Creature> undead = new ArrayList<>();
	private int durationInTicks = 6 * 20;

	static {
		entityTypes.add(EntityType.SKELETON);
		entityTypes.add(EntityType.STRAY);
		entityTypes.add(EntityType.ZOMBIE);
		entityTypes.add(EntityType.HUSK);
	}

	public UndyingServitude(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Undying Servitude";
		this.skillPointCost = 1;
		this.maximumLevel = 4;

		this.createItemStack(Material.ZOMBIE_HEAD);
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if (e.isCancelled())
			return;

		if (this.isOnCooldown())
			return;

		if (!e.getDamager().equals(this.player))
			return;

		if (player.getAttackCooldown() < 1.0)
			return;

		if (!(e.getEntity() instanceof LivingEntity))
			return;

		if (!e.getCause().equals(DamageCause.ENTITY_ATTACK))
			return;

		if (!(MaterialLists.HOE.getMaterials().contains(player.getInventory().getItemInMainHand().getType())))
			return;

		if (e.getFinalDamage() < 1.0)
			return;

		LivingEntity target = (LivingEntity) e.getEntity();

		if (target.hasMetadata("noexpdrop"))
			return;

		AbilityTriggerEvent thisEvent = this.callEvent();
		
		new BukkitRunnable() {

			@Override
			public void run() {
				if (target.isDead())
					summonDead(target.getLocation().add(0, 0.3, 0));
			}

		}.runTaskLater(plugin, 2);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}
	
	@EventHandler
	public void onMinionDamage(EntityDamageByEntityEvent e) {
		if (e.isCancelled())
			return;

		if(e.getDamager().equals(player) && e.getEntity() instanceof LivingEntity) {
			if(undead.contains(e.getEntity())) {
				e.setCancelled(true);
				return;
			}
			
			for (Creature c : undead) {
				c.setTarget((LivingEntity) e.getEntity());
			}
		} 
		/*else if(undead.contains(e.getDamager())) {
			for(Ability abil : fplayer.getClassAbilities()) {
				if(abil instanceof LifeRip) {
					((LifeRip)abil).trigger();
					break;
				}	
			}
			return false;
		}*/
		if (e.getEntity().equals(player) && e.getDamager() instanceof LivingEntity) {
			for (Creature c : undead) {
				c.setTarget((LivingEntity) e.getDamager());
			}
		}
	}
	
	@EventHandler
	public void onEntityTargetEvent(EntityTargetEvent e) {
		if (e.isCancelled())
			return;

		if (!undead.contains(e.getEntity()))
			return;

		if (e.getTarget() == null || entityTypes.contains(e.getTarget().getType()) || e.getTarget().equals(player)) {
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
		return "Strike the killing blow on an entity with a scythe";
	}

	@Override
	public String getDescription() {
		return "When you kill an entity with a scythe, summon an undead minion in its place for &6"
				+ (durationInTicks / 20) + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.durationInTicks = (6 * currentLevel) * 20;
	}

	private void summonDead(Location loc) {
		Random rng = new Random();

		loc.getWorld().playSound(loc, Sound.EVENT_RAID_HORN, 1.0f, 1.1f);
		loc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 15);

		EntityType type = entityTypes.get(rng.nextInt(entityTypes.size()));

		new BukkitRunnable() {

			@Override
			public void run() {
				Creature c = (Creature) loc.getWorld().spawnEntity(loc, type);
				c.setMetadata("noexpdrop", new NoExpDrop());
				undead.add(c);
				new BukkitRunnable() {

					@Override
					public void run() {
						removeDead(c);
					}
					
				}.runTaskLater(plugin, durationInTicks);
			}

		}.runTaskLater(plugin, DELAY);
	}

	private void removeDead(Creature creature) {
		if (!creature.isDead()) {
			creature.getWorld().spawnParticle(Particle.ASH, creature.getEyeLocation(), 15);
			
			creature.remove();
		}

		undead.remove(creature);
	}
}
