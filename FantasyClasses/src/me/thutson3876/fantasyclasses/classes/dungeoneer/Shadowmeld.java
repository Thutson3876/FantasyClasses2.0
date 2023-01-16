package me.thutson3876.fantasyclasses.classes.dungeoneer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class Shadowmeld extends AbstractAbility {

	private static List<PotionEffectType> POTION_EFFECTS = new ArrayList<>();
	
	static {
		POTION_EFFECTS.add(PotionEffectType.INCREASE_DAMAGE);
		POTION_EFFECTS.add(PotionEffectType.INVISIBILITY);
	}
	
	public Shadowmeld(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Shadowmeld";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.OBSIDIAN);
	}

	@Override
	public String getInstructions() {
		return "Activates with &6Dungeon &6Delver";
	}

	@Override
	public String getDescription() {
		return "Your dungeon delver now also applies invisibility and strength";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
	}
	
	public static List<PotionEffectType> getPotionEffects(){
		return POTION_EFFECTS;
	}

}
