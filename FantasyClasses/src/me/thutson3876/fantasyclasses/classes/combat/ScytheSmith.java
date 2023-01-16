package me.thutson3876.fantasyclasses.classes.combat;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.MaterialLists;

public class ScytheSmith extends AbstractAbility {

	private static Map<Material, Integer> typeBonus = new HashMap<>();
	private double damageMod = 1.5;
	private double aoeRange = 0.9;
	private final double attackSpeedMod = -3.2;
	private AttributeModifier attackSpeed;
	private AttributeModifier attackDamage;
	
	private final int maxTargets = 10; //  > 0
    private final double maxRange = 3.5D;
    private final double maxAngleCos = Math.cos(Math.toRadians(60)); // an angle between 0 and 90 degree

	static {
		typeBonus.put(Material.WOODEN_HOE, 1);
		typeBonus.put(Material.STONE_HOE, 2);
		typeBonus.put(Material.IRON_HOE, 3);
		typeBonus.put(Material.GOLDEN_HOE, 2);
		typeBonus.put(Material.DIAMOND_HOE, 4);
		typeBonus.put(Material.NETHERITE_HOE, 5);
	}

	public ScytheSmith(Player p) {
		super(p);
		
		this.attackSpeed = new AttributeModifier(new UUID(8, 3), "Scythe Smith", attackSpeedMod, Operation.ADD_NUMBER,
				EquipmentSlot.HAND);
		this.attackDamage = new AttributeModifier(new UUID(9, 3), "Scythe Smith", damageMod, Operation.ADD_NUMBER,
				EquipmentSlot.HAND);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Scythe Smith";
		this.skillPointCost = 1;
		this.maximumLevel = 4;
		
		this.createItemStack(Material.IRON_HOE);
	}

	@EventHandler
	public void onCraftItemEvent(CraftItemEvent e) {
		if (!e.getWhoClicked().equals(this.player)) {
			return;
		}

		ItemStack item = e.getCurrentItem();

		if (!MaterialLists.HOE.contains(item.getType()))
			return;

		ItemMeta meta = item.getItemMeta();
		if (meta == null)
			return;
		Collection<AttributeModifier> speedMods = meta.getAttributeModifiers(Attribute.GENERIC_ATTACK_SPEED);
		if (speedMods != null && speedMods.contains(attackSpeed)) {
			return;
		}
		Collection<AttributeModifier> dmgMods = meta.getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE);
		if (dmgMods != null && dmgMods.contains(attackDamage)) {
			return;
		}

		AbilityTriggerEvent thisEvent = this.callEvent();
		
		meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeed);
		meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,
				new AttributeModifier(new UUID(9, 3), "Scythe Smith",
						(damageMod * currentLevel) + typeBonus.get(item.getType()), Operation.ADD_NUMBER,
						EquipmentSlot.HAND));
		item.setItemMeta(meta);
		e.setCurrentItem(item);
		
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (!e.getPlayer().equals(player))
			return;
		
		if(this.isOnCooldown())
			return;

		Material itemType = player.getInventory().getItemInMainHand().getType();
		
		if (player.getAttackCooldown() < 1.0 || player.getCooldown(itemType) > 0)
			return;

		if (!MaterialLists.HOE.contains(itemType))
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		ItemStack item = e.getItem();
		World world = player.getWorld();
		double damage = (damageMod * currentLevel) + typeBonus.get(item.getType());
		
		for(LivingEntity le : findTargets(player)) {
			if(!le.isDead()) {
				le.damage(damage, player);
				world.playEffect(le.getEyeLocation(), Effect.LAVA_INTERACT, 5);
			}
		}
		
		player.setCooldown(itemType, 32);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

    private Set<LivingEntity> findTargets(Player player) {
        Set<LivingEntity> targets = new HashSet<LivingEntity>();
        if (player != null) {
            Vector playerLocation = player.getEyeLocation().toVector();
            Vector playerViewDirection = player.getEyeLocation().getDirection().normalize();
            for (Entity entity : player.getNearbyEntities(maxRange, maxRange, maxRange)) {
                if (entity instanceof LivingEntity && !entity.equals(player)) {
                    LivingEntity livingEntity = (LivingEntity) entity;
 
                    // check angle to target:
                    Vector toTarget = livingEntity.getEyeLocation().toVector().subtract(playerLocation).normalize();
                    double dotProduct = toTarget.dot(playerViewDirection);
                    if (dotProduct >= maxAngleCos) {
                        targets.add(livingEntity);
                        if (targets.size() >= maxTargets) break;
                    }
                }
            }
        }
        return targets;
    }
	
	@Override
	public String getInstructions() {
		return "Craft a hoe";
	}

	@Override
	public String getDescription() {
		return "Your crafted scythes have their attack speed decreased by &6" + attackSpeedMod
				+ " &rand their damage increased by &6" + damageMod * currentLevel
				+ "&r. When you attack with a scythe, it deals its damage to all mobs within &6" + this.aoeRange
				+ " &rblocks of your target";
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

}
