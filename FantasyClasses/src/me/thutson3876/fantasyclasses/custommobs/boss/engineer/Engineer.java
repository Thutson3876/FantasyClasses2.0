package me.thutson3876.fantasyclasses.custommobs.boss.engineer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.collectible.Collectible;
import me.thutson3876.fantasyclasses.custommobs.boss.AbstractBoss;
import me.thutson3876.fantasyclasses.custommobs.horde.Horde;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Engineer extends AbstractBoss {

	public Engineer(Location loc) {
		super(loc, "&4Engineer");
		this.setBossBar("&4Engineer", BarColor.RED, BarStyle.SOLID, new BarFlag[0]);
		
		((Zombie)ent).setAdult();
		
		Collection<ItemStack> loot = new HashSet<>();
		Random rng = new Random();
		int extraDrops = 12 - rng.nextInt(8);
		
		loot.add(new ItemStack(Material.TNT, rng.nextInt(7) + 6));
		loot.add(new ItemStack(Material.IRON_INGOT, 2 * (rng.nextInt(6) + 6)));
		loot.add(new ItemStack(Material.NETHERITE_SCRAP, extraDrops));
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
		
		this.setGear();
		
		abilities.add(new Bombardment());
		abilities.add(new SummonStudents());
		//abilities.add(new FireworkLauncher());
		//abilities.add(new PlagueUnleashed());
		
		this.startAbilityTick();
	}
	
	@Override
	protected void applyDefaults() {
		this.setMaxHealth(400);
		this.setAttackDamage(35);
		this.setSkillExpReward(80);
	}
	
	@Override
	protected void tookDamage(EntityDamageEvent e) {
		if(e.getEntity().equals(ent) && e.getCause().equals(DamageCause.BLOCK_EXPLOSION)) {
			e.setCancelled(true);
		}
	}
	
	@Override
	protected void tookDamage(EntityDamageByEntityEvent e) {
		if(e.getEntity().equals(ent) && e.getCause().equals(DamageCause.ENTITY_EXPLOSION)) {
			e.setCancelled(true);
		}
	}

	@Override
	public String getMetadataTag() {
		return "engineer";
	}
	
	private void setGear() {
		EntityEquipment equip = ent.getEquipment();
		
		ItemStack boots = new ItemStack(Material.IRON_BOOTS);
		boots.addEnchantment(Enchantment.BINDING_CURSE, 1);
		boots.addEnchantment(Enchantment.DURABILITY, 3);
		boots.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4);
		boots = AbilityUtils.setDisplayName("&4Iron Boots of Blastproofing", boots);
		ItemStack legs = new ItemStack(Material.IRON_LEGGINGS);
		legs.addEnchantment(Enchantment.BINDING_CURSE, 1);
		legs.addEnchantment(Enchantment.DURABILITY, 3);
		legs.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4);
		legs = AbilityUtils.setDisplayName("&4Iron Leggings of Blastproofing", legs);
		ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE);
		chest.addEnchantment(Enchantment.BINDING_CURSE, 1);
		chest.addEnchantment(Enchantment.DURABILITY, 3);
		chest.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4);
		chest = AbilityUtils.setDisplayName("&4Iron Chestplate of Blastproofing", chest);
		ItemStack helm = new ItemStack(Material.IRON_HELMET);
		helm.addEnchantment(Enchantment.BINDING_CURSE, 1);
		helm.addEnchantment(Enchantment.DURABILITY, 3);
		helm.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4);
		helm = AbilityUtils.setDisplayName("&4Iron Helmet of Blastproofing", helm);
		
		equip.setBoots(boots);
		equip.setBootsDropChance(0.25f);
		equip.setLeggings(legs);
		equip.setBootsDropChance(0.25f);
		equip.setChestplate(chest);
		equip.setChestplateDropChance(0.25f);
		equip.setHelmet(helm);
		equip.setHelmetDropChance(0.25f);
		
		equip.setItemInMainHand(new ItemStack(Material.GOLDEN_PICKAXE));
		equip.setItemInOffHand(new ItemStack(Material.TNT));
	}

	@Override
	protected EntityType getEntityType() {
		return EntityType.ZOMBIE;
	}

}
