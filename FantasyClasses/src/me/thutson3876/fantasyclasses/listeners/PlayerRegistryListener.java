package me.thutson3876.fantasyclasses.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.collectible.Collectible;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;
import me.thutson3876.fantasyclasses.util.particles.CustomParticle;
import me.thutson3876.fantasyclasses.util.particles.vanity.wings.WingType;
import me.thutson3876.fantasyclasses.util.particles.vanity.wings.Wings;

public class PlayerRegistryListener implements Listener {

	private static final FantasyClasses plugin = FantasyClasses.getPlugin();

	public PlayerRegistryListener() {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		plugin.getPlayerManager().addPlayer(p);
		
		//new Halo(new CustomParticle(Particle.FLAME)).run(p);
		new Wings(WingType.BUG, new CustomParticle(Particle.REDSTONE, 1, 0, 1.2, Color.PURPLE), 2).run(p);
		//new Wings(WingType.BUTTERFLY, new CustomParticle(Particle.WAX_ON), 2);
		
		ChatUtils.welcomeMessage(p);
	}
	
	@EventHandler
	public void onPlayerLeaveEvent(PlayerQuitEvent e) {
		plugin.getPlayerManager().removePlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerKickEvent(PlayerKickEvent e) {
		plugin.getPlayerManager().removePlayer(e.getPlayer());
	}
	
	@EventHandler (priority=EventPriority.LOWEST)
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if(!e.getAction().equals(Action.RIGHT_CLICK_AIR))
			return;
		
		Player p = e.getPlayer();
		FantasyPlayer fplayer = plugin.getPlayerManager().getPlayer(p);
		if(fplayer == null)
			return;
		
		ItemStack item = e.getItem();
		if(item == null)
			return;
		
		int exp = Collectible.getDropExpAmount(item);
		if(exp > 0) {
			fplayer.addSkillExp(exp);
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);
			new BukkitRunnable() {

				@Override
				public void run() {
					p.getInventory().setItem(e.getHand(), null);				
				}
				
			}.runTaskLater(plugin, 1);
		}
		else if(Collectible.isSkillResetItem(item)) {
			p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1.2f, 1.2f);
			fplayer.resetAllClassAbilities();
			fplayer.resetChosenClass();
			p.sendMessage(ChatUtils.chat("&5Successfully reset your class!"));
			p.sendMessage(Collectible.getAllRandomLore());
			p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 0.9f, 1.0f);
			
			new BukkitRunnable() {

				@Override
				public void run() {
					p.getInventory().setItem(e.getHand(), null);				
				}
				
			}.runTaskLater(plugin, 1);
		}
		else if(Collectible.isProfessionResetItem(item)) {
			p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1.2f, 1.2f);
			fplayer.resetAllProfAbilities();
			fplayer.resetChosenProfessions();
			p.sendMessage(ChatUtils.chat("&5Successfully reset your professions!"));
			p.sendMessage(Collectible.getAllRandomLore());
			p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 0.9f, 1.0f);
			
			new BukkitRunnable() {

				@Override
				public void run() {
					p.getInventory().setItem(e.getHand(), null);				
				}
				
			}.runTaskLater(plugin, 1);
		}
		
	}
}
