package me.thutson3876.fantasyclasses.professions.fisherman;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class FriendlyFaces extends AbstractAbility implements Bindable {

	private static Set<EntityType> FISH_TYPES = new HashSet<>();
	
	private double range = 8;
	
	private Material type = null;
	
	static {
		FISH_TYPES.add(EntityType.DOLPHIN);
		FISH_TYPES.add(EntityType.TROPICAL_FISH);
		FISH_TYPES.add(EntityType.PUFFERFISH);
		FISH_TYPES.add(EntityType.COD);
		FISH_TYPES.add(EntityType.SQUID);
		FISH_TYPES.add(EntityType.GLOW_SQUID);
	}
	
	public FriendlyFaces(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 16 * 20;
		this.displayName = "Friendly Faces";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.COD);
	}

	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		if(isOnCooldown())
			return;
		
		if(!e.getPlayer().equals(player))
			return;
		
		if(!e.getItemDrop().getItemStack().getType().equals(type))
			return;
		
		e.setCancelled(true);
		
		boolean triggered = false;
		for(Entity ent : player.getNearbyEntities(range, range, range)) {
			if(FISH_TYPES.contains(ent.getType())) {
				ent.setVelocity(AbilityUtils.getVectorBetween2Points(ent.getLocation(), player.getLocation(), 0.4));
				player.getWorld().spawnParticle(Particle.HEART, ent.getLocation(), 2);
				player.playSound(player.getLocation(), Sound.ENTITY_DOLPHIN_AMBIENT, 0.7f, 1.0f);
				triggered = true;
			}
		}
		
		this.onTrigger(triggered);
	}

	@Override
	public String getInstructions() {
		return "Drop bound item type";
	}

	@Override
	public String getDescription() {
		return "Befriend any and all dolphins within &6" + this.range + " &rblocks";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		range = 8 * currentLevel;
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
