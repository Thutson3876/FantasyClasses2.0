package me.thutson3876.fantasyclasses.professions.alchemist;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;

public class Alchemy extends AbstractFantasyClass {

	public Alchemy(FantasyPlayer player) {
		super(player, true);
		
		Player p = player.getPlayer();
		
		name = "Alchemist";
		
		this.setItemStack(Material.DRAGON_BREATH, name, "A profession for making powerful potions and transmuting materials");
		
		this.skillTree = new Skill(new EnhancedRepitoire(p));
		
		Skill enhanced = new Skill(new ReagantHarvest(p));
		enhanced.addChild(new PotentSplash(p));
		enhanced.addChild(new PotentBrewing(p));
		skillTree.addChild(enhanced);
		
		/*Skill poison = new Skill(new DeadlyPoison(p));
		poison.addChild(new SunderingSplash(p));
		poison.addChild(new LiquidDeath(p));
		skillTree.addChild(poison);*/
		
		Skill transmute = new Skill(new TransmuteStone(p));
		transmute.addChild(new TransmuteFlesh(p));
		transmute.addChild(new TransmuteMetal(p));
		
		skillTree.addChild(new Immunology(p)).addChild(new DragonInfusion(p));
		
		skillTree.addChild(transmute);
		
		setSkillInMap(4, skillTree);
		setSkillInMap(9 + 3, enhanced);
		setSkillInMap(18 + 1, enhanced.getNext().get(0));
		setSkillInMap(18 + 3, enhanced.getNext().get(1));
		
		setSkillInMap(9 + 4, skillTree.getNext().get(2));
		setSkillInMap(36 + 4, skillTree.getNext().get(2).getNext().get(0));
		setSkillInMap(9 + 5, transmute);
		setSkillInMap(18 + 5, transmute.getNext().get(0));
		setSkillInMap(18 + 7, transmute.getNext().get(1));
		
		this.setPrerequisites();
	}
	
}
