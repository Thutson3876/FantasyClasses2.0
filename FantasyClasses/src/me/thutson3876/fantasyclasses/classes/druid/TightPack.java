package me.thutson3876.fantasyclasses.classes.druid;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class TightPack extends AbstractAbility {

	private static final double HEALTH_MODIFIER_PER_LEVEL = 0.1;
	private static final double DAMAGE_MODIFIER_PER_LEVEL = 0.1;
	private double healthMod = HEALTH_MODIFIER_PER_LEVEL;
	private double damageMod = DAMAGE_MODIFIER_PER_LEVEL;
	
	public TightPack(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 20;
		this.displayName = "Tight Pack";
		this.skillPointCost = 1;
		this.maximumLevel = 3;

		this.createItemStack(Material.BEEF);
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		int count = 0;
		for(LivingEntity le : AbilityUtils.getNearbyLivingEntities(player, 20, 20, 20)) {
			if(le instanceof Tameable) {
				if(((Tameable)le).getOwner() != null && ((Tameable)le).getOwner().equals(player))
					count++;
			}
		}
		
		if(count > 3)
			return;
		
		if(e.getEntity() instanceof Tameable) {
			Tameable tamed = (Tameable) e.getEntity();
			if(tamed.getOwner() != null && tamed.getOwner().equals(player))
				e.setDamage(e.getDamage() * (1.0 - healthMod));
		}
		if(e.getDamager() instanceof Tameable) {
			Tameable tamed = (Tameable) e.getDamager();
			if(tamed.getOwner() != null && tamed.getOwner().equals(player))
				e.setDamage(e.getDamage() * (1.0 + damageMod));	
		}
	}

	@Override
	public String getInstructions() {
		return "Have less than 4 pets around you";
	}

	@Override
	public String getDescription() {
		return "Your pets are stronger when in smaller number. They deal &6" + AbilityUtils.doubleRoundToXDecimals(damageMod, 2) + "% &rand take &6" + AbilityUtils.doubleRoundToXDecimals(healthMod, 2) + "% &rreduced damage.";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		healthMod = HEALTH_MODIFIER_PER_LEVEL * currentLevel;
		damageMod = DAMAGE_MODIFIER_PER_LEVEL * currentLevel;
	}

}
