package me.thutson3876.fantasyclasses.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.curseditems.CursedLegendary;

public class CursedItemsListener implements Listener {

	private static final FantasyClasses plugin = FantasyClasses.getPlugin();

	public CursedItemsListener() {
		plugin.registerEvents(this);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		if(!(e.getEntity().getShooter() instanceof Entity))
			return;
		
		LivingEntity shooter = (LivingEntity) e.getEntity().getShooter();
		
		checkWeapons(shooter, getEntityEquipment(shooter), e);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		EntityEquipment damagerEquip = getEntityEquipment(e.getDamager());
		EntityEquipment victimEquip = getEntityEquipment(e.getEntity());
		
		if(e.getDamager() instanceof LivingEntity)
			checkWeapons((LivingEntity) e.getDamager(), damagerEquip, e);
		
		if(e.getEntity() instanceof LivingEntity)
			checkArmor((LivingEntity) e.getEntity(), victimEquip, e);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamageEvent(EntityDamageEvent e) {
		if(e instanceof EntityDamageByEntityEvent)
			return;
		
		EntityEquipment victimEquip = getEntityEquipment(e.getEntity());
		
		if(e.getEntity() instanceof LivingEntity)
			checkArmor((LivingEntity) e.getEntity(), victimEquip, e);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onFishEvent(PlayerFishEvent e) {
		checkWeapons(e.getPlayer(), e.getPlayer().getEquipment(), e);
	}

	private static EntityEquipment getEntityEquipment(Entity ent) {
		EntityEquipment equip = null;

		if (ent instanceof LivingEntity) {
			equip = ((LivingEntity) ent).getEquipment();
		}

		return equip;
	}
	
	private static void checkWeapons(LivingEntity owner, EntityEquipment equip, Event e) {
		if(equip == null)
			return;
		
		ItemStack main = equip.getItemInMainHand();
		ItemStack off = equip.getItemInOffHand();

		if (CursedLegendary.isCursed(main)) {
			CursedLegendary.runAction(main, owner, e);
		}

		if (CursedLegendary.isCursed(off)) {
			CursedLegendary.runAction(off, owner, e);
		}
	}
	
	private static void checkArmor(LivingEntity owner, EntityEquipment equip, Event e) {
		if(equip == null)
			return;
		
		ItemStack helm = equip.getHelmet();
		ItemStack chest = equip.getChestplate();
		ItemStack legs = equip.getLeggings();
		ItemStack boots = equip.getBoots();

		if (CursedLegendary.isCursed(helm)) {
			CursedLegendary.runAction(helm, owner, e);
		}
		if (CursedLegendary.isCursed(chest)) {
			CursedLegendary.runAction(chest, owner, e);
		}
		if (CursedLegendary.isCursed(legs)) {
			CursedLegendary.runAction(legs, owner, e);
		}
		if (CursedLegendary.isCursed(boots)) {
			CursedLegendary.runAction(boots, owner, e);
		}
	}
	
}
