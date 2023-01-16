package me.thutson3876.fantasyclasses.classes.dungeoneer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Shadowstep extends AbstractAbility implements Bindable {

	private Material boundType = null;
	private static Set<Material> passables = new HashSet<>();
	private static Set<Material> nonPassables;

	private int maxDistance = 20;

	static {
		passables.add(Material.AIR);
		passables.add(Material.WATER);
		passables.add(Material.LAVA);
		passables.add(Material.TALL_GRASS);
		passables.add(Material.GRASS);
		passables.add(Material.SEAGRASS);
		passables.add(Material.CAVE_VINES);
		passables.add(Material.VINE);
		passables.add(Material.TWISTING_VINES);
		passables.add(Material.WEEPING_VINES);
		passables.add(Material.GLOW_LICHEN);
		
		nonPassables = new HashSet<>();
		for(Material m : Material.values()) {
			if(passables.contains(m))
				continue;
			
			nonPassables.add(m);
		}
		
		
	}

	public Shadowstep(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 8 * 20;
		this.displayName = "Shadowstep";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.BLACK_DYE);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if (!e.getPlayer().equals(player))
			return;

		if (e.getItem() == null || !e.getItem().getType().equals(this.boundType))
			return;

		if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;

		if (isOnCooldown())
			return;

		LivingEntity target = null;
		target = AbilityUtils.rayTraceTarget(player, maxDistance);

		if (target != null) {
			AbilityTriggerEvent thisEvent = this.callEvent();
			omaewa(player, target);
			this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
		}
	}
	
	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		if (isOnCooldown())
			return;

		if (!e.getPlayer().equals(player))
			return;

		if (!e.getItemDrop().getItemStack().getType().equals(boundType))
			return;

		e.setCancelled(true);
		
		List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(passables, maxDistance);
		if (lastTwoTargetBlocks.size() != 2 || !lastTwoTargetBlocks.get(1).getType().isOccluding()) {
			player.playSound(player, Sound.ENTITY_ENDERMAN_HURT, 0.7f, 0.7f);
			return;
		}

		if (lastTwoTargetBlocks.get(0).getLightLevel() > 0) {
			player.playSound(player, Sound.ENTITY_ENDERMAN_HURT, 0.7f, 0.7f);
			return;
		}

		AbilityTriggerEvent thisEvent = this.callEvent();
		Location playerLoc = player.getLocation();
		float pitch = playerLoc.getPitch();
		float yaw = playerLoc.getYaw();
		Location teleportLocation = lastTwoTargetBlocks.get(0).getLocation();
		//player.sendMessage("Teleport location:" + teleportLocation.toString());
		Location newLoc = new Location(playerLoc.getWorld(), teleportLocation.getX(), teleportLocation.getY(), teleportLocation.getZ(), pitch, yaw);
		
		player.setFallDistance(0f);
		player.teleport(newLoc);
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.4f, 0.9f);
		player.getWorld().spawnParticle(Particle.REVERSE_PORTAL, player.getLocation(), 30);
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	public void omaewa(Player p, LivingEntity target) {
		Location targetLoc = target.getLocation();
		Location behindTarget = targetLoc.add(targetLoc.getDirection().multiply(-0.8D));
		p.teleport(behindTarget.add(new Location(target.getWorld(), 0.0D, 0.1D, 0.0D)));
	}

	@Override
	public String getInstructions() {
		return "Press your drop key while holding the bound item type and aiming at a block with light level 0. Can also use right-click to teleport behind your target";
	}

	@Override
	public String getDescription() {
		return "Teleport to a an area shrouded in darkness or behind your target";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
	}

	@Override
	public Material getBoundType() {
		return boundType;
	}

	@Override
	public void setBoundType(Material type) {
		this.boundType = type;
	}

}
