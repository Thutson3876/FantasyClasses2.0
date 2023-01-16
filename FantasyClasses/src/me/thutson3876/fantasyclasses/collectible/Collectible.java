package me.thutson3876.fantasyclasses.collectible;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.thutson3876.fantasyclasses.util.ChatUtils;

public enum Collectible {

	ANCIENT_TECHNIQUE(Material.LECTERN, generateAncientTechniqueLore()),
	MINING_SCHEMATICS(Material.SKELETON_SKULL, generateMiningSchematicsLore()),
	DRUIDIC_INSCRIPTION(Material.FLOWERING_AZALEA_LEAVES, generateDruidicInscriptionLore()),
	ETCHED_GLASS(Material.GLASS, generateEtchedIceLore());

	private final Material type;
	private final List<String> lore;
	private static final Random rng = new Random();

	private Collectible(Material type) {
		this.type = type;
		this.lore = null;
	}

	private Collectible(Material type, List<String> lore) {
		this.type = type;
		this.lore = lore;
	}

	public Material getType() {
		return type;
	}

	public static Collectible getMatchingCollectible(Material type) {
		for (Collectible c : Collectible.values()) {
			if (c.getType().equals(type))
				return c;
		}

		return null;
	}

	public static ItemStack generateDrop() {
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatUtils.chat("&6Untapped Knowledge"));
		List<String> lore = new ArrayList<>();
		lore.add(ChatUtils.chat("Contained Experience: &6" + (20 - rng.nextInt(15))));
		meta.setLore(lore);
		item.setItemMeta(meta);

		return item;
	}
	
	public static ItemStack generateClassResetDrop() {
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatUtils.chat("&6Tome of Decay"));
		List<String> lore = new ArrayList<>();
		lore.add(ChatUtils.chat("Resets class on right-click"));
		meta.setLore(lore);
		item.setItemMeta(meta);

		return item;
	}
	
	public static ItemStack generateProfessionResetDrop() {
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatUtils.chat("&6Tome of Oblivion"));
		List<String> lore = new ArrayList<>();
		lore.add(ChatUtils.chat("Resets professions on right-click"));
		meta.setLore(lore);
		item.setItemMeta(meta);

		return item;
	}

	public static int getDropExpAmount(ItemStack item) {
		if (item == null)
			return 0;

		if (!item.getType().equals(Material.ENCHANTED_BOOK))
			return 0;

		if (!item.hasItemMeta())
			return 0;

		ItemMeta meta = item.getItemMeta();
		if (!meta.getDisplayName().equals(ChatUtils.chat("&6Untapped Knowledge")))
			return 0;

		char[] chars = meta.getLore().get(0).toCharArray();
		StringBuilder sb = new StringBuilder();
		for (char c : chars) {
			if (Character.isDigit(c)) {
				sb.append(c);
			}
		}

		if (sb.length() == 0)
			return 0;

		String s = sb.toString();

		return Integer.parseInt(s.substring(1));
	}
	
	public static boolean isSkillResetItem(ItemStack item) {
		return generateClassResetDrop().isSimilar(item);
	}
	
	public static boolean isProfessionResetItem(ItemStack item) {
		return generateProfessionResetDrop().isSimilar(item);
	}

	public String getRandomLore() {
		if (this.lore == null || lore.isEmpty())
			return "";

		Random rng = new Random();

		return ChatUtils.chat(lore.get(rng.nextInt(lore.size())));
	}
	
	public static String getAllRandomLore() {
		Random rng = new Random();
		List<String> lore;
		
		int i = rng.nextInt(values().length);
		
		lore = values()[i].lore;
		
		if (lore == null || lore.isEmpty())
			return "";

		return ChatUtils.chat(lore.get(rng.nextInt(lore.size())));
	}

	/*private void drop(BlockBreakEvent e) {
		Block b = e.getBlock();
		Player p = e.getPlayer();
		p.sendMessage(this.getRandomLore());
		p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 0.8f, 1.0f);
		e.setDropItems(false);
		b.getWorld().dropItemNaturally(b.getLocation(), Collectible.generateDrop());
		//FantasyClasses.getPlugin().removeCollectible(b.getLocation(), this);
	}*/

	private static List<String> generateAncientTechniqueLore() {
		List<String> lore = new ArrayList<>();
		lore.add("&3Ascend. Ascend. Ascend!!!");
		lore.add("&3Oh Great One, grant us your wisdom. Guide us upwards. Let us rise as you have.");
		lore.add("&3Snowflakes gliding down. Each one shining beautifully. Such fools as you are.");
		lore.add("&3From the deep you rose. Come to save us from ourselves. Too late, did they learn.");
		lore.add(
				"&3Power so frequently sought after. Strength always the driving force. Ironic that such desirable traits have become your undoing.");
		lore.add(
				"&3Were you to fail to ascend, embrace the light. It will save you should you regress back to the realm of demons and parasites.");
		lore.add("&3Stave off the tarred thoughts, and your mind shall too glow. Purity in the next life.");
		lore.add(
				"&3The false god soared as we now dream. How does a creature composed of death and deepslate even float? Perhaps we are the fools to oppose such an unstoppable force.");
		lore.add("&3Redeem your soul through patience and control. Seek enlightenment, abandon your fortunes. Achieve a new life among the stars.");
		lore.add("&3Frozen plains, snowy mountain caps. Perhaps we aren't too different. But that fascination with your ancestry will forever seperate us.");
		lore.add("&3Wind torn ruins. Withered fields. A people with no memory of the past, scattering to reassemble what was broken. Each piece a grain of the ever-turning hourglass.");
		lore.add("&3Wind and sky command me! Grant me your haste! Strike unto me the light! Rebirth me so I can soar among your vast heights!");
		
		return lore;
	}

	private static List<String> generateMiningSchematicsLore() {
		List<String> lore = new ArrayList<>();

		lore.add("&8Another has disappeared among the mineshafts... Its only a matter of time before I'm next.");
		lore.add(
				"&8Sylvan claimed he was attacked by a tar covered skeleton. Its dark down here sure, but he insisted the corpse itself absorbed the light around him. Shit these wounds are festering quickly.");
		lore.add("&8I will find you brother. I will save you from your withered mind. I-");
		lore.add(
				"&8It's hard work down here. No telling how much time has passed. The newest delvers always whine and complain bout how dark it is. They'll get used to it though. I sure did.");
		lore.add("&8Diggy diggy hole...");
		lore.add(
				"&8-he admitted to it. He's the dumbass that ruined that compactor. Funny how these marvels of technology can still be ruined by our tiny ass bladders.");
		lore.add("&8The enginers here are so strange. I swear they'd rather be blown to pieces than lie in a grave.");
		lore.add(
				"&8I found a strange shrine today. Lying around a piece of debris, a few pieces of TNT. The way it was rested... felt deliberate.");
		lore.add(
				"&8Hit another record today. Now its up to 3 Engineers in one day. We're starting to run out of TNT and ancient debris...");
		lore.add("&8Don't bury me down here. Don't let me become like them. Please...");
		lore.add(
				"&8Excavator's Compliance Rule 2: Any and all fines accumulated by workers shall be transferred to the nearest living relative upon said worker's expiration.");
		lore.add(
				"&8Excavator's Compliance Rule 4: Always keep your pickaxe on hand and your wear your helmet. Safety first!");
		lore.add(
				"&8Excavator's Compliance Rule 5: No food or drink permitted in workspace. May attract unwanted parasitic pests.");
		lore.add(
				"&8Excavator's Compliance Rule 6: For each cleaned and dusted skull turned in you'll gain an extra 15 minute break!");
		lore.add(
				"&8Excavator's Compliance Rule 7: Any worker found to be lacking undergarments while on the clock will immediately be required to leave the premises.");
		lore.add(
				"&8Excavator's Compliance Rule 11: While working with molten materials, use netherite infused gloves.");
		lore.add(
				"&8Excavator's Compliance Rule 13: Any and all workers found dead in company workspace shall be fined an amount no lesser than one year's wage.");
		lore.add(
				"&8Excavator's Compliance Rule 17: Reflective and/or shiny clothing is prohibited. There have been reported cases of workers wearing such clothing going missing or being seriously injured for mysterious reasons.");
		lore.add(
				"&8Excavator's Compliance Rule 25: Breaks while operating void compactor are prohibited. Must provide an equal to take over, or wait for response from a supervisor.");
		lore.add(
				"&8Excavator's Compliance Rule 37: Don't approach the shadows you see. Contact your supervisor immediately upon seeing one.");
		lore.add(
				"&8Excavator's Compliance Rule 38: Keep all religious symbols, practices, and acts of worship out of the workplace. These may offend the creatures found to be roaming the mines and cause them to act aggressively.");
		lore.add(
				"&8Excavator's Compliance Rule 40: Don't stack Refined Deepslate more than 2 meters high. It can be unstable and fall. In rare cases it has been known to gain conciousness. Please advise.");
		lore.add(
				"&8Excavator's Compliance Rule 42: If you see a four-headed, tar-encased statue, be sure to bow down and submit to its will. Should it ever awaken and devour our societal structure, perhaps it will take pity on you.");
		lore.add(
				"&8Excavator's Compliance Rule 43: Should your discovery by the tarred become inevitable, please fold your vest and lay your helmet, pickaxe, and nametag together for easy corpse identification.");
		lore.add(
				"&8Excavator's Compliance Rule 46: It has been discovered that placing the remains of the tarred upon polished deepslate in a 4-segmented statue-like pattern could awaken into one of those monstrosities. We are informing you of this so that you know *not* to do it yourself.");

		return lore;
	}

	private static List<String> generateDruidicInscriptionLore() {
		List<String> lore = new ArrayList<>();

		lore.add("&2Freedom... at last...");
		lore.add("&2Fresh air. Light so bright. I cannot bear.");
		lore.add("&2These animals so pure and free. How I envy their naivety.");
		lore.add("&2Those religious zealots seak the frozen peaks. I am quite content down here in the warmth.");
		lore.add("&2Much to relearn. Such vast purity lost. Now we must rediscover, or face our hubris manifested.");
		lore.add(
				"&2If you find a remnant shard of the past taking the appearance of a miner, proceed with caution. You may try to help them-");
		lore.add("&2-and they will resist. You must ensure they swallow the golden fruit. It's the only cure.");
		lore.add("&2One needn't the bindings of social constraints when guided by such free creatures.");
		lore.add(
				"&2To expand, seek out new experiences, stretch across these vast plains and beyond their shores... it's as natural as the leaves floating in the wind.");
		lore.add("&2What is an ocean, but a plain? A sprawling hallowed land with creatures and fruits of plenty");
		lore.add(
				"&2Just as we migrated from the dark depths, destiny guides us further. The waves are calling, friend");
		lore.add("&2I found a strange altar in the forest today. It looked like some sort of metal spike surrounded by human skulls. Could it be a remnant from the past?");
		lore.add("&2That altar I found... it seems to attract storms. Perhaps the cube of metal that the spike sits upon helps to channel the dark magic in some way?");
		lore.add("&2Another meager harvest. These recent winters must be a sign. Adapt or die, there is no other option.");
		lore.add("&2Ancestors of old, these tales are for you alone. The sun is a beacon of light. Let it guide you in the afterlife.");
		lore.add("&2Ancestors of old, these tales are for you alone. Search for the fruit of holy light. Only then will you be freed.");
		lore.add("&2Ancestors of old, these tales are for you alone. You cast yourself into the depths. We have risen from them.");
		lore.add("&2Ancestors of old, these tales are for you alone. Your masters still haunt us. Does remorse feel alike in the afterlife?");
		lore.add("&2Ancestors of old, these tales are for you alone. Power lies not in the strength of your armies or the density of your walls, but by the abundantness of your harvest.");
		lore.add("&2Ancestors of old, these tales are for you alone. Residual magics still linger in the very air, leaking from your ancient tombs. Soon you'll be sealed away... permanently.");
		
		lore.add(
				"&8I won't let the knowledge be lost... we've forgotten our true nature and embraced this new life of complacency when we were once innovators, creators, and engineers.");
		lore.add(
				"&8Deepslate 2 meters high, then extending out 1 meter more on each side... 4 skulls dipped in tar and-");
		lore.add(
				"&8-a sunken conduit of power placed in the center.");
		lore.add(
				"&8A block of copper with a rod of lightning placed above it, surround with-");

		return lore;
	}

	private static List<String> generateEtchedIceLore() {
		List<String> lore = new ArrayList<>();

		lore.add(
				"&bI've stored the contents in the Temple Overseers. None shall access such cursed knowledge ever again.");
		lore.add("&bPoor Pretzel... she probably thinks she was abandoned... I'm so sorry.");
		lore.add(
				"&bThe conduits have proven invaluable in the Battle for Timal. I fear what would happen should the withered get their hands on such a device.");
		lore.add(
				"&bHave we risen to overcome great challenges? Or perhaps forgotten how to live in peace. To go back and ask those first fishmongers that decided to delve deeper... we will never know");
		lore.add(
				"&bA thousand years from now what will be left? Scarred remnants and ruined temples washed ashore. These accomplishments reek of echoed mistakes.");
		lore.add(
				"&bThe funeral was such an extravagence. Head to toe dawning gaudy jewels and frozen relics. Such bright colors for what is supposed to be a depressing event. The appeal of modern fashion continues to elude me. ");
		lore.add(
				"&bAhsmi has gone missing. Lost? Kidnapped for ransom? No one seems to know. Only the king ever thought it was a good idea to have a pet bear.");
		lore.add(
				"&bWhat do you suppose happens now? The royal line is broken. Are we just supposed to line up and agree on a new ruler? Entertaining times these are indeed.");
		lore.add(
				"&bRumor has it Ahsmi continues to roam the frozen wastes, reminiscing her hunting trips with the deceased king. Some even have claimed to see him riding her... at least the tablets are more entertaining nowadays.");
		lore.add("&bFind Ahsmi and her brood. She must be out there. They are the key.");
		lore.add(
				"&bWhen the royal mother claims the lives of her children with her dying breath, will the king return to her side.");
		lore.add("&4LOG 02CE: LARGE THREAT DETECTED | ORGANIC COMPONENTS WITHERING | FAILURE IMMINENT |");
		lore.add("&4LOG 02D9: SEVERED CONNECTION | CONDUIT MISSING | GUARDIAN POWERING DOWN |");
		lore.add("&4LOG 02DA: ILLEGAL DATABASE ACCESS | LEAVE IMMEDIATELY | SUFFER TERMINATION FOR NO COMPLIANCE |");
		lore.add("&4LOG 02DB: LEAVE IMMEDIATELY | LEAVE IMMEDIATELY | PLEASE LEAVE | DECOUPLE | PLEASE |");
		lore.add(
				"&4LOG 030E: ARSONIST CEREMONY DATA RETRIEVED | 0004 [REDACTED] | 0001 [REDACTED] | PRESERVED TARGET HEAD");
		lore.add("&4LOG 030F: [REDACTED] OBLIQUE AT EACH CORNER | REST HEAD ON [REDACTED] CENTERED |");
		lore.add("&4LOG 7E1A: TARRED REMAINS | POWER CONDUIT | DEEPSLATE [PRISTINELY POLISHED] |");
		lore.add("&4LOG 7E1B: FOUR-HEADED STATUE | INSERT POWER CONDUIT IN CENTER |");
		lore.add(
				"&4LOG 9131: COLD REACTOR FUEL AT 24% CAPACITY | AUXILARY PROCESSES CEASED | GUARDIAN PRODUCTION [REDACTED] |");

		return lore;
	}
}
