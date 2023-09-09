package me.thutson3876.fantasyclasses.classes.berserker;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
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
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;

public class DeepDive extends AbstractAbility implements Bindable {

	private Material type = null;
	
	private boolean pullNearbyEntities = false;
	
	private float power = 3.0F;
	private double dropSpeed = 2.0;
	private double projectedEntitiesSpeed = 1.0;

	public DeepDive(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 8 * 20;
		this.displayName = "Deep Dive";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.QUARTZ);
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
		
		if(this.type == null)
			return;
		
		Material mainHand = player.getInventory().getItemInMainHand().getType();
		Material offHand = player.getInventory().getItemInOffHand().getType();
		
		if (!(this.type.equals(mainHand) || this.type.equals(offHand))
				&& !(this.type.equals(mainHand) || this.type.equals(offHand)))
			return;
		
		//Only requires looking down if player has Cannonball ability
		/*Ability abil = fplayer.getClassAbility(Cannonball.class);
		if(abil != null) {
			float angle = player.getEyeLocation().getDirection().angle(new Vector(0, 1, 0));
			if(angle < 0.523599)
				return;
		}*/
		
		//Requires looking down in order to activate
		float angle = player.getEyeLocation().getDirection().angle(new Vector(0, 1, 0));
		player.sendMessage(ChatUtils.chat("&aEye Direction's Angle of Y-Axis: &r" + AbilityUtils.doubleRoundToXDecimals(angle, 3)));
		if(angle < 2.0)
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		if(thisEvent.isCancelled())
			return;
		
		if(tempGroundPound(p, projectedEntitiesSpeed)) {
			this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
		}
		else {
			thisEvent.setCancelled(true);
		}
	}

	private boolean tempGroundPound(Player p, double speed) {
		double height = AbilityUtils.getHeightAboveGround(p);
		List<Entity> near = null;
		if(this.pullNearbyEntities) {
			double pullRadius = 3.0;
			
			near = player.getNearbyEntities(pullRadius, 1, pullRadius);
			
			if(near.contains(p))
				near.remove(p);
		}
		

		p.setFallDistance(-(float) height);
		AbilityUtils.moveToward(p, p.getLocation().subtract(0, height, 0), dropSpeed);
		if(near != null) {
			for(Entity ent : near)
				AbilityUtils.moveToward(ent, ent.getLocation().subtract(0, height, 0), dropSpeed);
		}
		
		final List<Entity> nearbyEntities = near;

		World world = p.getWorld();
		/*Block[] blocksUnderPlayer = AbilityUtils.getBlocksAroundPlayer(p, 5);

		if (blocksUnderPlayer[0] == null) {
			return false;
		}
		*/

		BukkitRunnable task = new BukkitRunnable() {

			@Override
			public void run() {
				Random rng = new Random();
				Location pLoc = p.getLocation();
				Vector yAxis = new Vector(0, 1, 0);
				//Location bLoc = null;

				/*for (Block b : blocksUnderPlayer) {
					if (b != null) {
						bLoc = b.getLocation();
						FallingBlock temp = world.spawnFallingBlock(bLoc, b.getBlockData());

						double x = bLoc.getX() - pLoc.getX();
						double y = bLoc.getY() - pLoc.getY();
						double z = bLoc.getZ() - pLoc.getZ();
						double angle = (Math.PI / 2) + 0.4 * rng.nextDouble() - 0.2;
						double yBoost = 1.0;

						Vector velocity = new Vector(x, y + yBoost, z).normalize().rotateAroundAxis(yAxis, angle)
								.multiply(-speed);

						b.setType(Material.AIR);
						temp.setVelocity(velocity);
					}
				}*/
				
				if(nearbyEntities != null) {
					for(Entity ent : nearbyEntities) {
						world.createExplosion(ent.getLocation(), power / 1.5f, false, false, p);
					}
				}
				
				p.setInvulnerable(true);
				// System.out.println("Invulnerable!");
				world.createExplosion(p.getLocation(), power, false, false, p);
				p.setInvulnerable(false);
				// System.out.println("No Longer Invulnerable!");
				
				for(Entity ent : player.getNearbyEntities(power, power, power)) {
					Location entLoc = ent.getLocation();
					
					double x = entLoc.getX() - pLoc.getX();
					double y = entLoc.getY() - pLoc.getY();
					double z = entLoc.getZ() - pLoc.getZ();
					double angle = (Math.PI / 2) + 0.4 * rng.nextDouble() - 0.2;
					double yBoost = 0.8;

					Vector velocity = new Vector(x, y + yBoost, z).normalize().rotateAroundAxis(yAxis, angle)
							.multiply(-speed);
					
					ent.setVelocity(ent.getVelocity().add(velocity));
				}
			}
		};

		task.runTaskLater(plugin, Math.round(height / dropSpeed) - 2);

		return true;
	}

	@Override
	public String getInstructions() {
		return "Crouch while mid-air, looking down, and holding bound item type";
	}

	@Override
	public String getDescription() {
		return "Dive downwards and cause an explosion where you land";
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
