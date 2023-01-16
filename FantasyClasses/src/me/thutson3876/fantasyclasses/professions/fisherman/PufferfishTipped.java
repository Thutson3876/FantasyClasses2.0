package me.thutson3876.fantasyclasses.professions.fisherman;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class PufferfishTipped extends AbstractAbility {

	private int duration = 6 * 20;
	private int amp = 1;
	private double dmg = 2.0;
	
	private PotionEffect poison = new PotionEffect(PotionEffectType.POISON, duration, amp);
	private PotionEffect hunger = new PotionEffect(PotionEffectType.HUNGER, duration, amp);
	private PotionEffect nausea = new PotionEffect(PotionEffectType.CONFUSION, duration, amp);
	
	public PufferfishTipped(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 6 * 20;
		this.displayName = "Pufferfish Tipped";
		this.skillPointCost = 1;
		this.maximumLevel = 2;
		
		this.createItemStack(Material.PUFFERFISH);
	}

	@EventHandler
	public void onPlayerFishEvent(PlayerFishEvent e) {
		if(isOnCooldown())
			return;
		
		if(!e.getPlayer().equals(player))
			return;
		
		if(!(e.getCaught() instanceof LivingEntity))
			return;
		
		if(!e.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY))
			return;
		
		LivingEntity ent = (LivingEntity)e.getCaught();
		
		if(ent == null || ent.isDead())
			return;
		
		
		ent.addPotionEffect(poison);
		ent.addPotionEffect(hunger);
		ent.addPotionEffect(nausea);
		ent.damage(dmg);
		
		this.onTrigger(true);
	}

	@Override
	public String getInstructions() {
		return "Fish something";
	}

	@Override
	public String getDescription() {
		return "When you hook an entity, deal &6" + dmg + " &rdamage and apply poison, hunger, and nausea all at level &6" + (amp + 1);
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		dmg = 4.0 * currentLevel;
		amp = currentLevel;
		poison = new PotionEffect(PotionEffectType.POISON, duration, amp);
		hunger = new PotionEffect(PotionEffectType.HUNGER, duration, amp);
		nausea = new PotionEffect(PotionEffectType.CONFUSION, duration, amp);
	}

}
