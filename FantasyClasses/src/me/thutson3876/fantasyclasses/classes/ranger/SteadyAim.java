package me.thutson3876.fantasyclasses.classes.ranger;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;

public class SteadyAim extends AbstractAbility {

	private boolean isDisabled = false;
	private boolean isDrawing = false;
	private int amp = 6;
	private PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, 7, 3);

	public SteadyAim(Player p) {
		super(p, Priority.LOW);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Steady Aim";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.SPYGLASS);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if (!player.equals(e.getPlayer()))
			return;

		if (e.getItem() != null && e.getItem().getType() == Material.BOW) {
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				/*AbilityTriggerEvent thisEvent = this.callEvent();

				if (thisEvent.isCancelled())
					return;
				*/

				isDrawing = true;
				
				//this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
			}
		}
	}

	@EventHandler
	public void onPlayerItemHeldEvent(PlayerItemHeldEvent e) {
		if (player.equals(e.getPlayer()) && isDrawing) {
			isDrawing = false;
		}
	}

	@EventHandler
	public void onEntityShootBowEvent(EntityShootBowEvent e) {
		if (player.equals(e.getEntity())) {
			isDrawing = false;
		}
	}

	@Override
	public String getInstructions() {
		return "Draw a bow while crouching";
	}

	@Override
	public String getDescription() {
		return "Allows you to zoom in to get a better view of your target";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void deInit() {
		this.isDisabled = true;
	}
	
	@Override
	protected void init() {
		new BukkitRunnable() {

			public void run() {
				if(isDisabled)
					this.cancel();
				
				if (player == null)
					deInit();

				if (player.isDead())
					return;

				if (player.isSneaking() && isDrawing) {
					player.addPotionEffect(slowness);
				}
			}

		}.runTaskTimer(plugin, 5, 5);
	}

	@Override
	public void applyLevelModifiers() {		
		this.amp = 6 * this.currentLevel - 1;
		this.slowness = new PotionEffect(PotionEffectType.SLOW, 7, amp);
	}

}
