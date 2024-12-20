package me.thutson3876.fantasyclasses.commands.commandexecutors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import me.thutson3876.fantasyclasses.commands.AbstractCommand;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;
import me.thutson3876.fantasyclasses.util.chat.ColorCode;

public class Command_ToggleBuildMode extends AbstractCommand implements Listener {

	private static final int PUNISHMENT_CD = 90;
	
	private static final PotionEffect slowFallEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 6 * 20, 0);
	private static final PotionEffect slownessEffect = new PotionEffect(PotionEffectType.SLOWNESS, 10 * 20, 1);
	
	private List<Player> currentBuilders = new ArrayList<>();
	
	private Map<UUID, Integer> blackList = new HashMap<>();
	
	public Command_ToggleBuildMode() {
		super("togglebuildmode", "buildmode");
		
		plugin.registerEvents(this);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				if(blackList.isEmpty())
					return;
				
				for(Entry<UUID, Integer> entry : blackList.entrySet()) {
					if(entry.getValue() <= 1) {
						blackList.remove(entry.getKey());
						continue;
					}
					
					blackList.put(entry.getKey(), entry.getValue() - 1);
				}
			}
			
		}.runTaskTimer(plugin, 20, 20);
	}

	@Override
	protected boolean onInternalCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			
			if(blackList.keySet().contains(player.getUniqueId())) {
				sender.sendMessage(ChatUtils.chat(ColorCode.ERROR + "Error: Recently violated Builder Mode rule. Try again in " + ColorCode.DEFAULT_HIGHLIGHT + blackList.get(player.getUniqueId()) + ColorCode.ERROR + " seconds"));
				return true;
			}
			
			boolean isBuilder = currentBuilders.contains(player);
			
			if(isBuilder) {
				removePlayer(player);
			}
			else {
				addPlayer(player);
			}
			
			return true;
		}
		
		sender.sendMessage(ChatUtils.chat(ColorCode.ERROR + "Error: Must be player to use this command"));
		return true;
	}
	
	private void addPlayer(Player p) {
		currentBuilders.add(p);
		
		p.setAllowFlight(true);
		p.setFlying(true);
		p.setFlySpeed(0.05f);
		
		p.playSound(p, Sound.BLOCK_NOTE_BLOCK_CHIME, 1.5f, 1.2f);
		p.sendMessage(ChatUtils.chat(ColorCode.SUCCESS + "Build Mode has been toggled on!"));
		p.sendMessage(ChatUtils.chat(ColorCode.BLACK + "Don't wander too far..."));
	}
	
	private void removePlayer(Player p) {
		currentBuilders.remove(p);
		
		p.setAllowFlight(false);
		p.setFlying(false);
		p.setFlySpeed(0.1f);
		
		p.playSound(p, Sound.BLOCK_NOTE_BLOCK_CHIME, 1.5f, 0.9f);
		p.sendMessage(ChatUtils.chat(ColorCode.ERROR + "Build Mode has been toggled off!"));
	}
	
	@EventHandler
	public void onChunkLoadEvent(ChunkLoadEvent e) {
		if(!e.isNewChunk())
			return;
		Set<Player> builders = new HashSet<>(currentBuilders);
		Set<Player> chunkPlayers =  new HashSet<>(e.getChunk().getPlayersSeeingChunk());
		
		SetView<Player> intersection = Sets.intersection(builders, chunkPlayers);
		
		for(Player p : intersection) {
			removePlayer(p);
			
			p.playSound(p, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 2f, 0.85f);
			p.sendMessage(ColorCode.BLACK + "You were warned...");
			p.addPotionEffect(slowFallEffect);
			p.addPotionEffect(slownessEffect);
			
			blackList.put(p.getUniqueId(), PUNISHMENT_CD);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		Entity damager = e.getDamager();
		
		if(damager == null)
			return;
		
		if(!(damager instanceof Player) && !(damager instanceof Projectile))
			return;
		
		Player builderSuspect = null;
		for(Player p : currentBuilders) {
			if(AbilityUtils.isTrueCause(p, damager)) {
				builderSuspect = p;
				break;
			}
		}
		
		if(builderSuspect == null)
			return;
		
		e.setCancelled(true);
	}
	
	@Override
	public void deInit() {
		List<Player> toRemove = currentBuilders;
		for(Player p : toRemove) {
			removePlayer(p);
		}
	}

}
