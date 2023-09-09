package me.thutson3876.fantasyclasses.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.thutson3876.fantasyclasses.gui.GuiItem;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;
import me.thutson3876.fantasyclasses.util.chat.ColorCode;

public enum StatusTypeList {

	GENERAL(Material.BREWING_STAND, DefaultStatusType.LEECH, DefaultStatusType.STRIDER, DefaultStatusType.STEALTH),
	HIGHROLLER(Material.SKELETON_SKULL, DefaultStatusType.BLINDSIDED, DefaultStatusType.BROADSIDED,
			DefaultStatusType.ADRENALINE_RUSH, DefaultStatusType.DREADBLADES, DefaultStatusType.KEELHAUL,
			DefaultStatusType.RIDE_THE_WAVES, DefaultStatusType.RUTHLESSNESS, DefaultStatusType.TRUE_BEARING),
	BERSERKER(Material.DIAMOND_AXE, DefaultStatusType.ENRAGED),
	SEA_GUARDIAN, 
	RANGER, MONK;

	final List<DefaultStatusType> list;
	final ItemStack item;

	StatusTypeList(Material mat, DefaultStatusType... statusTypes) {
		List<DefaultStatusType> list = new ArrayList<>();
		list.addAll(Arrays.asList(statusTypes));
		this.list = list;

		this.item = generateItem(mat);
	}

	StatusTypeList(DefaultStatusType... statusTypes) {
		this(Material.STONE, statusTypes);
	}

	ItemStack generateItem(Material mat) {
		ItemStack item = new ItemStack(mat);

		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(ChatUtils.chat(ColorCode.STATUS + this.toString()));

		item.setItemMeta(meta);

		return item;
	}

	public List<DefaultStatusType> getList() {
		return list;
	}

	public boolean contains(DefaultStatusType type) {
		return list.contains(type);
	}

	public ItemStack getItem() {
		return item;
	}

	public List<ItemStack> getAllItems() {
		List<ItemStack> itemList = new ArrayList<>();

		for (DefaultStatusType type : this.list)
			itemList.add(type.getItem());

		return itemList;
	}

	public List<GuiItem> getAllGuiItems() {
		List<GuiItem> itemList = new ArrayList<>();

		for (DefaultStatusType type : this.list)
			itemList.add(new GuiItem(type.getItem(), null));

		return itemList;
	}
}
