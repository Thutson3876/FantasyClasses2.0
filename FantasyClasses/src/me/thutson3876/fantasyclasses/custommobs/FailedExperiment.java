package me.thutson3876.fantasyclasses.custommobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.collectible.Collectible;
import me.thutson3876.fantasyclasses.custommobs.horde.Horde;

public class FailedExperiment extends AbstractCustomMob {

	private static List<Material> dropMats = new ArrayList<>();
	
	static {
		dropMats.add(Material.TNT);
		dropMats.add(Material.FLINT_AND_STEEL);
	}
	
	public FailedExperiment(Location loc) {
		super(loc);
		
		((Zombie)ent).setBaby();
		
		Random rng = new Random();
		Collection<ItemStack> drops = new ArrayList<>();
		for(Material mat : dropMats) {
			ItemStack item = new ItemStack(mat, rng.nextInt(4));
			
			drops.add(item);
		}
		for(Horde horde : Horde.values()) {
			if(rng.nextDouble() < horde.getDropRate()) {
				drops.add(horde.generateDrop());
				break;
			}	
		}
		
		if(rng.nextDouble() < 0.33)
			drops.add(Collectible.generateClassResetDrop());
	}

	@Override
	protected void targeted(EntityTargetEvent e) {
		if(e.getTarget() == null)
			return;
		
		if(!e.getEntity().equals(ent))
			return;
		
		//Not spawning with TNT on head
		TNTPrimed tnt = (TNTPrimed) ent.getWorld().spawnEntity(ent.getEyeLocation(), EntityType.PRIMED_TNT);
		tnt.setTicksLived(5);
		tnt.setFuseTicks(4 * 20);
		tnt.setSource(ent);
		ent.addPassenger(tnt);
	}
	
	@Override
	protected void applyDefaults() {
		this.setMaxHealth(10);
		this.setAttackDamage(10);
		this.setMoveSpeed(0.3f);
		this.setSkillExpReward(1);
	}

	@Override
	protected void healed(EntityRegainHealthEvent e) {
		
	}

	@Override
	protected void tookDamage(EntityDamageByEntityEvent e) {
		
	}

	@Override
	protected void dealtDamage(EntityDamageByEntityEvent e) {
		
	}

	@Override
	protected void died(EntityDeathEvent e) {
		
	}

	@Override
	public String getMetadataTag() {
		return "failed_experiment";
	}

	@Override
	protected void tookDamage(EntityDamageEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected EntityType getEntityType() {
		return EntityType.ZOMBIE;
	}
	
}
