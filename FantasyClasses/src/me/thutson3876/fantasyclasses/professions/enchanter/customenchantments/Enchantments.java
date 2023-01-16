package me.thutson3876.fantasyclasses.professions.enchanter.customenchantments;

import java.util.Arrays;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import me.thutson3876.fantasyclasses.FantasyClasses;

public enum Enchantments {

	CURSE(Enchantment.BINDING_CURSE, Enchantment.VANISHING_CURSE), 
	UNCOMMON(Enchantment.DAMAGE_ARTHROPODS, Enchantment.DAMAGE_UNDEAD, Enchantment.PROTECTION_EXPLOSIONS,
			Enchantment.PROTECTION_FALL, Enchantment.PROTECTION_FIRE, Enchantment.PROTECTION_PROJECTILE,
			Enchantment.DEPTH_STRIDER, Enchantment.ARROW_FIRE, Enchantment.ARROW_KNOCKBACK, Enchantment.LURE,
			Enchantment.OXYGEN, Enchantment.QUICK_CHARGE, Enchantment.SWEEPING_EDGE, Enchantment.WATER_WORKER,
			Enchantment.KNOCKBACK, Enchantment.IMPALING),
	RARE(Enchantment.ARROW_DAMAGE, Enchantment.DAMAGE_ALL, Enchantment.PROTECTION_ENVIRONMENTAL,
			Enchantment.DIG_SPEED, Enchantment.ARROW_INFINITE, Enchantment.DURABILITY, Enchantment.LOYALTY,
			Enchantment.CHANNELING, Enchantment.LUCK, Enchantment.RIPTIDE, Enchantment.FIRE_ASPECT,
			Enchantment.FROST_WALKER, Enchantment.MULTISHOT, Enchantment.PIERCING, Enchantment.SWIFT_SNEAK,
			Enchantment.SOUL_SPEED, Enchantment.SILK_TOUCH),
	EPIC(Enchantment.MENDING, Enchantment.LOOT_BONUS_BLOCKS, Enchantment.LOOT_BONUS_MOBS), 
	CUSTOM(new Venom(new NamespacedKey(FantasyClasses.getPlugin(), "venom")), new Quickening(new NamespacedKey(FantasyClasses.getPlugin(), "quickening")), 
			new RockEater(new NamespacedKey(FantasyClasses.getPlugin(), "rockeater")), new Blaze(new NamespacedKey(FantasyClasses.getPlugin(), "blaze")), 
			new Cloaking(new NamespacedKey(FantasyClasses.getPlugin(), "cloaking")), new Crippling(new NamespacedKey(FantasyClasses.getPlugin(), "crippling")), 
			new Molten(new NamespacedKey(FantasyClasses.getPlugin(), "molten")), new Drunk(new NamespacedKey(FantasyClasses.getPlugin(), "drunk")), 
			new IceAspect(new NamespacedKey(FantasyClasses.getPlugin(), "iceaspect")), new Momentum(new NamespacedKey(FantasyClasses.getPlugin(), "momentum")), 
			new Transposition(new NamespacedKey(FantasyClasses.getPlugin(), "transposition")));

	private final List<Enchantment> ENCHANTS;
	
	private Enchantments(Enchantment... enchants) {
		this.ENCHANTS = Arrays.asList(enchants);
	}

	public List<Enchantment> getEnchants() {
		return ENCHANTS;
	}
}
