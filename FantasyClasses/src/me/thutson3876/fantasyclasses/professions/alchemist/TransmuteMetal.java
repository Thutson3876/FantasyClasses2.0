package me.thutson3876.fantasyclasses.professions.alchemist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class TransmuteMetal extends AbstractAbility {
	
	List<Material> transmutes = Arrays.asList(Material.IRON_INGOT, Material.GOLD_INGOT, Material.COPPER_INGOT);
	
	public TransmuteMetal(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Transmute: Metal";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.COPPER_INGOT);
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
		
		Map<Material, Integer> ingredientMap = new HashMap<Material, Integer>();
		Collection<Entity> entities =  block.getWorld().getNearbyEntities(block.getBoundingBox());
		List<ItemStack> ingredients = new ArrayList<>();
		for(Entity ent : entities) {
			if(ent.getType().equals(EntityType.ITEM)) {
				Item i = (Item) ent;
				ingredients.add(i.getItemStack());
				Material type = i.getItemStack().getType();
				if(!transmutes.contains(type))
					return;
				else if(ingredientMap.containsKey(type))
					ingredientMap.put(type, i.getItemStack().getAmount() + ingredientMap.get(type));
				else
					ingredientMap.put(type, i.getItemStack().getAmount());
			}
		}
		
		if(ingredientMap.keySet().size() < 2) {
			onFail();
			return;
		}
		
		Material[] mats = new Material[ingredientMap.keySet().size()];
		mats = (Material[]) ingredientMap.keySet().toArray(mats);
		
		if(mats.length < 2) {
			onFail();
			return;
		}
		
		boolean isFirstLargest = ingredientMap.get(mats[0]) > ingredientMap.get(mats[1]);
		
		if(!isFirstLargest) {
			Material temp = mats[1];
			mats[1] = mats[0];
			mats[0] = temp;
		}
		
		int mat1Count = ingredientMap.get(mats[0]);
		int mat2Count = ingredientMap.get(mats[1]);
		
		/*if(mat1Count < 2 * mat2Count) {
			onFail();
			return;
		}*/
		if(mat1Count < 2) {
			onFail();
			return;
		}
		
		block.setType(Material.CAULDRON);
		
		int expendableAmt = mat1Count % 2;
		int valuedAmt = (int)((double)mat1Count / 2) + mat2Count;
		
		int amtOfValuedItems = (valuedAmt / 64) + 1;
		
		List<ItemStack> valuedItems = new ArrayList<ItemStack>();
		for(int i = 0; i < amtOfValuedItems; i++) {
			valuedItems.add(new ItemStack(mats[1], Math.min(valuedAmt, 64)));
			valuedAmt -= 64;
			
			if(valuedAmt <= 0)
				break;
		}
		 
		
		//CauldronBrewEvent cauldronEvent = new CauldronBrewEvent(player, block, ingredients, brew);
		//Bukkit.getPluginManager().callEvent(cauldronEvent);
		
		
		for(Entity ent : entities) {
			if(ent.getType().equals(EntityType.ITEM)) {
				((Item)ent).remove();
			}
		}
		
		player.playSound(player.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1.0f, 1.0f);
		block.getWorld().spawnParticle(Particle.BUBBLE_COLUMN_UP, block.getLocation(), 20);
		for(ItemStack item : valuedItems)
			block.getWorld().dropItemNaturally(block.getLocation(), item);
		if(expendableAmt > 0)
			block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(mats[0], expendableAmt));
		
		this.onTrigger(true);
	}
	
	private void onFail() {
		player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_HURT, 1.0f, 1.0f);
		this.onTrigger(true);
	}

	@Override
	public String getInstructions() {
		return "Mix two ingots in a cauldron";
	}

	@Override
	public String getDescription() {
		return "Transmute the properties of one metal into another. Toss two different stacks of ingots into a cauldron and stir. The larger stack is converted into the same type as the smaller stack, at a price...";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
	}

}
