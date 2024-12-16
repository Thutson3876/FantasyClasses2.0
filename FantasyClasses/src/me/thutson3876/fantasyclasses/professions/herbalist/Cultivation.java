package me.thutson3876.fantasyclasses.professions.herbalist;

import static java.util.Map.entry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;
import me.thutson3876.fantasyclasses.util.geometry.Sphere;
import me.thutson3876.fantasyclasses.util.particles.CustomParticle;
import me.thutson3876.fantasyclasses.util.particles.GeneralParticleEffects;

public class Cultivation extends AbstractAbility {
	
	/*private void drawParticle(Vector toDraw, Location offset) {
        for(double d=0; d<=toDraw.length()+0.2; d+=0.01) {
            Location toSpawn = offset.clone().add(toDraw.clone().multiply(d));
            toSpawn.getWorld().spawnParticle(Particle.DUST, toSpawn, 2, 0, 0, 0, 0, new Particle.DustOptions(Color.GREEN, 0.4f));
        }
    }*/
	
	private final Map<Material, SpecialFlower> flowerMap = Map.ofEntries(
		    entry(Material.SUNFLOWER, new SpecialFlower("Sunflower", "...projects its photsynthesis onto mammals", (b)->{
		    	Location loc = b.getLocation();
		    	int radius = 10;
		    	int tickRate = 20;
		    	
		    	PotionEffect effect = new PotionEffect(PotionEffectType.SATURATION, 2 * 20, 0);
		    	
		    	loc.getWorld().playSound(loc, Sound.BLOCK_GROWING_PLANT_CROP, 1, 1.5f);
		    	BukkitTask task = new BukkitRunnable() {
		    		@Override
					public void run() {
		    			//GeneralParticleEffects.doubleEndedCircle(b.getLocation(), 1, 1, 360, tickRate, new CustomParticle(Particle.SPLASH, 3, 0.1, Color.YELLOW));
						GeneralParticleEffects.durationBasedHelix(b.getLocation().add(0, 0.25, 0), new CustomParticle(Particle.DUST, 2, 0.2, 0.8, Color.YELLOW), 0.8, 360, tickRate, 1, 0.007);
						//loc.getWorld().playSound(b.getLocation(), Sound.ITEM_BONE_MEAL_USE, 1, 1);
						//List<LivingEntity> entities = AbilityUtils.getNearbyLivingEntities(loc, radius, radius, radius);
						Collection<Entity> entities = loc.getWorld().getNearbyEntities(b.getLocation(), radius, radius, radius, (entity) -> entity instanceof LivingEntity);
						//drawParticle(new Vector(0, 1,0), b.getLocation());
		    			for(Entity ent : entities) {
		    				if(ent == null || ent.isDead())
		    					continue;
		    				
				    		((LivingEntity)ent).addPotionEffect(effect);
				    	}
		    			
		    		}
		    	}.runTaskTimer(plugin, 0, tickRate);
		    	
		    	return task;
		    	
		    })),
		    entry(Material.LILY_OF_THE_VALLEY, new SpecialFlower("Lily of the Valley", "...pacifies all", (b)->{
		    	Location loc = b.getLocation();
		    	int radius = 16;
		    	int tickRate = 20;
		    	
		    	loc.getWorld().playSound(loc, Sound.BLOCK_GROWING_PLANT_CROP, 1, 1.5f);
		    	BukkitTask task = new BukkitRunnable() {
		    		@Override
					public void run() {
		    			//GeneralParticleEffects.doubleEndedCircle(loc, 1, 1, 360, tickRate, new CustomParticle(Particle.DUST, 3, 0.1, Color.YELLOW));
						GeneralParticleEffects.durationBasedHelix(b.getLocation().add(0, 0.25, 0), new CustomParticle(Particle.DUST, 2, 0.2, 0.8, Color.WHITE), 0.8, 360, tickRate, 1, 0.007);
		    			
		    			for(LivingEntity ent : AbilityUtils.getNearbyLivingEntities(loc, radius, radius, radius)) {
		    				if(ent == null || ent.isDead() || !(ent instanceof Monster))
		    					continue;
		    				
		    				((Monster)ent).setTarget(null);
				    	}
		    		}
		    	}.runTaskTimer(plugin, 0, tickRate);
		    	
		    	return task;
		    })),
		    entry(Material.BLUE_ORCHID, new SpecialFlower("Blue Orchid", "...grants oxidization to nearby mammals", (b) -> {
		    	Location loc = b.getLocation();
		    	int radius = 20;
		    	int tickRate = 20;
		    	
		    	PotionEffect effect = new PotionEffect(PotionEffectType.WATER_BREATHING, 2 * 20, 0);
		    	PotionEffect effect2 = new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 2 * 20, 0);
		    	loc.getWorld().playSound(loc, Sound.BLOCK_GROWING_PLANT_CROP, 1, 1.5f);
		    	BukkitTask task = new BukkitRunnable() {
		    		@Override
					public void run() {
		    			//GeneralParticleEffects.doubleEndedCircle(loc, 1, 1, 360, tickRate, new CustomParticle(Particle.DUST, 3, 0.1, Color.BLUE));
						GeneralParticleEffects.durationBasedHelix(b.getLocation().add(0, 0.25, 0), new CustomParticle(Particle.DUST, 2, 0.2, 0.8, Color.BLUE), 0.8, 360, tickRate, 1, 0.007);
		    			
		    			for(LivingEntity ent : AbilityUtils.getNearbyLivingEntities(loc, radius, radius, radius)) {
		    				if(ent == null || ent.isDead())
		    					continue;
		    				
				    		ent.addPotionEffect(effect);
				    		ent.addPotionEffect(effect2);
				    	}
		    		}
		    	}.runTaskTimer(plugin, 0, tickRate);
		    	
		    	return task;
		    })),
		    entry(Material.LILAC, new SpecialFlower("Lilac", "...empowers nearby creatures with bounding leaps", (b)->{
		    	Location loc = b.getLocation();
		    	int radius = 16;
		    	int tickRate = 20;
		    	
		    	PotionEffect effect = new PotionEffect(PotionEffectType.JUMP_BOOST, 2 * 20, 1);
		    	loc.getWorld().playSound(loc, Sound.BLOCK_GROWING_PLANT_CROP, 1, 1.5f);
		    	BukkitTask task = new BukkitRunnable() {
		    		@Override
					public void run() {
		    			//GeneralParticleEffects.doubleEndedCircle(loc, 1, 1, 360, tickRate, new CustomParticle(Particle.DUST, 3, 0.1, Color.YELLOW));
						GeneralParticleEffects.durationBasedHelix(b.getLocation().add(0, 0.25, 0), new CustomParticle(Particle.DUST, 2, 0.2, 0.8, Color.PURPLE), 0.8, 360, tickRate, 1, 0.007);
		    			
		    			for(LivingEntity ent : AbilityUtils.getNearbyLivingEntities(loc, radius, radius, radius)) {
		    				if(ent == null || ent.isDead())
		    					continue;
		    				
				    		ent.addPotionEffect(effect);
				    	}
		    		}
		    	}.runTaskTimer(plugin, 0, tickRate);
		    	
		    	return task;
		    })),
		    entry(Material.PEONY, new SpecialFlower("Peony", "...spreads its nutrients to nearby plants", (b)->{
		    	Location loc = b.getLocation();
		    	int radius = 8;
		    	int tickRate = 5 * 20;
		    	
		    	loc.getWorld().playSound(loc, Sound.BLOCK_GROWING_PLANT_CROP, 1, 1.5f);
		    	BukkitTask task = new BukkitRunnable() {
		    		@Override
					public void run() {
		    			//GeneralParticleEffects.doubleEndedCircle(loc, 1, 1, 360, tickRate, new CustomParticle(Particle.DUST, 3, 0.1, Color.YELLOW));
						GeneralParticleEffects.durationBasedHelix(b.getLocation().add(0, 0.25, 0), new CustomParticle(Particle.DUST, 2, 0.2, 0.8, Color.WHITE), 0.8, 360, tickRate, 1, 0.007);
		    			
						List<Location> available = new ArrayList<Location>();
		    			for(Location l : Sphere.generateSphere(b.getLocation(), radius, false)) {
		    				if(l.getBlock().getRelative(BlockFace.UP).isEmpty())
		    					available.add(l);
				    	}
		    			
		    			Random rng = new Random();
		    			available.get(rng.nextInt(available.size())).getBlock().applyBoneMeal(BlockFace.UP);
		    		}
		    	}.runTaskTimer(plugin, 0, tickRate);
		    	
		    	return task;
		    }))
		    );
	
	Map<Block, BukkitTask> storedBlocks = new HashMap<Block, BukkitTask>();
	
	private double learnChance = 0.1;
	
	public Cultivation(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Cultivation";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.PEONY);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if(!e.getPlayer().equals(player))
			return;
		
		if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;
		
		Block b = e.getClickedBlock();
		
		if(b == null)
			return;
		
		if(storedBlocks.containsKey(b))
			return;
		
		Material mat = b.getType();
		SpecialFlower specialFlower = flowerMap.get(mat);
		if(specialFlower == null)
			return;
		
		storedBlocks.put(b, specialFlower.action.run(b));
		
		plugin.log("Started special for " + b.getType());
		//plugin.log("Ran!");
		this.onTrigger(false);
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e) {
		Block b = e.getBlock();
		if(!storedBlocks.containsKey(b)) {
			b = b.getRelative(BlockFace.UP);
			if(!storedBlocks.containsKey(b))
				return;
		}
			
		storedBlocks.remove(b).cancel();
		plugin.log("Cancelled special for " + b.getType());
	}
	
	@EventHandler
	public void onBlockFertilizeEvent(BlockFertilizeEvent e) {
		if(e.getPlayer() == null || !e.getPlayer().equals(player))
			return;
		
		Random rng = new Random();
		
		if(rng.nextDouble() > learnChance)
			return;
		
		Random generator = new Random();
		SpecialFlower[] values = (SpecialFlower[]) flowerMap.values().toArray();
		SpecialFlower randomValue = values[generator.nextInt(values.length)];
		
		player.sendMessage(ChatUtils.chat("&a" + randomValue.name + " " + randomValue.desc));

	}

	@Override
	public String getInstructions() {
		return "Right-click a flower";
	}

	@Override
	public String getDescription() {
		return "Different flowers have different effects when you interact with them... Learn more by experimenting or growing them...";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {

	}
	
	public class SpecialFlower{
		
		public String name;
		public String desc;
		public FlowerAction action;
		
		public SpecialFlower(String name, String desc, FlowerAction action) {
			this.name = name;
			this.desc = desc;
			this.action = action;
		}
	}
	
	public interface FlowerAction {
		public BukkitTask run(Block b);
	}
}

