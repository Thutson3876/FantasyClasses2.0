package me.thutson3876.fantasyclasses.util;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.potion.PotionEffectType;

public enum PotionList {

	BUFF, DEBUFF, DAMAGING;

	private List<PotionEffectType> potList = new ArrayList<>();

	static {

		// BUFF
		BUFF.potList.add(PotionEffectType.ABSORPTION);
		BUFF.potList.add(PotionEffectType.CONDUIT_POWER);
		BUFF.potList.add(PotionEffectType.DAMAGE_RESISTANCE);
		BUFF.potList.add(PotionEffectType.DOLPHINS_GRACE);
		BUFF.potList.add(PotionEffectType.FAST_DIGGING);
		BUFF.potList.add(PotionEffectType.FIRE_RESISTANCE);
		BUFF.potList.add(PotionEffectType.HEAL);
		BUFF.potList.add(PotionEffectType.HEALTH_BOOST);
		BUFF.potList.add(PotionEffectType.HERO_OF_THE_VILLAGE);
		BUFF.potList.add(PotionEffectType.INCREASE_DAMAGE);
		BUFF.potList.add(PotionEffectType.INVISIBILITY);
		BUFF.potList.add(PotionEffectType.JUMP);
		BUFF.potList.add(PotionEffectType.LUCK);
		BUFF.potList.add(PotionEffectType.NIGHT_VISION);
		BUFF.potList.add(PotionEffectType.REGENERATION);
		BUFF.potList.add(PotionEffectType.SATURATION);
		BUFF.potList.add(PotionEffectType.SLOW_FALLING);
		BUFF.potList.add(PotionEffectType.SPEED);
		BUFF.potList.add(PotionEffectType.WATER_BREATHING);
		// DEBUFF
		DEBUFF.potList.add(PotionEffectType.BAD_OMEN);
		DEBUFF.potList.add(PotionEffectType.BLINDNESS);
		DEBUFF.potList.add(PotionEffectType.CONFUSION);
		DEBUFF.potList.add(PotionEffectType.GLOWING);
		DEBUFF.potList.add(PotionEffectType.HARM);
		DEBUFF.potList.add(PotionEffectType.HUNGER);
		DEBUFF.potList.add(PotionEffectType.LEVITATION);
		DEBUFF.potList.add(PotionEffectType.POISON);
		DEBUFF.potList.add(PotionEffectType.SLOW);
		DEBUFF.potList.add(PotionEffectType.SLOW_DIGGING);
		DEBUFF.potList.add(PotionEffectType.UNLUCK);
		DEBUFF.potList.add(PotionEffectType.WEAKNESS);
		DEBUFF.potList.add(PotionEffectType.WITHER);
		// DAMAGING
		DEBUFF.potList.add(PotionEffectType.HARM);
		DEBUFF.potList.add(PotionEffectType.POISON);
		DEBUFF.potList.add(PotionEffectType.WITHER);
	}

	public List<PotionEffectType> getPotList() {
		return this.potList;
	}

}
