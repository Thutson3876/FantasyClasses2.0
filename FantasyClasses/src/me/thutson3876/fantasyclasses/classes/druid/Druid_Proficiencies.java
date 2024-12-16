package me.thutson3876.fantasyclasses.classes.druid;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.EquipmentSlotGroup;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.ArmorType;
import me.thutson3876.fantasyclasses.util.chat.ColorCode;

public class Druid_Proficiencies extends AbstractAbility {

	private double healthBonus = 4.0;
	
	public Druid_Proficiencies(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Druid Proficiencies";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.LEATHER_CHESTPLATE);
	}
	
	@EventHandler
	public void onPlayerItemDamageEvent(PlayerItemDamageEvent e) {
		if(!e.getPlayer().equals(player))
			return;
		
		if(Arrays.asList(ArmorType.LEATHER.getMaterials()).contains(e.getItem().getType())) {
			Random rng = new Random();
			e.setDamage(rng.nextInt(2));
		}
	}

	@Override
	public String getInstructions() {
		return "Wear no armor stronger than " + ColorCode.DEFAULT_HIGHLIGHT + "Leather " + ColorCode.INSTRUCTIONS + "to use your abilities";
	}

	@Override
	public String getDescription() {
		return "&aIncreases your movement &aspeed &aby 10%. " + "&aIncreases &ayour &aHealth by &6" + healthBonus;
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}
	
	@Override
	protected void init() {
		if(fplayer == null)
			return;
		
		player.setWalkSpeed(0.22f);
		
		AbilityUtils.setMaxHealth(player, new AttributeModifier(new NamespacedKey(FantasyClasses.getPlugin(), "druidproficiencies"), healthBonus, Operation.ADD_NUMBER, EquipmentSlotGroup.ANY));
		this.fplayer.setArmorType(1);
	}

	@Override
	public void applyLevelModifiers() {	
		
	}
}
