package me.thutson3876.fantasyclasses.playermanagement.currency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.gui.AbstractGUI;
import me.thutson3876.fantasyclasses.gui.BasicGUI;
import me.thutson3876.fantasyclasses.gui.GuiItem;

public class MarketGUI extends BasicGUI {

	private Map<MarketCategory, GuiItem> marketCategoryGUIs = new HashMap<>();
	
	public MarketGUI(Player p, AbstractGUI back) {
		super(p, "Market", 54, null, back, new ArrayList<GuiItem>());
		
		for(MarketCategory category : MarketCategory.values()) {
			marketCategoryGUIs.put(category, new GuiItem(category.getItem(), this));
		}
	}
	
	@Override
	protected void initializeItems() {
		generateCurrencyBar();
		
		for(MarketCategory category : MarketCategory.values()) {
			items.add(new GuiItem(category.getItem(), this, new MarketCategoryGUI(player.getPlayer(), category, this)));
		}
		
		defaultOrganization();
		
		//getInv().setItem(12, classes.getItem());
		//getInv().setItem(14, professions.getItem());

		fillGaps(createGuiItem(null, Material.BLACK_STAINED_GLASS_PANE, " ").getItem());
	}

}
