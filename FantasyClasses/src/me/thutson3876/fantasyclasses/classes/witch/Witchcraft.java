package me.thutson3876.fantasyclasses.classes.witch;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.classes.alchemy.DeadlyPoison;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.professions.alchemist.EnhancedRepitoire;
import me.thutson3876.fantasyclasses.professions.alchemist.Immunology;
import me.thutson3876.fantasyclasses.professions.alchemist.LiquidDeath;
import me.thutson3876.fantasyclasses.professions.alchemist.PotentBrewing;
import me.thutson3876.fantasyclasses.professions.alchemist.PotentSplash;
import me.thutson3876.fantasyclasses.professions.alchemist.ReagantHarvest;
import me.thutson3876.fantasyclasses.professions.alchemist.SunderingSplash;

public class Witchcraft extends AbstractFantasyClass {

	public Witchcraft(FantasyPlayer player) {
		super(player, false);
		
		Player p = player.getPlayer();

		name = "Witchcraft";

		this.setItemStack(Material.NETHER_WART, name, "A class for utilizing deep-rooted magical abilities");

		this.skillTree = new Skill(new ReagantHarvest(p));
		Skill hunt = new Skill(new WitchHunt(p));
		Skill broom = hunt.addChild(new MagicalTolerance(p)).addChild(new NoBroomNeeded(p));
		broom.addChild(new WitchesCauldron(p));
		
		Skill wand = new Skill(new WitchWand(p));
		wand.addChild(new VexWand(p));
		wand.addChild(new WitherWand(p));
		wand.addChild(new FireballWand(p)).addChild(new ConfusionWand(p));
		
		hunt.addChild(wand);
		
		skillTree.addChild(hunt);
		
		Skill enhanced = new Skill(new EnhancedRepitoire(p));
		enhanced.addChild(new PotentSplash(p)).addChild(new PotentBrewing(p)).addChild(new Immunology(p));
		skillTree.addChild(enhanced);
		
		Skill poison = new Skill(new DeadlyPoison(p));
		poison.addChild(new LiquidDeath(p)).addChild(new SunderingSplash(p));
		enhanced.addChild(poison);
		
		setSkillInMap(27 + 4, skillTree);
		setSkillInMap(27 + 3, enhanced);
		setSkillInMap(18 + 2, enhanced.getNext().get(0));
		setSkillInMap(9 + 2, enhanced.getNext().get(0).getNext().get(0));
		setSkillInMap(0 + 1, enhanced.getNext().get(0).getNext().get(0).getNext().get(0));
		setSkillInMap(36 + 2, poison);
		setSkillInMap(45 + 1, poison.getNext().get(0));
		setSkillInMap(36 + 0, poison.getNext().get(0).getNext().get(0));
		setSkillInMap(27 + 5, hunt);
		setSkillInMap(36 + 5, hunt.getNext().get(0));
		setSkillInMap(45 + 6, broom);
		setSkillInMap(45 + 7, broom.getNext().get(0));
		setSkillInMap(18 + 6, wand);
		setSkillInMap(9 + 7, wand.getNext().get(2));
		setSkillInMap(0 + 8, wand.getNext().get(2).getNext().get(0));
		setSkillInMap(9 + 6, wand.getNext().get(0));
		setSkillInMap(18 + 7, wand.getNext().get(1));
		
		this.setPrerequisites();
	}
}
