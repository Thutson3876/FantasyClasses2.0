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

public class HelpGUI extends BasicGUI {

	private static final String TITLE = "Help Menu";
	private static final int SIZE = 27;

	private static final ColorCode PRIMARY_COLOR = ColorCode.GREEN;
	private static final ColorCode SECONDARY_COLOR = ColorCode.AQUA;
	
	
	
	public HelpGUI(Player p, AbstractGUI back) {
		super(p, TITLE, SIZE, null, back, new ArrayList<GuiItem>());
	}
	
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

		items.add(AbilityUtils.createItem(ColorCode.BOLD + "" + ColorCode.GOLD + "Gaining Skillpoints",
				Material.GOLD_ORE, 1,
				Arrays.asList(new LoreEntry[] { new LoreEntry(PRIMARY_COLOR,
						"Skillpoints can be gained from any form of gameplay, from mining, to exploring, to even achievement hunting."),
						new LoreEntry(SECONDARY_COLOR,
								"The most fruitful method, though, is to hunt down the custom mobs and bosses. &4BEWARB") })));
		
		items.add(AbilityUtils.createItem(ColorCode.BOLD + "" + ColorCode.GOLD + "Spending Skillpoints",
				Material.GOLD_NUGGET, 1,
				Arrays.asList(new LoreEntry[] { new LoreEntry(PRIMARY_COLOR,
						"Left-click on a talent to select it for spending Skillpoints. Right-click on a talent to remove a point from it."),
						new LoreEntry(SECONDARY_COLOR,
								"Changes made don't take into effect until the Confirm button is clicked.") })));

		items.add(AbilityUtils.createItem(ColorCode.BOLD + "" + ColorCode.GOLD + "Binding Abilities",
				Material.DIAMOND_PICKAXE, 1,
				Arrays.asList(new LoreEntry[] {
						new LoreEntry(PRIMARY_COLOR, "Some abilities are &bBindable" + PRIMARY_COLOR
								+ ". To bind an ability to an item type, use the command &f/bindability &f<ability>"),
						new LoreEntry(SECONDARY_COLOR,
								"Activate that ability while holding any item of that same type. ") })));

		items.add(AbilityUtils.createItem(ColorCode.BOLD + "" + ColorCode.GOLD + "Commands", Material.COMMAND_BLOCK, 1,
				Arrays.asList(new LoreEntry[] { 
						new LoreEntry(ColorCode.WHITE, "/openmenu"),
						new LoreEntry(ColorCode.WHITE, "/bindability <ability>"),
						new LoreEntry(ColorCode.WHITE, "/togglefriendlyfire"),
						new LoreEntry(ColorCode.WHITE, "/toggledamagemeters"),
						new LoreEntry(ColorCode.WHITE, "/toggledetailedmeters"),
						new LoreEntry(ColorCode.WHITE, "/togglestatuseffectmessages"),
						new LoreEntry(ColorCode.WHITE, "/togglearrowtracker"),
						new LoreEntry(ColorCode.WHITE, "/fantasyhelp"),

						new LoreEntry(SECONDARY_COLOR, "Just mess around with these.") })));
		
		items.add(AbilityUtils.createItem(ColorCode.BOLD + "" + ColorCode.GOLD + "Custom Mobs/Bosses",
				Material.SKELETON_SKULL, 1,
				Arrays.asList(new LoreEntry[] { new LoreEntry(PRIMARY_COLOR,
						"Every custom mob added has unique spawn requirements and loot drops. They are difficult foes with fitting rewards, including hinting at how to build special ritual structures."),
						new LoreEntry(SECONDARY_COLOR,
								"Similar to the custom mobs, bosses added have varying spawn conditions. However, many are spawned by building a special kind of small structure. The last placed block is most important.") })));
		
		
		for(int i = 0; i < items.size(); i++) {
			getInv().setItem(10 + i, items.get(i));
		}
		
		this.items.add(back);

		fillGaps(createGuiItem(null, Material.BLACK_STAINED_GLASS_PANE, " ").getItem());
	
	}

}
