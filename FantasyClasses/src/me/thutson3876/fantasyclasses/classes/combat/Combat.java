package me.thutson3876.fantasyclasses.classes.combat;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.classes.berserker.DeepDive;
import me.thutson3876.fantasyclasses.classes.ranger.PiercingShots;
import me.thutson3876.fantasyclasses.classes.ranger.Disengage;
import me.thutson3876.fantasyclasses.classes.ranger.ReturningArrows;
import me.thutson3876.fantasyclasses.classes.ranger.Sharpshooter;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;

public class Combat extends AbstractFantasyClass {
	
	public Combat(FantasyPlayer player) {
		super(player, false);
		
		Player p = player.getPlayer();
		
		name = "Combat Proficiency";
		
		this.setItemStack(Material.IRON_SWORD, name, "A class focused on improving your weaponry");
		
		skillTree = new Skill(new Momentum(p));
		
		Skill sword = new Skill(new Swordsman(p));
		Skill dual = sword.addChild(new HonedEdge(p)).addChild(new DualWielding(p));
		dual.addChild(new Parry(p));
		skillTree.addChild(sword);
		
		Skill axe = new Skill(new AxeWielder(p));
		axe.addChild(new Rage(p)).addChild(new DeepDive(p));
		skillTree.addChild(axe);
		
		Skill bow = new Skill(new PiercingShots(p));
		Skill dis = bow.addChild(new Disengage(p));
		dis.addChild(new Sharpshooter(p));
		dis.addChild(new ReturningArrows(p));
		skillTree.addChild(bow);
		
		Skill scythe = new Skill(new ScytheSmith(p));
		Skill raise = scythe.addChild(new Reaper(p)).addChild(new Cripple(p)).addChild(new LifeRip(p)).addChild(new RaiseDead(p));
		raise.addChild(new UndyingServitude(p));
		skillTree.addChild(scythe);
		
		setSkillInMap(4, skillTree);
		setSkillInMap(9 + 1, sword);
		setSkillInMap(18 + 0, sword.getNext().get(0));
		setSkillInMap(27 + 0, dual);
		setSkillInMap(36 + 0, dual.getNext().get(0));
		setSkillInMap(9 + 3, axe);
		setSkillInMap(18 + 3, axe.getNext().get(0));
		setSkillInMap(27 + 3, axe.getNext().get(0).getNext().get(0));
		setSkillInMap(9 + 5, bow);
		setSkillInMap(18 + 5, dis);
		setSkillInMap(18 + 6, dis.getNext().get(0));
		setSkillInMap(27 + 5, dis.getNext().get(1));
		setSkillInMap(36 + 5, dis.getNext().get(1).getNext().get(0));
		setSkillInMap(9 + 7, scythe);
		setSkillInMap(9 + 8, scythe.getNext().get(0));
		setSkillInMap(18 + 8, scythe.getNext().get(0).getNext().get(0));
		setSkillInMap(27 + 8, raise.getPrev());
		setSkillInMap(36 + 8, raise);
		setSkillInMap(45 + 8, raise.getNext().get(0));
		
		this.setPrerequisites();
	}
}
