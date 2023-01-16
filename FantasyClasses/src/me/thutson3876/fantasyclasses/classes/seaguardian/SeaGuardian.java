package me.thutson3876.fantasyclasses.classes.seaguardian;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.professions.fisherman.FishyBusiness;
import me.thutson3876.fantasyclasses.professions.fisherman.ForsakenAncestry;
import me.thutson3876.fantasyclasses.professions.fisherman.FriendlyFaces;
import me.thutson3876.fantasyclasses.professions.fisherman.LostToTheSea;
import me.thutson3876.fantasyclasses.professions.fisherman.PufferfishTipped;
import me.thutson3876.fantasyclasses.professions.fisherman.RainDance;
import me.thutson3876.fantasyclasses.professions.fisherman.RefinedPalate;
import me.thutson3876.fantasyclasses.professions.fisherman.SharedBlessings;

public class SeaGuardian extends AbstractFantasyClass {

	public SeaGuardian(FantasyPlayer player) {
		super(player, false);
		
		Player p = player.getPlayer();
		name = "Sea Guardian";

		this.setItemStack(Material.KELP, name, "A class hailing from the dark depths of the sea");
		
		this.skillTree = new Skill(new ForsakenAncestry(p));
		
		Skill swim = new Skill(new SharedBlessings(p));
		swim.addChild(new FriendlyFaces(p));
		swim.addChild(new SummonPretzel(p));
		skillTree.addChild(swim);
		
		Skill protecc = new Skill(new ProtectorsDuty(p));
		protecc.addChild(new SoothingWaters(p));
		protecc.addChild(new RiptideMastery(p));
		protecc.addChild(new PolearmMastery(p));
		protecc.addChild(new Conductive(p));
		Skill ice = new Skill(new GlacialSmite(p));
		ice.addChild(new FrozenPrison(p)).addChild(new LostToTheSea(p));
		ice.addChild(new RemorselessWinter(p));
		protecc.addChild(ice);
		skillTree.addChild(protecc);
		
		Skill fish = new Skill(new RefinedPalate(p));
		fish.addChild(new Cleanse(p));
		fish.addChild(new FishyBusiness(p));
		fish.addChild(new PufferfishTipped(p));
		fish.addChild(new RainDance(p));
		skillTree.addChild(fish);
		
		setSkillInMap(4, skillTree);
		setSkillInMap(27 + 8, swim);
		setSkillInMap(18 + 7, swim.getNext().get(0));
		setSkillInMap(36 + 7, swim.getNext().get(1));
		setSkillInMap(9 + 4, fish);
		setSkillInMap(9 + 3, fish.getNext().get(0));
		setSkillInMap(18 + 2, fish.getNext().get(1));
		setSkillInMap(9 + 5, fish.getNext().get(2));
		setSkillInMap(9 + 6, fish.getNext().get(3));
		setSkillInMap(45 + 4, protecc);
		setSkillInMap(45 + 3, protecc.getNext().get(0));
		setSkillInMap(36 + 2, protecc.getNext().get(1));
		setSkillInMap(45 + 5, protecc.getNext().get(2));
		setSkillInMap(45 + 6, protecc.getNext().get(3));
		setSkillInMap(27 + 1, ice);
		setSkillInMap(18 + 0, ice.getNext().get(0));
		setSkillInMap(9 + 0, ice.getNext().get(0).getNext().get(0));
		setSkillInMap(36 + 0, ice.getNext().get(1));
		
		this.setPrerequisites();
	}

}
