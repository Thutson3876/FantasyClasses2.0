package me.thutson3876.fantasyclasses.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.gui.AbstractGUI;
import me.thutson3876.fantasyclasses.gui.GuiItem;
import me.thutson3876.fantasyclasses.gui.treegui.ClassGUI;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;
import me.thutson3876.fantasyclasses.util.chat.ClassDifficulty;
import me.thutson3876.fantasyclasses.util.chat.ColorCode;

public abstract class AbstractFantasyClass implements FantasyClass {

	protected final boolean IS_PROFESSION;
	private ItemStack item;
	protected String name;
	protected String description;
	protected Skill skillTree;
	protected Player p;
	protected FantasyPlayer fantasyPlayer;
	protected Map<Integer, Skill> skillMap = new HashMap<>();
	
	protected final ClassDifficulty difficulty;

	public AbstractFantasyClass(FantasyPlayer fantasyPlayer, boolean isProfession) {
		this.p = fantasyPlayer.getPlayer();
		this.IS_PROFESSION = isProfession;
		this.difficulty = ClassDifficulty.MEDIUM;

		this.fantasyPlayer = fantasyPlayer;
		if (this.fantasyPlayer.calculateNextLevelExpCost() > 2400000) {
			System.out.println("Player not null");
		}
	}
	
	public AbstractFantasyClass(FantasyPlayer fantasyPlayer, boolean isProfession, ClassDifficulty difficulty) {
		this.p = fantasyPlayer.getPlayer();
		this.IS_PROFESSION = isProfession;
		this.difficulty = difficulty;

		this.fantasyPlayer = fantasyPlayer;
		if (this.fantasyPlayer.calculateNextLevelExpCost() > 2400000) {
			System.out.println("Player not null");
		}
	}

	@Override
	public Skill getSkillTree() {
		return skillTree;
	}

	@Override
	public boolean isProfession() {
		return this.IS_PROFESSION;
	}

	@Override
	public void addPlayerToAll(Player p) {
		for (Skill s : skillTree) {
			s.getAbility().setPlayer(p);
		}
	}

	public GuiItem asTreeGuiItem(AbstractGUI currentInv) {
		return new GuiItem(item, new ClassGUI(this.p, currentInv, this));
	}

	public String getName() {
		return name;
	}

	public Player getPlayer() {
		return p;
	}

	public FantasyPlayer getFantasyPlayer() {
		return fantasyPlayer;
	}

	protected ItemStack setItemStack(Material mat, String name, String... lore) {
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatUtils.chat("&6" + name));
		List<String> list = new ArrayList<>();
		
		if(lore.length >= 1) {
			for(String s : lore)
				list.add(s);
				
			meta.setLore(ChatUtils.splitStringAtLength(list, 33));
		}
		
		if(!this.IS_PROFESSION) {
			String s = "Difficulty:" + ColorCode.BOLD + " " + difficulty.getColor() + "" + difficulty.name();
			list.add(s);
		}
		item.setItemMeta(meta);
		this.item = item;

		return item;
	}

	public String getConfigName() {
		String configName = name.toLowerCase();
		configName.replaceAll("\\s", "");
		return configName;
	}

	public boolean replaceSkillTreeAbility(Ability abil) {
		if (abil == null)
			return false;
		for (Skill s : this.skillTree) {
			if (s.getAbility().getClass().equals(abil.getClass())) {
				s.replaceSkillAbility(abil);
				return true;
			}
		}

		return false;
	}

	public Ability getSkillTreeAbility(Ability abil) {
		if (abil == null)
			return null;

		for (Skill s : this.skillTree) {
			if (s.getAbility().getClass().equals(abil.getClass())) {
				return s.getAbility();
			}
		}

		return null;
	}

	private boolean canSpendSkillPoints(Skill skill) {
		int skillPoints = IS_PROFESSION ? fantasyPlayer.getClassPoints() : fantasyPlayer.getProfPoints();
		Ability ability = getSkillTreeAbility(skill.getAbility());

		if (ability == null)
			return false;

		if (IS_PROFESSION) {
			if (has2ChosenProfessions()) {
				p.getPlayer().playSound(p, Sound.ENTITY_VILLAGER_NO, 1.0f, 0.8f);
				p.sendMessage(ChatUtils.chat(
						ColorCode.ERROR.getCode() + "ERROR: You have already chosen your two professions. You may reset your choices via a drop from Fantasy Bosses."));
				return false;
			}
		} else {
			if (hasAChosenClass()) {
				p.getPlayer().playSound(p, Sound.ENTITY_VILLAGER_NO, 1.0f, 0.8f);
				p.sendMessage(ChatUtils.chat(
						ColorCode.ERROR.getCode() + "ERROR: You have already chosen your class. You may reset your choices via a drop from Fantasy Mobs."));
				return false;
			}
		}

		if (ability.getCurrentLevel() >= ability.getMaxLevel()) {
			p.getPlayer().playSound(p, Sound.ENTITY_VILLAGER_NO, 1.0f, 0.8f);
			p.sendMessage(ChatUtils.chat(ColorCode.ERROR.getCode() + "ERROR: Maximum Level has been reached"));
			return false;
		} else if (skillPoints < ability.getSkillPointCost()) {
			p.getPlayer().playSound(p, Sound.ENTITY_VILLAGER_NO, 1.0f, 0.8f);
			p.sendMessage(ChatUtils.chat(ColorCode.ERROR.getCode() + "ERROR: Not enough Skillpoints"));
			return false;
		} else if (skill.getPrev() != null) {
			Ability a = skill.getPrev().getAbility();
			if (a.getCurrentLevel() < ((double) a.getMaxLevel()) / 2.0) {
				p.playSound(p, Sound.ENTITY_VILLAGER_NO, 1.0f, 0.8f);
				p.getPlayer().sendMessage(ChatUtils
						.chat(ColorCode.ERROR.getCode() + "Error: You need at least half of the max level invested in the prerequisite skill: &6"
								+ a.getCommandName()));
				return false;
			}
		}

		return true;
	}

	public void zeroOutSubsequentSkills(Skill skill) {
		for (Skill s : skill) {
			if (s.getAbility().getCurrentLevel() > 0) {
				s.getAbility().setLevel(0);
			}
		}
	}

	// Remove if not needed
	public boolean setBindable(Ability abil, Material type) {
		if (abil instanceof Bindable) {
			((Bindable) abil).setBoundType(type);
			return true;
		}

		return false;
	}

	public boolean has2ChosenProfessions() {
		int counter = 0;

		for (AbstractFantasyClass clazz : this.fantasyPlayer.getFantasyProfessions()) {
			if (clazz.equals(this))
				continue;

			if (clazz.getSkillTree().getAbility().getCurrentLevel() > 0) {
				counter++;
			}
		}

		return counter > 1;
	}

	public boolean hasAChosenClass() {
		int counter = 0;

		for (AbstractFantasyClass clazz : this.fantasyPlayer.getFantasyClasses()) {
			if (clazz.equals(this))
				continue;

			if (clazz.getSkillTree().getAbility().getCurrentLevel() > 0) {
				counter++;
			}
		}

		return counter > 0;
	}

	public boolean spendSkillPoints(Skill skill) {
		Ability abil = skill.getAbility();
		if (canSpendSkillPoints(skill)) {
			if (IS_PROFESSION) {
				fantasyPlayer.spendProfPoints(abil.getSkillPointCost());
			} else {
				fantasyPlayer.spendClassPoints(abil.getSkillPointCost());
			}

			abil.levelUp();
			this.skillTree.replaceSkillAbility(abil);

			return true;
		}

		return false;
	}

	public boolean resetSkillTreeAbility(Ability abil) {
		if (abil == null)
			return false;
		for (Skill s : this.skillTree) {
			Ability ability = s.getAbility();
			if (ability.getClass().equals(abil.getClass())) {
				ability.resetLevel();
				return true;
			}
		}

		return false;
	}

	protected void setSkillInMap(Integer index, Skill skill) {
		skillMap.put(index, skill);
	}

	public Map<Integer, Skill> getSkillMap() {
		return skillMap;
	}

	protected void setPrerequisites() {
		for (Skill s : skillTree) {
			if (s.getPrev() != null)
				s.getAbility().setPrerequisite(s.getPrev().getAbility().getCommandName());
		}
	}

	/*
	 * public static List<Class<? extends AbstractAbility>> getAbilityClassList() {
	 * List<Class<? extends AbstractAbility>> abilityClasses = new ArrayList<>();
	 * abilityClasses.add(Momentum.class);
	 * 
	 * return abilityClasses; }
	 */

	public Map<String, Object> serialize() {
		Map<String, Object> map = new LinkedHashMap<>();
		List<Map<String, Object>> abilList = new ArrayList<>();
		for (Skill s : this.skillTree) {
			if(s.getAbility().getCurrentLevel() <= 0)
				continue;
			
			abilList.add(s.getAbility().serialize());
		}
		map.put("name", this.getConfigName().toLowerCase());
		map.put("abilities", abilList);

		return map;
	}

	@SuppressWarnings("unchecked")
	public static AbstractFantasyClass deSerialize(Map<String, Object> map, FantasyPlayer p) {
		String name = null;
		List<Map<String, Object>> abilList = new ArrayList<>();
		if (map.containsKey("name")) {
			name = (String) map.get("name");
		}
		if (map.containsKey("abilities")) {
			abilList = (List<Map<String, Object>>) map.get("abilities");
		}

		AbstractFantasyClass clazz = null;
		for (AbstractFantasyClass c : p.getFantasyClasses()) {
			if (c.getConfigName().equalsIgnoreCase(name)) {
				clazz = c;
				break;
			}
		}

		if (clazz == null) {
			for (AbstractFantasyClass c : p.getFantasyProfessions()) {
				if (c.getConfigName().equalsIgnoreCase(name)) {
					clazz = c;
					break;
				}
			}
		}

		if (clazz == null)
			return null;

		if (abilList == null)
			return clazz;

		List<Ability> classAbilities = new ArrayList<>();
		for (Object o : abilList) {
			Ability temp = Ability.deSerialize((Map<String, Object>) o, p, clazz.IS_PROFESSION);
			if (temp != null) {
				classAbilities.add(temp);
			}
			else
				System.out.println("Ability was null");
		}
		
		for(Ability a : classAbilities) {
			Ability skillTreeAbil = clazz.getSkillTreeAbility(a);
			if(skillTreeAbil == null) {
				System.out.println("Ability [" + a.getName() + "] was null");
				continue;
			}
				
			skillTreeAbil.setLevel(a.getCurrentLevel());
			if (a instanceof Bindable) {
				((Bindable) skillTreeAbil).setBoundType(((Bindable) a).getBoundType());
			}
			System.out.println("Ability [" + a.getName() + "] has been loaded!");
			//Spends skillpoints accordingly
			/*if(clazz.isProfession())
				p.addProfPoints(-skillTreeAbil.getCurrentLevel() * skillTreeAbil.getSkillPointCost());
			else
				p.addClassPoints(-skillTreeAbil.getCurrentLevel() * skillTreeAbil.getSkillPointCost());*/
		}
		
		System.out.println(p.getPlayer().getName() + "'s class was loaded successfully!");
		System.out.println("Class: " + clazz.getConfigName());
		return clazz;
	}
}
