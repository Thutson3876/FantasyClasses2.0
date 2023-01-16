package me.thutson3876.fantasyclasses.professions.fisherman;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;

public class Fisherman extends AbstractFantasyClass {

	public Fisherman(FantasyPlayer player) {
		super(player, true);

		Player p = player.getPlayer();

		name = "Fisherman";

		this.setItemStack(Material.FISHING_ROD, name, "A profession to relax and catch some fish");

		this.skillTree = new Skill(new FishyBusiness(p));
		
		Skill ancestry = new Skill(new ForsakenAncestry(p));
		ancestry.addChild(new Hydrodynamics(p));
		ancestry.addChild(new SharedBlessings(p));
		
		skillTree.addChild(ancestry);
		
		Skill palate = new Skill(new RefinedPalate(p));
		palate.addChild(new Turboat(p));
		palate.addChild(new LostToTheSea(p)).addChild(new RainDance(p));
		
		skillTree.addChild(palate);
		
		Skill hook = new Skill(new HookLineAnd(p));
		hook.addChild(new PufferfishTipped(p));
		
		skillTree.addChild(hook);
		
		setSkillInMap(4, skillTree); //Cost: 1 Max: 1
		//Ancestry branch //Total Cost: 5 (3) 
		setSkillInMap(9 + 1, ancestry); //Cost: 1 Max: 2
		setSkillInMap(18 + 0, ancestry.getNext().get(0)); //Cost: 1 Max: 2
		setSkillInMap(18 + 2, ancestry.getNext().get(1)); //Cost: 1 Max: 1
		//Palate branch //Total Cost: 8 (6) 
		setSkillInMap(18 + 4, palate); //Cost: 1 Max: 2
		setSkillInMap(18 + 5, palate.getNext().get(0)); //Cost: 1 Max: 2
		setSkillInMap(27 + 4, palate.getNext().get(1)); //Cost: 2 Max: 1
		setSkillInMap(36 + 4, palate.getNext().get(1).getNext().get(0)); //Cost: 2 Max: 1
		//Hook branch //Total Cost: 3 (2) 
		setSkillInMap(9 + 7, hook); //Cost: 1 Max: 1
		setSkillInMap(18 + 7, hook.getNext().get(0)); //Cost: 1 Max: 2
		
		this.setPrerequisites();
	}

}
