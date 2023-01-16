package me.thutson3876.fantasyclasses.custommobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.collectible.Collectible;
import me.thutson3876.fantasyclasses.custommobs.horde.Horde;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.ChatUtils;

public class DrownedMiner extends AbstractCustomMob {

	private static List<Material> dropMats = new ArrayList<>(); 
	
	private int tickRate = 1 * 20;
	private double range = 7;
	private int duration = 5 * 20;
	private PotionEffect blindness = new PotionEffect(PotionEffectType.DARKNESS, duration, 0);
	private PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, duration, 0);
	private PotionEffect fatigue = new PotionEffect(PotionEffectType.SLOW_DIGGING, duration, 0);
	
	static {
		dropMats.add(Material.PRISMARINE);
		dropMats.add(Material.DARK_PRISMARINE);
		dropMats.add(Material.PRISMARINE_CRYSTALS);
		dropMats.add(Material.PRISMARINE_BRICKS);
	}
	
	public DrownedMiner(Location loc) {
		super(loc);
		
		Random rng = new Random();
		Collection<ItemStack> drops = new ArrayList<>();
		for(Material mat : dropMats) {
			ItemStack item = new ItemStack(mat, rng.nextInt(13) + 4);
			
			drops.add(item);
		}
		if(rng.nextDouble() < 0.2)
			drops.add(Collectible.generateClassResetDrop());
		
		for(Horde horde : Horde.values()) {
			if(rng.nextDouble() < horde.getDropRate()) {
				drops.add(horde.generateDrop());
				break;
			}	
		}
		
		this.setDrops(drops);
		
		EntityEquipment equip = ent.getEquipment();
		equip.setItemInMainHand(new ItemStack(Material.NETHERITE_PICKAXE));
		equip.setItemInMainHandDropChance(0.0f);
		
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
				
					le.addPotionEffect(blindness);
					le.addPotionEffect(slowness);
					le.addPotionEffect(fatigue);
				}
			}
			
		}.runTaskTimer(plugin, tickRate, tickRate));
	}

	@Override
	protected void targeted(EntityTargetEvent e) {
		
	}
	
	@Override
	protected void applyDefaults() {
		this.setMaxHealth(50);
		this.setAttackDamage(19);
		this.setMoveSpeed(0.6f);
		this.setSkillExpReward(3);
	}

	@Override
	protected void healed(EntityRegainHealthEvent e) {
		
	}

	@Override
	protected void tookDamage(EntityDamageByEntityEvent e) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                ent.setVelocity(new Vector());
            }
        }, 1L);
	}

	@Override
	protected void dealtDamage(EntityDamageByEntityEvent e) {
		
	}

	@Override
	protected void died(EntityDeathEvent e) {
		Player killer = ent.getKiller();
		if(killer == null)
			return;
		
		killer.sendMessage(ChatUtils.chat(Collectible.ETCHED_GLASS.getRandomLore()));
	}

	@Override
	public String getMetadataTag() {
		return "drowned_miner";
	}

	@Override
	protected void tookDamage(EntityDamageEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected EntityType getEntityType() {
		return EntityType.DROWNED;
	}

}
