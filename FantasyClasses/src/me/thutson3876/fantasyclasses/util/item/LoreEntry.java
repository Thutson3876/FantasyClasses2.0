package me.thutson3876.fantasyclasses.util.item;

import me.thutson3876.fantasyclasses.util.chat.ColorCode;

public class LoreEntry {

	private String lore;
	private ColorCode color;
	
	public LoreEntry(ColorCode color, String lore) {
		this.color = color;
		this.lore = lore;
	}

	public String getLore() {
		return lore;
	}

	public void setLore(String lore) {
		this.lore = lore;
	}

	public ColorCode getColor() {
		return color;
	}

	public void setColor(ColorCode color) {
		this.color = color;
	}
}
