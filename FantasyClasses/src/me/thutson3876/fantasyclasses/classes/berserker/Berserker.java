package me.thutson3876.fantasyclasses.classes.berserker;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.util.Particles;

public class Berserker extends AbstractFantasyClass {

	private BukkitTask task = null;
	private double tauntRange = 0.0;
	
	private int tickRate = 20;
	private int enrageDurationMax = 4 * 20;
	private int enrageDurationRemaining = 0;
	
	public Berserker(FantasyPlayer player) {
		super(player, false);
		
		Player p = player.getPlayer();
		
		name = "Berserker";

		this.setItemStack(Material.DIAMOND_AXE, name, "A tanky class with anger issues to spare");

		skillTree = new Skill(new Berserk_Proficiencies(p));
		
		Skill leap = new Skill(new Leap(p));
		Skill juggle = leap.addChild(new AirSkip(p)).addChild(new Heave(p)).addChild(new Juggle(p));
		juggle.addChild(new FloatPoint(p));
		skillTree.addChild(leap);
		
		Skill dive = leap.addChild(new DeepDive(p));
		dive.addChild(new PileDriver(p));
		Skill cannonball = dive.addChild(new Cannonball(p));
		cannonball.addChild(new Chainshot(p));
		
		Skill enrage = new Skill(new Enrage(p));
		
		Skill battle = enrage.addChild(new BattleHardened(p));
		Skill blows = battle.addChild(new MeatShield(p)).addChild(new RagingBlows(p));
		blows.addChild(new EndlessFury(p));
		
		Skill anger = enrage.addChild(new AngerManagement(p));
		anger.addChild(new IgnorePain(p)).addChild(new DemoralizingShout(p));
		anger.addChild(new Taunt(p));
		skillTree.addChild(enrage);
		
		this.setSkillInMap(4, skillTree); //Cost: 1 Max: 1
		//Brawler branch //Total Cost: 15 (1 + 5 + 6 = 12)
		this.setSkillInMap(9 + 2, leap); //Cost: 1 Max: 1
		//Knock Up
		this.setSkillInMap(18 + 1, leap.getNext().get(0)); //Cost: 1 Max: 1
		this.setSkillInMap(27 + 1, leap.getNext().get(0).getNext().get(0)); //Cost: 2 Max: 2
		this.setSkillInMap(36 + 1, juggle); //Cost: 1 Max: 2
		this.setSkillInMap(36 + 0, juggle.getNext().get(0)); //Cost: 1 Max: 1
		//Deep Dive
		this.setSkillInMap(18 + 3, dive); //Cost: 2 Max: 1
		this.setSkillInMap(27 + 2, dive.getNext().get(0)); //Cost: 1 Max: 1
		this.setSkillInMap(27 + 4, cannonball); //Cost: 2 Max: 1
		this.setSkillInMap(36 + 3, cannonball.getNext().get(0)); //Cost: 1 Max: 1
		//Enrage branch //Total Cost: 16 (1 + 4 + 4 = 11)
		this.setSkillInMap(9 + 6, enrage); //Cost: 1 Max: 1
		//Damage Dealt
		this.setSkillInMap(18 + 5, battle); //Cost: 1 Max: 2
		this.setSkillInMap(27 + 5, battle.getNext().get(0)); //Cost: 1 Max: 2
		this.setSkillInMap(36 + 5, blows); //Cost: 2 Max: 2
		this.setSkillInMap(45 + 5, blows.getNext().get(0)); //Cost: 1 Max: 2
		//Damage Taken
		this.setSkillInMap(18 + 7, anger); //Cost: 1 Max: 1
		this.setSkillInMap(18 + 8, anger.getNext().get(1)); //Cost: 1 Max: 1
		this.setSkillInMap(27 + 7, anger.getNext().get(0)); //Cost: 1 Max: 2
		this.setSkillInMap(36 + 7, anger.getNext().get(0).getNext().get(0)); //Cost: 1 Max: 1
		
		this.setPrerequisites();
	}

	public int getEnrageDurationMax() {
		return enrageDurationMax;
	}

	public void setEnrageDurationMax(int enrageDurationMax) {
		this.enrageDurationMax = enrageDurationMax;
	}

	public int getEnrageDurationRemaining() {
		return enrageDurationRemaining;
	}

	public void addEnrageDuration(int duration) {
		if(task == null) {
			this.enrageDurationRemaining = 0;
			setEnrageDurationRemaining(duration);
			return;
		}
		
		this.enrageDurationRemaining += duration;
		setPotionEffect(this.enrageDurationRemaining);
	}

	public void setEnrageDurationRemaining(int duration) {
		if(duration > enrageDurationRemaining)
			this.enrageDurationRemaining = duration;
		else
			return;
		
		if(this.task != null)
			task.cancel();
		
		setPotionEffect(this.enrageDurationRemaining);
		p.getWorld().playSound(p, Sound.BLOCK_GLASS_BREAK, 0.5f, 1.6f);
		
		this.task = new BukkitRunnable() {

			@Override
			public void run() {
				enrageDurationRemaining -= tickRate;
				
				if(enrageDurationRemaining < 0 || p.isDead()) {
					enrageDurationRemaining = 0;
					this.cancel();
					task = null;
					return;
				}
				else
					Particles.helix(p, Particle.DRIP_LAVA, 1.0, 12.5664, 5, 0.2);
			}
			
		}.runTaskTimer(FantasyClasses.getPlugin(), tickRate, tickRate);
	}
	
	public void setTauntRange(double range) {
		this.tauntRange = range;
	}
	
	private void setPotionEffect(int duration) {
		if(p.isDead())
			return;
		
		p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, 0));
		
		if(this.tauntRange > 0.1) {
			tauntNearbyMobs();
		}
	}
	
	private void tauntNearbyMobs() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_RAVAGER_ROAR, 1.5f, 0.9f);
		
		for(Entity ent : p.getNearbyEntities(tauntRange, tauntRange, tauntRange)) {
			if(ent instanceof Mob) {
				((Mob)ent).setTarget(p);
			}
		}
	}

}
