package me.thutson3876.fantasyclasses.classes.combat;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.MaterialLists;

public class Cripple extends AbstractAbility {

	private int durationInTicks = 1 * 20;
	private PotionEffect blindness;
	private PotionEffect slowness;
	
	public Cripple(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Cripple";
		this.skillPointCost = 1;
		this.maximumLevel = 4;
		this.blindness = new PotionEffect(PotionEffectType.BLINDNESS, durationInTicks, 0);
		this.slowness = new PotionEffect(PotionEffectType.SLOW, durationInTicks, 0);
		
		this.createItemStack(Material.BONE);
	}
	
	@EventHandler
	public void onDamageEvent(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		if(this.isOnCooldown())
			return;
		
		if(!e.getDamager().equals(this.player))
			return;

		if(player.getAttackCooldown() < 1.0)
			return;
		
		if(!(e.getEntity() instanceof LivingEntity))
			return;
		
		if(!e.getCause().equals(DamageCause.ENTITY_ATTACK))
			return;
		
		if(!(MaterialLists.HOE.getMaterials().contains(player.getInventory().getItemInMainHand().getType())))
			return;
		
		if(e.getFinalDamage() < 1.0)
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		
		LivingEntity target = (LivingEntity) e.getEntity();
		
		target.addPotionEffect(blindness);
		if(currentLevel > 1)
			target.addPotionEffect(slowness);
		
		player.playSound(player, Sound.BLOCK_CONDUIT_AMBIENT, 5.0f, 0.85f);
		
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}
	
	

	@Override
	public String getInstructions() {
		return "Attack an entity with a scythe";
	}

	@Override
	public String getDescription() {
		return "Applies blindness for " + this.durationInTicks / 20 + " seconds. Also applies slowness at level 3";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		if(currentLevel == 1) {
			blindness = new PotionEffect(PotionEffectType.BLINDNESS, durationInTicks, 0);
		}
		else if(currentLevel == 3) {
			slowness = new PotionEffect(PotionEffectType.SLOW, durationInTicks, 0);
		}
		
		this.durationInTicks = (currentLevel) * 20;
	}

}
