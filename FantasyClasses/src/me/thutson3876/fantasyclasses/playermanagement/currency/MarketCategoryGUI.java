package me.thutson3876.fantasyclasses.playermanagement.currency;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.gui.AbstractGUI;
import me.thutson3876.fantasyclasses.gui.BasicGUI;
import me.thutson3876.fantasyclasses.gui.GuiItem;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;

public class MarketCategoryGUI extends BasicGUI {

	private final MarketCategory category;
	private boolean isOnCooldown = false;
	
	public MarketCategoryGUI(Player p, MarketCategory category, AbstractGUI back) {
		super(p, category.toString(), 54, null, back, new ArrayList<>());
		
		this.category = category;
		initializeItems();
	}

	@Override
	protected void initializeItems() {
		if(this.category == null)
			return;
		
		generateCurrencyBar();
		
		for(ItemStack i : category.getStockItems()) {
			items.add(new GuiItem(i, null));
		}
		
		defaultOrganization();
		
		//getInv().setItem(12, classes.getItem());
		//getInv().setItem(14, professions.getItem());

		fillGaps(createGuiItem(null, Material.BLACK_STAINED_GLASS_PANE, " ").getItem());
	}
	
	@Override
	@EventHandler
	public void onInventoryClick(final InventoryClickEvent e) {
		Inventory inventory = e.getInventory();
		if (inventory.equals(getInv())) {
			e.setCancelled(true);
		} else {
			return;
		}
		
		if(isOnCooldown)
			return;

		final ItemStack clickedItem = e.getCurrentItem();
		final ClickType clickType = e.getClick();

		if (clickedItem == null || clickedItem.getType().isAir())
			return;
		
		if(!clickType.isLeftClick())
			return;

		isOnCooldown = true;
		new BukkitRunnable() {

			@Override
			public void run() {
				isOnCooldown = false;
			}
			
		}.runTaskLater(FantasyClasses.getPlugin(), 2);
		
		final Player p = (Player) e.getWhoClicked();
		p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 0.4F, 1F);

		if (clickedItem.equals(back.getItem())) {
			back.getLinkedInventory().refresh();
			return;
		}
		
		int indexInStock = category.getStockItems().indexOf(clickedItem);
		if(indexInStock < 0) {
			return;
		}
		
		Purchasable chosenStock = category.getStock().get(indexInStock);
		
		if(player.spendCurrency(chosenStock.getPrice())) {
			p.getInventory().addItem(new ItemStack(chosenStock.getMaterial()));
			p.getPlayer().playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.8f, 1.25f);
		}
		else {
			p.getPlayer().playSound(p, Sound.ENTITY_VILLAGER_NO, 0.5f, 0.8f);
			p.sendMessage(ChatUtils.chat("&4Not Enough " + FantasyPlayer.getCurrencyName()));
		}
	}
}
