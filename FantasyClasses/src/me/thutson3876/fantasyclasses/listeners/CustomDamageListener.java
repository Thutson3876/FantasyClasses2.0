package me.thutson3876.fantasyclasses.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.events.CustomLivingEntityDamageEvent;
import me.thutson3876.fantasyclasses.events.DamageModifier;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;
import me.thutson3876.fantasyclasses.util.chat.ColorCode;

public class CustomDamageListener implements Listener {

	private static final FantasyClasses plugin = FantasyClasses.getPlugin();
	
	private static final double BAD_LUCK_MOD = 0.1;

	public CustomDamageListener() {
		plugin.registerEvents(this);
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if(event.getEntity() instanceof LivingEntity) {
			LivingEntity ent = (LivingEntity)event.getEntity();
			
			if(ent.hasPotionEffect(PotionEffectType.UNLUCK)) {
				double dmgMod = 1 + (BAD_LUCK_MOD * (ent.getPotionEffect(PotionEffectType.UNLUCK).getAmplifier() + 1));
				event.setDamage(event.getDamage() * dmgMod);
			}
			
			CustomLivingEntityDamageEvent customEvent = null;
			Player damagerPlayer = null;
			Player victimPlayer = null;
			if(event instanceof EntityDamageByEntityEvent) {
				customEvent = new CustomLivingEntityDamageEvent(ent, ((EntityDamageByEntityEvent)event).getDamager(), event.getDamage(), event.getCause());
				
				Entity damager = AbilityUtils.getTrueCause(customEvent.getDamager());
				if(damager instanceof Player) 
					damagerPlayer = (Player)damager;
				else if(customEvent.getVictim() instanceof Player)
					victimPlayer = (Player)customEvent.getVictim();
			}
			else
				customEvent = new CustomLivingEntityDamageEvent(ent, null, event.getDamage(), event.getCause());
			
			
			customEvent.setCancelled(event.isCancelled());
			
			Bukkit.getPluginManager().callEvent(customEvent);
			
			if(!customEvent.isCancelled() && customEvent.doesIgnoreArmor()) {
				event.setCancelled(true);
				
				if(customEvent.getDamager() != null) {
					ent.damage(customEvent.getFinalModifiedDamage(), customEvent.getDamager());
				}
				else {
					ent.damage(customEvent.getFinalModifiedDamage());
				}
			}
			else {
				event.setDamage(customEvent.getFinalModifiedDamage());
			}
			
			if(damagerPlayer != null) {
				dealtDamage(damagerPlayer, customEvent);
			}
			
			if(victimPlayer != null) {
				tookDamage(victimPlayer, customEvent);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		Entity damager = AbilityUtils.getTrueCause(e.getDamager());
		Entity victim = e.getEntity();
		
		if(damager instanceof Player) {
			FantasyPlayer fplayer = plugin.getPlayerManager().getPlayer((Player) damager);

			if(fplayer != null) {
				if(!fplayer.hasFriendlyFire()) {
					if(victim instanceof Player) {
						e.setCancelled(true);
					}
				}
				
				if(!e.isCancelled()) {
					if(fplayer.hasDamageMeters()) {
						fplayer.getPlayer().sendMessage(ChatUtils.chat("&7You dealt &e" + AbilityUtils.doubleRoundToXDecimals(e.getFinalDamage(), 2) + " &7damage"));
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityExplodeEvent(EntityExplodeEvent e) {
		Entity ent = e.getEntity();
		if(ent.hasMetadata("noexplodeblocks")) {
			e.blockList().clear();
		}
	}
	
	private void dealtDamage(Player p, CustomLivingEntityDamageEvent customEvent) {
		FantasyPlayer fplayer = FantasyClasses.getPlugin().getPlayerManager().getPlayer(p);
		if(fplayer != null && fplayer.hasDamageDetailedMeters()) {
			double initialDamage = customEvent.getInitialDamage();
			double finalDamage = customEvent.getFinalModifiedDamage();
			
			if(finalDamage == initialDamage) {
				fplayer.getPlayer().sendMessage(ChatUtils.chat("&7You dealt &e" + AbilityUtils.doubleRoundToXDecimals(initialDamage, 2) + " &7damage"));
				return;
			}
			p.sendMessage(ChatUtils.chat("&9Initial Damage Dealt: &6" + AbilityUtils.doubleRoundToXDecimals(initialDamage, 2)));
			List<DamageModifier> additiveMods = customEvent.getAdditiveModifiers();
			if(!additiveMods.isEmpty()) {
				p.sendMessage(ChatUtils.chat("&0-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
				p.sendMessage(ChatUtils.chat("&eAdditive Modifiers: "));
				for(DamageModifier dmgMod : additiveMods) {
					String modName = dmgMod.getName();
					double modValue = dmgMod.getValue();
					ColorCode code = modValue < 0 ? ColorCode.RED : ColorCode.GREEN;
					p.sendMessage(ChatUtils.chat("&e" + modName + ": "+ code + AbilityUtils.doubleRoundToXDecimals(modValue, 2)));
				}
			}
			List<DamageModifier> multiplicativeMods = customEvent.getMultiplicativeModifiers();
			if(!multiplicativeMods.isEmpty()) {
				p.sendMessage(ChatUtils.chat("&0-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
				p.sendMessage(ChatUtils.chat("&bMultiplicative Modifiers: "));
				for(DamageModifier dmgMod : multiplicativeMods) {
					String modName = dmgMod.getName();
					double modValue = dmgMod.getValue();
					ColorCode code = modValue < 0 ? ColorCode.RED : ColorCode.GREEN;
					p.sendMessage(ChatUtils.chat("&b" + modName + ": "+ code + AbilityUtils.doubleRoundToXDecimals(modValue * 100D, 1) + "%"));
				}
			}
			p.sendMessage(ChatUtils.chat("&0-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
			p.sendMessage(ChatUtils.chat("&9Modified Damage Dealt: &6" + AbilityUtils.doubleRoundToXDecimals(finalDamage, 2)));
		}
	}
	
	private void tookDamage(Player p, CustomLivingEntityDamageEvent customEvent) {
		FantasyPlayer fplayer = FantasyClasses.getPlugin().getPlayerManager().getPlayer(p);
		if(fplayer != null && fplayer.hasDamageDetailedMeters()) {
			double initialDamage = customEvent.getInitialDamage();
			double finalDamage = customEvent.getFinalModifiedDamage();
			
			if(finalDamage == initialDamage) {
				fplayer.getPlayer().sendMessage(ChatUtils.chat("&7You took &c" + AbilityUtils.doubleRoundToXDecimals(initialDamage, 2) + " &7damage"));
				return;
			}
			p.sendMessage(ChatUtils.chat("&9Initial Damage Taken: &4" + AbilityUtils.doubleRoundToXDecimals(initialDamage, 2)));
			List<DamageModifier> additiveMods = customEvent.getAdditiveModifiers();
			if(!additiveMods.isEmpty()) {
				p.sendMessage(ChatUtils.chat("&0-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
				p.sendMessage(ChatUtils.chat("&eAdditive Modifiers: "));
				for(DamageModifier dmgMod : additiveMods) {
					String modName = dmgMod.getName();
					double modValue = dmgMod.getValue();
					ColorCode code = modValue < 0 ? ColorCode.RED : ColorCode.GREEN;
					p.sendMessage(ChatUtils.chat("&e" + modName + ": "+ code + AbilityUtils.doubleRoundToXDecimals(modValue, 2)));
				}
			}
			List<DamageModifier> multiplicativeMods = customEvent.getMultiplicativeModifiers();
			if(!multiplicativeMods.isEmpty()) {
				p.sendMessage(ChatUtils.chat("&0-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
				p.sendMessage(ChatUtils.chat("&bMultiplicative Modifiers: "));
				for(DamageModifier dmgMod : multiplicativeMods) {
					String modName = dmgMod.getName();
					double modValue = dmgMod.getValue();
					ColorCode code = modValue < 0 ? ColorCode.RED : ColorCode.GREEN;
					p.sendMessage(ChatUtils.chat("&b" + modName + ": "+ code + AbilityUtils.doubleRoundToXDecimals(modValue * 100D, 1) + "%"));
				}
			}
			p.sendMessage(ChatUtils.chat("&0-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
			p.sendMessage(ChatUtils.chat("&9Modified Damage Taken: &4" + AbilityUtils.doubleRoundToXDecimals(finalDamage, 2)));
		}
	}
	
	//Unfinished healing meters || finish if healing becomes a consistent part of plugin again
	/*@EventHandler (priority = EventPriority.HIGHEST)
	public void onHealEvent(HealEvent e) {
		Player p = null;
		if(e.getHealer() instanceof Player) 
			p = (Player)e.getHealer();
		else if(e.getTarget() instanceof Player)
			p = (Player)e.getTarget();
		
		FantasyPlayer fplayer = FantasyClasses.getPlugin().getPlayerManager().getPlayer(p);
		if(fplayer != null && fplayer.hasDamageDetailedMeters()) {
			p.sendMessage(ChatUtils.chat("&9Initial Damage: &6" + AbilityUtils.doubleRoundToXDecimals(customEvent.getInitialDamage(), 2)));
			p.sendMessage(ChatUtils.chat("&0-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
			p.sendMessage(ChatUtils.chat("&aAdditive Modifiers: "));
			for(DamageModifier dmgMod : customEvent.getAdditiveModifiers()) {
				p.sendMessage(ChatUtils.chat("&a" + dmgMod.getName() + ": &6" + AbilityUtils.doubleRoundToXDecimals(dmgMod.getValue(), 3)));
			}
			p.sendMessage(ChatUtils.chat("&0-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
			p.sendMessage(ChatUtils.chat("&bMultiplicative Modifiers: "));
			for(DamageModifier dmgMod : customEvent.getMultiplicativeModifiers()) {
				p.sendMessage(ChatUtils.chat("&b" + dmgMod.getName() + ": &6" + AbilityUtils.doubleRoundToXDecimals(dmgMod.getValue(), 3)));
			}
			p.sendMessage(ChatUtils.chat("&0-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
			p.sendMessage(ChatUtils.chat("&eModified Damage: &6" + AbilityUtils.doubleRoundToXDecimals(customEvent.getFinalModifiedDamage(), 2)));
		}
	}*/
}
