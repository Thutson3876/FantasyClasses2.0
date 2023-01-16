package me.thutson3876.fantasyclasses.gui;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.ChatUtils;

public class MainMenuGUI extends BasicGUI {

	public MainMenuGUI(Player p) {
		super(p, "Pick your Class or Professions", 27, null, null, new ArrayList<GuiItem>());
	}

	@Override
	protected void initializeItems() {
		generateExpBar();
		
		GuiItem classes = generateClassesItem();
		GuiItem professions = generateProfessionsItem();
		
		items.add(classes);
		items.add(professions);
		
		getInv().setItem(12, classes.getItem());
		getInv().setItem(14, professions.getItem());

		fillGaps(createGuiItem(null, Material.BLACK_STAINED_GLASS_PANE, " ").getItem());
	}

	
	private GuiItem generateClassesItem() {
		ItemStack item = AbilityUtils.createItem(ChatUtils.chat("&6Classes"), null, Material.BLAZE_POWDER, 1);
		GuiItem guiItem = new GuiItem(item, this, new ClassSelectionGUI(this.player.getPlayer(), this));
		
		return guiItem;
	}
	
	private GuiItem generateProfessionsItem() {
		ItemStack item = AbilityUtils.createItem(ChatUtils.chat("&6Professions"), null, Material.BOOK, 1);
		GuiItem guiItem = new GuiItem(item, this, new ProfessionSelectionGUI(this.player.getPlayer(), this));
		
		return guiItem;
	}
}
