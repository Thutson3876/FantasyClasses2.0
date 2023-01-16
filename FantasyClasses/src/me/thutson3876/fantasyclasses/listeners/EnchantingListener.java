package me.thutson3876.fantasyclasses.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.professions.enchanter.customenchantments.Enchantments;

public class EnchantingListener implements Listener {

	private static final FantasyClasses plugin = FantasyClasses.getPlugin();

	private static final Random rng = new Random();

	private List<EnchantmentEventStorage> eventStorageList = new ArrayList<>();

	public EnchantingListener() {
		plugin.registerEvents(this);
	}

	// Try to fix this spaghetti
	// putting on curses when it has no other option for some reason
	@EventHandler
	public void onPrepareItemEnchantEvent(PrepareItemEnchantEvent e) {
		FantasyPlayer fplayer = plugin.getPlayerManager().getPlayer(e.getEnchanter());

		if (fplayer == null)
			return;

		Map<Enchantments, Integer> availableEnchantments = fplayer.getAvailableEnchantments(false);
		Map<Enchantment, Integer> availableEnchantmentMap = new HashMap<>();
		for (Entry<Enchantments, Integer> entry : availableEnchantments.entrySet()) {
			for (Enchantment ench : entry.getKey().getEnchants())
				availableEnchantmentMap.put(ench, entry.getValue());
		}

		final List<EnchantmentOffer> oldOffers = Arrays.asList(e.getOffers().clone());

		for (EnchantmentOffer preparedEnchantOffer : e.getOffers()) {
			if (preparedEnchantOffer == null)
				continue;

			if (availableEnchantmentMap.containsKey(preparedEnchantOffer.getEnchantment())) {
				int maxLevel = availableEnchantmentMap.get(preparedEnchantOffer.getEnchantment());
				if (preparedEnchantOffer.getEnchantmentLevel() > maxLevel) {
					preparedEnchantOffer.setEnchantmentLevel(maxLevel);
				}

				continue;
			} else {
				Map<Enchantment, Integer> newEnchant = generateRandomEnchantment(e.getItem(), new ArrayList<>(),
						fplayer, preparedEnchantOffer.getEnchantmentLevel());

				if (newEnchant == null) {
					preparedEnchantOffer = null;
					continue;
				}

				for (Entry<Enchantment, Integer> entry : newEnchant.entrySet()) {
					preparedEnchantOffer.setEnchantment(entry.getKey());
					preparedEnchantOffer.setEnchantmentLevel(entry.getValue());
				}
			}
		}

		EnchantmentEventStorage toBeRemoved = null;
		for (EnchantmentEventStorage storage : eventStorageList) {
			if (storage.getPlayer().equals(e.getEnchanter()))
				toBeRemoved = storage;
		}

		if (toBeRemoved != null)
			this.eventStorageList.remove(toBeRemoved);

		this.eventStorageList.add(new EnchantmentEventStorage(e.getEnchanter(), e.getInventory(),
				Arrays.asList(e.getOffers()), oldOffers));
	}

	@EventHandler
	public void onEnchantItemEvent(EnchantItemEvent e) {
		EnchantmentEventStorage eventStorage = null;
		Player player = e.getEnchanter();
		FantasyPlayer fplayer = plugin.getPlayerManager().getPlayer(player);
		if (fplayer == null)
			return;

		for (EnchantmentEventStorage es : eventStorageList) {
			if (es.getInv().equals(e.getInventory()) && es.getPlayer().equals(player)) {
				eventStorage = es;
				break;
			}
		}

		if (eventStorage == null) {
			return;
		}

		Map<Enchantment, Integer> currentEnchants = e.getEnchantsToAdd();
		Map<Enchantment, Integer> newEnchants = new HashMap<>();

		for (EnchantmentOffer oldOffer : eventStorage.getOffersToBeReplaced()) {
			if (oldOffer == null)
				continue;

			EnchantmentOffer newOffer = null;
			int i = eventStorage.getNewOffers().indexOf(oldOffer);
			Enchantment replacementEnchantment = null;
			int replacementLevel = -1;

			if (i > -1) {
				newOffer = eventStorage.getNewOffers().get(i);
			}

			Integer enchLevel = currentEnchants.get(oldOffer.getEnchantment());
			if (enchLevel != null) {
				if(newOffer != null) {
					replacementEnchantment = newOffer.getEnchantment();
					replacementLevel = newOffer.getEnchantmentLevel();

					//player.sendMessage(ChatUtils.chat("&2New Offer: " + newOffer.getEnchantment().toString()));
				}else {
					
				}
				
			}
			else {
				continue;
			}

			do {

				if (replacementEnchantment == null) {
					Map<Enchantment, Integer> newEnchant = generateRandomEnchantment(e.getItem(), newEnchants.keySet(),
							fplayer, enchLevel == null ?  oldOffer.getEnchantmentLevel() : enchLevel);

					for (Entry<Enchantment, Integer> entry : newEnchant.entrySet()) {
						replacementEnchantment = entry.getKey();
						replacementLevel = entry.getValue();
					}

					//player.sendMessage(ChatUtils.chat(
						//	"&4Error Occurred while generating enchantment: &2" + replacementEnchantment.toString() + " &rput in place instead"));
				}

				for (Enchantment ench : newEnchants.keySet()) {
					if (replacementEnchantment.conflictsWith(ench) && !replacementEnchantment.equals(ench)) {
						replacementEnchantment = null;
						//player.sendMessage(ChatUtils.chat("&5Enchantment conflict detected!"));
						break;
					}
				}
			} while (replacementEnchantment == null);

			newEnchants.put(replacementEnchantment, replacementLevel);
		}

		currentEnchants.clear();

		for (Entry<Enchantment, Integer> entry : newEnchants.entrySet()) {
			currentEnchants.put(entry.getKey(), entry.getValue());
		}

		this.eventStorageList.remove(eventStorage);
	}

	@EventHandler
	public void onPrepareAnvilEvent(PrepareAnvilEvent e) {
		FantasyPlayer fplayer = null;
		for (HumanEntity ent : e.getViewers()) {
			if (ent instanceof Player) {
				fplayer = plugin.getPlayerManager().getPlayer((Player) ent);
			}
		}

		if (fplayer == null)
			return;

		Map<Enchantments, Integer> availableEnchantments = fplayer.getAvailableEnchantments(true);

		ItemStack result = e.getResult();
		Map<Enchantment, Integer> newEnchants = new HashMap<>();
		for (Entry<Enchantment, Integer> enchant : result.getEnchantments().entrySet()) {
			for (Entry<Enchantments, Integer> availableEntry : availableEnchantments.entrySet()) {
				if (availableEntry.getKey().getEnchants().contains(enchant.getKey())) {
					int enchLevel = enchant.getValue();
					if (enchLevel > availableEntry.getValue())
						enchLevel = availableEntry.getValue();

					if(enchLevel == 0)
						enchLevel = 1;
					newEnchants.put(enchant.getKey(), enchLevel);
					break;
				}
			}
		}

		for (Enchantment enchant : result.getEnchantments().keySet()) {
			result.removeEnchantment(enchant);
		}

		result.addEnchantments(newEnchants);

		e.setResult(result);
	}

	private static Map<Enchantment, Integer> generateRandomEnchantment(ItemStack item,
			Collection<Enchantment> otherEnchants, FantasyPlayer fplayer, int enchLevel) {
		Map<Enchantment, Integer> returnEnchant = new HashMap<>();

		Map<Enchantments, Integer> availableEnchantments = fplayer.getAvailableEnchantments(false);
		Map<Enchantment, Integer> availableEnchantmentMap = new HashMap<>();
		for (Entry<Enchantments, Integer> entry : availableEnchantments.entrySet()) {
			for (Enchantment ench : entry.getKey().getEnchants())
				availableEnchantmentMap.put(ench, entry.getValue());
		}

		List<Enchantment> enchantOptions = new ArrayList<>();
		for (Enchantment ench : availableEnchantmentMap.keySet()) {
			boolean hasConflict = false;
			for (Enchantment e : otherEnchants) {
				if (ench.conflictsWith(e)) {
					hasConflict = true;
					break;
				}
			}

			if (!ench.canEnchantItem(item))
				hasConflict = true;

			if (hasConflict)
				continue;

			enchantOptions.add(ench);
		}

		if (enchantOptions.isEmpty()) {
			int i = 0;
			int randomIndex = rng.nextInt(availableEnchantmentMap.keySet().size());
			Enchantment randomEnchant = null;
			for (Enchantment ench : availableEnchantmentMap.keySet()) {
				if (i == randomIndex) {
					randomEnchant = ench;
					break;
				}
				i++;
			}

			returnEnchant.put(randomEnchant, rng.nextInt(availableEnchantmentMap.get(randomEnchant)));
			return returnEnchant;
		}

		Enchantment newEnchantment = enchantOptions.get(rng.nextInt(enchantOptions.size()));
		int currentLevel = enchLevel;
		int maxLevel = availableEnchantmentMap.get(newEnchantment);
		int newLevel = currentLevel > maxLevel ? maxLevel : currentLevel;

		returnEnchant.put(newEnchantment, newLevel);

		return returnEnchant;
	}

	/*
	 * private static Map<Enchantment, Integer> getReplacementEnchant(Enchantments
	 * rarity, EnchantmentOffer offer, ItemStack item, Collection<Enchantment>
	 * otherEnchants, FantasyPlayer fplayer) { List<Enchantment> enchantOptions =
	 * new ArrayList<>(); Map<Enchantment, Integer> newEnchant = new HashMap<>();
	 * 
	 * for (Enchantment ench : rarity.getEnchants()) { boolean hasConflict = false;
	 * for (Enchantment e : otherEnchants) { if (ench.conflictsWith(e)) {
	 * hasConflict = true; break; } }
	 * 
	 * if (!ench.canEnchantItem(item)) hasConflict = true;
	 * 
	 * if (hasConflict) continue;
	 * 
	 * enchantOptions.add(ench); }
	 * 
	 * if (enchantOptions.isEmpty()) {
	 * newEnchant.put(rarity.getEnchants().get(rng.nextInt(rarity.getEnchants().size
	 * ())), rng.nextInt(fplayer.getAvailableEnchantments(false).get(rarity)));
	 * return newEnchant; }
	 * 
	 * 
	 * int currentLevel = offer.getEnchantmentLevel(); int maxLevel =
	 * fplayer.getAvailableEnchantments(false).get(rarity); int newLevel =
	 * currentLevel > maxLevel ? maxLevel : currentLevel; Enchantment newEnchantment
	 * = enchantOptions.get(rng.nextInt(enchantOptions.size()));
	 * 
	 * newEnchant.put(newEnchantment, newLevel);
	 * offer.setEnchantment(newEnchantment); offer.setEnchantmentLevel(newLevel);
	 * 
	 * return newEnchant; }
	 */

	private class EnchantmentEventStorage {

		private final Player player;
		private final Inventory inv;
		private final List<EnchantmentOffer> newOffers;
		private final List<EnchantmentOffer> offersToBeReplaced;

		public EnchantmentEventStorage(Player p, Inventory inv, List<EnchantmentOffer> newOffers,
				List<EnchantmentOffer> offersToBeReplaced) {
			this.player = p;
			this.inv = inv;
			this.newOffers = newOffers;
			this.offersToBeReplaced = offersToBeReplaced;
		}

		public Player getPlayer() {
			return player;
		}

		public Inventory getInv() {
			return inv;
		}

		public List<EnchantmentOffer> getNewOffers() {
			return newOffers;
		}

		public List<EnchantmentOffer> getOffersToBeReplaced() {
			return offersToBeReplaced;
		}
	}
}
