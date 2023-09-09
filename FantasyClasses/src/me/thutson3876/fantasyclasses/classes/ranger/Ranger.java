package me.thutson3876.fantasyclasses.classes.ranger;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.util.particles.CustomParticle;
import me.thutson3876.fantasyclasses.util.particles.GeneralParticleEffects;

public class Ranger extends AbstractFantasyClass {

	private List<Arrow> trickArrows = new ArrayList<>();
	
	public Ranger(FantasyPlayer player) {
		super(player, false);
		
		Player p = player.getPlayer();
		
		name = "Ranger";

		this.setItemStack(Material.BOW, name, "A class for sharpshooters and trick-shotters");

		skillTree = new Skill(new RangerProficiencies(p));
		
		Skill preciseShots = new Skill(new PreciseShots(p));
		
		Skill rapid = new Skill(new RapidFire(p));
		preciseShots.addChild(rapid);
		
		Skill sharpShooter = new Skill(new Sharpshooter(p));
		sharpShooter.addChild(new SteadyAim(p));
		sharpShooter.addChild(new PiercingShots(p)).addChild(new VanityShot(p));
		sharpShooter.getNext().get(1).addChild(new TrueShot(p));
		preciseShots.addChild(sharpShooter);
		skillTree.addChild(preciseShots);
		
		Skill trickShots = new Skill(new TrickShots(p));
		Skill quiverOfTricks = trickShots.addChild(new HatTrick(p)).addChild(new TrickyTricks(p)).addChild(new QuiverOfTricks(p));
		quiverOfTricks.addChild(new EndlessTrickery(p));
		skillTree.addChild(trickShots);
		
		Skill disengage = new Skill(new Disengage(p));
		
		Skill camo = disengage.addChild(new Camoflauge(p));
		camo.addChild(new NaturalRemedy(p));
		
		Skill web = disengage.addChild(new WebTrap(p));
		web.addChild(new ThickWebbing(p));
		
		Skill mark = camo.addChild(new HuntersMark(p));
		mark.addChild(new DoubleTap(p));
		
		skillTree.addChild(disengage);
		
		setSkillInMap(4, skillTree); //Cost: 1 Max: 1
		//Sharpshooter branch //Total Cost: 11 (7)
		setSkillInMap(1, preciseShots); //Cost: 1 Max: 2
		setSkillInMap(9 + 2, rapid); //Cost: 1 Max: 1
		setSkillInMap(9 + 1, sharpShooter); //Cost: 1 Max: 2
		setSkillInMap(9 + 0, sharpShooter.getNext().get(0)); //Cost: 1 Max: 1 (Optional)
		setSkillInMap(18 + 1, sharpShooter.getNext().get(1)); //Cost: 1 Max: 2
		setSkillInMap(18 + 2, sharpShooter.getNext().get(1).getNext().get(0)); //Cost: 2 Max: 1
		setSkillInMap(27 + 1, sharpShooter.getNext().get(1).getNext().get(1)); //Cost: 1 Max: 2
		//Trickshot branch //Total Cost: 11 (7)
		setSkillInMap(9 + 4, trickShots); //Cost: 1 Max: 1
		setSkillInMap(18 + 4, trickShots.getNext().get(0)); //Cost: 1 Max: 2
		setSkillInMap(27 + 4, trickShots.getNext().get(0).getNext().get(0)); //Cost: 1 Max: 2
		setSkillInMap(36 + 4, quiverOfTricks); //Cost: 2 Max: 2
		setSkillInMap(45 + 4, quiverOfTricks.getNext().get(0)); //Cost: 1 Max: 2
		//Utility branch //Total Cost: 11 (7)
		setSkillInMap(7, disengage); //Cost: 1 Max: 1
		setSkillInMap(9 + 7, camo); //Cost: 1 Max: 2
		setSkillInMap(9 + 6, camo.getNext().get(0)); //Cost: 1 Max: 2
		
		setSkillInMap(18 + 7, mark); //Cost: 1 Max: 2
		setSkillInMap(27 + 7, mark.getNext().get(0)); //Cost: 1 Max: 2
		
		setSkillInMap(9 + 8, web); //Cost: 1 Max: 1
		setSkillInMap(18 + 8, web.getNext().get(0)); //Cost: 1 Max: 1
		
		this.setPrerequisites();
	}

	public List<Arrow> getTrickArrows(){
		return this.trickArrows;
	}
	
	public void addTrickArrow(Arrow arrow) {
		if(!this.trickArrows.contains(arrow)) {
			this.trickArrows.add(arrow);
			GeneralParticleEffects.trail(arrow, new CustomParticle(Particle.REDSTONE, 1, 0.1, 0.1, Color.RED), 12 * 20, 1);
			GeneralParticleEffects.trail(arrow, new CustomParticle(Particle.REDSTONE, 1, 0.1, 0.1, Color.YELLOW), 12 * 20, 1);
			GeneralParticleEffects.trail(arrow, new CustomParticle(Particle.REDSTONE, 1, 0, 0.3, Color.WHITE), 12 * 20, 1);
			
			arrow.getWorld().playSound(arrow, Sound.BLOCK_NOTE_BLOCK_GUITAR, 0.6f, 1.2f);
		}
			
	}
	
	public boolean removeTrickArrow(Arrow arrow) {
		return this.trickArrows.remove(arrow);
	}
	
}