package me.thutson3876.fantasyclasses.classes.witch;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class WitherWand extends AbstractAbility implements Bindable {

	private Material type = null;
	private WitherSkull skull = null;
	private double bulletVelocity = 2.0;
	private double damage = 5;
	private float yield = 0.7f;
	
	public WitherWand(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 6 * 20;
		this.displayName = "Wither Wand";
		this.skillPointCost = 1;
		this.maximumLevel = 3;

		this.createItemStack(Material.WITHER_SKELETON_SKULL);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if (!e.getPlayer().equals(player))
			return;

		if (e.getItem() == null || !e.getItem().getType().equals(this.type))
			return;

		if (!e.getAction().equals(Action.RIGHT_CLICK_AIR))
			return;
		
		if(isOnCooldown())
			return;

		this.launchProjectile();
		this.onTrigger(true);
	}
	
	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		if(!(e.getEntity() instanceof WitherSkull))
			return;

		WitherSkull skull = (WitherSkull) e.getEntity();
		
		if(!skull.getShooter().equals(player))
			return;
		
		Entity hit = e.getHitEntity();
		if (hit == null)
			return;

		if (!(hit instanceof LivingEntity))
			return;

		LivingEntity livingHit = (LivingEntity) hit;
		if(hit.isDead())
			return;
		
		livingHit.damage(damage, player);
	}

	private void launchProjectile() {
		Location spawnAt = player.getEyeLocation().toVector().add(player.getEyeLocation().getDirection())
				.toLocation(player.getWorld());

		skull = (WitherSkull) player.getWorld().spawnEntity(spawnAt, EntityType.WITHER_SKULL);
		skull.setShooter(player);
		skull.setDirection(player.getEyeLocation().getDirection().normalize().multiply(bulletVelocity));
		skull.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(bulletVelocity));
		skull.setCharged(true);
		skull.setYield(yield);
		/*new BukkitRunnable() {

			@Override
			public void run() {
				if(skull != null && !skull.isDead()) {
					Vector newVelocity;
					if(player.getTargetBlockExact(30) != null)
						newVelocity = player.getTargetBlockExact(30).getLocation().toVector().subtract(skull.getLocation().toVector());
					else 
						newVelocity = player.getEyeLocation().getDirection();
					
					skull.setDirection(newVelocity.normalize().multiply(bulletVelocity));
					skull.setVelocity(newVelocity.normalize().multiply(bulletVelocity));
				}	
			}
			
		}.runTaskLater(plugin, 30);*/

		player.getWorld().playSound(spawnAt, Sound.ENTITY_WITHER_SHOOT, 0.9f, 1.0F);
		
	}

	@Override
	public String getInstructions() {
		return "Right-click with your bound item";
	}

	@Override
	public String getDescription() {
		return "Launch a charged wither skull from your wand that causes its target to wither away. It deals &6"
				+ AbilityUtils.doubleRoundToXDecimals(damage, 1) + " &rdamage and has a yield of &6" + AbilityUtils.doubleRoundToXDecimals(yield, 2) +" &rand a cooldown of &6" + this.coolDowninTicks / 20
				+ " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		damage = 5 * currentLevel;
		yield = (0.7f * currentLevel);
		bulletVelocity = 1 + currentLevel;
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
