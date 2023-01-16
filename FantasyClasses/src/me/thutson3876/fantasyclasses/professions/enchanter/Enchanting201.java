package me.thutson3876.fantasyclasses.professions.enchanter;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.professions.enchanter.customenchantments.Enchantments;

public class Enchanting201 extends AbstractAbility {

	public Enchanting201(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Enchanting 201";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.END_PORTAL_FRAME);
	}

	@Override
	public String getInstructions() {
		return "Enchant an item";
	}

	@Override
	public String getDescription() {
		return "Unlock the lost secrets of ancient enchantments. You may now apply &6Custom Enchantments &rfound via recovering enchantment books from &6Ancient &6Knowledge&r, unique mobs, and bosses";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void deInit() {
		if (this.fplayer == null)
			return;

		fplayer.disallowEnchantments(Enchantments.CUSTOM);
	}

	@Override
	public void applyLevelModifiers() {
		if (this.fplayer == null)
			return;

		fplayer.putEnchantments(Enchantments.CUSTOM, 3);
	}
}
