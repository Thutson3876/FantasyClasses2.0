package me.thutson3876.fantasyclasses.classes.seaguardian;

import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.CustomLivingEntityDamageEvent;
import me.thutson3876.fantasyclasses.events.DamageModifier;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.chat.ColorCode;

public class SeaGuardian_Proficiencies extends AbstractAbility {

	private double healthBonus = 10.0;
	private double damageReduction = 0.15;
	private final DamageModifier dmgMod; 
	
	public SeaGuardian_Proficiencies(Player p) {
		super(p);
		
		dmgMod = new DamageModifier(displayName, Operation.MULTIPLY_SCALAR_1, -damageReduction);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Sea Guardian Proficiencies";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.DIAMOND_CHESTPLATE);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntityEvent(CustomLivingEntityDamageEvent e) {
		if (!AbilityUtils.getTrueCause(e.getDamager()).equals(player))
			return;

		e.addModifier(dmgMod);
	}

	@Override
	public void init() {
		if(fplayer == null)
			return;
		
		AbilityUtils.setMaxHealth(player, new AttributeModifier("seaguardianproficiencies", healthBonus, Operation.ADD_NUMBER));
		this.fplayer.setArmorType(5);
	}
	
	@Override
	public String getInstructions() {
		return "Wear no armor stronger than " + ColorCode.DEFAULT_HIGHLIGHT + "Diamond " + ColorCode.INSTRUCTIONS
				+ "to use your abilities";
	}

	@Override
	public String getDescription() {
		return "&4Reduces your Damage &4by &6" + AbilityUtils.doubleRoundToXDecimals(damageReduction * 100, 1)
				+ "%&4. &aIncreases &ayour &aHealth by &6" + healthBonus;
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
	}

}
