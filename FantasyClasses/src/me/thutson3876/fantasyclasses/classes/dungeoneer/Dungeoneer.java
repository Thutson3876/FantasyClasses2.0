package me.thutson3876.fantasyclasses.classes.dungeoneer;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.professions.miner.ArtifactCollector;
import me.thutson3876.fantasyclasses.professions.miner.BuiltToLast;
import me.thutson3876.fantasyclasses.professions.miner.DiggyDiggyHole;
import me.thutson3876.fantasyclasses.professions.miner.DungeonDelver;
import me.thutson3876.fantasyclasses.professions.miner.Excavation;
import me.thutson3876.fantasyclasses.professions.miner.HeatedTip;
import me.thutson3876.fantasyclasses.professions.miner.HotHeaded;
import me.thutson3876.fantasyclasses.professions.miner.VeinFinder;

public class Dungeoneer extends AbstractFantasyClass {

	public Dungeoneer(FantasyPlayer player) {
		super(player, false);
		
		Player p = player.getPlayer();
		name = "Dungeoneer";

		this.setItemStack(Material.IRON_PICKAXE, name, "A class based on diving into the mysterious depths of the world");

		skillTree = new Skill(new BuiltToLast(p));
		
		Skill mine = new Skill(new DiggyDiggyHole(p));
		Skill delv = mine.addChild(new Excavation(p)).addChild(new StoneSkin(p)).addChild(new DungeonDelver(p));
		Skill hot = delv.addChild(new HotHeaded(p)).addChild(new HeatedTip(p)).addChild(new HotHands(p));
		hot.addChild(new FieryExplosion(p));
		skillTree.addChild(mine);
		delv.addChild(new VeinFinder(p));
		delv.addChild(new ArtifactCollector(p));
		
		Skill shadow = new Skill(new Shadowstep(p));
		shadow.addChild(new Backstab(p)).addChild(new Shadowmeld(p));
		shadow.getNext().get(0).addChild(new Nimble(p));
		delv.addChild(shadow);
		
		setSkillInMap(3, skillTree);
		setSkillInMap(4, mine);
		setSkillInMap(5, mine.getNext().get(0));
		setSkillInMap(9 + 5, delv.getPrev());
		setSkillInMap(18 + 6, delv);
		setSkillInMap(9 + 6, delv.getNext().get(1));
		setSkillInMap(18 + 7, delv.getNext().get(2));
		setSkillInMap(27 + 7, hot.getPrev().getPrev());
		setSkillInMap(27 + 8, hot.getPrev());
		setSkillInMap(36 + 8, hot);
		setSkillInMap(45 + 8, hot.getNext().get(0));
		setSkillInMap(27 + 5, shadow);
		setSkillInMap(36 + 4, shadow.getNext().get(0));
		setSkillInMap(45 + 3, shadow.getNext().get(0).getNext().get(0));
		setSkillInMap(27 + 3, shadow.getNext().get(0).getNext().get(1));
		
		this.setPrerequisites();
	}

}
