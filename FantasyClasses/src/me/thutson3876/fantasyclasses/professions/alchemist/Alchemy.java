package me.thutson3876.fantasyclasses.professions.alchemist;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.classes.alchemy.DeadlyPoison;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;

public class Alchemy extends AbstractFantasyClass {

	public Alchemy(FantasyPlayer player) {
		super(player, false);
		
		Player p = player.getPlayer();
		
		name = "Alchemist";
		
		this.setItemStack(Material.POTION, name, "A profession for making powerful potions");
		
		this.skillTree = new Skill(new ReagantHarvest(p));
		
		Skill enhanced = new Skill(new EnhancedRepitoire(p));
		enhanced.addChild(new PotentSplash(p));
		enhanced.addChild(new PotentBrewing(p));
		skillTree.addChild(enhanced);
		
		Skill poison = new Skill(new DeadlyPoison(p));
		poison.addChild(new SunderingSplash(p));
		poison.addChild(new LiquidDeath(p));
		skillTree.addChild(poison);
		
		skillTree.addChild(new Immunology(p));
		
		setSkillInMap(4, skillTree);
		setSkillInMap(9 + 3, enhanced);
		setSkillInMap(18 + 1, enhanced.getNext().get(0));
		setSkillInMap(18 + 3, enhanced.getNext().get(1));
		setSkillInMap(9 + 5, poison);
		setSkillInMap(18 + 5, poison.getNext().get(0));
		setSkillInMap(18 + 7, poison.getNext().get(1));
		setSkillInMap(9 + 4, skillTree.getNext().get(2));
	}
	
}
