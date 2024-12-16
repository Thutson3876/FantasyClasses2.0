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
import org.bukkit.entity.Tameable;
import org.bukkit.entity.WitherSkull;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.metadatavalue.NoExplodeBlocks;
import me.thutson3876.fantasyclasses.util.particles.CustomParticle;
import me.thutson3876.fantasyclasses.util.particles.GeneralParticleEffects;

public class Druid extends AbstractFantasyClass {

	private List<WitherSkull> shootingStars = new ArrayList<>();
	
	private double shootingStarSpeed = 0.07;
	
	public Druid(FantasyPlayer player) {
		super(player, false);
		
		Player p = player.getPlayer();
		name = "Druid";

		this.setItemStack(Material.RABBIT_FOOT, name, "A class based on revering nature and its gifts");
		
		skillTree = new Skill(new Druid_Proficiencies(p));
		
		Skill barkskin = new Skill(new Barkskin(p));
		barkskin.addChild(new Eclipse(p)).addChild(new Solstice(p));
		
		Skill rejuv = new Skill(new Rejuvination(p));
		rejuv.addChild(new NaturesBlessing(p));
		rejuv.addChild(new NaturesBalance(p)).addChild(new Tranquility(p));
		
		Skill shootingStars = new Skill(new ShootingStars(p));
		shootingStars.addChild(new Starfall(p));
		shootingStars.addChild(new UmbralEmbrace(p));
		
		Skill beast = new Skill(new BestFriend(p));
		Skill stamp = beast.addChild(new BeastMaster(p)).addChild(new Stampede(p));
		stamp.addChild(new BirdSinger(p));
		stamp.addChild(new FelinesGrace(p));
		stamp.addChild(new TightPack(p));
		
		skillTree.addChild(barkskin);
		skillTree.addChild(rejuv);
		skillTree.addChild(shootingStars); 
		skillTree.addChild(beast);
		skillTree.addChild(new SurvivalInstincts(p));
		
		setSkillInMap(27 + 4, skillTree);
		setSkillInMap(27 + 5, barkskin);
		setSkillInMap(27 + 6, barkskin.getNext().get(0));
		setSkillInMap(27 + 8, barkskin.getNext().get(0).getNext().get(0));
		
		setSkillInMap(9 + 6, rejuv);
		setSkillInMap(0 + 6, rejuv.getNext().get(0));
		setSkillInMap(9 + 7, rejuv.getNext().get(1));
		setSkillInMap(9 + 8, rejuv.getNext().get(1).getNext().get(0));
		
		setSkillInMap(36 + 4, shootingStars);
		setSkillInMap(45 + 3, shootingStars.getNext().get(0));
		setSkillInMap(45 + 5, shootingStars.getNext().get(1));
		
		setSkillInMap(27 + 2, beast);
		setSkillInMap(27 + 1, beast.getNext().get(0));
		setSkillInMap(27 + 0, stamp);
		setSkillInMap(18 + 0, stamp.getNext().get(0));
		setSkillInMap(9 + 1, stamp.getNext().get(2));
		setSkillInMap(36 + 0, stamp.getNext().get(1));
		
		setSkillInMap(9 + 4, skillTree.getNext().get(4));
		
		this.setPrerequisites();
	}
	
	public List<WitherSkull> getShootingStars(){
		return this.shootingStars;
	}
	
	public WitherSkull spawnShootingStar(Entity target) {
		if (target instanceof Tameable) {
			Tameable tamed = (Tameable) target;
			if (tamed.getOwner() != null && tamed.getOwner().equals(p)) {
				return null;
			}
		}
		
		World world = target.getWorld();
		Location spawnPoint = target.getLocation();
		
		Random rng = new Random();

		spawnPoint.add(-2 + 4*rng.nextFloat(), target.getHeight() + 4 + rng.nextFloat(), -2 + 4*rng.nextFloat());
		
		WitherSkull star = (WitherSkull) world.spawnEntity(spawnPoint, EntityType.WITHER_SKULL);
		
		Vector launchVector = AbilityUtils.getVectorBetween2Points(spawnPoint, target.getLocation(), shootingStarSpeed).normalize();
		
		//star.setVisibleByDefault(false);
		star.setGlowing(true);
		star.setCharged(true);
		star.setYield(0.1f);
		star.setDirection(launchVector);
		star.setVelocity(launchVector.multiply(shootingStarSpeed));
		star.setShooter(p);
		star.setMetadata("noexplodeblocks", new NoExplodeBlocks());
		
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
	
	public WitherSkull spawnShootingStar(Location loc) {
		World world = loc.getWorld();
		Location spawnPoint = loc;
		
		Random rng = new Random();

		spawnPoint.add(-2 + 4*rng.nextFloat(), 2 + 4 + rng.nextFloat(), -2 + 4*rng.nextFloat());
		
		WitherSkull star = (WitherSkull) world.spawnEntity(spawnPoint, EntityType.WITHER_SKULL);
		
		Vector launchVector = AbilityUtils.getVectorBetween2Points(spawnPoint, loc, shootingStarSpeed).normalize();
		
		//star.setVisibleByDefault(false);
		star.setGlowing(true);
		star.setCharged(true);
		star.setYield(0.1f);
		star.setDirection(launchVector);
		star.setVelocity(launchVector.multiply(shootingStarSpeed));
		star.setShooter(p);
		star.setMetadata("noexplodeblocks", new NoExplodeBlocks());
		
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
