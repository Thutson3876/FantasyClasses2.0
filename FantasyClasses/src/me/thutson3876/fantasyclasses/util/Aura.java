package me.thutson3876.fantasyclasses.util;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.FantasyClasses;

public abstract class Aura implements Runnable {

	private FantasyClasses plugin = FantasyClasses.getPlugin();
	private final BossBar bar;
	private boolean isOn = false;
	private int taskID;
	private long counter;
	
	protected final Player p;
	protected final double range;
	protected final long duration;
	protected int tickRate;
	
	public Aura(Player p, double range, String name, BarColor color, int tickRate, long duration) {
		bar = Bukkit.createBossBar(name, color, BarStyle.SEGMENTED_20, new org.bukkit.boss.BarFlag[0]);
		this.p = p;
		this.range = range;
		this.tickRate = tickRate;
		this.duration = (duration / tickRate) + 1;
		counter = this.duration;
	}
	
	public BossBar getBar() {
		return this.bar;
	}
	
	public void toggleAura() {
		if(!this.isOn) {
			taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 1L, this.tickRate);
			this.isOn = true;
		}
		else {
			Bukkit.getScheduler().cancelTask(this.taskID);
			this.getBar().setVisible(false);
			this.isOn = false;
		}
	}
	
	protected void counterTick() {
		double value = ((double)this.counter / (double)this.duration);
	      if (value <= 0.0D) {
	        cancel();
	        return;
	      } 
	      value = Math.min(Math.max(0.0D, value), 1.0D);
	      this.bar.setProgress(value);
	      if (!this.bar.isVisible())
	        this.bar.setVisible(true); 
	      this.counter--;
	}
	
	public boolean isOn() {
		return isOn;
	}
	
	public int getTickRate() {
		return this.tickRate;
	}
	
	public void setTickRate(int tickRate) {
		this.tickRate = tickRate;
	}
	
    private void resetCounter() {
        this.counter = (int) this.duration;
      }
      
      private void cancel() {
        this.bar.setVisible(false);
        this.bar.setProgress(1.0D);
        Bukkit.getScheduler().cancelTask(taskID);
        isOn = false;
        resetCounter();
      }
}
