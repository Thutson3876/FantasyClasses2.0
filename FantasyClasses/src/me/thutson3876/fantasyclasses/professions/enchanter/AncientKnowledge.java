package me.thutson3876.fantasyclasses.professions.enchanter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.collectible.Collectible;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.professions.enchanter.customenchantments.Enchantments;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;

public class AncientKnowledge extends AbstractAbility {

	static List<ItemStack> drops = new ArrayList<>();
	
	static {
		drops.add(new ItemStack(Material.BOOK, 7));
		drops.add(new ItemStack(Material.BOOKSHELF, 2));
		drops.add(new ItemStack(Material.LECTERN, 2));
		drops.add(new ItemStack(Material.END_ROD, 4));
		drops.add(new ItemStack(Material.HONEYCOMB_BLOCK, 2));
	}
	
	private double dropChance = 0.1;
	
	public AncientKnowledge(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Ancient Knowledge";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.BOOK);
	}

	@EventHandler
	public void onEntityDeathEvent(EntityDeathEvent e) {
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
			player.sendMessage(ChatUtils.chat(Collectible.ANCIENT_TECHNIQUE.getRandomLore()));
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		ent.getWorld().dropItemNaturally(ent.getLocation(), drops.get(rng.nextInt(drops.size())));
		ent.getWorld().playSound(ent.getLocation(), Sound.AMBIENT_CAVE, 0.8f, 0.8f);
		
		if(rng.nextDouble() < dropChance / 3.0) {
			ItemStack extra = new ItemStack(Material.ENCHANTED_BOOK);
			
			List<Enchantment> enchants = new ArrayList<>();
			for(Enchantments key : fplayer.getAvailableEnchantments(false).keySet())
				enchants.addAll(key.getEnchants());
			
			Enchantment ench = enchants.get(rng.nextInt(enchants.size()));
			
			extra.addEnchantment(ench, ench.getMaxLevel());
			
			ent.getWorld().dropItemNaturally(ent.getLocation(), extra);
			ent.getWorld().playSound(ent.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 5.0f, 1.2f);
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
