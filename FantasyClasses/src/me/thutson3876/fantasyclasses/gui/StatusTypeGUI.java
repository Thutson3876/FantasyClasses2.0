package me.thutson3876.fantasyclasses.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.status.StatusTypeList;

public class StatusTypeGUI extends BasicGUI {
	
	private static final String TITLE = "Status Types";
	private static final int SIZE = 27;
		
	public StatusTypeGUI(Player p, AbstractGUI back) {
		super(p, TITLE, SIZE, null, back, new ArrayList<GuiItem>());
	}
	
	@Override
	protected void initializeItems() {
		List<GuiItem> items = new ArrayList<>();
		StatusTypeList[] listOfLists = StatusTypeList.values();
		
		for(int i = 0; i < listOfLists.length; i++) {
			if(listOfLists[i].getList().isEmpty())
				continue;
			
			items.add(new GuiItem(listOfLists[i].getItem(), new StatusTypeListGUI(this.player.getPlayer(), this, listOfLists[i])));
		}
		
		this.items.addAll(items);
		
		for(int i = 0; i < items.size(); i++) {
			getInv().setItem(10 + i, items.get(i).getItem());
		}

		this.items.add(back);

		fillGaps(createGuiItem(null, Material.BLACK_STAINED_GLASS_PANE, " ").getItem());
	
	}

}
