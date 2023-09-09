package me.thutson3876.fantasyclasses.classes.monk;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;

public class PressurePoints extends AbstractAbility {

	private int duration = 4 * 20;
	private PotionEffect weak = new PotionEffect(PotionEffectType.WEAKNESS, duration, 1);

	public PressurePoints(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 12 * 20;
		this.displayName = "Pressure Points";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.LEAD);
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if (e.isCancelled())
			return;

		if (isOnCooldown())
			return;

		if (!e.getDamager().equals(player))
			return;

		if (!e.getCause().equals(DamageCause.ENTITY_ATTACK))
			return;

		Entity damaged = e.getEntity();
		Entity damager = e.getDamager();
		float damagedYaw = (damaged.getLocation().getYaw() >= 0.0F) ? damaged.getLocation().getYaw()
				: (180.0F + -damaged.getLocation().getYaw());
		float damagerYaw = (damager.getLocation().getYaw() >= 0.0F) ? damager.getLocation().getYaw()
				: (180.0F + -damager.getLocation().getYaw());
		float angle = (damagedYaw - damagerYaw >= 0.0F) ? (damagedYaw - damagerYaw) : (damagerYaw - damagedYaw);
		if (angle > 50.0F) {
			return;
		}

		if (damaged instanceof LivingEntity) {
			AbilityTriggerEvent thisEvent = this.callEvent();

			if (thisEvent.isCancelled())
				return;

			((LivingEntity) damaged).setArrowCooldown(duration);
			((LivingEntity) damaged).addPotionEffect(weak);

			if (damaged instanceof Player) {

				Player target = ((Player) damaged);
				ItemStack used = target.getItemInUse();
				if (used != null)
					target.setCooldown(used.getType(), duration);

				ItemStack hand = target.getInventory().getItemInMainHand();
				if (hand != null)
					target.setCooldown(hand.getType(), duration);

				ItemStack off = target.getInventory().getItemInOffHand();
				if (off != null)
					target.setCooldown(off.getType(), duration);

			}

			this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
		}

	}

	@Override
	public String getInstructions() {
		return "Strike an entity from behind";
	}

	@Override
	public String getDescription() {
		return "Strike the pressure points of your opponent, causing them to have an attack cooldown and Weakness for &6"
				+ duration / 20 + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.coolDowninTicks = (18 - 6 * this.currentLevel) * 20;
	}

}
