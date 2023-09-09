package me.thutson3876.fantasyclasses.classes.highroller.randomabilities;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;

public class MidasTouch implements RandomAbility {
	
	private static final int MAX_DURATION = 4 * 20;
	
	private int count = MAX_DURATION;
	private BossBar bar = Bukkit.createBossBar(ChatUtils.chat("&6Midas Touch"), BarColor.YELLOW, BarStyle.SEGMENTED_12, new org.bukkit.boss.BarFlag[0]);
	
	@Override
	public void run(Player p) {
		bar.setProgress(1.0);
		bar.setVisible(true);
		bar.addPlayer(p);
		
		p.sendMessage(ChatUtils.chat("&aYou feeling that tingly sensation? Its carpal tunnel go touch some grass"));
		
		new BukkitRunnable() {

			@Override
			public void run() {
				if(p == null || p.isDead()) {
					this.cancel();
					bar.setVisible(false);
					bar.removePlayer(p);
					return;
				}
				if(count <= 1) {
					this.cancel();
					bar.setVisible(false);
					bar.removePlayer(p);
					return;
				}
				
				double value = count / MAX_DURATION;
				value = Math.min(Math.max(0.0D, value), 1.0D);
				bar.setProgress(value);
				bar.setVisible(true);
				
				Block b = p.getLocation().getBlock();
				b.setType(Material.GOLD_BLOCK);
				
				count--;
			}
			
		}.runTaskTimer(FantasyClasses.getPlugin(), 1, 1);
		
	}

}
