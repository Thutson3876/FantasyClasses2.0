package me.thutson3876.fantasyclasses.classes.witch;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class WitchWand extends AbstractAbility implements Bindable {

	private Material boundType = null;
	private double bulletVelocity = 2.0;
	private double damage = 1.5;
	private int duration = 1 * 20;

	public WitchWand(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 5 * 20;
		this.displayName = "Witch's Wand";
		this.skillPointCost = 1;
		this.maximumLevel = 6;

		this.createItemStack(Material.SHULKER_BOX);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if (!e.getPlayer().equals(player))
			return;

		if (e.getItem() == null || !e.getItem().getType().equals(this.boundType))
			return;

		if (!e.getAction().equals(Action.LEFT_CLICK_AIR))
			return;
		
		if(isOnCooldown())
			return;

		launchProjectile();
		
		this.onTrigger(true);
	}
	
	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		if(e.getEntity().getShooter() == null)
			return;
		
		if (!e.getEntity().getShooter().equals(player))
			return;

		if (!e.getEntityType().equals(EntityType.SHULKER_BULLET))
			return;

		Entity hit = e.getHitEntity();
		if (hit == null)
			return;

		if (!(hit instanceof LivingEntity))
			return;

		LivingEntity livingHit = (LivingEntity) hit;
		if(hit.isDead())
			return;
		
		livingHit.damage(this.damage, player);
		livingHit.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, duration, 0));
	}

	@Override
	public String getInstructions() {
		return "Left-click with your bound item";
	}

	@Override
	public String getDescription() {
		return "Launch a ball of energy from your wand that causes its target to levitate for &6" + AbilityUtils.doubleRoundToXDecimals(duration / 20.0, 2) + " &rseconds. It deals &6"
				+ AbilityUtils.doubleRoundToXDecimals(this.damage, 1) + " &rdamage and has a cooldown of &6" + this.coolDowninTicks / 20
				+ " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		damage = 1.5 * currentLevel;
		this.coolDowninTicks = (7 - currentLevel) * 20;
		this.duration = 20 * currentLevel;
	}

	@Override
	public Material getBoundType() {
		return this.boundType;
	}

	@Override
	public void setBoundType(Material type) {
		this.boundType = type;
	}

	private void launchProjectile() {
		Location spawnAt = player.getEyeLocation().toVector().add(player.getEyeLocation().getDirection())
				.toLocation(player.getWorld());

		ShulkerBullet bullet = (ShulkerBullet) player.getWorld().spawnEntity(spawnAt, EntityType.SHULKER_BULLET);
		bullet.setShooter(player);
		bullet.setVelocity(player.getEyeLocation().getDirection().multiply(bulletVelocity));
		LivingEntity target = AbilityUtils.getNearestLivingEntity(player.getLocation(), AbilityUtils.onlyLiving(AbilityUtils.getEntitiesInAngle(player, 0.8, 25, 0.2)));
		if (target != null)
			bullet.setTarget(target);

		//bullet.setVelocity(bullet.getVelocity().multiply(bulletVelocity));

		player.getWorld().playSound(spawnAt, Sound.ENTITY_SHULKER_SHOOT, 0.7f, 1.2F);
	}
}
