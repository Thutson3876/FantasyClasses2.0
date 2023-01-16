package me.thutson3876.fantasyclasses.classes.ranger;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;

public class NaturalRemedy extends AbstractAbility {

	private int amp = 0;
	
	public NaturalRemedy(Player p) {
		super(p, Priority.LOW);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Natural Remedy";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.DARK_OAK_LEAVES);
	}

	@EventHandler
	public void onAbilityTriggerEvent(AbilityTriggerEvent e) {
		if(!(e.getAbility() instanceof Camoflauge))
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		if(thisEvent.isCancelled())
			return;
		
		e.getFplayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, ((Camoflauge)e.getAbility()).getDuration(), amp));
		e.getFplayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, ((Camoflauge)e.getAbility()).getDuration(), amp + 1));
		
		//this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Cast &6Camoflauge";
	}

	@Override
	public String getDescription() {
		return "At level 1: &6Camoflauge applies Jump Boost. At level 2: Camoflauge also applies Regeneration";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
	}

}
