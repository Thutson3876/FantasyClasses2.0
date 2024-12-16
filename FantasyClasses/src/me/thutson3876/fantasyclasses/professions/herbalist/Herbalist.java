package me.thutson3876.fantasyclasses.professions.herbalist;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;

public class Herbalist extends AbstractFantasyClass {
	public Herbalist(FantasyPlayer player) {
		super(player, true);

		Player p = player.getPlayer();

		name = "Herbalist";

		this.setItemStack(Material.LILY_OF_THE_VALLEY, name, "A profession for gardners and farmers alike");

		this.skillTree = new Skill(new Forager(p));
		
		Skill treeFeller = new Skill(new TreeFeller(p));
		treeFeller.addChild(new Cultivation(p));
		
		Skill regrowth = new Skill(new Regrowth(p));
		regrowth.addChild(new GreenThumb(p));
		
		Skill druidCraft = new Skill(new Druidcraft(p));
		druidCraft.addChild(new WildGrowth(p));
		druidCraft.addChild(new BountifulHarvest(p));
		
		skillTree.addChild(treeFeller);
		skillTree.addChild(regrowth);
		skillTree.addChild(druidCraft);
		
		setSkillInMap(0 + 4, skillTree);
		
		setSkillInMap(9 + 6, treeFeller);
		setSkillInMap(18 + 7, treeFeller.getNext().get(0));
		
		setSkillInMap(9 + 2, regrowth);
		setSkillInMap(18 + 1, regrowth.getNext().get(0));
		
		setSkillInMap(9 + 4, druidCraft);
		setSkillInMap(18 + 5, druidCraft.getNext().get(0));
		setSkillInMap(18 + 3, druidCraft.getNext().get(1));
		
		// Utilizes flowers to do stuff + farming and talents from druid
		
		this.setPrerequisites();
	}
}
