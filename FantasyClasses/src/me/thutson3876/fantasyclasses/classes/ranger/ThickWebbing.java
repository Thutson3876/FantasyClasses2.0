package me.thutson3876.fantasyclasses.classes.ranger;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class ThickWebbing extends AbstractAbility {

	private int amp = 0;
	private int duration = 3 * 20;

	private PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, duration, amp);
	private PotionEffect miningFatigue = new PotionEffect(PotionEffectType.SLOW_DIGGING, duration, amp);
	private PotionEffect unluck = new PotionEffect(PotionEffectType.UNLUCK, duration, amp);

	public ThickWebbing(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Thick Webbing";
		this.skillPointCost = 1;
		this.maximumLevel = 1;
		
		this.createItemStack(Material.COBWEB);
	}

	@Override
	public String getInstructions() {
		return "Catch a creature in your &6Web Trap";
	}

	@Override
	public String getDescription() {
		return "Your &6Web Trap &ris no longer hollow. Additionally, any creature caught in it when it triggers is afflicted with Blindness, Mining Fatigue, and Unluck &6"
				+ (amp + 1) + " &rfor &6" + duration / 20 + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		if(fplayer == null || fplayer.getChosenClass() == null)
			return;
		
		Ability abil = fplayer.getClassAbility(WebTrap.class);
		if (abil == null)
			return;

		WebTrap webTrap = (WebTrap) abil;

		if (this.currentLevel > 0)
			webTrap.setHollow(false);

		List<PotionEffect> potionEffects = new ArrayList<>();

		this.duration = 5 * 20 * (this.currentLevel);
		this.amp = this.currentLevel;

		blindness = new PotionEffect(PotionEffectType.BLINDNESS, duration, amp);
		miningFatigue = new PotionEffect(PotionEffectType.SLOW_DIGGING, duration, amp);
		unluck = new PotionEffect(PotionEffectType.UNLUCK, duration, amp);

		potionEffects.add(blindness);
		potionEffects.add(miningFatigue);
		potionEffects.add(unluck);

		webTrap.setPotionEffects(potionEffects);
		if (this.currentLevel <= 0) {
			webTrap.setPotionEffects(new ArrayList<>());
			webTrap.setHollow(true);
		}
	}

}
