package me.thutson3876.fantasyclasses.classes.seaguardian;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class ProtectorsDuty extends AbstractAbility {

	private static final List<EntityType> hostileAquatics = generateHostiles();

	private double dmgMod = 0.03;

	public ProtectorsDuty(Player p) {
		super(p, Priority.HIGH);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "A Protector's Duty";
		this.skillPointCost = 1;
		this.maximumLevel = 6;

		this.createItemStack(Material.PRISMARINE_SHARD);
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if (e.isCancelled())
			return;

		Entity damager = e.getDamager();
		if (!damager.equals(player)) {
			if (damager instanceof Projectile) {
				if (!(((Projectile) damager).getShooter() != null
						&& ((Projectile) damager).getShooter().equals(player))) {
					return;
				}
			} else
				return;
		}

		if (!hostileAquatics.contains(e.getEntityType()))
			return;

		e.setDamage(e.getDamage() * (1 + dmgMod));
		
		this.onTrigger(true);
	}

	@Override
	public String getInstructions() {
		return "Attack a hostile aquatic entity";
	}

	@Override
	public String getDescription() {
		return "You deal &6" + AbilityUtils.doubleRoundToXDecimals(dmgMod * 100, 1)
				+ "% &rmore damage to hostile aquatic entities";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		dmgMod = 0.03 * currentLevel;
	}

	private static List<EntityType> generateHostiles() {
		List<EntityType> hostiles = new ArrayList<>();

		hostiles.add(EntityType.DROWNED);
		hostiles.add(EntityType.GUARDIAN);
		hostiles.add(EntityType.ELDER_GUARDIAN);

		return hostiles;
	}
}
