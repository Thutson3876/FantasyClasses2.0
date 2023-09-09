package me.thutson3876.fantasyclasses.custommobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.collectible.Collectible;
import me.thutson3876.fantasyclasses.custommobs.horde.Horde;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;

public class UndeadMiner extends AbstractCustomMob {

	private int tickRate = 1 * 20;
	private double range = 7;
	private int duration = 5 * 20;
	private PotionEffect blindness = new PotionEffect(PotionEffectType.DARKNESS, duration, 0);
	private PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, duration, 0);
	private PotionEffect fatigue = new PotionEffect(PotionEffectType.SLOW_DIGGING, duration, 0);
	private static List<Material> dropMats = new ArrayList<>();
	
	static {
		dropMats.add(Material.DIAMOND);
		dropMats.add(Material.IRON_INGOT);
		dropMats.add(Material.GOLD_INGOT);
		dropMats.add(Material.LAPIS_LAZULI);
		dropMats.add(Material.COBBLED_DEEPSLATE);
		dropMats.add(Material.REDSTONE);
	}
	
	public UndeadMiner(Location loc) {
		super(loc);
		Random rng = new Random();
		ItemStack pick = new ItemStack(Material.NETHERITE_PICKAXE);
		ItemMeta meta = pick.getItemMeta();
		Damageable damageable = (Damageable)meta;
		damageable.setDamage(rng.nextInt(1000) + 1000);
		pick.setItemMeta(damageable);
		
		giveItems(null, pick, 0.1f);
		
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
		
		onSpawn();
		Collection<ItemStack> drops = new ArrayList<>();
		for(Material mat : dropMats) {
			ItemStack item = new ItemStack(mat, rng.nextInt(3));
			if(item.getAmount() <= 0)
				continue;
			
			drops.add(item);
		}
		if(rng.nextDouble() < 0.4)
			drops.add(Collectible.generateClassResetDrop());
		
		for(Horde horde : Horde.values()) {
			if(rng.nextDouble() < horde.getDropRate()) {
				drops.add(horde.generateDrop());
				break;
			}	
		}
		
		this.setDrops(drops);
	}

	@Override
	protected void applyDefaults() {
		this.setMaxHealth(60);
		this.setAttackDamage(20);
		this.setSkillExpReward(6);
		this.setMoveSpeed(0.2f);
	}

	@Override
	protected void tookDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			if(!e.getCause().equals(DamageCause.ENTITY_ATTACK)) {
				return;
			}
			
			Player p = (Player) e.getDamager();
			ItemStack item = p.getInventory().getItemInMainHand();
			if(item.getType().equals(Material.GOLDEN_APPLE) || item.getType().equals(Material.ENCHANTED_GOLDEN_APPLE)) {
				item.setAmount(item.getAmount() - 1);
				if (item.getAmount() <= 0)
					item = null;

				p.getInventory().setItem(p.getInventory().getHeldItemSlot(), item);
				p.sendMessage(Collectible.MINING_SCHEMATICS.getRandomLore());
				
				FantasyPlayer fplayer = plugin.getPlayerManager().getPlayer(p);
				if(fplayer != null)
					fplayer.addSkillExp(9);
				
				Location loc = ent.getLocation();
				ent.remove();
				loc.getWorld().spawnEntity(loc, EntityType.SKELETON);
			}
		}
		
	}

	@Override
	protected void dealtDamage(EntityDamageByEntityEvent e) {
		e.getEntity().setFireTicks(3 * 20);
	}

	protected void onSpawn() {
		for(LivingEntity le : AbilityUtils.getNearbyLivingEntities(ent, range, range, range)){
			if(le.isDead())
				continue;
		
			le.addPotionEffect(blindness);
			le.addPotionEffect(slowness);
			le.addPotionEffect(fatigue);
			
			if(le instanceof Player) {
				((Player)le).sendMessage(ChatUtils.chat("&0You feel a darkness encroaching upon you..."));
				((Player) le).playSound(le.getLocation(), Sound.AMBIENT_CAVE, 1.2f, 1.0f);
			}
		}
	}

	@Override
	protected void died(EntityDeathEvent e) {
		Player killer = ent.getKiller();
		if(killer == null)
			return;
		
		killer.sendMessage(ChatUtils.chat(Collectible.MINING_SCHEMATICS.getRandomLore()));
	}

	@Override
	protected void targeted(EntityTargetEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void healed(EntityRegainHealthEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getMetadataTag() {
		return "undead_miner";
	}

	@Override
	protected void tookDamage(EntityDamageEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected EntityType getEntityType() {
		return EntityType.WITHER_SKELETON;
	}

}
