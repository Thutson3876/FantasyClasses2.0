package me.thutson3876.fantasyclasses.professions.miner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.collectible.Collectible;
import me.thutson3876.fantasyclasses.util.ChatUtils;

public class ArtifactCollector extends AbstractAbility {
static List<ItemStack> drops = new ArrayList<>();
	
	static {
		drops.add(new ItemStack(Material.END_STONE, 12));
		drops.add(new ItemStack(Material.PURPUR_BLOCK, 24));
		drops.add(new ItemStack(Material.END_STONE_BRICKS, 12));
		drops.add(new ItemStack(Material.QUARTZ, 36));
	}
	
	private double dropChance = 0.05;

	public ArtifactCollector(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Artifact Collector";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.SKELETON_SKULL);
	}

	@EventHandler
	public void onEntityDeathEvent(EntityDeathEvent e) {
		LivingEntity ent = e.getEntity();

		if (ent.getKiller() == null)
			return;

		if (!ent.getKiller().equals(player))
			return;

		if(ent.hasMetadata("noexpdrop"))
			return;
		
		if(!(ent instanceof Mob))
			return;
		
		this.onTrigger(rollDrops(ent.getLocation(), this.dropChance));
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e) {
		if(e.isCancelled())
			return;
		
		if (!e.getPlayer().equals(player))
			return;
		
		this.onTrigger(rollDrops(e.getBlock().getLocation(), this.dropChance / 5.0));
	}

	private boolean rollDrops(Location loc, double dropChance) {
		Random rng = new Random();
		
		if(rng.nextDouble() > dropChance)
			return false;
		
		World world = loc.getWorld();
		
		if(rng.nextBoolean())
			player.sendMessage(ChatUtils.chat(Collectible.MINING_SCHEMATICS.getRandomLore()));
		
		world.dropItemNaturally(loc, drops.get(rng.nextInt(drops.size())));
		world.playSound(loc, Sound.AMBIENT_CAVE, 0.8f, 0.8f);
		
		if(rng.nextDouble() < dropChance / 3.0) {
			world.dropItemNaturally(loc, new ItemStack(Material.WITHER_SKELETON_SKULL));
			world.playSound(loc, Sound.ENTITY_WITHER_AMBIENT, 5.0f, 1.2f);
		}
		
		return true;
	}
	
	@Override
	public String getInstructions() {
		return "Kill a mob";
	}

	@Override
	public String getDescription() {
		return "Mobs have a chance to drop something extra";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {		
	}
}
