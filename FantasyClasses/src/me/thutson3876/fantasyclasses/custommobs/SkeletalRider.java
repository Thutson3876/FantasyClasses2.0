package me.thutson3876.fantasyclasses.custommobs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.custommobs.boss.skeletonlord.SkeletonLord;

import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetEvent;

public class SkeletalRider extends AbstractCustomMob {

	final SkeletonHorse mount;

	public SkeletalRider(Location loc) {
		super(loc);

		mount = (SkeletonHorse) loc.getWorld().spawnEntity(loc, EntityType.SKELETON_HORSE);
		mount.addPassenger(ent);

		setGear();
	}

	@Override
	protected EntityType getEntityType() {
		return EntityType.SKELETON;
	}

	@Override
	public String getMetadataTag() {
		return "skeletal_rider";
	}

	@Override
	protected void targeted(EntityTargetEvent e) {
		if(e.getTarget() == null)
			return;
		
		if(SkeletonLord.getSkeletons().contains(e.getTarget().getType()))
			e.setCancelled(true);
	}

	@Override
	protected void healed(EntityRegainHealthEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void tookDamage(EntityDamageByEntityEvent e) {
		if(SkeletonLord.getSkeletonimmunities().contains(e.getCause()))
			e.setCancelled(true);
	}

	@Override
	protected void tookDamage(EntityDamageEvent e) {
		if(SkeletonLord.getSkeletonimmunities().contains(e.getCause()))
			e.setCancelled(true);
	}

	@Override
	protected void dealtDamage(EntityDamageByEntityEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void died(EntityDeathEvent e) {
		// TODO Auto-generated method stub

	}

	private void setGear() {
		EntityEquipment equip = ent.getEquipment();

		ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);
		ItemStack legs = new ItemStack(Material.CHAINMAIL_LEGGINGS);
		ItemStack chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		ItemStack helm = new ItemStack(Material.CHAINMAIL_HELMET);

		ItemStack bow = new ItemStack(Material.BOW);

		equip.setBoots(boots);
		equip.setLeggings(legs);
		equip.setChestplate(chest);
		equip.setHelmet(helm);

		equip.setItemInMainHand(bow);
	}
}
