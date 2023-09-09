package me.thutson3876.fantasyclasses.custommobs.horde;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.util.MaterialLists;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;

public abstract class AbstractHordeWave implements HordeWave, Listener {

	protected static final FantasyClasses plugin = FantasyClasses.getPlugin();
	
	protected List<Mob> horde = new ArrayList<>();
	
	private List<ItemStack> drops = new ArrayList<>();
	
	private final long maxTimeLimit;
	
	protected Location blockLoc;
	
	private long timer = 0;
	
	private BossBar bar;
	
	private AbstractHordeWave next = null;
	
	protected AbstractHordeWave(long maxTimeLimit) {
		this.maxTimeLimit = maxTimeLimit;
		
		plugin.registerEvents(this);
	}
	
	protected void setBossBar(Location loc, String title, BarColor color, BarStyle style, BarFlag... flags) {
		this.bar = Bukkit.createBossBar(ChatUtils.chat(title), color, style, flags);
		this.bar.setVisible(true);

		new BukkitRunnable() {

			@Override
			public void run() {
				if (timer > 0) {
					List<Player> barPlayers = new ArrayList<>();
					barPlayers.addAll(bar.getPlayers());
					List<Player> nearbyPlayers = new ArrayList<>();
					for (Player p : loc.getWorld().getPlayers()) {
						if (p.getLocation().distance(loc) <= 30) {
							nearbyPlayers.add(p);
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
					
					bar.setProgress(timer / maxTimeLimit);
				} else {
					List<Player> players = bar.getPlayers();
					for (Player player : players) {
						bar.removePlayer(player);
					}
					bar.setVisible(false);
					cancel();
				}
			}
		}.runTaskTimer(plugin, 1, 20);
	}
	
	public void onTimerEnd() {
		HandlerList.unregisterAll(this);
		
		if(blockLoc == null)
			return;
		
		if(this.horde.isEmpty()) {
			this.blockLoc.getWorld().playSound(blockLoc, Sound.BLOCK_BELL_RESONATE, 6.0f, 1.5f);
			
			if(next != null) {
				next.spawn(blockLoc);
				
				List<ItemStack> newDrops = next.getDrops();
				newDrops.addAll(drops);
				
				next.setDrops(newDrops);
			}
			else {
				World world = blockLoc.getWorld();
				
				for(ItemStack i : drops)
					world.dropItemNaturally(blockLoc, i);
			}
			
		}
		else {
			this.blockLoc.getWorld().playSound(blockLoc, Sound.BLOCK_BELL_RESONATE, 6.0f, 0.5f);
		}
		
	}
	
	protected void equipHorde(EntityEquipment equip) {
		for(Mob m : horde) {
			equipEntity(equip, m);
		}
	}
	
	protected void equipHordeRandomly() {
		for(Mob m : horde) {
			equipRandomEquipment(m);
		}
	}
	
	protected static void equipRandomEquipment(Mob m) {
		EntityEquipment equip = m.getEquipment();
		Random rng = new Random();
		
		equip.setHelmet(new ItemStack(MaterialLists.HELMET.getMaterials().get(rng.nextInt(MaterialLists.HELMET.getMaterials().size()))));
		equip.setChestplate(new ItemStack(MaterialLists.CHESTPLATE.getMaterials().get(rng.nextInt(MaterialLists.CHESTPLATE.getMaterials().size()))));
		equip.setLeggings(new ItemStack(MaterialLists.LEGGINGS.getMaterials().get(rng.nextInt(MaterialLists.LEGGINGS.getMaterials().size()))));
		equip.setBoots(new ItemStack(MaterialLists.BOOTS.getMaterials().get(rng.nextInt(MaterialLists.BOOTS.getMaterials().size()))));
	}
	
	protected static void equipEntity(EntityEquipment equip, Mob entity) {
		EntityEquipment currentEquip = entity.getEquipment();
		
		if(currentEquip.getArmorContents().length == equip.getArmorContents().length)
			currentEquip.setArmorContents(equip.getArmorContents());
		
		currentEquip.setItemInMainHand(equip.getItemInMainHand());
		currentEquip.setItemInOffHand(equip.getItemInOffHand());
	}
	
	protected static List<Mob> spawnEntities(List<Location> locations, boolean spawnWithArmor, EntityType... entityTypes) {
		Random rng = new Random();
		List<Mob> mobs = new ArrayList<>();
		
		int length = entityTypes.length;
		
		for(Location l : locations) {
			Entity e = l.getWorld().spawnEntity(l, entityTypes[rng.nextInt(length)], spawnWithArmor);
			
			if(e instanceof Mob) {
				if(spawnWithArmor)
					equipRandomEquipment((Mob)e);
				
				mobs.add((Mob)e);
			}
				
		}
		
		return mobs;
	}
	
	protected static List<Mob> spawnEntityStack(Mob base, int amt, EntityType... types) {
		List<Mob> mobs = new ArrayList<>();
		mobs.add(base);
		
		Random rng = new Random();
		World world = base.getWorld();
		Location loc = base.getEyeLocation();
		
		for(int i = 0; i < amt; i++) {
			Mob m = (Mob) world.spawnEntity(loc, types[rng.nextInt(types.length)], true);
			mobs.add(m);
			loc = m.getEyeLocation();
		}
		
		return mobs;
	}
	
	protected List<ItemStack> getDrops() {
		return drops;
	}
	
	protected void setDrops(List<ItemStack> newDrops) {
		this.drops = newDrops;
	}
	
	public void setNextWave(AbstractHordeWave wave) {
		this.next = wave;
	}
	
	abstract protected List<ItemStack> generateDrops();
	
	@EventHandler
	public void onEntityDeathEvent(EntityDeathEvent e) {
		if(this.horde.contains(e.getEntity())) {
			this.horde.remove(e.getEntity());
		}
	}
	
	@EventHandler
	public void onTargetEvent(EntityTargetEvent e) {
		if(horde.contains(e.getEntity()) && horde.contains(e.getTarget()))
			e.setCancelled(true);
	}
	
}
