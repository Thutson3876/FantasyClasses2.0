package me.thutson3876.fantasyclasses.classes.druid;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;

public class Druid extends AbstractFantasyClass {

	public Druid(FantasyPlayer player) {
		super(player, false);
		
		Player p = player.getPlayer();
		name = "Druid";

		this.setItemStack(Material.RABBIT_FOOT, name, "A class based on revering nature and its gifts");

		skillTree = new Skill(new Forager(p));
		
		Skill craft = new Skill(new Druidcraft(p));
		Skill bark = craft.addChild(new Rejuvination(p)).addChild(new Barkskin(p));
		bark.addChild(new NaturesBlessing(p));
		bark.addChild(new NaturesBalance(p)).addChild(new Tranquility(p));
		skillTree.addChild(craft);
		craft.addChild(new GreenThumb(p)).addChild(new Regrowth(p));
		
		Skill beast = new Skill(new BestFriend(p));
		Skill stamp = beast.addChild(new BeastMaster(p)).addChild(new Stampede(p));
		stamp.addChild(new BirdSinger(p));
		stamp.addChild(new FelinesGrace(p));
		stamp.addChild(new TightPack(p));
		skillTree.addChild(beast);
		
		Skill tree = new Skill(new TreeFeller(p));
		tree.addChild(new BountifulHarvest(p)).addChild(new SurvivalInstincts(p));
		skillTree.addChild(tree);
		
		setSkillInMap(4, skillTree);
		setSkillInMap(9 + 1, craft.getNext().get(1));
		setSkillInMap(9 + 0, craft.getNext().get(1).getNext().get(0));
		setSkillInMap(9 + 2, craft);
		setSkillInMap(18 + 2, craft.getNext().get(0));
		setSkillInMap(27 + 2, bark);
		setSkillInMap(36 + 2, bark.getNext().get(0));
		setSkillInMap(27 + 1, bark.getNext().get(1));
		setSkillInMap(27 + 0, bark.getNext().get(1).getNext().get(0));
		setSkillInMap(9 + 4, beast);
		setSkillInMap(18 + 4, beast.getNext().get(0));
		setSkillInMap(27 + 4, stamp);
		setSkillInMap(36 + 3, stamp.getNext().get(2));
		setSkillInMap(36 + 4, stamp.getNext().get(0));
		setSkillInMap(36 + 5, stamp.getNext().get(1));
		setSkillInMap(9 + 6, tree);
		setSkillInMap(9 + 7, tree.getNext().get(0));
		setSkillInMap(18 + 7, tree.getNext().get(0).getNext().get(0));
		
		this.setPrerequisites();
	}
}
