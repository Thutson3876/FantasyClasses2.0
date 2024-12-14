package me.thutson3876.fantasyclasses.classes.druid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.particles.CustomParticle;
import me.thutson3876.fantasyclasses.util.particles.GeneralParticleEffects;

public class Druid extends AbstractFantasyClass {

	private List<WitherSkull> shootingStars = new ArrayList<>();
	
	private double shootingStarSpeed = 0.1;
	
	public Druid(FantasyPlayer player) {
		super(player, false);
		
		Player p = player.getPlayer();
		name = "Druid";

		this.setItemStack(Material.RABBIT_FOOT, name, "A class based on revering nature and its gifts");

		skillTree = new Skill(new Forager(p));
		
		Skill craft = new Skill(new Druidcraft(p));
		Skill bark = craft.addChild(new Rejuvination(p)).addChild(new Barkskin(p));
		bark.addChild(new NaturesBlessing(p));
		bark.addChild(new NaturesBalance(p)).addChild(new Tranquility(p));
		skillTree.addChild(craft);
		craft.addChild(new GreenThumb(p)).addChild(new Regrowth(p));
		
		Skill beast = new Skill(new BestFriend(p));
		Skill stamp = beast.addChild(new BeastMaster(p)).addChild(new Stampede(p));
		stamp.addChild(new BirdSinger(p));
		stamp.addChild(new FelinesGrace(p));
		stamp.addChild(new TightPack(p));
		skillTree.addChild(beast);
		
		Skill tree = new Skill(new TreeFeller(p));
		tree.addChild(new BountifulHarvest(p)).addChild(new SurvivalInstincts(p));
		skillTree.addChild(tree);
		skillTree.addChild(new ShootingStars(p)).addChild(new Eclipse(p));
		
		// new Skill(new WildGrowth(p));
		
		setSkillInMap(4, skillTree);
		setSkillInMap(9 + 1, craft.getNext().get(1));
		setSkillInMap(9 + 0, craft.getNext().get(1).getNext().get(0));
		setSkillInMap(9 + 2, craft);
		setSkillInMap(18 + 2, craft.getNext().get(0));
		setSkillInMap(27 + 2, bark);
		setSkillInMap(36 + 2, bark.getNext().get(0));
		setSkillInMap(27 + 1, bark.getNext().get(1));
		setSkillInMap(27 + 0, bark.getNext().get(1).getNext().get(0));
		setSkillInMap(9 + 4, beast);
		setSkillInMap(18 + 4, beast.getNext().get(0));
		setSkillInMap(27 + 4, stamp);
		setSkillInMap(36 + 3, stamp.getNext().get(2));
		setSkillInMap(36 + 4, stamp.getNext().get(0));
		setSkillInMap(36 + 5, stamp.getNext().get(1));
		setSkillInMap(9 + 6, tree);
		setSkillInMap(9 + 7, tree.getNext().get(0));
		setSkillInMap(18 + 7, tree.getNext().get(0).getNext().get(0));
		setSkillInMap(0 + 1, skillTree.getNext().get(3));
		setSkillInMap(0 + 0, skillTree.getNext().get(3).getNext().get(0));
		
		this.setPrerequisites();
	}
	
	public List<WitherSkull> getShootingStars(){
		return this.shootingStars;
	}
	
	public WitherSkull spawnShootingStar(Entity target) {
		World world = target.getWorld();
		Location spawnPoint = target.getLocation();
		
		Random rng = new Random();

		spawnPoint.add(-2 + 4*rng.nextFloat(), target.getHeight() + 4 + rng.nextFloat(), -2 + 4*rng.nextFloat());
		
		WitherSkull star = (WitherSkull) world.spawnEntity(spawnPoint, EntityType.WITHER_SKULL);
		
		Vector launchVector = AbilityUtils.getVectorBetween2Points(spawnPoint, target.getLocation(), shootingStarSpeed).normalize();
		
		//star.setVisibleByDefault(false);
		star.setGlowing(true);
		star.setYield(0.1f);
		star.setDirection(launchVector);
		star.setVelocity(launchVector.multiply(shootingStarSpeed));
		star.setShooter(p);
		
		if(!this.shootingStars.contains(star)) {
			this.shootingStars.add(star);
			star.playEffect(EntityEffect.TELEPORT_ENDER);
			GeneralParticleEffects.trail(star, new CustomParticle(Particle.DUST, 5, 0.1, 0.5, Color.PURPLE), 12 * 20, 1);
			GeneralParticleEffects.trail(star, new CustomParticle(Particle.DUST, 5, 0.1, 0.5, Color.BLUE), 12 * 20, 1);
			GeneralParticleEffects.trail(star, new CustomParticle(Particle.DUST, 10, 0, 1, Color.WHITE), 12 * 20, 1);
			GeneralParticleEffects.trail(star, new CustomParticle(Particle.CHERRY_LEAVES, 5, 0, 1, Color.PURPLE), 12 * 20, 1);
			
			world.playSound(star, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1f, 1.0f);
		}
		
		return star;
			
	}
	
	public boolean removeShootingStar(WitherSkull star) {
		return this.shootingStars.remove(star);
	}
}
