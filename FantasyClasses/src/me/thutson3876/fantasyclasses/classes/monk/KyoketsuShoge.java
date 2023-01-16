package me.thutson3876.fantasyclasses.classes.monk;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class KyoketsuShoge extends AbstractAbility implements Bindable {

	private double velocityMod = 2.5;
	private double damage = 4.0;
	private Arrow arrow = null;
	//private double yMod = 1.0;
	private boolean applyWhiplash = false;
	private PotionEffect unluck = new PotionEffect(PotionEffectType.UNLUCK, 6 * 20, 1);
	
	private Material type = null;

	public KyoketsuShoge(Player p) {
		super(p);
	}

	//fix weird launching
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 12 * 20;
		this.displayName = "Kyoketsu-Shoge";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.ARROW);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if (!e.getPlayer().equals(this.player))
			return;
		
		if (isOnCooldown())
			return;

		if (!(e.getAction() == Action.RIGHT_CLICK_AIR))
			return;
		if (!(player.getInventory().getItemInOffHand().getType().equals(type) || player.getInventory().getItemInMainHand().getType().equals(type)))
			return;

		AbilityTriggerEvent thisEvent = this.callEvent();
		
		if(thisEvent.isCancelled())
			return;
		
		fireArrow();
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}
	
	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		if(e.isCancelled())
			return;
		
		if (!e.getEntity().equals(this.arrow))
			return;

		Entity hitEntity = e.getHitEntity();
		if (hitEntity != null) {
			hitEntity.setVelocity(AbilityUtils.getVectorBetween2Points(hitEntity.getLocation(), player.getLocation(), 0.5));
			if(applyWhiplash && hitEntity instanceof LivingEntity && !hitEntity.isDead()) {
				((LivingEntity)hitEntity).addPotionEffect(unluck);
			}
				
			
		}
		else if (e.getHitBlock() != null) {
			player.setVelocity(AbilityUtils.getVectorBetween2Points(player.getLocation(), e.getHitBlock().getLocation(), 0.21));
		}
	}

	private void fireArrow() {
		Vector velocity = player.getEyeLocation().getDirection().multiply(this.velocityMod);
		Location spawnAt = player.getEyeLocation().toVector().add(player.getEyeLocation().getDirection())
				.toLocation(player.getWorld());
		arrow = player.getWorld().spawnArrow(spawnAt, velocity, 1F, 1F);
		arrow.setShooter(player);
		arrow.setDamage(this.damage);
		arrow.setPickupStatus(PickupStatus.CREATIVE_ONLY);

		BukkitRunnable task = new BukkitRunnable() {

			@Override
			public void run() {
				if (arrow != null) {
					arrow.remove();
					arrow = null;
				}

			}
		};

		task.runTaskLater(plugin, 3 * 20);
	}

	@Override
	public String getInstructions() {
		return "Right-click with bound item type";
	}

	@Override
	public String getDescription() {
		return "Launches an arrow that will pull enemies towards you or act as a grappling hook with a cooldown of &6"
				+ coolDowninTicks / 20 + "&r seconds";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		coolDowninTicks = (18 - currentLevel * 6) * 20;
		damage = 4.0 * currentLevel;
	}

	@Override
	public Material getBoundType() {
		return type;
	}

	@Override
	public void setBoundType(Material type) {
		this.type = type;
	}
	
	public void setApplyWhiplash(boolean applyWhiplash) {
		this.applyWhiplash = applyWhiplash;
	}

}
