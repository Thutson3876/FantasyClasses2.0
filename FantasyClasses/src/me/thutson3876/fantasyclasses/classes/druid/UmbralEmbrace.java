package me.thutson3876.fantasyclasses.classes.druid;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;

public class UmbralEmbrace extends AbstractAbility {

	private int duration = 9 * 20;
	private int amp = 0;
	private List<PotionEffect> effects = new ArrayList<>();
	
	public UmbralEmbrace(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Umbral Embrace";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.SOUL_FIRE);	
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
			for(PotionEffect effect : effects)
				((LivingEntity)hit).addPotionEffect(effect);
		}
	}
	
	@Override
	public String getInstructions() {
		return "Hit a creature with a &6Shooting Star";
	}

	@Override
	public String getDescription() {
		return "Your &6Shooting Stars &rapply &6" + amp + " &rof &dBad Luck &rand &dSlowness";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		amp = currentLevel - 1;
		
		effects.add(new PotionEffect(PotionEffectType.SLOWNESS, duration, amp));
		effects.add(new PotionEffect(PotionEffectType.UNLUCK, duration, amp));
	}

}
