package me.thutson3876.fantasyclasses.professions.fisherman;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class RainDance extends AbstractAbility {

	private Material costType = Material.NAUTILUS_SHELL;
	private int cost = 1;

	public RainDance(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 3 * 20;
		this.displayName = "Rain Dance";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.LIGHTNING_ROD);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if (isOnCooldown())
			return;

		if (!e.getPlayer().equals(player))
			return;

		if (e.getClickedBlock() == null)
			return;

		if (!e.getClickedBlock().getType().equals(Material.LIGHTNING_ROD))
			return;

		if (!player.isSneaking())
			return;

		ItemStack item = player.getInventory().getItemInMainHand();
		if (item != null && item.getType().equals(this.costType)) {
			if (item.getAmount() >= cost) {
				item.setAmount(item.getAmount() - cost);
				player.getInventory().setItemInMainHand(item);

				World world = player.getWorld();
				boolean isClear = world.isClearWeather();
				if (isClear) {
					world.setStorm(true);
				} else {
					world.setClearWeatherDuration(coolDowninTicks);
				}

				world.strikeLightning(e.getClickedBlock().getLocation());

				this.onTrigger(true);
			}
		}

	}

	@Override
	public String getInstructions() {
		return "Right-click a lightning rod while crouching and holding a Nautilus Shell";
	}

	@Override
	public String getDescription() {
		return "Command the water cycle and control the weather. This ability toggles weather on or off depending on the current conditions";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
	}

}
