package me.thutson3876.fantasyclasses.util;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public abstract class AbstractActivationWindow implements Runnable {

	protected Player player;
	protected boolean isOn = false;
	protected int tickCounter = 0;
	protected int maxTickDuration = 1;
	protected String title = "";
	protected BossBar bar;

	public AbstractActivationWindow(Player player, String title, int maxTickDuration) {
		this.player = player;
		this.title = title;
		this.maxTickDuration = maxTickDuration;

		this.bar = Bukkit.createBossBar(this.title, BarColor.YELLOW, BarStyle.SEGMENTED_20,
				new org.bukkit.boss.BarFlag[0]);
		this.bar.setVisible(false);
		bar.addPlayer(player);
	}

	protected void updateBar() {
		/*
		 * if (this.tickCounter >= this.maxTickDuration) { deInit(); }
		 */
		if (this.tickCounter < this.maxTickDuration) {
			this.isOn = true;
			double value = this.tickCounter / this.maxTickDuration;
			value = Math.min(Math.max(0.0D, value), 1.0D);
			bar.setProgress(value);
			if (!bar.isVisible()) {
				bar.setVisible(true);
			}
		}
	}

	public BossBar getBar() {
		return this.bar;
	}

	public int getTickCounter() {
		return this.tickCounter;
	}

	public boolean isOn() {
		return this.isOn;
	}

	public void setOn(boolean isOn) {
		this.isOn = isOn;
	}

	public void init() {
		this.isOn = true;
		this.tickCounter = this.maxTickDuration;
		this.bar.setVisible(true);
	}

	public void deInit() {
		this.isOn = false;
		this.bar.setVisible(false);
	}
}
