package me.thutson3876.fantasyclasses.classes;

import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.skills.Skill;

public interface FantasyClass {

	public Skill getSkillTree();
	
	public boolean isProfession();
	
	public void addPlayerToAll(Player p);
	
}
