package me.thutson3876.fantasyclasses.custommobs.boss.voidremnant;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.collectible.Collectible;
import me.thutson3876.fantasyclasses.custommobs.boss.AbstractBoss;
import me.thutson3876.fantasyclasses.custommobs.horde.Horde;

public class VoidRemnant extends AbstractBoss {

	private static Collection<PotionEffect> onHitEffects = new HashSet<>();
	
	public VoidRemnant(Location loc) {
		super(loc, "&0Void Remnant");
		
		Collection<ItemStack> loot = new HashSet<>();
		
		loot.add(new ItemStack(Material.NETHER_STAR, 2));
		loot.add(new ItemStack(Material.WITHER_SKELETON_SKULL));
		loot.add(new ItemStack(Material.SHULKER_BOX));
		loot.add(new ItemStack(Material.DRAGON_BREATH, 2));
		for(int i = 0; i < 3; i++)
			loot.add(Collectible.generateProfessionResetDrop());
		
		Random rng = new Random();
		for(Horde horde : Horde.values()) {
			if(rng.nextDouble() < horde.getDropRate() * 2) {
				loot.add(horde.generateDrop());
				break;
			}	
		}
		
		this.setDrops(loot);
		
		onHitEffects.add(new PotionEffect(PotionEffectType.WITHER, 2 * 20, 1));
		onHitEffects.add(new PotionEffect(PotionEffectType.BLINDNESS, 2 * 20, 1));
		onHitEffects.add(new PotionEffect(PotionEffectType.SLOW, 2 * 20, 1));
		
		abilities.add(new Barrage());
		abilities.add(new SummonTar());
		abilities.add(new PlagueUnleashed());
		abilities.add(new WitheringRain());
		
		this.startAbilityTick();
	}
	
	@Override
	protected void applyDefaults() {
		//this.setMaxHealth(500);
		this.setAttackDamage(30);
		this.setSkillExpReward(120);
	}

	@Override
	protected void dealtDamage(EntityDamageByEntityEvent e) {
		Random rng = new Random();
		if(rng.nextDouble() > 0.33)
			return;
		
		if(e.getEntity() instanceof LivingEntity) {
			LivingEntity target = (LivingEntity) e.getEntity();
			
			target.addPotionEffects(onHitEffects);
		}
	}
	
	@Override
	protected void tookDamage(EntityDamageByEntityEvent e) {
		if(e.getCause().equals(DamageCause.ENTITY_EXPLOSION)) {
			e.setDamage(e.getDamage() * 2.0);
		}
	}
	
	@Override
	public String getMetadataTag() {
		return "void_remnant";
	}

	@Override
	protected EntityType getEntityType() {
		return EntityType.WITHER;
	}

}
