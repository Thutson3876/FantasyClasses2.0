package me.thutson3876.fantasyclasses.professions.enchanter.customenchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.FantasyClasses;

public class Momentum extends Enchantment implements Listener {
	//Weapon - increase damage done based on velocity and if sprinting increase it by flat 20%
	
	private double dmgMod = 0.4;
	
	public Momentum(NamespacedKey key) {
		super(key);
		
		FantasyClasses.getPlugin().registerEvents(this);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onHit(EntityDamageByEntityEvent e) {
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;
		
		Player player = (Player) damager;
		
		ItemStack mainHand = player.getEquipment().getItemInMainHand();
		
		if(!mainHand.containsEnchantment(this))
			return;
		
		Entity victim = e.getEntity();
		if(!(victim instanceof LivingEntity))
			return;
		
		LivingEntity leVictim = (LivingEntity) victim;
		
		if(leVictim.isDead())
			return;
		
		double speed = 0;
		if (player.getVelocity().length() != 0) {
			speed = player.getVelocity().length();
		}
		else if (player.isSprinting()) {
			speed = 0.8;
		} else
			return;

		e.setDamage(e.getDamage() * (1.0 + (speed * dmgMod)));
		
		World world = player.getWorld();
		Entity ent = e.getEntity();
		
		world.playSound(ent, Sound.BLOCK_ANCIENT_DEBRIS_BREAK, (float)(1.0 + speed * 2.0), 0.85f);
		world.spawnParticle(Particle.CRIT_MAGIC, ent.getLocation().add(0, ent.getHeight() / 2.0, 0), 4 + (int)(15 * speed));
	}
	
	@Override
	public boolean canEnchantItem(ItemStack item) {
		if(item == null)
			return false;
		
		Material type = item.getType();
		
		return type.name().contains("SWORD") || type.name().contains("AXE") || type.name().contains("TRIDENT") || type.name().contains("HOE");
	}

	@Override
	public boolean conflictsWith(Enchantment enchant) {
		if(enchant.equals(Enchantment.DAMAGE_ALL) || enchant.equals(Enchantment.DAMAGE_ARTHROPODS) || enchant.equals(Enchantment.DAMAGE_UNDEAD))
			return true;
		
		return Enchantments.CUSTOM.getEnchants().contains(enchant);
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.WEAPON;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public String getName() {
		return "Momentum";
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

	@Override
	public boolean isCursed() {
		return false;
	}

	@Override
	public boolean isTreasure() {
		return true;
	}
}
