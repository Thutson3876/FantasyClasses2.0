package me.thutson3876.fantasyclasses.custommobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.custommobs.horde.Horde;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.ChatUtils;
import me.thutson3876.fantasyclasses.util.NoExpDrop;

public class Parasite extends AbstractCustomMob {

	private PotionEffect wither = new PotionEffect(PotionEffectType.WITHER, 4 * 20, 0);
	private final short minLightLevel = 8;
	private double dropChance = 0.2;
	
	public Parasite(Location loc) {
		super(loc);
		
		ent.setCustomName(ChatUtils.chat("&cParasite"));
		ent.setCustomNameVisible(true);
		
		Random rng = new Random();
		if(rng.nextDouble() < dropChance) {
			Collection<ItemStack> drops = new ArrayList<>();
			drops.add(new ItemStack(Material.WITHER_ROSE));
			this.setDrops(drops);
		}
		
		for(Horde horde : Horde.values()) {
			if(rng.nextDouble() < horde.getDropRate()) {
				drops.add(horde.generateDrop());
				break;
			}	
		}
			
	}

	@Override
	protected void applyDefaults() {
		this.setMaxHealth(16);
		this.setAttackDamage(8);
		this.setMoveSpeed(0.35f);
		this.setSkillExpReward(4);
	}

	@Override
	protected void tookDamage(EntityDamageByEntityEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void dealtDamage(EntityDamageByEntityEvent e) {
		if(e.getCause().equals(DamageCause.ENTITY_ATTACK)) {
			AbilityUtils.heal(ent, 2, ent);
			if(e.getEntity() instanceof LivingEntity) {
				((LivingEntity)e.getEntity()).addPotionEffect(wither);
			}
			
			if(ent.getLocation().getBlock().getLightLevel() <= minLightLevel) {
				(new Parasite(e.getEntity().getLocation())).getEntity().setMetadata("noexpdrop", new NoExpDrop());
			}
		}
		
	}

	@Override
	protected void died(EntityDeathEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void targeted(EntityTargetEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void healed(EntityRegainHealthEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMetadataTag() {
		return "parasite";
	}

	@Override
	protected void tookDamage(EntityDamageEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected EntityType getEntityType() {
		return EntityType.SILVERFISH;
	}
	
}
