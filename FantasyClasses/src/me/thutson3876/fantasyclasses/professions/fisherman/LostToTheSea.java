package me.thutson3876.fantasyclasses.professions.fisherman;

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
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.collectible.Collectible;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;

public class LostToTheSea extends AbstractAbility {

static List<ItemStack> drops = new ArrayList<>();
	
	static {
		drops.add(new ItemStack(Material.PRISMARINE, 36));
		drops.add(new ItemStack(Material.PRISMARINE_BRICKS, 8));
		drops.add(new ItemStack(Material.DARK_PRISMARINE, 8));
		drops.add(new ItemStack(Material.SEA_LANTERN, 3));
		drops.add(new ItemStack(Material.PRISMARINE_CRYSTALS, 24));
	}
	
	private double dropChance = 0.1;
	
	public LostToTheSea(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Lost To The Sea";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.DARK_OAK_BOAT);
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
		
		this.onTrigger(rollDrops(ent.getLocation(), dropChance));
	}
	
	@EventHandler
	public void onPlayerFishEvent(PlayerFishEvent e) {
		if (isOnCooldown())
			return;

		if(e.isCancelled())
			return;
		
		if (!e.getPlayer().equals(player))
			return;
		
		if(!e.getState().equals(State.CAUGHT_FISH))
			return;
		
		this.onTrigger(rollDrops(e.getHook().getLocation(), dropChance / 5));
	}
	
	private boolean rollDrops(Location loc, double dropChance) {
		Random rng = new Random();
		
		if(rng.nextDouble() > dropChance)
			return false;
		
		World world = loc.getWorld();
		if(rng.nextBoolean())
			player.sendMessage(ChatUtils.chat(Collectible.ETCHED_GLASS.getRandomLore()));
		
		world.dropItemNaturally(loc, drops.get(rng.nextInt(drops.size())));
		world.playSound(loc, Sound.AMBIENT_CAVE, 0.8f, 0.8f);
		
		if(rng.nextDouble() < dropChance / 3.0) {
			world.dropItemNaturally(loc, new ItemStack(Material.TRIDENT));
			world.playSound(loc, Sound.ITEM_TRIDENT_HIT, 5.0f, 1.2f);
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
