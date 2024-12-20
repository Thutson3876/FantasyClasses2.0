package me.thutson3876.fantasyclasses.professions.hunter;

import static java.util.Map.entry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class HeadHunter extends AbstractAbility {

	static final Map<EntityType, Material> headMap = Map.ofEntries(
			entry(EntityType.CREEPER, Material.CREEPER_HEAD),
			entry(EntityType.ZOMBIE, Material.ZOMBIE_HEAD),
			entry(EntityType.PIGLIN, Material.PIGLIN_HEAD),
			entry(EntityType.SKELETON, Material.SKELETON_SKULL),
			entry(EntityType.WITHER_SKELETON, Material.WITHER_SKELETON_SKULL)
			);
	
	private double dropChance = 0.05;
	
	public HeadHunter(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Head Hunter";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.CREEPER_HEAD);
	}

	@EventHandler
	public void EntityDeathEvent(EntityDeathEvent e) {

		LivingEntity ent = e.getEntity();

		if (ent.getKiller() == null)
			return;

		if (!ent.getKiller().equals(player))
			return;
		
		if(!headMap.containsKey(e.getEntityType()))
			return;
		
		Random rng = new Random();
		
		double chance = dropChance;
		if(e.getEntityType().equals(EntityType.WITHER_SKELETON))
			chance /= 3;
		
		if(rng.nextDouble() > chance)
			return;
		
		ItemStack head = new ItemStack(headMap.get(e.getEntityType()));
		
		Collection<ItemStack> drops = e.getDrops();
		if (drops == null)
			drops = new ArrayList<>();

		if (!drops.isEmpty()) {
			drops.add(head);
		}
		
		// player.getWorld().playSound(player.getLocation(), Sound., 0.7f, 1.2f);
	}

	@Override
	public String getInstructions() {
		return "Kill a mob";
	}

	@Override
	public String getDescription() {
		return "Mobs have a &6" + AbilityUtils.doubleRoundToXDecimals(dropChance * 100, 1) + "% &rchance to drop their head when you kill them";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {

	}

}
