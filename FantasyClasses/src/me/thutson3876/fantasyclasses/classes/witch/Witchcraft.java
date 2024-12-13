package me.thutson3876.fantasyclasses.classes.witch;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;

public class Witchcraft extends AbstractFantasyClass {

	public Witchcraft(FantasyPlayer player) {
		super(player, false);
		
		Player p = player.getPlayer();

		name = "Witch";

		this.setItemStack(Material.NETHER_WART, name, "A class for utilizing deep-rooted magical abilities");

		skillTree = new Skill(new WitchHunt(p));
		
		Skill wand = new Skill(new WitchWand(p));
		wand.addChild(new VexWand(p));
		wand.addChild(new WitherWand(p));
		wand.addChild(new FireballWand(p));
		wand.addChild(new ConfusionWand(p));
		
		skillTree.addChild(wand);
		Skill cauldron = new Skill(new WitchesCauldron(p));

		cauldron.addChild(new TasteTest(p));
		cauldron.addChild(new MagicalTolerance(p)).addChild(new KittyAid(p));
		
		skillTree.addChild(cauldron);
		
		skillTree.addChild(new NineLives(p)).addChild(new NoBroomNeeded(p));
		
		setSkillInMap(27 + 4, skillTree);
		
		setSkillInMap(27 + 2, wand);
		setSkillInMap(0 + 0, wand.getNext().get(0));
		setSkillInMap(9 + 0, wand.getNext().get(1));
		setSkillInMap(18 + 0, wand.getNext().get(2));
		setSkillInMap(27 + 0, wand.getNext().get(3));
		
		setSkillInMap(27 + 6, cauldron);
		setSkillInMap(18 + 7, cauldron.getNext().get(0));
		setSkillInMap(36 + 7, cauldron.getNext().get(1));
		setSkillInMap(36 + 8, cauldron.getNext().get(1).getNext().get(0));
		
		setSkillInMap(36 + 4, skillTree.getNext().get(2));
		setSkillInMap(45 + 4, skillTree.getNext().get(2).getNext().get(0));
		
		this.setPrerequisites();
	}
}
