package me.thutson3876.fantasyclasses.classes.alchemy;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class DeadlyPoison extends AbstractAbility {

	private int durationInTicks = 15;
	private PotionEffect poison;

	public DeadlyPoison(Player p) {
		super(p, Priority.LOW);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 15;
		this.displayName = "Deadly Poison";
		this.skillPointCost = 1;
		this.maximumLevel = 2;
		poison = new PotionEffect(PotionEffectType.POISON, durationInTicks, 0);

		this.createItemStack(Material.GREEN_DYE);
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;

		if (!e.getDamager().equals(player))
			return;

		if (!(e.getEntity() instanceof LivingEntity))
			return;

		if(e.getCause().equals(DamageCause.POISON))
			return;
		
		LivingEntity target = (LivingEntity) e.getEntity();

		AbilityUtils.applyStackingPotionEffect(poison, target, 9, 30);
		
		this.onTrigger(false);
	}

	@Override
	public String getInstructions() {
		return "Attack an entity";
	}

	@Override
	public String getDescription() {
		return "Your attacks apply poison for &6" + durationInTicks / 20.0
				+ "&r seconds. If the target is already poisoned, increase its potency and reset its duration";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		durationInTicks = 15 * currentLevel;
		poison = new PotionEffect(PotionEffectType.POISON, durationInTicks, 0);
	}

}
