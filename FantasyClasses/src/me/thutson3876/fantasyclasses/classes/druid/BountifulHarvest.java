package me.thutson3876.fantasyclasses.classes.druid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.collectible.Collectible;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;

public class BountifulHarvest extends AbstractAbility {

	static List<ItemStack> drops = new ArrayList<>();
	
	static {
		drops.add(new ItemStack(Material.MUD, 8));
		drops.add(new ItemStack(Material.MYCELIUM, 4));
		drops.add(new ItemStack(Material.MANGROVE_PROPAGULE, 1));
		drops.add(new ItemStack(Material.GOLDEN_CARROT, 6));
		drops.add(new ItemStack(Material.CHORUS_PLANT, 3));
		drops.add(new ItemStack(Material.CHORUS_FRUIT, 6));
	}
	
	private double dropChance = 0.1;

	public BountifulHarvest(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Bountiful Harvest";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.GOLDEN_CARROT);
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		LivingEntity ent = e.getEntity();

		if (ent.getKiller() == null)
			return;

		if (!ent.getKiller().equals(player))
			return;
		
		if(ent.hasMetadata("noexpdrop"))
			return;

		if(!(ent instanceof Mob))
			return;
		
		Random rng = new Random();
		
		if(rng.nextDouble() > dropChance)
			return;
		
		if(rng.nextBoolean())
			player.sendMessage(ChatUtils.chat(Collectible.DRUIDIC_INSCRIPTION.getRandomLore()));
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		ent.getWorld().dropItemNaturally(ent.getLocation(), drops.get(rng.nextInt(drops.size())));
		ent.getWorld().playSound(ent.getLocation(), Sound.AMBIENT_CAVE, 0.8f, 0.8f);
		
		if(rng.nextDouble() < dropChance / 25.0) {
			ent.getWorld().dropItemNaturally(ent.getLocation(), new ItemStack(Material.TOTEM_OF_UNDYING));
			ent.getWorld().playSound(ent.getLocation(), Sound.ITEM_TOTEM_USE, 5.0f, 1.2f);
		}
		
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());

	}

	@Override
	public String getInstructions() {
		return "Kill a mob";
	}

	@Override
	public String getDescription() {
		return "Mobs have a chance to drop something extra";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {		
	}
}
