package me.thutson3876.fantasyclasses.classes.berserker;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class RagingBlows extends AbstractAbility {

	private double dmgConversion = 0.2;
	private double radius = 1.5;
	
	public RagingBlows(Player p) {
		super(p, Priority.HIGHEST);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Raging Blows";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.DIAMOND_AXE);
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		if(this.isOnCooldown())
			return;
		
		if(!e.getDamager().equals(this.player))
			return;
		
		if(player.getAttackCooldown() < 1.0)
			return;
		
		if (!AbilityUtils.isCritical(player))
			return;
		
		if(!e.getCause().equals(DamageCause.ENTITY_ATTACK))
			return;
		
		AbstractFantasyClass clazz = this.getFantasyPlayer().getChosenClass();
		if (!(clazz instanceof Berserker))
			return;

		Berserker berserker = (Berserker) clazz;

		if(berserker.getEnrageDurationRemaining() <= 0)
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		Entity victim = e.getEntity();
		for(Entity ent : e.getEntity().getNearbyEntities(radius, radius / 2.0, radius)) {
			if(ent instanceof Mob && !victim.equals(ent))
				player.damage(e.getDamage() * this.dmgConversion, ent);
		}
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Attack while Enraged";
	}

	@Override
	public String getDescription() {
		return "Your attacks deal &6" + AbilityUtils.doubleRoundToXDecimals(dmgConversion * 100, 1) + "% &rof their damage to nearby enemies while you are Enraged";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.dmgConversion = 0.2 * this.currentLevel;
	}

}
