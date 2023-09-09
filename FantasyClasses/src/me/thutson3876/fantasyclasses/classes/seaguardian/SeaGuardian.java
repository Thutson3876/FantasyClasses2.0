package me.thutson3876.fantasyclasses.classes.seaguardian;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;

public class SeaGuardian extends AbstractFantasyClass {

	public SeaGuardian(FantasyPlayer player) {
		super(player, false);
		
		Player p = player.getPlayer();
		name = "Sea Guardian";

		this.setItemStack(Material.KELP, name, "A class hailing from the dark depths of the sea");
		
		this.skillTree = new Skill(new SeaGuardian_Proficiencies(p));
		
		Skill protecc = new Skill(new ProtectorsDuty(p));
		protecc.addChild(new PolearmMastery(p)).addChild(new StormBorn(p)).addChild(new RiptideMastery(p));
		
		Skill ice = new Skill(new GlacialSmite(p));
		Skill snap = ice.addChild(new SnapFreeze(p));
		snap.addChild(new FrozenPrison(p));
		snap.addChild(new RemorselessWinter(p));
		protecc.addChild(ice);
		skillTree.addChild(protecc);
		
		Skill cleanse = new Skill(new Cleanse(p));
		cleanse.addChild(new SoothingWaters(p));
		cleanse.addChild(new SummonPretzel(p));
		protecc.addChild(cleanse);
		
		setSkillInMap(4, skillTree);
		
		setSkillInMap(9 + 4, protecc); // 1
		
		//storm //Total Cost: 9 (6)
		setSkillInMap(18 + 4, protecc.getNext().get(0)); //Cost: 1 Max: 3
		setSkillInMap(27 + 4, protecc.getNext().get(0).getNext().get(0)); //Cost: 1 Max: 2
		setSkillInMap(36 + 4, protecc.getNext().get(0).getNext().get(0).getNext().get(0)); //Cost: 3 Max: 1
		
		//cleanse //Total Cost: 8 (5) 
		setSkillInMap(9 + 5, cleanse); //Cost: 1 Max: 2
		setSkillInMap(0 + 6, cleanse.getNext().get(0)); //Cost: 1 Max: 3
		setSkillInMap(18 + 6, cleanse.getNext().get(1)); //Cost: 1 Max: 3
		
		//freeze //Total Cost: 10 (7) 
		setSkillInMap(9 + 3, ice); //Cost: 1 Max: 3
		setSkillInMap(9 + 2, ice.getNext().get(0)); //Cost: 2 Max: 1
		setSkillInMap(1, ice.getNext().get(0).getNext().get(0)); //Cost: 1 Max: 2
		setSkillInMap(18 + 1, ice.getNext().get(0).getNext().get(1)); //Cost: 1 Max: 3
		
		
		this.setPrerequisites();
	}

}
