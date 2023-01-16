package me.thutson3876.fantasyclasses.classes.witch;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vex;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class VexWand extends AbstractAbility implements Bindable {

	private Material boundType = null;
	private List<Creature> vexes = new ArrayList<>();
	private int maxAmt = 2;
	private int duration = 20 * 20;
	private static final double MAX_RANGE = 30.0;
	
	public VexWand(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 25 * 20;
		this.displayName = "Vex Wand";
		this.skillPointCost = 1;
		this.maximumLevel = 5;

		this.createItemStack(Material.END_ROD);	
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if(!e.getPlayer().equals(player))
			return;
		
		if(e.getItem() == null || !e.getItem().getType().equals(this.boundType))
			return;
		
		if(!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;
		
		if(isOnCooldown())
			return;
		
		spawnVexes();
		
		this.onTrigger(true);
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(e.getDamager().equals(player) && e.getEntity() instanceof LivingEntity) {
			for(Creature vex : vexes) {
				vex.setTarget((LivingEntity) e.getEntity());
			}
		}
		else if(e.getEntity().equals(player) && e.getDamager() instanceof LivingEntity) {
			for(Creature vex : vexes) {
				vex.setTarget((LivingEntity) e.getDamager());
			}
		}
	}
	
	@EventHandler
	public void onEntityTargetEvent(EntityTargetEvent e) {
		if(!vexes.contains(e.getEntity()))
			return;
		
		if(e.getTarget() == null || e.getTarget().getType().equals(EntityType.VEX) || e.getTarget().equals(player)) {
			List<LivingEntity> nearbyEntities = AbilityUtils.getNearbyLivingEntities(player, 15, 15, 15);
			e.setTarget(AbilityUtils.getNearestLivingEntity(player.getLocation(), nearbyEntities));
			return;
		}
	}
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		if(!e.getPlayer().equals(player))
			return;
		
		for(Creature vex : vexes) {
			if(e.getTo().distance(vex.getLocation()) > MAX_RANGE) {
				vex.teleport(e.getTo());
			}
		}
	}

	@Override
	public String getInstructions() {
		return "Right-click with the bound item type";
	}

	@Override
	public String getDescription() {
		return "Spawn &6" + maxAmt + " &rVex to attack nearby entities. Amount increases per 100 magicka";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		maxAmt = currentLevel;
	}

	@Override
	public Material getBoundType() {
		return boundType;
	}

	@Override
	public void setBoundType(Material type) {
		boundType = type;
	}

	private void spawnVexes() {
		World world = player.getWorld();
		Location spawn = player.getEyeLocation();
		List<LivingEntity> nearbyEntities = AbilityUtils.getNearbyLivingEntities(player, 15, 15, 15);
		
		for(int i = 0; i < maxAmt; i++) {
			Vex vex = (Vex) world.spawnEntity(spawn, EntityType.VEX);
			vexes.add(vex);
			vex.setTarget(AbilityUtils.getNearestLivingEntity(player.getLocation(), nearbyEntities));
		}
		
		new BukkitRunnable() {

			@Override
			public void run() {
				for(Creature vex : vexes) {
					if(!vex.isDead()) {
						vex.getWorld().playSound(vex.getLocation(), Sound.ENTITY_VEX_DEATH, 1.3f, 1.0f);
						vex.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, vex.getLocation(), 4);
						vex.remove();
					}
				}
				vexes.clear();
			}
			
		}.runTaskLater(plugin, duration);
	}
}
