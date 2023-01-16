package me.thutson3876.fantasyclasses.classes.ranger;

import org.bukkit.Material;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.ChatUtils;


public class RapidFire extends AbstractAbility {

	private int duration = 2 * 20;
	private PotionEffect haste = new PotionEffect(PotionEffectType.FAST_DIGGING, duration, 0);
	private int maxStacks = 3;
	
	public RapidFire(Player p) {
		super(p, Priority.LOW);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Rapid Fire";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.CLOCK);
	}
	
	//test with crossbow (might have to disable for crossbow)
	@EventHandler
	public void onEntityShootBowEvent(EntityShootBowEvent e) {
		if (!e.getEntity().equals(player))
			return;

		if (!(e.getProjectile() instanceof AbstractArrow))
			return;
		
		if(!player.hasPotionEffect(PotionEffectType.FAST_DIGGING))
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		if(thisEvent.isCancelled())
			return;
		
		int stackAmt = player.getPotionEffect(PotionEffectType.FAST_DIGGING).getAmplifier();

		AbstractArrow arrow = (AbstractArrow) e.getProjectile();
		
		float force = e.getForce();
		
		if(force >= 1.0)
			return;
		
		float tempForce = (((float)stackAmt) * 0.1f) + force;
		final float finalForce = tempForce > 1.0f ? 1.0f : tempForce;
		
		new BukkitRunnable() {

			@Override
			public void run() {
				arrow.setVelocity(arrow.getVelocity().normalize().multiply(3.0 * finalForce));
				player.sendMessage(ChatUtils.chat("&6Rapid Fire Arrow Velocity: &f" + AbilityUtils.doubleRoundToXDecimals(3.0 * finalForce, 3)));
			}
			
		}.runTaskLater(plugin, 1);
		
		
		
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}
	
	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		ProjectileSource shooter = e.getEntity().getShooter();
		if(shooter == null || !shooter.equals(player))
			return;
		
		if(player.isDead())
			return;
		
		if(e.getHitEntity() == null)
			return;
		
		AbilityUtils.applyStackingPotionEffect(haste, player, maxStacks, maxStacks * duration);
	}

	@Override
	public String getInstructions() {
		return "Shoot arrows in rapid succession";
	}

	@Override
	public String getDescription() {
		return "Your successive arrow shots reduce the amount you need to draw your bow to get full effect from it";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		
	}

}
