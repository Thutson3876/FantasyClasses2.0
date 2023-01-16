package me.thutson3876.fantasyclasses.gui.treegui;

import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.gui.GuiItem;

public class SkillGUI extends GuiItem {

	private Skill skill;
	private int tempLevel = 0;
	
	public SkillGUI(Skill skill, ItemStack item) {
		super(item, null);
		
		this.skill = skill;
		tempLevel = skill.getAbility().getCurrentLevel();
		
	}
	
	public int getTempLevel() {
		return tempLevel;
	}
	
	public boolean addTempLevel(int i) {
		if(tempLevel + i > this.skill.getAbility().getMaxLevel()) {
			return false;
		}
		else if(tempLevel + i < 0)
			return false;
		
		
		tempLevel += i;
		return true;
	}

	public Skill getSkill() {
		return this.skill;
	}
	
	public Ability getAbility() {
		return this.skill.getAbility();
	}
	
}
