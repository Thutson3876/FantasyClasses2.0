package me.thutson3876.fantasyclasses.classes.seaguardian;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.events.CustomLivingEntityDamageEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class PolearmMastery extends AbstractAbility {

	private PotionEffect haste = new PotionEffect(PotionEffectType.HASTE, 4 * 20, 1);
	
	public PolearmMastery(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Polearm Mastery";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.TRIDENT);
	}

	@EventHandler
	public void onCustomLivingEntityDamageEvent(CustomLivingEntityDamageEvent e) {
		if(e.isCancelled())
			return;
		
		if(!(e.getDamager() instanceof Trident))
			return;
		
		Trident trident = (Trident) e.getDamager();
		
		if(trident.getShooter() == null || !trident.getShooter().equals(player))
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		AbilityUtils.applyStackingPotionEffect(haste, player, 10, 4 * 20);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Throw a trident";
	}

	@Override
	public String getDescription() {
		return "Your thrown tridents empower you. Each thrown trident hit grants you stacking &dHaste";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {

	}

}
