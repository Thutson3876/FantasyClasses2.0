package me.thutson3876.fantasyclasses.custommobs.boss.uthroes;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.custommobs.boss.MobAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class WinteryGrasp implements MobAbility {

	private final PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, 4 * 20, 3);
	private final int freezeAmt = 160;
	
	@Override
	public String getName() {
		return "Wintery Grasp";
	}

	@Override
	public void run(Mob entity) {
		entity.getWorld().playSound(entity, Sound.ENTITY_SNOW_GOLEM_AMBIENT, 5.0f, 0.8f);
		
		Player target = AbilityUtils.getFurthestPlayer(entity, 30);
		if(target == null)
			return;
		
		target.setVelocity(AbilityUtils.getVectorBetween2Points(target.getLocation(), entity.getLocation(), 0.25));

		if (target instanceof LivingEntity) {
			LivingEntity livingTarget = (LivingEntity) target;
			livingTarget.addPotionEffect(slowness);
			livingTarget.setFreezeTicks(livingTarget.getFreezeTicks() + freezeAmt);
			livingTarget.damage(16.0, entity);
		}

		entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_CONDUIT_AMBIENT, 10.0f, 0.8f);
		target.getWorld().playSound(target.getLocation(), Sound.ENTITY_FISHING_BOBBER_RETRIEVE, 10.0f, 0.5f);
		
	}

}
