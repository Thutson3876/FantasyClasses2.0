package me.thutson3876.fantasyclasses.classes.witch;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.DamageType;

public class NineLives extends AbstractAbility {
	
	public NineLives(Player p) {
		super(p, Priority.LOW);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 12 * 20;
		this.displayName = "Nine Lives";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.FEATHER);		
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent e) {
		if(!e.getEntity().equals(player))
			return;
		
		if(!DamageType.ENVIRONMENTAL.getDamageCauseList().contains(e.getCause()))
			return;
		
		e.setDamage(0);
		
		this.onTrigger(true);
	}

	@Override
	public String getInstructions() {
		return "Take magical damage";
	}

	@Override
	public String getDescription() {
		return "When you take environmental damage, it is completely negated. This effect has a cooldown of &6" + AbilityUtils.doubleRoundToXDecimals(coolDowninTicks / 20.0, 1) + "&r seconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.coolDowninTicks = (18 - 6 * currentLevel) * 20;
	}

}
