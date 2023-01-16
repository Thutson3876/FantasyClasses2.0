package me.thutson3876.fantasyclasses.classes.combat;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.MaterialLists;

public class DualWielding extends AbstractAbility {

	private int breakDuration = 60;
	// private double healAmt = 2.0;
	private double healMod = 0.05;
	private AttributeModifier attackSpeed;

	public DualWielding(Player p) {
		super(p);
		this.attackSpeed = new AttributeModifier(displayName, 2.0, Operation.ADD_NUMBER);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 4 * 20;
		this.displayName = "Dual Wielding";
		this.skillPointCost = 3;
		this.maximumLevel = 1;

		this.createItemStack(Material.IRON_SWORD);
	}

	@EventHandler
	public void onSwapHandsEvent(PlayerSwapHandItemsEvent e) {
		if (e.isCancelled())
			return;

		if (!e.getPlayer().equals(this.player))
			return;

		ItemStack offhandItem = e.getOffHandItem();

		if (offhandItem == null) {
			swordPassiveOff();
			return;
		}

		if (MaterialLists.SWORD.getMaterials().contains(offhandItem.getType())) {
			swordPassiveOn();
		} else {
			swordPassiveOff();
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (!e.getPlayer().equals(this.player))
			return;

		if (e.getInventory().getSize() != 46)
			return;

		ItemStack offhandItem = e.getInventory().getItem(-106);

		if (offhandItem == null) {
			swordPassiveOff();
			return;
		} else if (MaterialLists.SWORD.getMaterials().contains(offhandItem.getType())) {
			swordPassiveOn();
		} else {
			swordPassiveOff();
		}
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageByEntityEvent e) {
		if (e.isCancelled())
			return;

		if (!e.getDamager().equals(this.player)) {
			return;
		}

		ItemStack offhand = this.player.getInventory().getItemInOffHand();

		if (offhand == null)
			return;
		Material mat = offhand.getType();

		if (MaterialLists.AXE.getMaterials().contains(mat)) {
			if (!(e.getEntity() instanceof Player)) {
				return;
			}
			Player target = (Player) e.getEntity();
			if (target.isBlocking()) {
				target.setCooldown(Material.SHIELD, this.breakDuration);
				AbilityTriggerEvent thisEvent = this.callEvent();
				this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
				return;
			}
		} else if (MaterialLists.HOE.getMaterials().contains(mat)) {
			if (!(e.getEntity() instanceof LivingEntity))
				return;

			if (e.getFinalDamage() > 1.0) {
				AbilityUtils.heal(this.player, e.getFinalDamage() * healMod, this.player);
				AbilityTriggerEvent thisEvent = this.callEvent();
				this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
			}
		}
	}

	private void swordPassiveOn() {
		if (!this.player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getModifiers().contains(attackSpeed))
			this.player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).addModifier(this.attackSpeed);
	}

	private void swordPassiveOff() {
		if (this.player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getModifiers().contains(attackSpeed))
			this.player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).removeModifier(this.attackSpeed);
	}

	@Override
	public String getName() {
		return this.displayName;
	}

	@Override
	public String getInstructions() {
		return "Equip a melee weapon in your offhand for a special bonus";
	}

	@Override
	public String getDescription() {
		return "While you have a sword in your offhand you attack faster, an axe allows you to break shields, and a hoe acts as a powerful rejuvenative scythe";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return currentLevel > 0;
	}

	@Override
	public void applyLevelModifiers() {
	}

	@Override
	public void deInit() {
		swordPassiveOff();
	}
}
