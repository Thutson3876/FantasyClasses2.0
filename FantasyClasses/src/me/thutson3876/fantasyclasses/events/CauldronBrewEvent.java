package me.thutson3876.fantasyclasses.events;

import java.util.Collection;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class CauldronBrewEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final Player player;
	private final Block cauldron;
	private final Collection<ItemStack> ingredients;
	private ItemStack result;
	
	public CauldronBrewEvent(Player player, Block cauldron, Collection<ItemStack> ingredients, ItemStack result) {
		this.player = player;
		this.cauldron = cauldron;
		this.ingredients = ingredients;
		this.result = result;
	}

    public Player getPlayer() {
		return player;
	}

	public Block getCauldron() {
		return cauldron;
	}

	public Collection<ItemStack> getIngredients() {
		return ingredients;
	}

	public ItemStack getResult() {
		return result;
	}
	
	public void setResult(ItemStack item) {
		result.equals(item);
	}

	@Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
