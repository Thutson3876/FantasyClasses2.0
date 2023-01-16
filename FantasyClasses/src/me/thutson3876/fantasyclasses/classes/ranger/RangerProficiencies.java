package me.thutson3876.fantasyclasses.classes.ranger;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.ArmorType;

public class RangerProficiencies extends AbstractAbility {

	public RangerProficiencies(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Ranger Proficiencies";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.LEATHER);
	}
	
	@EventHandler
	public void onPlayerItemDamageEvent(PlayerItemDamageEvent e) {
		if(!e.getPlayer().equals(player))
			return;
		
		if(Arrays.asList(ArmorType.LEATHER.getMaterials()).contains(e.getItem().getType())) {
			e.setDamage(1);
		}
	}

	@Override
	public String getInstructions() {
		return "Wear no armor stronger than &6Leather &3to use your abilities";
	}

	@Override
	public String getDescription() {
		return "&aIncreases your movement &aspeed &aby 10%";
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
		this.fplayer.setArmorType(1);
	}

	@Override
	public void applyLevelModifiers() {	
		
	}

}
