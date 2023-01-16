package me.thutson3876.fantasyclasses.custommobs.boss.voidremnant;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.custommobs.boss.MobAbility;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.ChatUtils;
import me.thutson3876.fantasyclasses.util.PotionList;

public class PlagueUnleashed implements MobAbility {

	private static PotionEffect effect = new PotionEffect(PotionEffectType.UNLUCK, 30 * 20, 1);
	private static int range = 20;
	
	@Override
	public String getName() {
		return "Plague Unleashed";
	}

	@Override
	public void run(Mob entity) {
		for(LivingEntity ent : AbilityUtils.getNearbyLivingEntities(entity, range, range, range)) {
			if(ent instanceof Player) {
				for(PotionEffect effect : ent.getActivePotionEffects()) {
					if(PotionList.BUFF.getPotList().contains(effect.getType()))
						ent.removePotionEffect(effect.getType());
				}
				
				AbilityUtils.applyStackingPotionEffect(effect, ent, 9, 60 * 20);
				
				((Player)ent).sendMessage(ChatUtils.chat("&4You begin to feel like your luck is running out..."));
				((Player)ent).playSound(ent, Sound.BLOCK_ANVIL_DESTROY, 0.8f, 0.5f);
			}
		}
	}

	
}
