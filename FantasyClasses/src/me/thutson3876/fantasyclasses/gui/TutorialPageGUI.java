package me.thutson3876.fantasyclasses.gui;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;

public class TutorialPageGUI extends BasicGUI {

	private Material diamond_mat = Material.SPYGLASS;
	
	public TutorialPageGUI(Player p, String title, int size, AbstractGUI forward, AbstractGUI back,
			List<GuiItem> items) {
		super(p, title, size, forward, back, items);
		
		if (forward != null)
			initializeForwardItem(forward);

		if (back != null)
			initializeBackItem(back);
	}
	
	@Override
	protected void initializeItems() {
		for(GuiItem i : this.items)
			if(!i.equals(this.back) && !i.equals(this.forward))
				getInv().setItem(13, i.getItem());
		
		fillGaps(createGuiItem(null, Material.BLACK_STAINED_GLASS_PANE, " ").getItem());
	}
	
	@EventHandler
	public void onClickEvent(InventoryClickEvent e) {
		if(!this.getInv().equals(e.getClickedInventory()))
			return;
		
		Player p = (Player)e.getWhoClicked();
		FantasyPlayer fplayer = FantasyClasses.getPlugin().getPlayerManager().getPlayer(p);
		
		if(fplayer == null)
			return;
		
		ItemStack item = e.getCurrentItem();
		
		if(item == null)
			return;
		
		if(item.getType().equals(diamond_mat) && fplayer.isFirstTime()) {
			fplayer.getPlayer().getInventory().addItem(new ItemStack(Material.DIAMOND, 2));
			fplayer.getPlayer().playSound(p, Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.8f, 1.0f);
			fplayer.setFirstTime(false);
		}
	}

}
