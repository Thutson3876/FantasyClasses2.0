package me.thutson3876.fantasyclasses.professions.fisherman;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class ForsakenAncestry extends AbstractAbility implements Bindable {

	private int duration = 8 * 20;
	private float prevWalkSpeed = 0.2f;
	private float swimSpeedBonus = 0.0f;

	private List<PotionEffect> effects = new ArrayList<>();

	private Material type = null;

	public ForsakenAncestry(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 20 * 20;
		this.displayName = "Forsaken Ancestry";
		this.skillPointCost = 2;
		this.maximumLevel = 2;

		this.createItemStack(Material.AXOLOTL_BUCKET);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if (isOnCooldown())
			return;

		if (!e.getPlayer().equals(player))
			return;

		if (e.getItem() == null || !e.getItem().getType().equals(this.type))
			return;

		player.addPotionEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, duration, 2));
		player.setWalkSpeed(prevWalkSpeed + swimSpeedBonus);
		Ability abil = fplayer.getClassAbility(SharedBlessings.class);
		if (abil != null) {
			double range = ((SharedBlessings) abil).getRange();
			for (LivingEntity l : AbilityUtils.getNearbyLivingEntities(player, range, range,
					range)) {
				if (l instanceof Mob || l.isDead())
					continue;

				l.addPotionEffects(effects);
			}
		}
		
		new BukkitRunnable() {
			public void run() {
				
				player.setWalkSpeed(prevWalkSpeed);
		
			}
		}.runTaskLater(plugin, duration);
		
		this.onTrigger(true);

	}

	@Override
	public String getInstructions() {
		return "Right-click bound item type";
	}

	@Override
	public String getDescription() {
		return "While in water, you feel more attuned to your surroundings. Gain conduit power and faster swim speed for &6" + this.duration/20 + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		this.duration = 8 * 20 * this.currentLevel;
		
		effects.add(new PotionEffect(PotionEffectType.CONDUIT_POWER, this.duration, 2));
	}
	
	public void setSwimSpeedBonus(float swimSpeedBonus) {
		this.swimSpeedBonus = swimSpeedBonus;
	}
	
	public float getSwimSpeedBonus() {
		return this.swimSpeedBonus;
	}

	@Override
	public Material getBoundType() {
		return this.type;
	}

	@Override
	public void setBoundType(Material type) {
		this.type = type;
	}

}
