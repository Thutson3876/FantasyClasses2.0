package me.thutson3876.fantasyclasses.classes.witch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class WitchesCauldron extends AbstractAbility {

	public WitchesCauldron(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Witch's Cauldron";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.CAULDRON);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if(!e.getPlayer().equals(player))
			return;
		
		if(isOnCooldown())
			return;
		
		if(e.getHand() == null)
			return;
		
		if(player.getInventory().getItem(e.getHand()) == null)
			return;
		
		if(!player.getInventory().getItem(e.getHand()).getType().equals(Material.STICK))
			return;
		
		Block block = e.getClickedBlock();
		if(block == null)
			return;
		
		if(!block.getType().equals(Material.WATER_CAULDRON))
			return;
		
		Levelled l = ((Levelled)block.getBlockData());
		if(l.getLevel() < l.getMaximumLevel())
			return;
		
		block.setType(Material.CAULDRON);
		
		Collection<Entity> entities =  block.getWorld().getNearbyEntities(block.getBoundingBox());
		Collection<Material> mats = new ArrayList<>();
		for(Entity ent : entities) {
			if(ent.getType().equals(EntityType.ITEM)) {
				Item i = (Item) ent;
				mats.add(i.getItemStack().getType());
			}
		}
		ItemStack brew = AbilityUtils.getWitchesBrew();

		PotionMeta meta = (PotionMeta)brew.getItemMeta();
		List<String> ingredients = new ArrayList<>();
		
		for (Material mat : mats) {
			ingredients.add(mat.name());
		}
		meta.setDisplayName(WitchBrewRecipe.serializeIngredients(mats));
		brew.setItemMeta(meta);
		
		boolean isPerfect = false;
		for(WitchBrewRecipe recipe : WitchBrewRecipe.values()) {
			if(new HashSet<>(mats).equals(new HashSet<>(recipe.getIngredients()))) {
			//if(mats.equals(recipe.getIngredients())) {
			
			//if(recipe.getResult().isSimilar(brew)) {
			//if(recipe.getResult().getItemMeta().getDisplayName().equals(brew.getItemMeta().getDisplayName())) {
				player.playSound(player.getLocation(), Sound.ENTITY_WITCH_CELEBRATE, 1.0f, 1.0f);
				block.getWorld().spawnParticle(Particle.WITCH, block.getLocation(), 4);
				block.getWorld().dropItemNaturally(block.getLocation(), brew);
				isPerfect = true;
				break;
			}
		}
		
		if(!isPerfect) {
			this.onTrigger(true);
			player.playSound(player.getLocation(), Sound.ENTITY_WITCH_HURT, 1.0f, 1.0f);
			return;
		}
		else {
			for(Entity ent : entities) {
				if(ent.getType().equals(EntityType.ITEM)) {
					Item i = (Item) ent;
					i.remove();
				}
			}
		}
			
		
		this.onTrigger(true);
	}

	@Override
	public String getInstructions() {
		return "Toss ingredients into a filled cauldron and right-click on it with a stick";
	}

	@Override
	public String getDescription() {
		return "You can brew a special kind of splash potion that has varying effects depending on the ingredients you put in";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
	}

	
}
