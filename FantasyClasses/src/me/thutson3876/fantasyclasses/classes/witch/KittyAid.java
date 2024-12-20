package me.thutson3876.fantasyclasses.classes.witch;

import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Cat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.PotionList;

public class KittyAid extends AbstractAbility implements Bindable {

	private Material type = null;
	
	private int counter = 0;
	private int tickRate = 20;
	private int duration = 4 * 20;
	private double range = 6;
	
	public KittyAid(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 18 * 20;
		this.displayName = "Kitty Aid";
		this.skillPointCost = 1;
		this.maximumLevel = 3;

		this.createItemStack(Material.RABBIT_FOOT);	
	}

	@EventHandler
	public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {
		if(isOnCooldown())
			return;
		
		if(!e.getPlayer().equals(player))
			return;
		
		boolean correctType = false;
		
		if(e.getMainHandItem() != null) {
			if(e.getMainHandItem().getType().equals(type))
				correctType = true;
		}
		if(e.getOffHandItem() != null) {
			if(e.getOffHandItem().getType().equals(type))
				correctType = true;
		}
			
		if(!correctType)
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;
		
		spawnPretzel();
		
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	private void spawnPretzel() {
		Cat cat = (Cat) player.getWorld().spawnEntity(player.getLocation(), EntityType.CAT);
		cat.setCatType(Cat.Type.BLACK);
		cat.setInvulnerable(true);
		cat.setOwner(player);
		cat.setTamed(true);
		cat.setBreed(false);
		player.addPassenger(cat);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				if(cat == null || cat.isDead()) {
					counter = 0;
					this.cancel();
				}
				
				Random rng = new Random();
				
				List<PotionEffectType> types = PotionList.BUFF.getPotList();
				types.remove(PotionEffectType.NIGHT_VISION);
				types.remove(PotionEffectType.HERO_OF_THE_VILLAGE);
				PotionEffectType type = types.get(rng.nextInt(types.size()));
				PotionEffect effect = new PotionEffect(type, 10 * 20, rng.nextInt(2));
				
				cat.getWorld().playEffect(cat.getEyeLocation(), Effect.VILLAGER_PLANT_GROW, 1);
				
				for(LivingEntity ent : AbilityUtils.getNearbyPlayers(cat, range)) {
					if(ent instanceof Mob)
						continue;
					else if(ent == null || ent.isDead())
						continue;
					
					ent.addPotionEffect(effect);
					//AbilityUtils.heal(player, healAmt, ent);
					
				}
				
				if(counter > (duration / tickRate)) {
					counter = 0;
					cat.remove();
					this.cancel();
				}
				
				counter++;
			}
			
		}.runTaskTimer(plugin, tickRate, tickRate);
	}

	@Override
	public String getInstructions() {
		return "Swap hands with bound item type";
	}

	@Override
	public String getDescription() {
		return "Summon your a &6black cat &rthat applies random buffs to you and your allies for a short duration";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		duration = (1 + 2 * currentLevel) * 20;
	}

	@Override
	public Material getBoundType() {
		return type;
	}

	@Override
	public void setBoundType(Material type) {
		this.type = type;
	}

}
