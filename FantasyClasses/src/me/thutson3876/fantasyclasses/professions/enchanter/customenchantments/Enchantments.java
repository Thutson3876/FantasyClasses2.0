package me.thutson3876.fantasyclasses.professions.enchanter.customenchantments;

import java.util.Arrays;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import me.thutson3876.fantasyclasses.FantasyClasses;

public enum Enchantments {

	CURSE(Enchantment.BINDING_CURSE, Enchantment.VANISHING_CURSE), 
	UNCOMMON(Enchantment.BANE_OF_ARTHROPODS, Enchantment.SMITE, Enchantment.BLAST_PROTECTION,
			Enchantment.FEATHER_FALLING, Enchantment.FIRE_PROTECTION, Enchantment.PROJECTILE_PROTECTION,
			Enchantment.DEPTH_STRIDER, Enchantment.FLAME, Enchantment.PUNCH, Enchantment.LURE,
			Enchantment.RESPIRATION, Enchantment.QUICK_CHARGE, Enchantment.SWEEPING_EDGE, Enchantment.AQUA_AFFINITY,
			Enchantment.KNOCKBACK, Enchantment.IMPALING),
	RARE(Enchantment.POWER, Enchantment.SHARPNESS, Enchantment.PROTECTION,
			Enchantment.EFFICIENCY, Enchantment.INFINITY, Enchantment.UNBREAKING, Enchantment.LOYALTY,
			Enchantment.CHANNELING, Enchantment.LUCK_OF_THE_SEA, Enchantment.RIPTIDE, Enchantment.FIRE_ASPECT,
			Enchantment.FROST_WALKER, Enchantment.MULTISHOT, Enchantment.PIERCING, Enchantment.SWIFT_SNEAK,
			Enchantment.SOUL_SPEED, Enchantment.SILK_TOUCH),
	EPIC(Enchantment.MENDING, Enchantment.FORTUNE, Enchantment.LOOTING), 
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
