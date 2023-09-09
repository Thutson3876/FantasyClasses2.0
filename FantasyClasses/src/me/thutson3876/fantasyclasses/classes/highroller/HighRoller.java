package me.thutson3876.fantasyclasses.classes.highroller;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;

public class HighRoller extends AbstractFantasyClass {

	public HighRoller(FantasyPlayer player) {
		super(player, false);
		
		Player p = player.getPlayer();
		
		name = "High Roller";

		this.setItemStack(Material.SKELETON_SKULL, name, "A brawling class that utilizes skill with a crossbow, a sword, and an unhealthy dose of luck to deal damage");

		skillTree = new Skill(new Roller_Proficiencies(p));
		
		Skill blindside = new Skill(new Blindside(p));
		
		blindside.addChild(new Acrobat(p));
		blindside.addChild(new PreciseStrikes(p));
		
		skillTree.addChild(blindside);
		
		Skill broadside = new Skill(new Broadside(p));
		
		broadside.addChild(new Feint(p)).addChild(new Vanish(p));
		broadside.addChild(new SneakAttack(p)).addChild(new GhostlyStrike(p));
		
		skillTree.addChild(broadside);
		
		Skill rollTheBones = new Skill(new RollTheBones(p));
		
		rollTheBones.addChild(new RollToPort(p));
		rollTheBones.addChild(new RollToStarboard(p));
		
		skillTree.addChild(rollTheBones);
		
		//
		setSkillInMap(4, skillTree);
		
		//Blindside branch //Total Cost: 15 (1 + 5 + 6 = 12)
		setSkillInMap(2, blindside);
		setSkillInMap(0, blindside.getNext().get(0));
		setSkillInMap(9 + 1, blindside.getNext().get(1));
		
		//Broadside branch //Total Cost: 15 (1 + 5 + 6 = 12)
		setSkillInMap(6, broadside);
		setSkillInMap(7, broadside.getNext().get(0));
		setSkillInMap(9 + 8, broadside.getNext().get(0).getNext().get(0));
		setSkillInMap(9 + 6, broadside.getNext().get(1));
		setSkillInMap(18 + 6, broadside.getNext().get(1).getNext().get(0));
		
		//Roll the Bones branch //Total Cost: 15 (1 + 5 + 6 = 12)
		setSkillInMap(9 + 4, rollTheBones);
		setSkillInMap(18 + 3, rollTheBones.getNext().get(0));
		setSkillInMap(18 + 5, rollTheBones.getNext().get(1));
		
		this.setPrerequisites();
	}

}
