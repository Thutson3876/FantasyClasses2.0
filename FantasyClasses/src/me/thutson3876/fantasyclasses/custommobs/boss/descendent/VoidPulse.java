package me.thutson3876.fantasyclasses.custommobs.boss.descendent;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.custommobs.boss.AbstractBoss;
import me.thutson3876.fantasyclasses.custommobs.boss.BossAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.geometry.Sphere;

public class VoidPulse extends BossAbility {

	//Deal 10 dmg + 5 times # of minions within 5 blocks of boss
	
	private double damage = 5.0;
	private double bonusDmg = 4.0;
	private int radius = 4;
	
	public VoidPulse(AbstractBoss boss) {
		super(boss);
	}

	@Override
	public String getName() {
		return "Void Pulse";
	}

	@Override
	public void run(Mob entity) {
		World world = entity.getWorld();
		
		for(Location loc : Sphere.generateCircle(entity.getEyeLocation().add(0, -0.5, 0), radius, true)) {
			world.playEffect(loc, Effect.ENDER_SIGNAL, 0);
		}
		world.playEffect(entity.getEyeLocation(), Effect.ENDERDRAGON_GROWL, 0);
		
		List<Player> nearbyPlayers = AbilityUtils.getNearbyPlayers(entity, radius);
		double dmg = calculateDamage();
		
		for(Player p : nearbyPlayers) {
			if(p.getLocation().distance(entity.getLocation()) < radius)
				p.damage(dmg, entity);
			
		}
	}

	private double calculateDamage() {
		return damage + (bonusDmg * boss.getMinions().size());
	}
}
