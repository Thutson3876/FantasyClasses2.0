package me.thutson3876.fantasyclasses.custombrewing;

import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

public interface BrewAction {
	/**
     * This method would be called when ever a brewer is done and would be called for the certain brew recipe
     * @param inventory The inventory everything is getting brewed on
     * @param item The item that is currently getting "Brewed"
     * @param ingridient The ingredient of the brewing recipe
     */
    public void brew(BrewerInventory inventory , ItemStack item , ItemStack ingredient);
}
