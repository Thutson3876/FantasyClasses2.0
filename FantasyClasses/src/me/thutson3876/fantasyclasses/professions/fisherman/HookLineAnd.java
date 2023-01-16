package me.thutson3876.fantasyclasses.professions.fisherman;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class HookLineAnd extends AbstractAbility {

	private double procChance = 0.01;

	private static List<EntityType> goodRolls = new ArrayList<>();
	private static List<EntityType> badRolls = new ArrayList<>();
	private static List<Material> goodItemRolls = new ArrayList<>();

	static {
		// Good rolls
		goodRolls.add(EntityType.BOAT);
		goodRolls.add(EntityType.DOLPHIN);
		goodRolls.add(EntityType.COD);
		goodRolls.add(EntityType.SALMON);
		goodRolls.add(EntityType.SKELETON_HORSE);
		goodRolls.add(EntityType.TURTLE);
		goodRolls.add(EntityType.AXOLOTL);
		goodRolls.add(EntityType.CAT);
		goodRolls.add(EntityType.DROPPED_ITEM);
		// Bad rolls
		badRolls.add(EntityType.PRIMED_TNT);
		badRolls.add(EntityType.CREEPER);
		badRolls.add(EntityType.DROWNED);
		badRolls.add(EntityType.GUARDIAN);
		badRolls.add(EntityType.PUFFERFISH);
		// Item Types
		goodItemRolls.add(Material.DIAMOND_BLOCK);
		goodItemRolls.add(Material.EMERALD_BLOCK);
		goodItemRolls.add(Material.GOLD_BLOCK);
		goodItemRolls.add(Material.IRON_BLOCK);
		goodItemRolls.add(Material.LAPIS_BLOCK);
		goodItemRolls.add(Material.REDSTONE_BLOCK);
		goodItemRolls.add(Material.COAL_BLOCK);
		goodItemRolls.add(Material.NAME_TAG);
	}

	public HookLineAnd(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 12 * 20;
		this.displayName = "Hook, Line, and ???";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.FISHING_ROD);
	}

	@EventHandler
	public void onPlayerFishEvent(PlayerFishEvent e) {
		if(this.isOnCooldown())
			return;
		
		if (!e.getState().equals(PlayerFishEvent.State.CAUGHT_FISH))
			return;

		if (!e.getPlayer().equals(player))
			return;

		this.onTrigger(roll(e));
	}

	private boolean roll(PlayerFishEvent e) {
		Random rng = new Random();
		Entity newEntity = null;

		if (rng.nextDouble() > this.procChance)
			return false;

		if (rng.nextDouble() > 0.1) {
			e.getCaught().remove();
			newEntity = player.getWorld().spawnEntity(e.getHook().getLocation(),
					goodRolls.get(rng.nextInt(goodRolls.size())));
			if (newEntity.getType().equals(EntityType.DROPPED_ITEM)) {
				Material type = goodItemRolls.get(rng.nextInt(goodItemRolls.size()));
				((Item) newEntity).setItemStack(new ItemStack(type));
			}

		} else {
			e.getCaught().remove();
			newEntity = player.getWorld().spawnEntity(e.getHook().getLocation(),
					badRolls.get(rng.nextInt(badRolls.size())));
		}

		if (newEntity != null) {
			e.getHook().addPassenger(newEntity);
		}
		
		return true;
	}

	@Override
	public String getInstructions() {
		return "Catch a fish";
	}

	@Override
	public String getDescription() {
		return "Roll a d20 when you catch a 'fish'";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
	
	}
}
