package me.thutson3876.fantasyclasses.classes.druid;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.cooldowns.CooldownManager;

public class Solstice extends AbstractAbility {

	private double cooldownReduction = 0.5;
	
	public Solstice(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 16 * 20;
		this.displayName = "Solstice";
		this.skillPointCost = 2;
		this.maximumLevel = 2;

		this.createItemStack(Material.ECHO_SHARD);	
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
			CooldownManager cdManager = plugin.getCooldownManager();
			cdManager.modifyAllCooldowns(player, -cooldownReduction);
		}
	}
	
	@Override
	public String getInstructions() {
		return "While crouching, right-click with your bound item type";
	}

	@Override
	public String getDescription() {
		return "When you hit a creature with &6Shooting Stars&r, ALL of your active cooldowns are reduced by &6" + cooldownReduction + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		cooldownReduction = 0.5 * currentLevel;
	}


}
