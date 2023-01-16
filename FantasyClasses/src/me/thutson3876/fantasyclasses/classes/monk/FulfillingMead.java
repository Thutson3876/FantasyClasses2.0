package me.thutson3876.fantasyclasses.classes.monk;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class FulfillingMead extends AbstractAbility {

	private static int duration = 6 * 20;
	private static PotionEffect saturation = new PotionEffect(PotionEffectType.SATURATION, duration, 1);
	//Defensives
	private static List<PotionEffect> defensives = Arrays.asList(new PotionEffect[]{
			new PotionEffect(PotionEffectType.REGENERATION, duration, 1), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, 1)});
	//Offensives
	private static List<PotionEffect> offensives = Arrays.asList(new PotionEffect[]{
			new PotionEffect(PotionEffectType.INCREASE_DAMAGE, duration, 1), new PotionEffect(PotionEffectType.SPEED, duration, 1)});
	
	private boolean isOffensive = false;
	
	public FulfillingMead(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 16 * 20;
		this.displayName = "Fulfilling Mead";
		this.skillPointCost = 2;
		this.maximumLevel = 1;
		
		this.createItemStack(Material.HONEY_BOTTLE);
	}

	@EventHandler
	public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
		if(!e.getPlayer().equals(player))
			return;
		
		if(isOnCooldown())
			return;
		
		if(!e.getItem().getType().equals(Material.HONEY_BOTTLE))
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		if(thisEvent.isCancelled())
			return;
		
		player.addPotionEffect(saturation);
		player.setExhaustion(0.0f);
		
		if(isOffensive) {
			player.addPotionEffects(offensives);
			AbilityUtils.heal(player, 4.0, player);
		}	
		else
			player.addPotionEffects(defensives);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Drink a bottle of honey";
	}

	@Override
	public String getDescription() {
		return "Drinking a bottle of honey (mead) will greatly improve your stamina for a short time, granting you Saturation, Regeneration, and Resistance for &6" + (duration / 20) + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		
	}

	public void toggleOffensives(boolean isOffensive) {
		this.isOffensive = isOffensive;
	}
}
