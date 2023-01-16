package me.thutson3876.fantasyclasses.gui;

import java.util.List;

import org.bukkit.entity.Player;

public class BasicGUI extends AbstractGUI {

	public BasicGUI(Player p, String title, int size, AbstractGUI forward, AbstractGUI back, List<GuiItem> items) {
		super(p, title, size, forward, back);
		this.setItems(items);
		initializeItems();
		
		/*if(this.forward != null)
			this.initializeForwardItem(forward);

		if(this.back != null)
			this.initializeBackItem(back);*/
	}

	@Override
	protected void initializeItems() {
		int halfway = Math.round((getInv().getSize() - 1) / 2);
		List<GuiItem> itemList = this.items;
		
		int listSize = itemList.size();
		if(this.back != null) {
			listSize--;
		}
		if(this.forward != null) {
			listSize--;
		}
		int startIndex = (halfway - Math.round(listSize / 3.0f));

		if (listSize > 5) {
			startIndex = 0;
			for (GuiItem item : itemList) {
				if(item.equals(this.back) || item.equals(this.forward))
					continue;
				getInv().setItem(startIndex, item.getItem());
				startIndex++;
			}
		} else if (listSize % 2 == 0) {
			for (GuiItem item : itemList) {
				if(item.equals(this.back) || item.equals(this.forward))
					continue;
				getInv().setItem(startIndex, item.getItem());
				startIndex++;
				if (startIndex == 13) {
					startIndex++;
				}
			}
		} else {
			for (GuiItem item : itemList) {
				if(item.equals(this.back) || item.equals(this.forward))
					continue;
				getInv().setItem(startIndex, item.getItem());
				startIndex++;
			}
		}
	}

}
