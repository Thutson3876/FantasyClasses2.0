package me.thutson3876.fantasyclasses.classes.combat;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.MaterialLists;

public class Swordsman extends AbstractAbility {

	private final float speedMod = 0.45f;
	private AttributeModifier attackSpeed;

	public Swordsman(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Swordsman";
		this.skillPointCost = 1;
		this.maximumLevel = 6;
		this.attackSpeed = new AttributeModifier(new UUID(6, 3), "Swordsman", speedMod, Operation.ADD_NUMBER,
				EquipmentSlot.HAND);

		this.createItemStack(Material.IRON_SWORD);
	}

	@EventHandler
	public void onPlayerItemHeldEvent(PlayerItemHeldEvent e) {
		if (!e.getPlayer().equals(this.player))
			return;

		AttributeInstance att = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
		if (player.getInventory().getItem(e.getNewSlot()) != null && MaterialLists.SWORD.getMaterials()
				.contains(player.getInventory().getItem(e.getNewSlot()).getType())) {
			if (!att.getModifiers().contains(attackSpeed)) {
				AbilityTriggerEvent thisEvent = this.callEvent();
				player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).addModifier(attackSpeed);
				this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
				return;
			}
			return;
		} else {
			if (att.getModifiers().contains(attackSpeed)) {
				player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).removeModifier(attackSpeed);
				return;
			}
		}
	}
	
	@EventHandler
	public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {
		if(!e.getPlayer().equals(this.player))
			return;
		
		ItemStack mainHand = e.getMainHandItem();
		AttributeInstance att = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
		
		if(mainHand == null || !MaterialLists.SWORD.contains(mainHand.getType())) {
			if (att.getModifiers().contains(attackSpeed)) {
				player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).removeModifier(attackSpeed);
				return;
			}
		}
		else {
			if (!att.getModifiers().contains(attackSpeed)) {
				AbilityTriggerEvent thisEvent = this.callEvent();
				player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).addModifier(attackSpeed);
				this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
				return;
			}
		}
	}

	@Override
	public String getName() {
		return displayName;
	}

	@Override
	public String getInstructions() {
		return "Wield a sword";
	}

	@Override
	public String getDescription() {
		return "Your sword swings have their attack speed increased by &6" + AbilityUtils.doubleRoundToXDecimals(speedMod * currentLevel, 3) ;
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
		attackSpeed = new AttributeModifier("Swordsman", speedMod * currentLevel, Operation.ADD_NUMBER);
	}
	
	@Override
	public void deInit() {
		if(player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getModifiers().contains(attackSpeed))
			player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).removeModifier(attackSpeed);
	}

}
