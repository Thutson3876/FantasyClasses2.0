package me.thutson3876.fantasyclasses.professions.enchanter;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.professions.enchanter.customenchantments.Enchantments;

public class Enchanting102 extends AbstractAbility {
	private int maxLevel = 3;
	
	public Enchanting102(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Enchanting 102";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.ENCHANTING_TABLE);
	}

	@Override
	public String getInstructions() {
		return "Enchant an item";
	}

	@Override
	public String getDescription() {
		return "Increase the maximum enchantment level of your &9Rare &renchantments to &6" + maxLevel;
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}
	
	@Override
	public void deInit() {
		if(this.fplayer == null)
			return;
		
		fplayer.disallowEnchantments(Enchantments.RARE);
	}

	@Override
	public void applyLevelModifiers() {
		this.maxLevel = this.currentLevel * 2 + 1;
		
		if(this.fplayer == null)
			return;
		
		fplayer.putEnchantments(Enchantments.RARE, maxLevel);
	}
}
