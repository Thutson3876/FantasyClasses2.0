package me.thutson3876.fantasyclasses.professions.enchanter;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.professions.enchanter.customenchantments.Enchantments;

public class Enchanting101 extends AbstractAbility {

	private int maxLevel = 3;
	
	public Enchanting101(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Enchanting 101";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.LECTERN);
	}

	@Override
	public String getInstructions() {
		return "Enchant an item";
	}

	@Override
	public String getDescription() {
		return "Increase the maximum enchantment level of your &2Uncommon &renchantments to &6" + maxLevel;
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}
	
	@Override
	public void deInit() {
		if(this.fplayer == null)
			return;
		
		fplayer.allowEnchantments(Enchantments.UNCOMMON);
	}

	@Override
	public void applyLevelModifiers() {
		this.maxLevel = this.currentLevel * 2 + 1;
		
		if(this.fplayer == null)
			return;
		
		fplayer.putEnchantments(Enchantments.UNCOMMON, maxLevel);
	}

}
