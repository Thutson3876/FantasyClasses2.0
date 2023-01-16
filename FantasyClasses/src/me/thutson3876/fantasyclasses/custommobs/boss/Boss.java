package me.thutson3876.fantasyclasses.custommobs.boss;

import org.bukkit.boss.BossBar;

public interface Boss {
	
	BossBar getBossBar();
	
	void resetCount();
}
