package me.thutson3876.fantasyclasses.gui;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.playermanagement.currency.MarketGUI;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;

public class MainMenuGUI extends BasicGUI {

	public MainMenuGUI(Player p) {
		super(p, "Pick your Class or Professions", 27, null, null, new ArrayList<GuiItem>());
	}

	@Override
	protected void initializeItems() {
		generateExpBar();
		
		GuiItem classes = generateClassesItem();
		GuiItem professions = generateProfessionsItem();
		GuiItem market = generateMarketItem();
		GuiItem help = generateHelpItem();
		GuiItem status = generateStatusItem();
		
		items.add(classes);
		items.add(professions);
		items.add(market);
		items.add(help);
		items.add(status);
		
		if(player.getPlayerLevel() >= FantasyPlayer.getMaxLevel()) {
			items.add(market);
			getInv().setItem(13 + 9, market.getItem());
		}
			
		getInv().setItem(9, status.getItem());
		
		getInv().setItem(12, classes.getItem());
		getInv().setItem(14, professions.getItem());
		
		getInv().setItem(17, help.getItem());

		fillGaps(createGuiItem(null, Material.BLACK_STAINED_GLASS_PANE, " ").getItem());
	}

	
	private GuiItem generateClassesItem() {
		ItemStack item = AbilityUtils.createItem(ChatUtils.chat("&6Classes"), null, Material.BLAZE_POWDER, 1);
		GuiItem guiItem = new GuiItem(item, this, new ClassSelectionGUI(this.player.getPlayer(), this));
		
		return guiItem;
	}
	
	private GuiItem generateProfessionsItem() {
		ItemStack item = AbilityUtils.createItem(ChatUtils.chat("&6Professions"), null, Material.FISHING_ROD, 1);
		GuiItem guiItem = new GuiItem(item, this, new ProfessionSelectionGUI(this.player.getPlayer(), this));
		
		return guiItem;
	}
	
	private GuiItem generateMarketItem() {
		ItemStack item = AbilityUtils.createItem(ChatUtils.chat("&6Market"), null, Material.EMERALD, 1);
		GuiItem guiItem = new GuiItem(item, this, new MarketGUI(this.player.getPlayer(), this));
		
		return guiItem;
	}
	
	private GuiItem generateHelpItem() {
		ItemStack item = AbilityUtils.createItem(ChatUtils.chat("&6Help"), null, Material.BOOK, 1);
		GuiItem guiItem = new GuiItem(item, this, new HelpGUI(this.player.getPlayer(), this));
		
		return guiItem;
	}
	
	private GuiItem generateStatusItem() {
		ItemStack item = AbilityUtils.createItem(ChatUtils.chat("&6Statuses"), null, Material.DRAGON_BREATH, 1);
		GuiItem guiItem = new GuiItem(item, this, new StatusTypeGUI(this.player.getPlayer(), this));
		
		return guiItem;
	}
	
	@Override
	public void openInventory(final HumanEntity ent) {
		refresh();
		
		if(player.isFirstTime()) {
			player.setFirstTime(false);
		}
	}
}
