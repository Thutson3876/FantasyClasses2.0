package me.thutson3876.fantasyclasses.classes.witch;

import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class WitchHunt extends AbstractAbility {

	public WitchHunt(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Witch Hunt";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.IRON_AXE);
	}

	@EventHandler
	public void EntityDeathEvent(EntityDeathEvent e) {

		LivingEntity ent = e.getEntity();

		if (ent.getKiller() == null)
			return;

		if (!ent.getKiller().equals(player))
			return;

		if (!ent.getType().equals(EntityType.WITCH))
			return;

		Collection<ItemStack> drops = e.getDrops();
		if (drops == null)
			drops = new ArrayList<>();

		if (!drops.isEmpty()) {
			drops.addAll(drops);
			drops.addAll(drops);
		}

		e.setDroppedExp(e.getDroppedExp() * 3);
		drops.add(WitchBrewRecipe.getRandom());

		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITCH_CELEBRATE, 0.7f, 1.2f);
	}

	@Override
	public String getInstructions() {
		return "Kill a witch";
	}

	@Override
	public String getDescription() {
		return "Kill other witches to absorb their power and attain their possessions";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
	}

}
