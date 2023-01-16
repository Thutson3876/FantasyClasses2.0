package me.thutson3876.fantasyclasses.professions.miner;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;

public class Miner extends AbstractFantasyClass {

	public Miner(FantasyPlayer player) {
		super(player, true);
		
		Player p = player.getPlayer();
		
		name = "Miner";
		
		this.setItemStack(Material.IRON_PICKAXE, name, "A profession for delving into the deepest darkest places of the world");
		
		this.skillTree = new Skill(new BuiltToLast(p));
		
		Skill darkvision = new Skill(new Darkvision(p));
		darkvision.addChild(new SwiftShadow(p));
		
		skillTree.addChild(darkvision);
		
		Skill hot = new Skill(new HotHeaded(p));
		hot.addChild(new HeatedTip(p));
		
		skillTree.addChild(hot);
		
		Skill diggy = new Skill(new DiggyDiggyHole(p));
		diggy.addChild(new Excavation(p));
		
		Skill veinFinder = new Skill(new VeinFinder(p));
		veinFinder.addChild(new ArtifactCollector(p));
		
		diggy.getNext().get(0).addChild(veinFinder);
		
		setSkillInMap(9 + 6, skillTree); //Cost: 1 Max: 1
		//Darkvision branch //Total Cost: 3 (2)
		setSkillInMap(5, darkvision); //Cost: 1 Max: 2
		setSkillInMap(4, darkvision.getNext().get(0)); //Cost: 1 Max: 1
		//Hot branch //Total Cost: 4 (3)
		setSkillInMap(18 + 7, hot); //Cost: 1 Max: 2
		setSkillInMap(27 + 7, hot.getNext().get(0)); //Cost: 2 Max: 1
		//Diggy branch //Total Cost: 9 (7)
		setSkillInMap(18 + 5, diggy); //Cost: 1 Max: 2
		setSkillInMap(27 + 4, diggy.getNext().get(0)); //Cost: 1 Max: 3
		setSkillInMap(36 + 3, veinFinder); //Cost: 2 Max: 1
		setSkillInMap(45 + 2, veinFinder.getNext().get(0)); //Cost: 2 Max: 1
		
		this.setPrerequisites();
		
	}

}
