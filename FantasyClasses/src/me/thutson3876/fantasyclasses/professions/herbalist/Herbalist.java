package me.thutson3876.fantasyclasses.professions.herbalist;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.professions.fisherman.FishyBusiness;

public class Herbalist extends AbstractFantasyClass {
	public Herbalist(FantasyPlayer player) {
		super(player, true);

		Player p = player.getPlayer();

		name = "Herbalist";

		this.setItemStack(Material.LILY_OF_THE_VALLEY, name, "A profession for gardners and farmers alike");

		this.skillTree = new Skill(new FishyBusiness(p));
		
		// Utilizes flowers to do stuff + farming and talents from druid
		
		this.setPrerequisites();
	}
}
