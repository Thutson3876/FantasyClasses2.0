package me.thutson3876.fantasyclasses.classes.dungeoneer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.DamageType;

public class StoneSkin extends AbstractAbility {

	private double dmgReduction = 0.01;
	
	public StoneSkin(Player p) {
		super(p, Priority.LOW);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Stoneskin";
		this.skillPointCost = 1;
		this.maximumLevel = 6;
		
		this.createItemStack(Material.SMOOTH_STONE);
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent e) {
		if(!e.getEntity().equals(player))
			return;
		
		if(!DamageType.PHYSICAL.contains(e.getCause()))
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		e.setDamage((1.0 - dmgReduction) * e.getDamage());
		
		World world = player.getWorld();
		Location loc = player.getLocation();
		
		world.playSound(loc, Sound.BLOCK_DRIPSTONE_BLOCK_BREAK, 0.5f, 0.8f);
		world.spawnParticle(Particle.ASH, loc, currentLevel * 2 + 4);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Take damage";
	}

	@Override
	public String getDescription() {
		return "Take &6" + AbilityUtils.doubleRoundToXDecimals(dmgReduction * 100, 2) + "% &rless physical damage";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		dmgReduction = 0.01 * currentLevel;
	}

}
