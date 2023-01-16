package me.thutson3876.fantasyclasses.professions.enchanter;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.professions.enchanter.customenchantments.Enchantments;

public class Enchanting103 extends AbstractAbility {
	private int maxLevel = 1;

	public Enchanting103(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Enchanting 103";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.ENDER_CHEST);
	}

	@Override
	public String getInstructions() {
		return "Enchant an item";
	}

	@Override
	public String getDescription() {
		return "Increase the maximum enchantment level of your &dEpic &renchantments to &6" + maxLevel;
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void deInit() {
		if (this.fplayer == null)
			return;

		fplayer.disallowEnchantments(Enchantments.EPIC);
	}

	@Override
	public void applyLevelModifiers() {
		this.maxLevel = this.currentLevel * 2 - 1;

		if (this.fplayer == null)
			return;

		fplayer.putEnchantments(Enchantments.EPIC, maxLevel);
	}
}
