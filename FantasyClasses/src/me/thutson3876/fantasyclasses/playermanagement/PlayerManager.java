package me.thutson3876.fantasyclasses.playermanagement;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;

public class PlayerManager {
	
	private Set<FantasyPlayer> players = new HashSet<>();

	public PlayerManager() {
	}

	public Set<FantasyPlayer> getFantasyPlayers() {
		return players;
	}

	public FantasyPlayer getPlayer(Player player) {
		for (FantasyPlayer p : players) {
			if (p.getPlayer().equals(player)) {
				return p;
			}
		}

		return null;
	}

	public void init() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if(p != null)
				addPlayer(p);
		}
	}
	
	public void deInit() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			removePlayer(p);
		}
	}

	public boolean addPlayer(Player player) {
		if (player == null || players.contains(getPlayer(player)))
			return false;

		FantasyPlayer fPlayer = new FantasyPlayer(player);
		
		players.add(fPlayer);
		
		return true;
	}

	public void removePlayer(Player player) {
		FantasyPlayer fPlayer = getPlayer(player);
		if (fPlayer == null) {
			return;
		}
		
		fPlayer.deInit();

		players.remove(fPlayer);
	}

	public boolean contains(Player player) {
		for (FantasyPlayer p : players) {
			if (p.getPlayer().equals(player)) {
				return true;
			}
		}

		return false;
	}

	public static boolean checkAdvancementValidity(AdvancementProgress adv) {
		Collection<String> criteriaList = adv.getAdvancement().getCriteria();
		for (String criteria : criteriaList) {
			if (criteria.equalsIgnoreCase("has_the_recipe")) {
				return false;
			}
		}
		
		return true;
	}
}
