package me.thutson3876.fantasyclasses.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;

public class ClassSelectionGUI extends BasicGUI {
	
	public ClassSelectionGUI(Player p, AbstractGUI back) {
		super(p, "Choose a Class", 27, null, back,  new ArrayList<GuiItem>());
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
		items.add(back);

		fillGaps(createGuiItem(null, Material.BLACK_STAINED_GLASS_PANE, " ").getItem());
	}

	
}
