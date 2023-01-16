package me.thutson3876.fantasyclasses.classes.berserker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Cannonball extends AbstractAbility implements Bindable {
	private Material type = null;

	private List<Entity> trackedEntities = new ArrayList<>();
	private boolean pullNearbyEntities = false;

	private float power = 3.0F;
	private double projectedEntitiesSpeed = 1.0;
	private double launchSpeed = 2.3;
	
	private double minVelocity = 0.1;

	public Cannonball(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 8 * 20;
		this.displayName = "Cannonball";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.CLAY_BALL);
	}

	@EventHandler
	public void onSneakEvent(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();

		if (!this.player.equals(p))
			return;

		if (AbilityUtils.getHeightAboveGround(p) < 3.0) {
			return;
		}
		if (this.isOnCooldown()) {
			return;
		}

		if (this.type == null)
			return;

		Material mainHand = player.getInventory().getItemInMainHand().getType();
		Material offHand = player.getInventory().getItemInOffHand().getType();

		if (!(this.type.equals(mainHand) || this.type.equals(offHand))
				&& !(this.type.equals(mainHand) || this.type.equals(offHand)))
			return;

		float angle = player.getEyeLocation().getDirection().angle(new Vector(0, 1, 0));
		if(angle > 2.0)
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;

		this.trackedEntities.clear();
		if (tempGroundPound(p, projectedEntitiesSpeed)) {
			this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
		} else {
			thisEvent.setCancelled(true);
		}
	}

	private boolean tempGroundPound(Player p, double speed) {
		List<Entity> entitiesToTrack = new ArrayList<>();
		if (this.pullNearbyEntities) {
			double pullRadius = 3.0;

			entitiesToTrack = player.getNearbyEntities(pullRadius, pullRadius / 1.5, pullRadius);
		}

		Vector newVelocity = player.getEyeLocation().getDirection().normalize().multiply(launchSpeed);
		player.setVelocity(newVelocity);
		if (entitiesToTrack != null) {
			for (Entity ent : entitiesToTrack)
				ent.setVelocity(newVelocity);
		}

		trackedEntities = entitiesToTrack;

		World world = p.getWorld();
		/*
		 * Block[] blocksUnderPlayer = AbilityUtils.getBlocksAroundPlayer(p, 5);
		 * 
		 * if (blocksUnderPlayer[0] == null) { return false; }
		 */

		BukkitRunnable task = new BukkitRunnable() {

			@Override
			public void run() {
				Random rng = new Random();
				Vector yAxis = new Vector(0, 1, 0);
				if(!trackedEntities.isEmpty()) {
					List<Entity> toBeRemoved = new ArrayList<>();
					for (Entity ent : trackedEntities) {
						//Remove dead entities
						if(ent.isDead()) {
							toBeRemoved.add(ent);
							continue;
						}
						
						//Create explosion if velocity is slowed below min
						if(ent.getVelocity().length() < minVelocity) {
							//If player, then make them invulnerable
							if(ent.equals(p))
								p.setInvulnerable(true);
							world.createExplosion(ent.getLocation(), power / 1.5f, false, false, p);
							if(ent.equals(p))
								p.setInvulnerable(false);
							
							
							//Throws entities all around
							Location entLoc = ent.getLocation();
							for (Entity e : ent.getNearbyEntities(power, power, power)) {
								Location eLoc = e.getLocation();

								double x = eLoc.getX() - entLoc.getX();
								double y = eLoc.getY() - entLoc.getY();
								double z = eLoc.getZ() - entLoc.getZ();
								double angle = (Math.PI / 2) + 0.4 * rng.nextDouble() - 0.2;
								double yBoost = 0.1;

								Vector velocity = new Vector(x, y + yBoost, z).normalize().rotateAroundAxis(yAxis, angle)
										.multiply(-speed);

								e.setVelocity(e.getVelocity().add(velocity));
							}
							
							toBeRemoved.add(ent);
						}
						else {
							ent.getWorld().spawnParticle(Particle.SONIC_BOOM, ent.getLocation().add(0, ent.getHeight() / 2.0, 0), 6, 0.5, ent.getHeight() / 2.0, 0.5);
						}
					}
					
					trackedEntities.removeAll(toBeRemoved);
					toBeRemoved.clear();
				}
				else {
					this.cancel();
				}
			}
		};

		task.runTaskTimer(plugin, 2, 1);

		return true;
	}

	@Override
	public String getInstructions() {
		return "Crouch while mid-air, not looking down, and holding bound item type";
	}

	@Override
	public String getDescription() {
		return "Dive forwards and cause an explosion where you land";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
	}

	public boolean isPullNearbyEntities() {
		return pullNearbyEntities;
	}

	public void setPullNearbyEntities(boolean pullNearbyEntities) {
		this.pullNearbyEntities = pullNearbyEntities;
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
