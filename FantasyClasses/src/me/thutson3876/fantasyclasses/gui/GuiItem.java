package me.thutson3876.fantasyclasses.gui;

import org.bukkit.inventory.ItemStack;

public class GuiItem {

	private AbstractGUI linkedInventory;
	private AbstractGUI currentInventory;
	private ItemStack item;

	public GuiItem(ItemStack item, AbstractGUI linkedInventory) {
		this.item = item;
		this.linkedInventory = linkedInventory;
	}

	public GuiItem(ItemStack item, AbstractGUI currentInventory, AbstractGUI linkedInventory) {
		this.item = item;
		this.setCurrentInventory(currentInventory);
		this.linkedInventory = linkedInventory;
	}

	public AbstractGUI getLinkedInventory() {
		return linkedInventory;
	}

	public void setLinkedInventory(AbstractGUI linkedInventory) {
		this.linkedInventory = linkedInventory;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public AbstractGUI getCurrentInventory() {
		return currentInventory;
	}

	public void setCurrentInventory(AbstractGUI currentInventory) {
		this.currentInventory = currentInventory;
	}

}
