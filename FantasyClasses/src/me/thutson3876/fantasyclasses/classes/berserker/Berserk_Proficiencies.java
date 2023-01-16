package me.thutson3876.fantasyclasses.classes.berserker;

import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Berserk_Proficiencies extends AbstractAbility {

	private double healthBonus = 10.0;
	private double damageReduction = 0.3;
	
	public Berserk_Proficiencies(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Berserk Proficiencies";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.DIAMOND);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(!e.getDamager().equals(player))
			return;
		
		e.setDamage(e.getDamage() * (1 - this.damageReduction));
	}

	@Override
	public String getInstructions() {
		return "Wear no armor stronger than &6Diamond &3to use &3your abilities";
	}

	@Override
	public String getDescription() {
		return "&4Reduces your Damage &4by &630%&4. &aIncreases &ayour &aHealth by &6" + healthBonus;
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void init() {
		if(fplayer == null)
			return;
		
		AbilityUtils.setMaxHealth(player, new AttributeModifier("berserkproficiencies", healthBonus, Operation.ADD_NUMBER));
		this.fplayer.setArmorType(5);
	}
	
	@Override
	public void applyLevelModifiers() {	
		
	}

}
