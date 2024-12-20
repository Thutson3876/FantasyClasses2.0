package me.thutson3876.fantasyclasses.professions.hunter;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;

public class Hunter extends AbstractFantasyClass {

	public Hunter(FantasyPlayer player) {
		super(player, true);
		
		Player p = player.getPlayer();
		
		name = "Hunter";
		
		this.setItemStack(Material.BOW, name, "A profession for those who wish to wander and explore, living on the land");
		
		this.skillTree = new Skill(new Expedition(p));
		
		Skill stamina = new Skill(new EndlessStamina(p));
		
		skillTree.addChild(stamina);
		
		Skill dressing = new Skill(new FieldDressing(p));
		dressing.addChild(new FightDressing(p)).addChild(new HeadHunter(p));
		
		skillTree.addChild(dressing);
		
		Skill diet = new Skill(new PaleoDiet(p));
		diet.addChild(new IronStomach(p));
		
		skillTree.addChild(diet);
		
		setSkillInMap(4, skillTree); 

		setSkillInMap(9 + 1, dressing); 
		setSkillInMap(18 + 0, dressing.getNext().get(0)); 
		setSkillInMap(27 + 0, dressing.getNext().get(0).getNext().get(0)); 

		setSkillInMap(18 + 4, stamina); 
		
		setSkillInMap(9 + 7, diet); 
		setSkillInMap(18 + 8, diet.getNext().get(0)); 
		
		this.setPrerequisites();
		
	}

}
