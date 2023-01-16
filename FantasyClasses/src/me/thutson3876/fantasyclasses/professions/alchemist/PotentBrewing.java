package me.thutson3876.fantasyclasses.professions.alchemist;

import java.util.List;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.CauldronBrewEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.PotionList;

public class PotentBrewing extends AbstractAbility {

	private double chance = 0.1;
	private int maxAmp = 1;
	private int minAmp = 0;
	private int maxDuration = 40;
	private int minDuration = 20;

	public PotentBrewing(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Potent Brewing";
		this.skillPointCost = 1;
		this.maximumLevel = 10;

		this.createItemStack(Material.NETHER_WART);
	}
	
	public ItemStack newTrigger(Event event) {
		if (!(event instanceof CauldronBrewEvent))
			return null;

		CauldronBrewEvent e = (CauldronBrewEvent) event;

		ItemStack prevResult = e.getResult();

		if (!e.getPlayer().equals(player))
			return prevResult;

		Random rng = new Random();
		if (rng.nextDouble() > chance)
			return prevResult;

		ItemStack i = e.getResult();

		ItemMeta meta = i.getItemMeta();
		if (!(meta instanceof PotionMeta)) {
			return prevResult;
		}
		PotionMeta potMeta = (PotionMeta) meta;

		PotionEffectType type = potMeta.getCustomEffects().get(0).getType();
		potMeta.addCustomEffect(potMeta.getCustomEffects().get(0), true);

		List<PotionEffectType> buffs = PotionList.BUFF.getPotList();
		List<PotionEffectType> debuffs = PotionList.DEBUFF.getPotList();
		if (i == null || (!i.getType().equals(Material.POTION) && !i.getType().equals(Material.SPLASH_POTION))) {
			return prevResult;
		}

		PotionEffectType newType = null;
		if (buffs.contains(type)) {
			potMeta.setColor(Color.YELLOW);
			do {
				newType = buffs.get(rng.nextInt(buffs.size()));
			} while (newType.equals(type) || potMeta.hasCustomEffect(newType));

			potMeta.addCustomEffect(new PotionEffect(newType, (rng.nextInt(maxDuration) + minDuration) * 20,
					rng.nextInt(maxAmp) + minAmp), false);

		} else if (debuffs.contains(type)) {
			potMeta.setColor(Color.BLACK);
			do {
				newType = debuffs.get(rng.nextInt(debuffs.size()));
			} while (newType.equals(type) || potMeta.hasCustomEffect(newType));
			potMeta.addCustomEffect(new PotionEffect(newType,
					(rng.nextInt((int) (maxDuration * 0.5)) + minDuration) * 20, rng.nextInt(maxAmp) + minAmp), false);
		}

		i.setItemMeta(potMeta);
		e.setResult(i);
		return i;
	}

	@Override
	public String getInstructions() {
		return "Brew a potion using Enhanced Repitoire";
	}

	@Override
	public String getDescription() {
		return "When brewing using Enhanced Repitoire, gain a &6" + AbilityUtils.doubleRoundToXDecimals(chance * 100, 2)
				+ "% &rchance to give it a random additional effect. Bonus duration at level 5 and bonus potency at level 8";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		chance = 0.15 * currentLevel;
		if (currentLevel > 4) {
			maxDuration = 180;
			minDuration = 45;
		}
		if (currentLevel > 7) {
			maxAmp = 3;
			minAmp = 1;
		}

	}

}
