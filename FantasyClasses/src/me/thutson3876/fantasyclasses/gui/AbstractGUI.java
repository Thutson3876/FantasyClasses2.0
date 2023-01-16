package me.thutson3876.fantasyclasses.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.util.ChatUtils;

public abstract class AbstractGUI implements Listener {
	private final Inventory inv;
	protected List<GuiItem> items = new ArrayList<>();
	protected FantasyPlayer player;
	protected GuiItem back = null;
	protected GuiItem forward = null;

	public AbstractGUI(Player p, final String title, final int size, AbstractGUI forward, AbstractGUI back) {
		FantasyClasses plugin = FantasyClasses.getPlugin();
		player = plugin.getPlayerManager().getPlayer(p);
		Bukkit.getPluginManager().registerEvents(this, plugin);
		
		inv = Bukkit.createInventory(null, size, title);
		if(items != null && !items.isEmpty())
			initializeItems();
		
		if (forward != null)
			initializeForwardItem(forward);

		if (back != null)
			initializeBackItem(back);
	}

	protected abstract void initializeItems();

	protected void initializeForwardItem(AbstractGUI forward) {
		int size = getInv().getSize();
		int forPos = size - 1;

		this.forward = createGuiItem(forward, Material.EMERALD_BLOCK, ChatUtils.chat("&2Next"));
		ItemStack forwardItem = this.forward.getItem();

		getInv().setItem(forPos, forwardItem);
		items.add(this.forward);
	}

	protected void initializeBackItem(AbstractGUI back) {
		int size = getInv().getSize();
		int backPos = size - 9;

		this.back = createGuiItem(back, Material.REDSTONE_BLOCK, ChatUtils.chat("&4Back"));
		ItemStack backItem = this.back.getItem();

		getInv().setItem(backPos, backItem);
		items.add(this.back);
	}

	public void setItems(List<GuiItem> items) {
		this.items = items;
		initializeItems();
	}
	
	public void setJustItems(List<GuiItem> items) {
		this.items = items;
	}
	
	protected void reInitializeForwardBack() {
		if(this.back != null) {
			if(!this.items.contains(back)) {
				this.items.add(back);
			}
		}
		
		if(this.forward != null) {
			if(!this.items.contains(forward)) {
				this.items.add(forward);
			}
		}
	}
	
	protected void fillGaps(ItemStack filler) {
		for (int i = 0; i < getInv().getSize(); i++) {
			ItemStack item = getInv().getItem(i);
			if (item == null || item.getType().equals(Material.AIR)) {
				getInv().setItem(i, filler);
			}
		}
	}
	
	public void defaultFillGaps(Material filler) {
		ItemStack fill = new ItemStack(filler);
		ItemMeta meta = fill.getItemMeta();
		meta.setDisplayName(" ");
		fill.setItemMeta(meta);
		
		for (int i = 0; i < getInv().getSize(); i++) {
			ItemStack item = getInv().getItem(i);
			if (item == null || item.getType().equals(Material.AIR)) {
				getInv().setItem(i, fill);
			}
		}
	}

	protected static GuiItem createGuiItem(final AbstractGUI linkedInventory, final Material material, final String name, final String... lore) {
		final ItemStack item = new ItemStack(material, 1);
		final ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		
		GuiItem guiItem = new GuiItem(item, linkedInventory);

		return guiItem;
	}

	public void openInventory(final HumanEntity ent) {
		refresh();
		//ent.openInventory(getInv());
	}
	
	public void refresh() {
		initializeItems();
		player.getPlayer().openInventory(getInv());
	}
	
	public static Inventory defaultFillGaps(Inventory inv, Material filler) {
		ItemStack fill = new ItemStack(filler);
		ItemMeta meta = fill.getItemMeta();
		meta.setDisplayName(" ");
		fill.setItemMeta(meta);
		
		for (int i = 0; i < inv.getSize(); i++) {
			ItemStack item = inv.getItem(i);
			if (item == null || item.getType().equals(Material.AIR)) {
				inv.setItem(i, fill);
			}
		}
		
		return inv;
	}
	
	public static ItemStack getDefaultFiller() {
		ItemStack fill = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta meta = fill.getItemMeta();
		meta.setDisplayName(" ");
		fill.setItemMeta(meta);
		
		return fill;
	}
	
	/*protected void generateExpBar(FantasyPlayer player) {
		long currentExp = player.getSkillExp() - player.calculateCurrentLevelExpCost();
		long neededExp = player.calculateNextLevelExpCost() - player.calculateCurrentLevelExpCost();
		int progressPercent = (int) Math.round(((double) currentExp / neededExp) * 10) - 1;
		List<String> lore = new ArrayList<>();
		lore.add(ChatUtils.chat("Class Points Available: &6" + player.getClassPoints()));
		lore.add(ChatUtils.chat("Profession Points Available: &6" + player.getProfPoints()));
		lore.add(ChatUtils.chat("Exp: &6" + currentExp + " &r/ &6" + neededExp));
		
		ItemStack item = new ItemStack(Material.GOLD_BLOCK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatUtils.chat("&3Player Level: &6" + player.getPlayerLevel()));
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		for(int i = 0; i < 9; i++) {
			if(i > progressPercent) {
				item.setType(Material.STONE);
			}
			
			getInv().setItem(i, item);
		}
	}*/
	
	protected void generateExpBar() {
		long currentExp = player.getSkillExp() - player.calculateCurrentLevelExpCost();
		long neededExp = player.calculateNextLevelExpCost() - player.calculateCurrentLevelExpCost();
		int progressPercent = (int) Math.round(((double) currentExp / neededExp) * 10) - 1;
		List<String> lore = new ArrayList<>();
		lore.add(ChatUtils.chat("Class Skillpoints Available: &6" + player.getClassPoints()));
		lore.add(ChatUtils.chat("Profession Skillpoints Available: &6" + player.getProfPoints()));
		lore.add(ChatUtils.chat("Exp: &6" + currentExp + " &r/ &6" + neededExp));
		
		ItemStack item = new ItemStack(Material.GOLD_BLOCK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatUtils.chat("&3Player Level: &6" + player.getPlayerLevel()));
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		for(int i = 0; i < 9; i++) {
			if(i > progressPercent) {
				item.setType(Material.STONE);
			}
			
			getInv().setItem(i, item);
		}
	}

	@EventHandler
	public void onInventoryClick(final InventoryClickEvent e) {
		if (e.getInventory() != getInv()) {
			return;
		}
			

		e.setCancelled(true);

		final ItemStack clickedItem = e.getCurrentItem();

		if (clickedItem == null || clickedItem.getType().isAir())
			return;

		final Player p = (Player) e.getWhoClicked();
		
		p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 0.4F, 1F);
		
		for(GuiItem item : items) {
			if(clickedItem.equals(item.getItem())) {
				if(item.getLinkedInventory() == null)
					break;
				item.getLinkedInventory().refresh();
				break;
			}
		}
	}

	@EventHandler
	public void onInventoryDrag(final InventoryDragEvent e) {
		if (e.getInventory().equals(getInv())) {
			e.setCancelled(true);
		}
	}

	public Inventory getInv() {
		return inv;
	}
	
}
