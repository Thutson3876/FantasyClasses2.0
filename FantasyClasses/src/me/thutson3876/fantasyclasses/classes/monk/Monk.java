package me.thutson3876.fantasyclasses.classes.monk;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;

public class Monk extends AbstractFantasyClass {

	private double unarmedDmgMod = 1.0;
	
	private boolean isHealer = false;
	
	public Monk(FantasyPlayer player) {
		super(player, false);
		
		Player p = player.getPlayer();
		name = "Monk";

		this.setItemStack(Material.BAMBOO, name, "A class based on martial arts and letting go of the material world");
		
		skillTree = new Skill(new Monk_Proficiencies(p));
		
		Skill walker = skillTree.addChild(new WindWalker(p));
		walker.addChild(new Lightfoot(p));
		
		Skill balanced = walker.addChild(new WindWeaver(p)).addChild(new BalancedLanding(p));
		balanced.addChild(new SpinningMixer(p)).addChild(new Serenity(p));
		
		Skill palm = skillTree.addChild(new OpenPalm(p));
		Skill kyo = palm.addChild(new KyoketsuShoge(p));
		kyo.addChild(new Whiplash(p));
		
		Skill blade = kyo.addChild(new WayOfTheBlade(p));
		blade.addChild(new PressurePoints(p));
		
		Skill dex = skillTree.addChild(new UnarmoredDexterity(p));
		dex.addChild(new FulfillingMead(p)).addChild(new SpicedBrew(p));
		
		Skill spirits = dex.addChild(new UpliftingSpirits(p));
		spirits.addChild(new Renewal(p));
		spirits.addChild(new EssenceFont(p));
		
		setSkillInMap(4, skillTree);

		setSkillInMap(9 + 4, walker);
		setSkillInMap(9 + 5, walker.getNext().get(0));
		setSkillInMap(18 + 4, walker.getNext().get(1));
		setSkillInMap(27 + 4, balanced);
		setSkillInMap(36 + 4, balanced.getNext().get(0));
		setSkillInMap(45 + 4, balanced.getNext().get(0).getNext().get(0));
		
		setSkillInMap(9 + 2, palm);
		setSkillInMap(18 + 2, kyo);
		setSkillInMap(18 + 1, kyo.getNext().get(0));
		setSkillInMap(27 + 2, blade);
		setSkillInMap(36 + 2, blade.getNext().get(0));
		
		setSkillInMap(9 + 6, dex);
		setSkillInMap(18 + 6, dex.getNext().get(0));
		setSkillInMap(18 + 7, dex.getNext().get(0).getNext().get(0));
		setSkillInMap(27 + 6, spirits);
		setSkillInMap(27 + 7, spirits.getNext().get(0));
		setSkillInMap(36 + 6, spirits.getNext().get(1));
		
		this.setPrerequisites();
	}

	public double getUnarmedDmgMod() {
		return unarmedDmgMod;
	}

	public void setUnarmedDmgMod(double unarmedDmgMod) {
		this.unarmedDmgMod = unarmedDmgMod;
	}
	
	public void setIsHealer(boolean isHealer) {
		this.isHealer = isHealer;
	}
	
	public boolean isHealer() {
		return isHealer;
	}

}
