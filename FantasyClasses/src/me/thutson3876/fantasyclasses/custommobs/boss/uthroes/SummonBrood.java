package me.thutson3876.fantasyclasses.custommobs.boss.uthroes;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.PolarBear;

import me.thutson3876.fantasyclasses.custommobs.boss.MobAbility;
import me.thutson3876.fantasyclasses.util.NoExpDrop;

public class SummonBrood implements MobAbility {

	private final int SUMMON_AMT = 4;
	
	@Override
	public String getName() {
		return "Summon Brood";
	}

	@Override
	public void run(Mob entity) {
		Location loc = entity.getLocation();
		World world = loc.getWorld();
		for(int i = 0; i < SUMMON_AMT; i++) {
			PolarBear bear = (PolarBear) world.spawnEntity(loc, EntityType.POLAR_BEAR);
			bear.setBaby();
			bear.setTarget(entity.getTarget());
			bear.setMetadata("noexpdrop", new NoExpDrop());
		}
		
		world.playSound(entity, Sound.ENTITY_POLAR_BEAR_AMBIENT, 4.0f, 1.1f);
	}

}
