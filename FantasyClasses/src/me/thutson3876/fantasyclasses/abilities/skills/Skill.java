package me.thutson3876.fantasyclasses.abilities.skills;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.gui.AbstractGUI;
import me.thutson3876.fantasyclasses.gui.GuiItem;

public class Skill implements Iterable<Skill> {

	Ability ability;
	String name;
	List<Skill> next;
	Skill prev;
	
	private List<Skill> elementsIndex;
	
	public Skill(Ability ability, Skill prev){
		name = ability.getName();
		this.prev = prev;
		if(prev != null) {
			prev.addToNext(this);
		}
	}
	
	public Skill(Ability abil) {
		this.ability = abil;
		this.name = abil.getName();
		this.next = new LinkedList<Skill>();
		this.elementsIndex = new LinkedList<Skill>();
		this.elementsIndex.add(this);
	}
	
	public Skill addChild(Ability abil) {
		Skill childNode = new Skill(abil);
		childNode.prev = this;
		this.next.add(childNode);
		this.registerChildForSearch(childNode);
		return childNode;
	}
	
	public Skill addChild(Skill skill) {
		skill.prev = this;
		this.next.add(skill);
		this.registerChildForSearch(skill);
		return skill;
	}

	public int getLevel() {
		if (this.isRoot())
			return 0;
		else
			return prev.getLevel() + 1;
	}

	private void registerChildForSearch(Skill node) {
		elementsIndex.add(node);
		if (prev != null)
			prev.registerChildForSearch(node);
	}

	public Skill findSkill(Comparable<Ability> cmp) {
		for (Skill element : this.elementsIndex) {
			Ability elData = element.ability;
			if (cmp.compareTo(elData) == 0)
				return element;
		}

		return null;
	}
	
	private void addToNext(Skill s) {
		this.next.add(s);
		this.registerChildForSearch(s);
	}
	
	public List<Skill> getNext() {
		return next;
	}
	
	public Skill getPrev() {
		return prev;
	}
	
	public Ability getAbility() {
		return ability;
	}

	@Override
	public Iterator<Skill> iterator() {
		SkillIterator iterator = new SkillIterator(this);
		return iterator;
	}
	
	public boolean isRoot() {
		return prev == null;
	}

	public boolean isLeaf() {
		return next.size() == 0;
	}
	
	public boolean replaceSkillAbility(Ability abil) {
		if(abil == null) {
			return false;
		}
		if(this.name.equalsIgnoreCase(abil.getName())) {
			this.ability = abil;
			return true;
		}
		
		for(Skill s : this.elementsIndex) {
			if(s.name.equalsIgnoreCase(abil.getName())) {
				s.ability = abil;
				return true;
			}
		}
		return false;
	}
	
	public boolean isPrerequisiteMet() {
		if(this.isRoot())
			return true;
		
		Ability prevAbility = this.getPrev().getAbility();
		
		return ((double)prevAbility.getCurrentLevel() / (double)prevAbility.getMaxLevel()) >= 0.5;
	}
	
	//Recursive method that turns every skill node into a GuiItem that links an inventory of GuiItems of its children
	public GuiItem asGuiItem(AbstractGUI currentInv) {
		GuiItem guiItem = new GuiItem(this.getAbility().getItemStack(), currentInv, null);

		return guiItem;
	}
	
	public Ability getMatchingAbility(ItemStack item) {
		for(Skill s : this) {
			if(item.isSimilar(s.getAbility().getItemStack())) {
				return s.getAbility();
			}
		}
		
		return null;
	}
	
}
