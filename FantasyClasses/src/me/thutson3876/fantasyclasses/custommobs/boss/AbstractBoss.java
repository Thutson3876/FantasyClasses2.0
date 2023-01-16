package me.thutson3876.fantasyclasses.custommobs.boss;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.thutson3876.fantasyclasses.custommobs.AbstractCustomMob;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.ChatUtils;

public abstract class AbstractBoss extends AbstractCustomMob implements Boss {

	protected List<MobAbility> abilities = new ArrayList<>();

	protected BossBar bar = null;
	
	protected final String name;

	protected boolean requiresTarget = true;
	
	private int abilityDelay = 7 * 20;
	
	private int prevAbilIndex = -1;

	//for health regen
	int count = 0;
	int timer = 7;
	boolean tookDamageWithinTimer = false;
	protected BukkitTask task = null;
	
	public AbstractBoss(Location loc, String name) {
		super(loc);

		this.name = name;
		ent.setCustomName(ChatUtils.chat(name));
		ent.setCustomNameVisible(true);
		
		task = new BukkitRunnable() {

			@Override
			public void run() {
				if(ent.isDead())
					cancel();
				
				if(ent.getHealth() >= getMaxHealth())
					return;
				
				if(count >= timer) {
					AbilityUtils.heal(ent, 10, ent);
					//count = 0;
					return;
				}
				count++;
			}
			
		}.runTaskTimer(plugin, 20, 20);
	}

	@Override
	protected void applyDefaults() {

	}
	
	@Override
	public void deInit() {
		removeBossBar();
	}

	protected void startAbilityTick() {
		if (abilities.isEmpty())
			return;

		Random rng = new Random();

		new BukkitRunnable() {

			@Override
			public void run() {
				if (ent == null || ent.isDead()) {
					this.cancel();
					return;
				}
				
				//Doesn't run if entity has no targets
				if(requiresTarget && ent instanceof Mob && ((Mob)ent).getTarget() == null)
					return;

				int abilsLength = abilities.size();
				int abilIndex = rng.nextInt(abilsLength);
				while(abilIndex == prevAbilIndex) {
					abilIndex = rng.nextInt(abilsLength);
				}
				prevAbilIndex = abilIndex;
				
				abilities.get(abilIndex).run((Mob) ent);
				
				//Broadcasts ability name to nearby players
				for(Player p : AbilityUtils.getNearbyPlayers(ent, 20)){
					p.sendMessage(ChatUtils.chat(name + " &8cast &4" + abilities.get(abilIndex).getName()));
				}
			}

		}.runTaskTimer(plugin, getAbilityDelay(), getAbilityDelay());
	}

	@Override
	public BossBar getBossBar() {
		return bar;
	}

	protected void setBossBar(String title, BarColor color, BarStyle style, BarFlag... flags) {
		this.bar = Bukkit.createBossBar(ChatUtils.chat(title), color, style, flags);
		double maxHealth = ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		this.bar.setVisible(true);

		new BukkitRunnable() {

			@Override
			public void run() {
				if (!ent.isDead()) {
					List<Player> barPlayers = new ArrayList<>();
					barPlayers.addAll(bar.getPlayers());
					List<Player> nearbyPlayers = new ArrayList<>();
					for (Entity e : ent.getNearbyEntities(30, 30, 30)) {
						if (e instanceof Player) {
							nearbyPlayers.add((Player) e);
						}
					}
					
					if(barPlayers != null && !barPlayers.isEmpty()) {
						
						for (Player p : nearbyPlayers) {
							if(barPlayers.contains(p))
								barPlayers.remove(p);
						}
						
						for(Player p : barPlayers) {
							bar.removePlayer(p);
						}
					}
					
					for(Player p : nearbyPlayers) {
						bar.addPlayer(p);
					}
					
					bar.setProgress(ent.getHealth() / maxHealth);
				} else {
					List<Player> players = bar.getPlayers();
					for (Player player : players) {
						bar.removePlayer(player);
					}
					bar.setVisible(false);
					cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 1);
	}

	protected void addAbility(MobAbility abil) {
		abilities.add(abil);
	}
	
	protected void removeBossBar() {
		if(bar != null) {
			bar.setVisible(false);
			bar.removeAll();
		}
	}
	
	protected void broadcastDamageDealt(EntityDamageByEntityEvent e) {
		Entity damager = e.getDamager();
		Player player = null;
		
		
		if(damager instanceof Player) {
			player = (Player) damager;
		}
		else if(damager instanceof Projectile) {
			Projectile proj = (Projectile) damager;
			if(proj.getShooter() instanceof Player) {
				player = (Player) proj.getShooter();
			}
		}
		
		if(player != null)
			player.sendMessage(ChatUtils.chat("&7You dealt &c" + AbilityUtils.doubleRoundToXDecimals(e.getFinalDamage(), 2) + " &7damage"));
	}
	
	@EventHandler
	public void onEntityPotionEffectEvent(EntityPotionEffectEvent e) {
		if(e.getEntity().equals(ent) && (e.getModifiedType().equals(PotionEffectType.LEVITATION) || e.getModifiedType().equals(PotionEffectType.SLOW)))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerSpawnEvent(PlayerRespawnEvent e) {
		if(ent.isDead())
			return;
		
		if(e.getRespawnLocation().distance(ent.getLocation()) < 30.0) {
			Player player = e.getPlayer();
			new BukkitRunnable() {

				@Override
				public void run() {
					if(player.isDead())
						return;
					
					player.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, 60 * 20, 4));
					player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60 * 20, 4));
					player.sendMessage(ChatUtils.chat("&8Your respawn is close to a &4powerful foe... &8You feel weakened and vulnerable..."));
				}
				
			}.runTaskLater(plugin, 20);
		}
	}
	
	@Override
	protected void dealtDamage(EntityDamageByEntityEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void tookDamage(EntityDamageEvent e) {
		
	}
	
	@Override
	protected void tookDamage(EntityDamageByEntityEvent e) {
		
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
	protected void died(EntityDeathEvent e) {
		// TODO Auto-generated method stub
		
	}

	protected int getAbilityDelay() {
		return abilityDelay;
	}

	protected void setAbilityDelay(int abilityDelay) {
		this.abilityDelay = abilityDelay;
	}
	
	@Override
	public void resetCount() {
		this.count = 0;
	}
}
