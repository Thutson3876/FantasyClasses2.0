package me.thutson3876.fantasyclasses.classes.highroller.randomabilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.classes.highroller.randomabilities.classriches.AbstractClassRiches;
import me.thutson3876.fantasyclasses.classes.highroller.randomabilities.classriches.CombatRiches;
import me.thutson3876.fantasyclasses.classes.highroller.randomabilities.classriches.DruidRiches;
import me.thutson3876.fantasyclasses.classes.highroller.randomabilities.classriches.DungeoneerRiches;
import me.thutson3876.fantasyclasses.classes.highroller.randomabilities.classriches.MonkRiches;
import me.thutson3876.fantasyclasses.classes.highroller.randomabilities.classriches.SeaGuardianRiches;
import me.thutson3876.fantasyclasses.classes.highroller.randomabilities.classriches.WitchRiches;

public class ClassBasedRiches implements RandomAbility {

	private static List<AbstractClassRiches> richesList = new ArrayList<>(); 
	
	static {
		richesList.add(new CombatRiches());
		richesList.add(new DruidRiches());
		richesList.add(new DungeoneerRiches());
		richesList.add(new MonkRiches());
		richesList.add(new SeaGuardianRiches());
		richesList.add(new WitchRiches());
	}
	
	@Override
	public void run(Player p) {
		Random rng = new Random();
		AbstractClassRiches riches = richesList.get(rng.nextInt(richesList.size()));
		
		riches.generateEvent(p);
	}
	
	
}
