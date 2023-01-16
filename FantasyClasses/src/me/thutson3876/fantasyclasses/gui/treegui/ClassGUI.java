package me.thutson3876.fantasyclasses.gui.treegui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.gui.AbstractGUI;
import me.thutson3876.fantasyclasses.gui.GuiItem;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.util.ChatUtils;

public class ClassGUI extends AbstractGUI {

	AbstractFantasyClass clazz;
	private static final ItemStack BUFFER_ITEM;
	private GuiItem confirm;

	private Inventory tempInv;
	private int tempSkillpoints = 0;
	private boolean isOnCooldown = false;

	private Map<TempSkill, Integer> pendingAbilityChanges = new HashMap<>();

	static {
		ItemStack buffer = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
		ItemMeta meta = buffer.getItemMeta();
		meta.setDisplayName(" ");
		buffer.setItemMeta(meta);

		BUFFER_ITEM = buffer;
	}

	public ClassGUI(Player p, AbstractGUI back, AbstractFantasyClass clazz) {
		super(p, clazz.getName(), 54, null, back);
		this.clazz = clazz;
		initializeConfirmItem();
		setMapInInventory();
		this.defaultFillGaps(Material.BLACK_STAINED_GLASS_PANE);
	}

	@Override
	protected void initializeItems() {
		this.pendingAbilityChanges.clear();
		setMapInInventory();
		initializeConfirmItem();
		this.defaultFillGaps(Material.BLACK_STAINED_GLASS_PANE);
	}

	private void initializeConfirmItem() {
		int size = getInv().getSize();
		int position = size - 1;

		this.confirm = createGuiItem(null, Material.EMERALD_BLOCK, ChatUtils.chat("&aConfirm Choices"));
		ItemStack confirmItem = this.confirm.getItem();

		getInv().setItem(position, confirmItem);
		items.add(this.confirm);
	}

	public void setMapInInventory() {
		Map<Integer, Skill> skillMap = clazz.getSkillMap();
		List<GuiItem> items = new ArrayList<>();
		for (Entry<Integer, Skill> entry : skillMap.entrySet()) {
			Skill s = entry.getValue();
			if (s == null)
				getInv().setItem(entry.getKey(), ClassGUI.getBufferItem());
			else {
				GuiItem guiItem = s.asGuiItem(null);
				items.add(guiItem);
				getInv().setItem(entry.getKey(), guiItem.getItem());
			}
		}
		items.add(this.back);
		this.setJustItems(items);
	}

	public static ItemStack getBufferItem() {
		return BUFFER_ITEM;
	}

	@EventHandler
	public void onInventoryOpen(final InventoryOpenEvent e) {
		Inventory inventory = e.getInventory();
		if (inventory.equals(getInv())) {
			tempInv = inventory;
			if (pendingAbilityChanges.isEmpty()) {
				tempSkillpoints = this.clazz.isProfession() ? this.player.getProfPoints()
						: this.player.getClassPoints();
			}
		}

	}

	@Override
	@EventHandler
	public void onInventoryClick(final InventoryClickEvent e) {
		Inventory inventory = e.getInventory();
		if (inventory.equals(getInv()) || inventory.equals(tempInv)) {
			e.setCancelled(true);
		} else {
			return;
		}
		
		if(isOnCooldown)
			return;

		final ItemStack clickedItem = e.getCurrentItem();
		final ClickType clickType = e.getClick();

		if (clickedItem == null || clickedItem.getType().isAir())
			return;

		isOnCooldown = true;
		new BukkitRunnable() {

			@Override
			public void run() {
				isOnCooldown = false;
			}
			
		}.runTaskLater(FantasyClasses.getPlugin(), 2);
		
		final Player p = (Player) e.getWhoClicked();
		p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 0.4F, 1F);

		if (clickedItem.equals(back.getItem())) {
			back.getLinkedInventory().refresh();
			return;
		} else if (clickedItem.equals(confirm.getItem())) {
			onConfirmClick();
			return;
		}

		Skill skill = getMatchingSkill(clickedItem, e.getSlot());
		if (skill == null)
			return;

		Ability ability = skill.getAbility();
		TempSkill tempSkill = new TempSkill(skill);
		
		int cost = ability.getSkillPointCost();
		int changeAmt = 1;
		int currentLevel = -1;

		for (Entry<TempSkill, Integer> entry : pendingAbilityChanges.entrySet()) {
			if (entry.getKey().getRealAbility().getClass().equals(ability.getClass())) {
				tempSkill = entry.getKey();
				ability = tempSkill.getFakeAbility();
				currentLevel = ability.getCurrentLevel();
				break;
			}
		}

		if (currentLevel < 0)
			currentLevel = ability.getCurrentLevel();

		boolean isSpendingPoints = false;

		if (clickType.equals(ClickType.LEFT)) {
			isSpendingPoints = true;

			if (!canSpendSkillPoints(skill))
				return;

		} else if (clickType.equals(ClickType.RIGHT)) {
			isSpendingPoints = false;

			if (!isPrereqAmtMet(ability)) {
				zeroOutSubsequentSkillGUIs(skill);
			}

		} else
			return;

		if (!isSpendingPoints) {
			changeAmt = -changeAmt;
		}

		int newLevel = calculateNewLevel(currentLevel, changeAmt, ability);

		tempSkill.setLevel(newLevel);
		pendingAbilityChanges.put(tempSkill, newLevel);
		tempSkillpoints -= (newLevel - currentLevel) * cost;

		//player.getPlayer().sendMessage("Ability: " + tempSkill.getFakeAbility().getName());
		//player.getPlayer().sendMessage("New Level: " + newLevel);

		tempInv.setItem(e.getSlot(), setGUIItemLevel(tempSkill.getFakeAbility(), newLevel));
		p.openInventory(tempInv);
	}

	private int calculateNewLevel(int startAmt, int changeAmt, Ability abil) {
		if (startAmt + changeAmt > abil.getMaxLevel()) {
			return abil.getMaxLevel();
		} else if (startAmt + changeAmt < 0) {
			return 0;
		} else if (changeAmt * abil.getSkillPointCost() > this.tempSkillpoints) {
			Player p = this.player.getPlayer();
			p.getPlayer().playSound(p, Sound.ENTITY_VILLAGER_NO, 0.5f, 0.8f);
			p.sendMessage(ChatUtils.chat("&4Not Enough Skillpoints"));
			return startAmt;
		}

		return startAmt + changeAmt;
	}

	// When Confirm Button is Clicked
	private void onConfirmClick() {
		Player p = player.getPlayer();

		if (pendingAbilityChanges.isEmpty()) {
			p.getPlayer().playSound(p, Sound.BLOCK_ANVIL_FALL, 0.6f, 0.7f);
			p.sendMessage(ChatUtils.chat("&6No Changes Made"));

			return;
		}

		int skillpoints = this.clazz.isProfession() ? this.player.getProfPoints() : this.player.getClassPoints();
		int totalCost = 0;
		for (Entry<TempSkill, Integer> entry : pendingAbilityChanges.entrySet()) {
			totalCost = (entry.getValue() - entry.getKey().getFakeAbility().getCurrentLevel())
					* entry.getKey().getFakeAbility().getSkillPointCost();
		}

		if (totalCost > skillpoints) {
			p.getPlayer().playSound(p, Sound.ENTITY_VILLAGER_NO, 0.5f, 0.8f);
			p.sendMessage(ChatUtils.chat("&4Not Enough Skillpoints"));

			return;
		}

		if (clazz.isProfession()) {
			if (!player.addChosenProfession(clazz)) {
				p.getPlayer().playSound(p, Sound.ENTITY_VILLAGER_NO, 0.5f, 0.8f);
				p.sendMessage(ChatUtils.chat(
						"&4ERROR: You have already chosen your two professions. You may reset your choices via a drop from Fantasy Bosses."));
				return;
			}
		} else {
			if (!player.setChosenClass(clazz)) {
				p.getPlayer().playSound(p, Sound.ENTITY_VILLAGER_NO, 0.5f, 0.8f);
				p.sendMessage(ChatUtils.chat(
						"&4ERROR: You have already chosen your class. You may reset your choice via a drop from Fantasy Mobs."));
				return;
			}
		}

		boolean changesMade = false;

		for (Entry<TempSkill, Integer> entry : pendingAbilityChanges.entrySet()) {
			/*p.sendMessage(ChatUtils.chat("&6Ability: " + entry.getKey().getRealAbility().getName()));
			p.sendMessage(ChatUtils.chat("&4Old Ability Level: " + entry.getKey().getRealAbility().getCurrentLevel()));
			p.sendMessage(ChatUtils.chat("&aNew Ability Level: " + entry.getValue()));
			p.sendMessage(ChatUtils.chat("&2Temp Ability Level: " + entry.getKey().getFakeAbility().getCurrentLevel()));*/
			if (entry.getKey().getRealAbility().getCurrentLevel() != entry.getValue()) {
				entry.getKey().getRealAbility().setLevel(entry.getValue());
				if (entry.getValue() > 0) {
					entry.getKey().getRealAbility().enable();
				}
					changesMade = true;
				}
		}

		if (!changesMade) {
			p.getPlayer().playSound(p, Sound.BLOCK_ANVIL_FALL, 0.6f, 0.7f);
			p.sendMessage(ChatUtils.chat("&6No Changes Made"));

			return;
		}

		if (clazz.isProfession())
			player.setProfPoints(tempSkillpoints);
		else
			player.setClassPoints(tempSkillpoints);

		p.getPlayer().playSound(p, Sound.BLOCK_ANVIL_USE, 0.55f, 1.25f);
		p.sendMessage(ChatUtils.chat("&aTalents Changed!"));
		pendingAbilityChanges.clear();

		refresh();
	}

	private ItemStack setGUIItemLevel(Ability abil, int amt) {
		Ability tempSkill = abil;
		tempSkill.setLevel(amt);

		ItemStack item = tempSkill.getItemStack();
		ItemMeta meta = item.getItemMeta();

		List<String> newLore = meta.getLore();
		newLore.set(0, ChatUtils.chat("Level: &b" + amt + "&r/&6" + abil.getMaxLevel()));

		meta.setLore(newLore);

		item.setItemMeta(meta);

		return item;
	}

	private boolean canSpendSkillPoints(Skill skill) {
		FantasyPlayer fantasyPlayer = clazz.getFantasyPlayer();
		Player p = clazz.getPlayer();

		int skillPoints = !clazz.isProfession() ? fantasyPlayer.getClassPoints() : fantasyPlayer.getProfPoints();
		Ability ability = skill.getAbility();
		for (Entry<TempSkill, Integer> entry : pendingAbilityChanges.entrySet()) {
			if (entry.getKey().getRealAbility().getClass().equals(ability.getClass())) {
				ability = entry.getKey().getFakeAbility();
				break;
			}
		}

		if (ability == null)
			return false;

		if (clazz.isProfession()) {
			if (clazz.has2ChosenProfessions()) {
				p.getPlayer().playSound(p, Sound.ENTITY_VILLAGER_NO, 1.0f, 0.8f);
				p.sendMessage(ChatUtils.chat(
						"&4ERROR: You have already chosen your two professions. You may reset your choices via a drop from Fantasy Bosses."));
				return false;
			}
		} else {
			if (clazz.hasAChosenClass()) {
				p.getPlayer().playSound(p, Sound.ENTITY_VILLAGER_NO, 1.0f, 0.8f);
				p.sendMessage(ChatUtils.chat(
						"&4ERROR: You have already chosen your class. You may reset your choices via a drop from Fantasy Mobs."));
				return false;
			}
		}

		if (ability.getCurrentLevel() >= ability.getMaxLevel()) {
			p.getPlayer().playSound(p, Sound.ENTITY_VILLAGER_NO, 1.0f, 0.8f);
			p.sendMessage(ChatUtils.chat("&4ERROR: Maximum Level has been reached"));
			return false;
		}
		if (skillPoints < ability.getSkillPointCost()) {
			p.getPlayer().playSound(p, Sound.ENTITY_VILLAGER_NO, 1.0f, 0.8f);
			p.sendMessage(ChatUtils.chat("&4ERROR: Not enough Skillpoints"));
			return false;
		}
		if (skill.getPrev() != null) {
			Ability prev = skill.getPrev().getAbility();
			int prevLevel = -1;

			for (Entry<TempSkill, Integer> entry : pendingAbilityChanges.entrySet()) {
				if (entry.getKey().getRealAbility().equals(prev)) {
					prevLevel = entry.getKey().getFakeAbility().getCurrentLevel();
					break;
				}
			}

			if (prevLevel < 0)
				prevLevel = prev.getCurrentLevel();

			if (prevLevel < ((double) prev.getMaxLevel()) / 2.0) {
				p.playSound(p, Sound.ENTITY_VILLAGER_NO, 1.0f, 0.8f);
				p.getPlayer().sendMessage(ChatUtils
						.chat("&4Error: You need at least half of the max level invested in the prerequisite skill: &6"
								+ prev.getCommandName()));
				return false;
			}
		}

		return true;
	}

	private void zeroOutSubsequentSkillGUIs(Skill skill) {
		Set<Entry<Integer, Skill>> entrySet = clazz.getSkillMap().entrySet();
		for (Skill s : skill) {
			if (s.equals(skill))
				continue;

			Ability a = s.getAbility();
			if (a.getCurrentLevel() == 0)
				continue;

			int currentLevel = -1;
			for (Entry<TempSkill, Integer> entry : pendingAbilityChanges.entrySet()) {
				if (entry.getKey().getRealAbility().getClass().equals(a.getClass())) {
					currentLevel = entry.getKey().getFakeAbility().getCurrentLevel();
					break;
				}
			}

			if (currentLevel < 0)
				currentLevel = a.getCurrentLevel();

			for (Entry<Integer, Skill> e : entrySet) {
				if (e.getValue().equals(s)) {
					TempSkill tempSkill = new TempSkill(s);
					tempSkillpoints += currentLevel * a.getSkillPointCost();
					pendingAbilityChanges.put(tempSkill, 0);
					tempInv.setItem(e.getKey(), setGUIItemLevel(tempSkill.getFakeAbility(), 0));
				}
			}
		}
	}

	private static boolean isPrereqAmtMet(Ability abil) {
		return abil.getCurrentLevel() >= ((double) abil.getMaxLevel()) / 2.0;
	}

	public Skill getMatchingSkill(ItemStack item, int slot) {
		if (item == null)
			return null;

		ItemMeta meta = item.getItemMeta();

		for (Entry<Integer, Skill> e : this.clazz.getSkillMap().entrySet()) {
			if (meta.getDisplayName().equalsIgnoreCase(ChatUtils.chat("&6" + e.getValue().getAbility().getName()))
					&& slot == e.getKey()) {
				return e.getValue();
			}
		}

		return null;
	}

	private class TempSkill {

		private Ability realAbility;
		private Ability fakeAbility;

		TempSkill(Skill skill) {
			this.setRealAbility(skill.getAbility());
			this.setFakeAbility(Ability.clone(skill.getAbility()));

			this.fakeAbility.setPrerequisite(realAbility.getPrerequisite());
		}

		public void setLevel(int newLevel) {
			this.fakeAbility.setLevel(newLevel);
		}

		public Ability getRealAbility() {
			return realAbility;
		}

		private void setRealAbility(Ability realAbility) {
			this.realAbility = realAbility;
		}

		public Ability getFakeAbility() {
			return fakeAbility;
		}

		private void setFakeAbility(Ability fakeAbility) {
			this.fakeAbility = fakeAbility;
		}

	}
}
