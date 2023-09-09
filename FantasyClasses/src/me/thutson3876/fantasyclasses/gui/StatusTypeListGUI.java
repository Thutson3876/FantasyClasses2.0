package me.thutson3876.fantasyclasses.gui;

import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.status.StatusTypeList;

public class StatusTypeListGUI extends BasicGUI {

	private static final String TITLE = "Status Types";
	private static final int SIZE = 27;
	
	public StatusTypeListGUI(Player p, AbstractGUI back, StatusTypeList statusList) {
		super(p, TITLE, SIZE, null, back, statusList.getAllGuiItems());
	}

}
