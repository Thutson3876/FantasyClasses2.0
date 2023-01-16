package me.thutson3876.fantasyclasses.playermanagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.classes.berserker.Berserker;
import me.thutson3876.fantasyclasses.classes.monk.Monk;
import me.thutson3876.fantasyclasses.classes.ranger.Ranger;
import me.thutson3876.fantasyclasses.gui.MainMenuGUI;
import me.thutson3876.fantasyclasses.gui.treegui.TreeGUI;
import me.thutson3876.fantasyclasses.professions.enchanter.Enchanter;
import me.thutson3876.fantasyclasses.professions.enchanter.customenchantments.Enchantments;
import me.thutson3876.fantasyclasses.professions.fisherman.Fisherman;
import me.thutson3876.fantasyclasses.professions.miner.Miner;
import me.thutson3876.fantasyclasses.util.ArmorType;
import me.thutson3876.fantasyclasses.util.ChatUtils;

public class FantasyPlayer {

	protected static final FantasyClasses plugin = FantasyClasses.getPlugin();

	private static final Map<Material, Integer> ARMOR_VALUES;
	
	private Player bukkitPlayer;
	private List<AbstractFantasyClass> classes = new ArrayList<>();
	private List<AbstractFantasyClass> professions = new ArrayList<>();
	private AbstractFantasyClass chosenClass;
	private List<AbstractFantasyClass> chosenProfessions = new ArrayList<>();

	private boolean friendlyFire = true;
	private boolean damageMeters = false;

	private int classPoints = 0;
	private int profPoints = 0;
	private int level = 0;
	private long skillExp = 0;

	private int armorType = 0;
	private Map<Enchantments, Integer> availableEnchants = new HashMap<>();
	
	static {
		Map<Material, Integer> values = new HashMap<>();
		
		int value = 1;
		
		for(ArmorType armor : ArmorType.values()) {
			for(Material mat : armor.getMaterials())
				values.put(mat, value);
			
			value++;
		}
		
		ARMOR_VALUES = values;
	}
	
	public FantasyPlayer(Player p) {
		availableEnchants.put(Enchantments.CURSE, 0);
		availableEnchants.put(Enchantments.UNCOMMON, 1);
		availableEnchants.put(Enchantments.RARE, 1);
		
		bukkitPlayer = p;
		String uuid = p.getUniqueId().toString();
		classes.add(new Ranger(this));
		classes.add(new Monk(this));
		classes.add(new Berserker(this));
		//classes.add(new Druid(this));
		//classes.add(new Witchcraft(this));
		//classes.add(new HighRoller(this));
		//classes.add(new Dungeoneer(this));
		//classes.add(new SeaGuardian(this));

		professions.add(new Miner(this));
		professions.add(new Fisherman(this));
		professions.add(new Enchanter(this));
		
		FileConfiguration config = plugin.getConfig();
		if (!config.contains("players." + uuid)) {
			config.set("players." + uuid + ".name", p.getDisplayName());
			config.set("players." + uuid + ".friendlyfire", this.friendlyFire);
			config.set("players." + uuid + ".damagemeters", this.damageMeters);
			config.set("players." + uuid + ".classPoints", this.classPoints);
			config.set("players." + uuid + ".profPoints", this.profPoints);
			config.set("players." + uuid + ".exp", this.skillExp);
			// config.set("players." + uuid + ".class.", this.chosenClass);
			// config.set("players." + uuid + ".professions.", this.chosenProfessions);

		} else {
			this.loadFromConfig();
		}

		plugin.saveConfig();
	}

	public void setArmorType(int val) {
		this.armorType = val;
	}
	
	public int getArmorType() {
		return this.armorType;
	}
	
	public boolean isViolatingArmorType() {
		ItemStack[] armorContents = this.bukkitPlayer.getInventory().getArmorContents();
		
		for (int i = 0; i < armorContents.length; i++) {
			if (armorContents[i] != null) {
				Integer value = ARMOR_VALUES.get(armorContents[i].getType());
				
				if (value != null && value > armorType) {
					bukkitPlayer.sendMessage(ChatUtils.chat("&3Armor slot " + i + ": &4violating armor type"));
					return true;
				}
					
			}
		}
		return false;
	}
	
	public boolean hasFriendlyFire() {
		return friendlyFire;
	}

	public void setFriendlyFire(boolean friendlyFire) {
		this.friendlyFire = friendlyFire;
	}

	public boolean hasDamageMeters() {
		return damageMeters;
	}

	public void setDamageMeters(boolean damageMeters) {
		this.damageMeters = damageMeters;
	}

	public void addClassPoints(int amt) {
		this.classPoints += amt;
	}

	public boolean spendClassPoints(int amt) {
		int newAmt = this.classPoints - amt;
		if (newAmt < 0) {
			return false;
		}

		this.classPoints = newAmt;
		return true;
	}

	public void setClassPoints(int amt) {
		this.classPoints = amt;
	}

	public void addProfPoints(int amt) {
		this.profPoints += amt;
	}

	public boolean spendProfPoints(int amt) {
		int newAmt = this.profPoints - amt;
		if (newAmt < 0) {
			return false;
		}

		this.profPoints = newAmt;
		return true;
	}

	public void setProfPoints(int amt) {
		this.profPoints = amt;
	}

	public long getSkillExp() {
		return skillExp;
	}

	public void addSkillExp(int amt) {
		if (calculateLevel() >= 50)
			return;

		if (calculateLevel(skillExp + amt) >= 50)
			skillExp = calculateExperience(51) - 2;
		else
			skillExp += amt;
		
		int newLevel = calculateLevel();
		if (amt > 0)
			bukkitPlayer.sendMessage(ChatUtils.chat("&6Gained &3" + amt + " &6experience!"));

		if (newLevel > level) {
			int start = 0;
			int end = newLevel - level;
			int classPointsGain = 0;
			int profPointsGain = 0;
			
			if(newLevel % 2 != 0) {
				start++;
				end++;
			}
				
			for(int i = start; i < end; i++) {
					if(i % 2 != 0)
						classPointsGain++;
					else
						profPointsGain++;
			}
			
			this.classPoints += classPointsGain;
			this.profPoints += profPointsGain;
			level = newLevel;

			bukkitPlayer.playSound(bukkitPlayer.getLocation(), Sound.ENTITY_WANDERING_TRADER_YES, 1.0f, 1.3f);
			bukkitPlayer.sendMessage(ChatUtils.chat("&6Level Up! Your new level is &3" + newLevel));
			bukkitPlayer.sendMessage(ChatUtils.chat("&6Use &3/openmenu &6to spend your new skillpoints!"));
			if(classPointsGain > 0)
				bukkitPlayer.sendMessage(ChatUtils.chat("&6Class Skillpoints: &3+" + classPointsGain));
			if(profPointsGain > 0)
				bukkitPlayer.sendMessage(ChatUtils.chat("&6Profession Skillpoints: &3+" + profPointsGain));
		}
	}

	public int getPlayerLevel() {
		return level;
	}

	private int calculateLevel() {
		// 7,000 xp = lvl 50
		return (int) (Math.sqrt((0.4 * skillExp) + 9) - 3);
	}

	public static int calculateLevel(long exp) {
		return (int) (Math.sqrt((0.4 * exp) + 9) - 3);
	}

	public static long calculateExperience(int level) {
		return Math.round((Math.pow(((level) + 3), 2) - 9) / 0.4);
	}

	/*
	 * private int calculateLevel(int exp) { return (int) (Math.sqrt((0.4 * exp) +
	 * 9) - 3); }
	 */

	public long calculateCurrentLevelExpCost() {
		return Math.round((Math.pow(((level) + 3), 2) - 9) / 0.4);
	}

	public long calculateNextLevelExpCost() {
		return Math.round((Math.pow(((level + 1) + 3), 2) - 9) / 0.4);
	}

	public Player getPlayer() {
		return bukkitPlayer;
	}

	public int getClassPoints() {
		return classPoints;
	}

	public int getProfPoints() {
		return profPoints;
	}
	
	public Map<Enchantments, Integer> getAvailableEnchantments(boolean includeCurses){
		if(includeCurses)
			return this.availableEnchants;
		
		Map<Enchantments, Integer> newEnchantments = new HashMap<>();
		newEnchantments.putAll(this.availableEnchants);
		newEnchantments.remove(Enchantments.CURSE);
		
		return newEnchantments;
	}
	
	public void disallowEnchantments(Enchantments rarity) {
		this.availableEnchants.remove(rarity);
	}
	
	public void allowEnchantments(Enchantments rarity) {
		this.availableEnchants.put(rarity, 1);
	}
	
	//Only allows player to add to rarity if they have it
	public void putEnchantments(Enchantments rarity, int level) {
		if(this.availableEnchants.containsKey(rarity))
			this.availableEnchants.put(rarity, level);
	}

	public Ability getClassAbility(Class<? extends Ability> clazz){
		for(Ability a : this.getClassAbilities()) {
			if(clazz.isInstance(a)) {
				return a;
			}
		}
		
		return null;
	}
	
	public Ability getProfessionAbility(Class<? extends Ability> clazz){
		for(Ability a : this.getProfAbilities()) {
			if(clazz.isInstance(a)) {
				return a;
			}
		}
		
		return null;
	}
	
	public List<AbstractFantasyClass> getFantasyClasses() {
		return classes;
	}

	public List<AbstractFantasyClass> getFantasyProfessions() {
		return professions;
	}

	public void openClassGui(boolean isProfession) {
		String title = "";
		if (isProfession)
			title = "Choose up to two Professions";
		else
			title = "Choose a Class";

		TreeGUI gui = new TreeGUI(bukkitPlayer, title);
		gui.openInventory(bukkitPlayer);
	}

	public void openMainMenu() {
		MainMenuGUI menu = new MainMenuGUI(bukkitPlayer);
		menu.openInventory(bukkitPlayer);
	}

	public void resetAllClassAbilities() {
		if (this.chosenClass == null)
			return;

		for (Skill s : getChosenClass().getSkillTree()) {
			Ability abil = s.getAbility();

			abil.deInit();
			abil.setLevel(0);
		}

		int start = 0;
		int end = level;
		classPoints = 0;
			
		for(int i = start; i < end; i++) {
				if(i % 2 != 0)
					classPoints++;
		}

	}

	public void resetAllProfAbilities() {
		for (AbstractFantasyClass clazz : getChosenProfessions()) {
			for (Skill s : clazz.getSkillTree()) {
				Ability abil = s.getAbility();

				abil.deInit();
				abil.setLevel(0);
			}
		}

		int start = 0;
		int end = level;
		profPoints = 0;
			
		for(int i = start; i < end; i++) {
				if(i % 2 == 0)
					profPoints++;
		}
	}

	public void removeAndResetClassAbility(Ability abil) {
		if (chosenClass != null)
			chosenClass.resetSkillTreeAbility(abil);
	}

	public void removeAndResetProfAbility(Ability abil) {
		for (AbstractFantasyClass clazz : this.chosenProfessions) {
			clazz.resetSkillTreeAbility(abil);
		}
	}

	public AbstractFantasyClass getChosenClass() {
		return this.chosenClass;
	}

	public List<AbstractFantasyClass> getChosenProfessions() {
		return this.chosenProfessions;
	}

	public List<Ability> getClassAbilities() {
		if (this.chosenClass == null)
			return new ArrayList<Ability>();

		List<Ability> abilList = new ArrayList<>();

		for (Skill s : chosenClass.getSkillTree())
			abilList.add(s.getAbility());

		return abilList;
	}

	public List<Ability> getProfAbilities() {
		List<Ability> abilList = new ArrayList<>();
		
		for (AbstractFantasyClass prof : this.chosenProfessions) {
			if (prof == null)
				continue;

			for (Skill s : prof.getSkillTree())
				abilList.add(s.getAbility());
		}
		
		return abilList;
	}

	public boolean hasChosenClass() {
		return chosenClass != null;
	}
	
	public boolean hasBothProfessions() {
		return this.chosenProfessions.size() >= 2;
	}
	
	public boolean setChosenClass(AbstractFantasyClass clazz) {
		if (chosenClass == null) {
			this.chosenClass = clazz;
			return true;
		} else if (chosenClass.equals(clazz))
			return true;

		return false;
	}

	public boolean addChosenProfession(AbstractFantasyClass profession) {
		if (chosenProfessions.contains(profession))
			return true;
		
		for(AbstractFantasyClass clazz : this.chosenProfessions) {
			if(clazz.getConfigName().equalsIgnoreCase(profession.getConfigName()))
				return false;
		}
		
		if (this.chosenProfessions.size() < 2) {
			this.chosenProfessions.add(profession);
			return true;
		}

		return false;
	}

	public void resetChosenClass() {
		this.resetAllClassAbilities();

		resetAttributes();
		
		this.chosenClass = null;
	}

	public void resetChosenProfessions() {
		this.resetAllProfAbilities();
		
		resetAttributes();

		this.chosenProfessions.clear();
	}
	
	public void resetAttributes() {
		removeAttributeModifiers(bukkitPlayer, Attribute.GENERIC_ATTACK_SPEED);
		removeAttributeModifiers(bukkitPlayer, Attribute.GENERIC_MOVEMENT_SPEED);
		removeAttributeModifiers(bukkitPlayer, Attribute.GENERIC_MAX_HEALTH);
		bukkitPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
	}

	/*private AbstractFantasyClass deserializeChosenClass(String name) {
		for (AbstractFantasyClass clazz : this.classes) {
			if (clazz.getConfigName().equalsIgnoreCase(name)) {
				return clazz;
			}
		}

		return null;
	}

	private AbstractFantasyClass deserializeChosenProfession(String name) {
		for (AbstractFantasyClass clazz : this.professions) {
			if (clazz.getConfigName().equalsIgnoreCase(name)) {
				return clazz;
			}
		}

		return null;
	}*/

	@SuppressWarnings("unchecked")
	private void loadFromConfig() {
		String uuid = this.bukkitPlayer.getUniqueId().toString();
		FileConfiguration config = plugin.getConfig();
		this.friendlyFire = config.getBoolean("players." + uuid + ".friendlyfire");
		this.damageMeters = config.getBoolean("players." + uuid + ".damagemeters");
		this.classPoints = config.getInt("players." + uuid + ".classPoints");
		this.profPoints = config.getInt("players." + uuid + ".profPoints");
		this.skillExp = config.getInt("players." + uuid + ".exp");

		this.level = calculateLevel();
		/*int start = 0;
		int end = level;
		
		if(level % 2 != 0) {
			start++;
			end++;
		}
			
		for(int i = start; i < end; i++) {
				if(i % 2 != 0)
					classPoints++;
				else
					profPoints++;
		}*/

		List<Map<?, ?>> clazzList =  config.getMapList("players." + uuid + ".class");
		if(clazzList == null)
			System.out.println("Class list is null");
		else if(clazzList.isEmpty())
			System.out.println("Class list is empty");
		
		for(Map<?, ?> c : clazzList) {
			this.chosenClass = AbstractFantasyClass.deSerialize((Map<String, Object>) c, this);
			if(chosenClass == null)
				System.out.println("Chosen Class is null");
		}
		
		List<Map<?, ?>> professions = config.getMapList("players." + uuid + ".professions");
		for(Map<?, ?> p : professions) {
			this.addChosenProfession(AbstractFantasyClass.deSerialize((Map<String, Object>) p, this));
		}
		
		//Enables abilities of class and professions
		if(this.chosenClass != null) {
			for(Skill s : chosenClass.getSkillTree()) {
				Ability a = s.getAbility();
				if(a.getCurrentLevel() > 0) {
					a.setFantasyPlayer(this);
					a.setLevel(a.getCurrentLevel());
					a.enable();
				}	
			}
				
		}
		
		for(AbstractFantasyClass clazz : this.chosenProfessions)
			for(Skill s : clazz.getSkillTree()) {
				if(s.getAbility().getCurrentLevel() > 0) {
					s.getAbility().setFantasyPlayer(this);
					s.getAbility().setLevel(s.getAbility().getCurrentLevel());
					s.getAbility().enable();
				}	
			}
		
	}

	public void deInit() {
		bukkitPlayer.closeInventory();
		bukkitPlayer.setInvulnerable(false);
		bukkitPlayer.setFlying(false);
		
		String uuid = bukkitPlayer.getUniqueId().toString();
		FileConfiguration config = plugin.getConfig();
		config.set("players." + uuid + ".friendlyfire", this.friendlyFire);
		config.set("players." + uuid + ".damagemeters", this.damageMeters);
		config.set("players." + uuid + ".classPoints", this.classPoints);
		config.set("players." + uuid + ".profPoints", this.profPoints);
		config.set("players." + uuid + ".exp", this.skillExp);
		
		List<AbstractFantasyClass> clazzAsList = new ArrayList<>();
		clazzAsList.add(chosenClass);
		
		List<Map<String, Object>> serializedClasses = new ArrayList<>();
		for (AbstractFantasyClass clazz : clazzAsList) {
			if (clazz == null)
				continue;

			serializedClasses.add(clazz.serialize());
		}

		config.set("players." + uuid + ".class", serializedClasses);
		
		List<Map<String, Object>> serializedProfessions = new ArrayList<>();
		for (AbstractFantasyClass clazz : this.chosenProfessions) {
			if (clazz == null)
				continue;

			serializedProfessions.add(clazz.serialize());
		}
		
		config.set("players." + uuid + ".professions", serializedProfessions);

		plugin.saveConfig();

		resetAttributes();
	}

	private static void removeAttributeModifiers(Player p, Attribute att) {
		Collection<AttributeModifier> mods = p.getAttribute(att).getModifiers();

		if (mods == null || mods.isEmpty())
			return;

		for (AttributeModifier mod : mods) {
			p.getAttribute(att).removeModifier(mod);
		}
	}
}
