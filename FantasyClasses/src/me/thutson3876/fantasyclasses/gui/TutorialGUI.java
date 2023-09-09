package me.thutson3876.fantasyclasses.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.chat.ColorCode;
import me.thutson3876.fantasyclasses.util.item.LoreEntry;

public class TutorialGUI extends BasicGUI {

	private static final String TITLE = "Tutorial";
	private static final int SIZE = 27;

	private static final ColorCode PRIMARY_COLOR = ColorCode.GREEN;
	private static final ColorCode SECONDARY_COLOR = ColorCode.AQUA;

	private AbstractGUI invToLinkTo = null;

	public TutorialGUI(Player p) {
		super(p, TITLE, SIZE, null, null, new ArrayList<GuiItem>());

		if (items == null || items.isEmpty())
			return;

		getInv().setItem(13, items.get(0).getItem());

		if (forward != null)
			initializeForwardItem(items.get(0).getLinkedInventory());

		fillGaps(createGuiItem(null, Material.BLACK_STAINED_GLASS_PANE, " ").getItem());
	}

	// make this display a single guiitem that links to [generatepages()]
	@Override
	protected void initializeItems() {
		List<ItemStack> items = new ArrayList<>();

		items.add(AbilityUtils.createItem(ColorCode.BOLD + "" + ColorCode.GOLD + "Classes and Professions",
				Material.BOOK, 1,
				Arrays.asList(new LoreEntry[] { new LoreEntry(PRIMARY_COLOR,
						"Classes change the flow of combat. Professions allow one to specialize in resource gathering and management."),
						new LoreEntry(SECONDARY_COLOR,
								"Players may have 1 Class and 2 Professions. Each are Chosen once you spend Skillpoints in that particular class/profession.") })));

		items.add(AbilityUtils.createItem(ColorCode.BOLD + "" + ColorCode.GOLD + "Changing Class/Professions",
				Material.GRINDSTONE, 1,
				Arrays.asList(new LoreEntry[] { new LoreEntry(PRIMARY_COLOR,
						"Class choice can only be reset via a special item that drops from mobs unique to the plug-in."),
						new LoreEntry(SECONDARY_COLOR,
								"Professions can only be changed via a special item that drops from bosses.") })));

		items.add(AbilityUtils.createItem(ColorCode.BOLD + "" + ColorCode.GOLD + "Spending Skillpoints",
				Material.GOLD_NUGGET, 1,
				Arrays.asList(new LoreEntry[] { new LoreEntry(PRIMARY_COLOR,
						"Left-click on a talent to select it for spending Skillpoints. Right-click on a talent to remove a point from it."),
						new LoreEntry(SECONDARY_COLOR,
								"Changes made don't take into effect until the Confirm button is clicked.") })));

		items.add(AbilityUtils.createItem(ColorCode.BOLD + "" + ColorCode.GOLD + "Blah Blah Blah", Material.SPYGLASS, 1,
				Arrays.asList(new LoreEntry[] { new LoreEntry(ColorCode.GRAY,
						"The echoing collapse threatens us all. Take these for your journey. May the stars guide your eye..."),
						new LoreEntry(SECONDARY_COLOR, "Click this to get diamonds.") })));

		items.add(AbilityUtils.createItem(ColorCode.BOLD + "" + ColorCode.GOLD + "Binding Abilities",
				Material.DIAMOND_PICKAXE, 1,
				Arrays.asList(new LoreEntry[] {
						new LoreEntry(PRIMARY_COLOR, "Some abilities are &bBindable" + PRIMARY_COLOR
								+ ". To bind an ability to an item type, use the command &f/bindability &f<ability>"),
						new LoreEntry(SECONDARY_COLOR,
								"Activate that ability while holding any item of that same type. ") })));

		items.add(AbilityUtils.createItem(ColorCode.BOLD + "" + ColorCode.GOLD + "Commands", Material.COMMAND_BLOCK, 1,
				Arrays.asList(new LoreEntry[] { new LoreEntry(ColorCode.WHITE, "/openmenu"),
						new LoreEntry(ColorCode.WHITE, "/fantasyhelp"),
						new LoreEntry(ColorCode.WHITE, "/bindability <ability>"),
						new LoreEntry(ColorCode.WHITE, "/togglefriendlyfire"),
						new LoreEntry(ColorCode.WHITE, "/toggledamagemeters"),
						new LoreEntry(ColorCode.WHITE, "/toggledetailedmeters"),
						new LoreEntry(ColorCode.WHITE, "/togglestatuseffectmessages"),
						new LoreEntry(ColorCode.WHITE, "/togglearrowtracker"),

						new LoreEntry(SECONDARY_COLOR, "Just mess around with these.") })));

		items.add(AbilityUtils.createItem(ColorCode.BOLD + "" + ColorCode.GOLD + "Thank You", Material.HEART_OF_THE_SEA,
				1,
				Arrays.asList(new LoreEntry[] {
						new LoreEntry(PRIMARY_COLOR,
								"You're all set! There's more info in the main menu so don't forget to use "
										+ ColorCode.WHITE + "/openmenu" + PRIMARY_COLOR + "!"),
						new LoreEntry(SECONDARY_COLOR,
								"Thank you for taking the time to read this and engage with the plug-in. I hope you enjoy it!") })));

		generatePages(items);

		GuiItem page0 = new GuiItem(
				AbilityUtils.createItem(ColorCode.BOLD + "" + ColorCode.GOLD + "Tutorial", Material.ENCHANTED_BOOK, 1,
						Arrays.asList(new LoreEntry[] {
								new LoreEntry(PRIMARY_COLOR, "Click through to learn about the plug-in!"),
								new LoreEntry(SECONDARY_COLOR, "Will also be available in the menu") })),
				invToLinkTo);

		this.items.add(page0);
	}

	private BasicGUI generatePages(List<ItemStack> items) {
		return generatePage(items, items.size() - 1, new MainMenuGUI(this.player.getPlayer()));
	}

	// recursively generates pages (starting with final page and ending with this
	// page
	private BasicGUI generatePage(List<ItemStack> items, int index, BasicGUI nextGUI) {
		if (index < 0) {
			return this;
		}

		Player p = this.player.getPlayer();
		List<GuiItem> guiList = new ArrayList<>();
		TutorialPageGUI gui = new TutorialPageGUI(p, TITLE, SIZE, nextGUI, null, guiList);
		GuiItem guiItem;

		if (index == 0) {
			this.invToLinkTo = gui;
		}

		guiItem = new GuiItem(items.get(index), gui, nextGUI);
		guiList.add(guiItem);
		gui.setBack(generatePage(items, index - 1, gui));
		gui.setItems(guiList);
		return gui;

	}
}
