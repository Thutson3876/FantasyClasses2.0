package me.thutson3876.fantasyclasses.custommobs.boss.descendent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.collectible.Collectible;
import me.thutson3876.fantasyclasses.custommobs.boss.AbstractBoss;
import me.thutson3876.fantasyclasses.custommobs.horde.Horde;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Descendant extends AbstractBoss {

	private double dmgModPerMinion = 0.1;
	
	public Descendant(Location loc) {
		super(loc, "&7Descendant");
		this.setBossBar("&7Descendant", BarColor.PURPLE, BarStyle.SOLID, new BarFlag[0]);
		
		Collection<ItemStack> loot = new HashSet<>();
		Random rng = new Random();
		int extraDrops = 12 - rng.nextInt(8);
		
		loot.add(new ItemStack(Material.ENDER_PEARL, rng.nextInt(7) + 6));
		loot.add(new ItemStack(Material.END_CRYSTAL, 1));
		loot.add(new ItemStack(Material.ENDER_CHEST, 1));
		loot.add(new ItemStack(Material.SHULKER_SHELL, extraDrops));
		loot.add(new ItemStack(Material.DIAMOND, 12 - extraDrops));
		loot.add(new ItemStack(Material.DRAGON_BREATH, 1));
		loot.add(new ItemStack(Material.SHULKER_BOX));
		loot.add(Collectible.generateProfessionResetDrop());
		
		for(Horde horde : Horde.values()) {
			if(rng.nextDouble() < horde.getDropRate() * 2) {
				loot.add(horde.generateDrop());
				break;
			}	
		}
		
		this.setDrops(loot);
		
		
	}
	
	@Override
	protected void applyDefaults() {
		this.setMaxHealth(400);
		this.setAttackDamage(35);
		this.setSkillExpReward(80);
	}
	
	//Damage increases based on how many minions are alive
	@Override
	protected void dealtDamage(EntityDamageByEntityEvent e) {
		e.setDamage(e.getDamage() * (1 + (dmgModPerMinion * this.minions.size())));
	}
	
	@Override
	protected void tookDamage(EntityDamageByEntityEvent e) {
		Entity damager = AbilityUtils.getTrueCause(e.getDamager());
		
		if(damager instanceof LivingEntity)
			ent.setTarget((LivingEntity)damager);
	}
	
	@Override
	protected void minionDied(EntityDeathEvent e) {
		this.ent.damage(AbilityUtils.getMaxHealth((Mob)e.getEntity()) * 2, ent);
	}
	
	@Override
	protected EntityType getEntityType() {
		return EntityType.ENDERMAN;
	}

	@Override
	public String getMetadataTag() {
		return "descendant";
	}

}
