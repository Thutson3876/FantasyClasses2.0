package me.thutson3876.fantasyclasses.professions.enchanter;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;

public class Enchanter extends AbstractFantasyClass {

	public Enchanter(FantasyPlayer player) {
		super(player, true);

		Player p = player.getPlayer();
		
		name = "Enchanter";
		
		this.setItemStack(Material.ENCHANTED_BOOK, name, "A profession for exploring the secrets of the arcane");
		
		this.skillTree = new Skill(new Enchanting101(p));
		
		Skill e102 = skillTree.addChild(new Enchanting102(p));
		e102.addChild(new AnvilAdept(p));
		e102.addChild(new CurseMastery(p));
		
		Skill e103 = e102.addChild(new Enchanting103(p));
		e103.addChild(new AncientKnowledge(p));
		e103.addChild(new ExperiencedEnchanter(p));
		e103.addChild(new Enchanting201(p));
		
		
		this.setSkillInMap(4, skillTree); //Cost: 1 Max: 2
		//102 branch //Total Cost: 5 (3)
		this.setSkillInMap(18 + 4, e102); //Cost: 1 Max: 2
		this.setSkillInMap(18 + 2, e102.getNext().get(0)); //Cost: 1 Max: 3
		this.setSkillInMap(18 + 6, e102.getNext().get(1)); //Cost: 1 Max: 1
		//103 branch //Total Cost: 7 (5)
		this.setSkillInMap(36 + 4, e103); //Cost: 1 Max: 2
		this.setSkillInMap(36 + 2, e103.getNext().get(0)); //Cost: 2 Max: 1
		this.setSkillInMap(36 + 6, e103.getNext().get(1)); //Cost: 1 Max: 3
		this.setSkillInMap(45 + 4, e103.getNext().get(2)); //Cost: 2 Max: 1
		
		this.setPrerequisites();
	}

}
