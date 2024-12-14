package me.thutson3876.fantasyclasses.classes.druid;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class ShootingStars extends AbstractAbility implements Bindable {

	private Material boundType = null;
	
	private double minDot = 0.6;
	private double distance = 8.0;
	private double offset = 0.0;
	
	private double damage = 6.0;
	
	public ShootingStars(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 8 * 20;
		this.displayName = "Shooting Stars";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.PALE_OAK_BUTTON);	
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if(!e.getPlayer().equals(player))
			return;
		
		if(e.getItem() == null || !e.getItem().getType().equals(this.boundType))
			return;
		
		if(isOnCooldown())
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		boolean hasFriendlyFire = fplayer.hasFriendlyFire();
		
		List<Entity> enemies = AbilityUtils.getEntitiesInAngle(player, minDot, distance, offset);
		
		AbstractFantasyClass clazz = this.getFantasyPlayer().getChosenClass();
		if (!(clazz instanceof Druid))
			return;

		Druid druid = (Druid) clazz;

		
		for (Entity ent : enemies) {
			if (ent instanceof Player) {
				if (!hasFriendlyFire) {
					continue;
				}
			}

			druid.spawnShootingStar(ent);
		}
			
		this.onTrigger(true);
	}
	
	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		Projectile eventProjectile = e.getEntity();

		if (!(eventProjectile instanceof WitherSkull)) {
			return;
		}

		WitherSkull star = (WitherSkull) eventProjectile;
		if(this.getFantasyPlayer() == null)
			return;
		
		AbstractFantasyClass clazz = this.getFantasyPlayer().getChosenClass();
		if (!(clazz instanceof Druid))
			return;

		Druid druid = (Druid) clazz;

		if (e.getHitBlock() != null) {
			druid.removeShootingStar(star);
			return;
		}
		
		if(e.isCancelled())
			return;
		
		// this.player.sendMessage("Shooting Star Hit!");
		Entity hit = e.getHitEntity();
		
		if(hit instanceof LivingEntity && !hit.isDead()) {
			((LivingEntity)hit).damage(damage);
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(!e.getCause().equals(DamageCause.ENTITY_EXPLOSION))
			return;
		
		if(!e.getDamager().equals(player))
			return;
		
		if(!(e.getEntity() instanceof LivingEntity))
			return;
		
		LivingEntity le = ((LivingEntity)e.getEntity());
		if(le.isDead())
			return;
		
		le.damage(damage / 2, player);
	}

	
	@Override
	public String getInstructions() {
		return "Right-click with your bound item type";
	}

	@Override
	public String getDescription() {
		return "Summon a wave of shooting stars that fall towards mobs in front of you";
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
		return boundType;
	}

	@Override
	public void setBoundType(Material type) {
		boundType = type;
	}

}
