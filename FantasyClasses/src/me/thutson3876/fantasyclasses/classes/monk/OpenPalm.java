package me.thutson3876.fantasyclasses.classes.monk;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;

public class OpenPalm extends AbstractAbility {

	private static double damageModPerLevel = 2.0;
	private double damageMod = damageModPerLevel;
	
	public OpenPalm(Player p) {
		super(p, Priority.HIGH);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Open Palm";
		this.skillPointCost = 1;
		this.maximumLevel = 3;
		
		this.createItemStack(Material.ACACIA_TRAPDOOR);
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(!e.getDamager().equals(this.player))
			return;
		
		if(!e.getCause().equals(DamageCause.ENTITY_ATTACK))
			return;
		
		if(player.getAttackCooldown() < 1.0)
			return;
		
		if (!(player.getInventory().getItemInMainHand() == null
				|| player.getInventory().getItemInMainHand().getType().equals(Material.AIR)  || player.getInventory().getItemInMainHand().getType().equals(Material.STICK)))
			return;
		
		AbilityTriggerEvent thisEvent = this.callEvent();
		e.setDamage(e.getDamage() + damageMod);
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}

	@Override
	public String getInstructions() {
		return "Attack with an empty hand or stick";
	}

	@Override
	public String getDescription() {
		return "Your monk attacks deal &6" + damageMod + "&r extra damage";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		damageMod = damageModPerLevel * currentLevel;
		
		if(fplayer == null || fplayer.getChosenClass() == null)
			return;
		
		AbstractFantasyClass clazz = this.getFantasyPlayer().getChosenClass();
		if (!(clazz instanceof Monk))
			return;

		Monk monk = (Monk) clazz;
		
		monk.setUnarmedDmgMod(damageMod);
	}
	
	@Override
	public void deInit() {
		if(fplayer == null || fplayer.getChosenClass() == null)
			return;
		
		AbstractFantasyClass clazz = this.getFantasyPlayer().getChosenClass();
		if (!(clazz instanceof Monk))
			return;

		Monk monk = (Monk) clazz;
		
		monk.setUnarmedDmgMod(1.0);
	}
}
