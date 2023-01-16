package me.thutson3876.fantasyclasses.classes.monk;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class SpinningMixer extends AbstractAbility implements Bindable {

	private double range = 4.0D;

	private int counter = 0;

	private int duration = 24;
	
	private double damage = 1.0;

	private List<Entity> entities = new ArrayList<>();

	private float vertical_ticker = 0.0F;

	private float horizontal_ticker = (float) (Math.random() * 2.0D * Math.PI);
	
	private Material type = null;

	public SpinningMixer(Player p) {
		super(p);
	}

	
	@Override
	public void deInit() {
		
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 16 * 20;
		this.displayName = "Spinning Mixer";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.TARGET);
	}

	@EventHandler
	public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {
		if(isOnCooldown())
			return;
		
		if (!e.getPlayer().equals(player))
			return;
			
		boolean correctType = false;
		
		if(e.getMainHandItem() != null) {
			if(e.getMainHandItem().getType().equals(type))
				correctType = true;
		}
		if(e.getOffHandItem() != null) {
			if(e.getOffHandItem().getType().equals(type))
				correctType = true;
		}
			
		if(!correctType)
			return;
		
		AbstractFantasyClass clazz = this.getFantasyPlayer().getChosenClass();
		if (!(clazz instanceof Monk))
			return;

		Monk monk = (Monk) clazz;

		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		this.damage = monk.getUnarmedDmgMod();
		boolean isHealer = monk.isHealer();
		
		if (spawnTornado(isHealer)) {
			e.setCancelled(true);
			this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
		}
	}

	@Override
	public String getInstructions() {
		return "Swap hands with bound item type";
	}

	@Override
	public String getDescription() {
		return "Swiftly twirl the air around you to create a small tornado that sucks up any nearby entities";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		
	}

	private boolean spawnTornado(boolean isHealer) {
		List<Entity> enemies = player.getNearbyEntities(this.range, this.range, this.range);
		if (enemies == null || enemies.isEmpty())
			return false;
		
		this.counter = 0;
		this.entities = enemies;
		BukkitRunnable task = new BukkitRunnable() {
			public void run() {
				if (counter > duration) {
					for (Entity e : entities) {
						e.setVelocity(e.getVelocity().add(Vector.getRandom().multiply(0.4D)));
						if(e instanceof LivingEntity)
							((LivingEntity)e).damage(damage, player);
					}
					cancel();
					return;
				}
				tick();
				counter++;
			}
		};
		task.runTaskTimer(plugin, 1L, 1L);
		
		player.getWorld().playSound(player.getLocation(), Sound.ITEM_ELYTRA_FLYING, 1.0f, 1.0f);
		
		return true;
	}

	private void tick() {
		double radius = Math.sin(verticalTicker()) * 2.0D;
		float horisontal = horizontalTicker();
		Vector v = new Vector(radius * Math.cos(horisontal), 0.1D, radius * Math.sin(horisontal));
		List<Entity> new_entities = player.getNearbyEntities(this.range, this.range, this.range);
		for (Entity e : new_entities) {
			if (!this.entities.contains(e))
				this.entities.add(e);
		}
		for (Entity e : this.entities) {
			if(e instanceof Player && AbilityUtils.getHeightAboveGround(e) < 0.3)
				continue;
			
			e.setVelocity(v);
			if(e instanceof LivingEntity)
				((LivingEntity)e).damage(damage);
			
			if (counter % 3 == 0) {
				e.getWorld().playSound(e.getLocation(), Sound.ENTITY_PHANTOM_FLAP, 1.0f, 0.8f);
				e.getWorld().spawnParticle(Particle.WHITE_ASH, e.getLocation(), 1);
			}
		}
	}

	private float verticalTicker() {
		if (this.vertical_ticker < 6.0F)
			this.vertical_ticker += 0.07F;
		return this.vertical_ticker;
	}

	private float horizontalTicker() {
		return this.horizontal_ticker += 0.25F;
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
