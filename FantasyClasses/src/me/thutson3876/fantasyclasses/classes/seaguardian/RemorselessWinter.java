package me.thutson3876.fantasyclasses.classes.seaguardian;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.particles.customeffect.Snowstorm;

public class RemorselessWinter extends AbstractAbility implements Bindable {

	private Material type = null;
	private BukkitTask task = null;
	private int taskID = 0;
	
	private double dmg = 0.25;
	private int freezeAmt = 20;
	private static int tickRate = 10;
	private int counter = 0;
	private int radius = 3;
	private int durationInTicks = 3 * 20;
	//private double speed = 1.0;
	//private int count = 3;
	private Snowball snow = null;
	private Snowstorm stormParticle = new Snowstorm(radius, durationInTicks, 5);
	
	private int duration = 20;
	private int amp = 1;
	private PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, duration, amp);
	private PotionEffect fatigue = new PotionEffect(PotionEffectType.SLOW_DIGGING, duration, amp);
	
	private boolean stackFrostFever = false;
	
	public RemorselessWinter(Player p) {
		super(p);
	}
	
	@Override
	public void deInit() {
		if (this.task == null || this.taskID == 0)
			return;
		Bukkit.getScheduler().cancelTask(this.taskID);
		this.taskID = 0;
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 16 * 20;
		this.displayName = "Remorseless Winter";
		this.skillPointCost = 1;
		this.maximumLevel = 3;

		this.createItemStack(Material.POWDER_SNOW_BUCKET);
	}

	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		if(e.getEntity().equals(snow)) {
			spawnSnowStorm(snow.getLocation(), radius);
			if(e.getHitEntity() instanceof LivingEntity) {
				LivingEntity ent = (LivingEntity) e.getHitEntity();
				ent.damage(dmg * 2, player);
				ent.setFreezeTicks(ent.getFreezeTicks() + freezeAmt);
				ent.getWorld().playSound(ent.getLocation(), Sound.BLOCK_GLASS_HIT, 1.4f, 0.8f);
			}
				
			snow = null;
		}
	}
	
	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		if(isOnCooldown())
			return;
		
		if(!e.getPlayer().equals(player))
			return;
		
		if(!e.getItemDrop().getItemStack().getType().equals(type))
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		e.setCancelled(true);
		
		throwSnow();
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}
	
	@Override
	public String getInstructions() {
		return "Press your drop key while holding the bound item type";
	}

	@Override
	public String getDescription() {
		return "Hurl a snowball at your foe that deals &6" + AbilityUtils.doubleRoundToXDecimals(dmg, 1) + " &rdamage, freezes them, and spawns a snowstorm for &6" + AbilityUtils.doubleRoundToXDecimals(durationInTicks / 20, 1)  + " &6seconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		dmg = 0.25 * currentLevel;
		freezeAmt = 20 + 20 * currentLevel;
	}

	@Override
	public Material getBoundType() {
		return type;
	}

	@Override
	public void setBoundType(Material type) {
		this.type = type;
	}
	
	/*private void throwSnowLag() {
		Vector velocity = player.getEyeLocation().getDirection();
		World world = player.getWorld();
		Location loc = player.getEyeLocation();
		Random rng = new Random();
		Block temp = world.getBlockAt(0, -64, 0);
		temp.setType(Material.POWDER_SNOW);
		
		for(int i = 0; i < count; i++) {
			FallingBlock fb = (FallingBlock) world.spawnFallingBlock(loc, temp.getBlockData());
			
			fb.setDropItem(false);
			fb.setHurtEntities(true);
			fb.setVelocity(velocity.add(Vector.getRandom().multiply(rng.nextGaussian() * 0.5)).multiply(speed));
		}
		
		temp.setType(Material.BEDROCK);
	}*/
	
	private void throwSnow() {
		Vector velocity = player.getEyeLocation().getDirection().normalize().multiply(1.3);
		World world = player.getWorld();
		Location loc = player.getEyeLocation();
		snow = (Snowball) world.spawnEntity(loc, EntityType.SNOWBALL);
		snow.setShooter(player);
		snow.setVelocity(velocity);
	}

	private void spawnSnowStorm(Location loc, int radius) {
		World world = loc.getWorld();
		stormParticle.run(loc);
		
		counter = 0;
		task = new BukkitRunnable() {

			@Override
			public void run() {
				if(counter > durationInTicks) {
					this.cancel();
					return;
				}
				
				world.playSound(loc, Sound.ITEM_ELYTRA_FLYING, ((float)radius) / 4.5f, 1.0f);
				
				
				for(Entity e : world.getNearbyEntities(loc, radius, radius, radius)){
					if(e instanceof LivingEntity && !e.isDead()) {
						LivingEntity ent = (LivingEntity) e;
						ent.addPotionEffect(fatigue);
						ent.addPotionEffect(slow);
						ent.damage(dmg, player);
						ent.setFreezeTicks(ent.getFreezeTicks() + freezeAmt);
						
						if(stackFrostFever) {
							
							
							
						}
					}
				}
				
				counter += tickRate;
			}
			
		}.runTaskTimer(plugin, 1, tickRate);
		taskID = task.getTaskId();
	}
	
	public void setStackFrostFever() {
		
	}
}
