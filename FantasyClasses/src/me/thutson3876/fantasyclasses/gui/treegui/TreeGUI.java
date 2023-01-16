package me.thutson3876.fantasyclasses.gui.treegui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.gui.BasicGUI;
import me.thutson3876.fantasyclasses.gui.GuiItem;

public class TreeGUI extends BasicGUI {

	public TreeGUI(Player p, String title) {
		super(p, title, 27, null, null, new ArrayList<GuiItem>());
	}

	@Override
	public void initializeItems() {
		List<AbstractFantasyClass> classes = player.getFantasyClasses();
		for(int i = 0; i < classes.size(); i++) {
			GuiItem temp = player.getFantasyClasses().get(i).asTreeGuiItem(this);
			items.add(temp);
			getInv().setItem(10 + i, temp.getItem());
		}
		for(int i = 0; i < 9; i++) {
			getInv().setItem(i, getDefaultFiller());
		}
		generateExpBar();

		fillGaps(createGuiItem(null, Material.BLACK_STAINED_GLASS_PANE, " ").getItem());
	}

}
