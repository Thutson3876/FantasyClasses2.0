package me.thutson3876.fantasyclasses.custommobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.TropicalFish.Pattern;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.collectible.Collectible;
import me.thutson3876.fantasyclasses.custommobs.horde.Horde;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;

public class LostGuardian extends AbstractCustomMob {

	private static List<Material> dropMats = new ArrayList<>();
	//private PotionEffect poison = new PotionEffect(PotionEffectType.POISON, 4 * 20, 0);
	//private PotionEffect nausea = new PotionEffect(PotionEffectType.CONFUSION, 4 * 20, 0);
	//private PotionEffect hunger = new PotionEffect(PotionEffectType.HUNGER, 4 * 20, 0);
	
	private double range = 4.0;
	private int tickRate = 6 * 20;
	private int counter = 0;
	private int duration = 8 * 20;
	private int salTick = 10;

	static {
		dropMats.add(Material.PRISMARINE);
		dropMats.add(Material.PRISMARINE_CRYSTALS);
		dropMats.add(Material.DARK_PRISMARINE);
		dropMats.add(Material.PRISMARINE_SHARD);
		dropMats.add(Material.GOLD_INGOT);
	}
	
	public LostGuardian(Location loc) {
		super(loc);
		
		ent.setCustomName(ChatUtils.chat("&3Lost Guardian"));
		ent.setCustomNameVisible(true);
		
		Random rng = new Random();
		Collection<ItemStack> drops = new ArrayList<>();
		for(Material mat : dropMats) {
			ItemStack item = new ItemStack(mat, rng.nextInt(17) + 16);
			
			drops.add(item);
		}
		if(rng.nextDouble() < 0.08)
			drops.add(new ItemStack(Material.TRIDENT));
		if(rng.nextDouble() < 0.4)
			drops.add(Collectible.generateClassResetDrop());
		for(Horde horde : Horde.values()) {
			if(rng.nextDouble() < horde.getDropRate()) {
				drops.add(horde.generateDrop());
				break;
			}	
		}
		
		this.setDrops(drops);
		
		setTask(new BukkitRunnable() {

			@Override
			public void run() {
				if(ent.isDead()) {
					this.cancel();
					return;
				}
				
				for(LivingEntity le : AbilityUtils.getNearbyLivingEntities(ent, range, range, range)){
					if(le.isDead())
						continue;
				
					ent.getWorld().strikeLightning(le.getLocation());
				}
			}
			
		}.runTaskTimer(plugin, tickRate, tickRate));
	}

	@Override
	protected void applyDefaults() {
		this.setMaxHealth(80);
		this.setAttackDamage(20);
		this.setMoveSpeed(0.9f);
		this.setSkillExpReward(5);
	}

	@Override
	protected void tookDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof LivingEntity) {
			if(!ent.isDead())
				ent.setVelocity(((LivingEntity)e.getDamager()).getEyeLocation().getDirection().normalize().multiply(2.0));
		}
			
	}

	@Override
	protected void dealtDamage(EntityDamageByEntityEvent e) {
		e.setDamage(e.getDamage() + 3);
	}

	@Override
	protected void died(EntityDeathEvent e) {
		TropicalFish pretzel = (TropicalFish) ent.getWorld().spawnEntity(ent.getLocation(), EntityType.TROPICAL_FISH);
		pretzel.setPattern(Pattern.GLITTER);
		pretzel.setPatternColor(DyeColor.BLACK);
		pretzel.setBodyColor(DyeColor.PURPLE);
		pretzel.setCustomName(ChatUtils.chat("&4Sal"));
		pretzel.setCustomNameVisible(true);
		
		Player killer = ent.getKiller();
		if(killer == null)
			return;
		
		killer.sendMessage(ChatUtils.chat(Collectible.ETCHED_GLASS.getRandomLore()));
		
		new BukkitRunnable() {

			@Override
			public void run() {
				if(pretzel == null || pretzel.isDead()) {
					counter = 0;
					this.cancel();
				}
				
				for(LivingEntity ent : AbilityUtils.getNearbyLivingEntities(pretzel, range, range, range)) {
					if(ent instanceof Mob)
						continue;
					
					ent.damage(2.0);
				}
				
				if(counter > (duration / salTick)) {
					counter = 0;
					pretzel.remove();
					this.cancel();
				}
				
				counter++;
			}
			
		}.runTaskTimer(plugin, salTick, salTick);
	}

	@Override
	protected void targeted(EntityTargetEvent e) {
		Entity target = e.getTarget();
		if(target != null && ent.hasLineOfSight(target))
			target.getWorld().strikeLightning(target.getLocation());
		
	}

	@Override
	protected void healed(EntityRegainHealthEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getMetadataTag() {
		return "lost_guardian";
	}

	@Override
	protected void tookDamage(EntityDamageEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected EntityType getEntityType() {
		return EntityType.GUARDIAN;
	}

}
