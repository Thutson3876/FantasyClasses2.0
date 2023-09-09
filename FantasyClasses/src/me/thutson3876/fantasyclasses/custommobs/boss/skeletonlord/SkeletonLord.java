package me.thutson3876.fantasyclasses.custommobs.boss.skeletonlord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.collectible.Collectible;
import me.thutson3876.fantasyclasses.custommobs.boss.AbstractBoss;
import me.thutson3876.fantasyclasses.custommobs.horde.Horde;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;

public class SkeletonLord extends AbstractBoss {

	private final SkeletonHorse mount;
	
	private Map<Entity, Integer> hitEntities = new HashMap<>();
	
	private static final List<EntityType> skeletons;
	
	private static final List<DamageCause> skeletonImmunities;
	
	private double lightningDmgMod = 0.25;
	
	static {
		List<DamageCause> list = new ArrayList<>();
		list.add(DamageCause.LIGHTNING);
		list.add(DamageCause.FIRE_TICK);
		list.add(DamageCause.FIRE);
		list.add(DamageCause.FALL);
		
		skeletonImmunities = list;
		
		List<EntityType> skeles = new ArrayList<>();
		skeles.add(EntityType.SKELETON);
		skeles.add(EntityType.STRAY);
		skeles.add(EntityType.SKELETON_HORSE);
		
		skeletons = skeles;
	}
	
	public SkeletonLord(Location loc) {
		super(loc, "&8Skeleton Lord");
		this.setBossBar("&8Skeleton Lord", BarColor.WHITE, BarStyle.SOLID, new BarFlag[0]);
		
		Random rng = new Random();
		int extraDrops = 64 - rng.nextInt(32);
		Collection<ItemStack> loot = new HashSet<>();
		
		loot.add(new ItemStack(Material.IRON_INGOT, extraDrops));
		loot.add(new ItemStack(Material.COPPER_INGOT, 64 - extraDrops));
		loot.add(new ItemStack(Material.LIGHTNING_ROD));
		loot.add(new ItemStack(Material.DRAGON_BREATH, 1));
		loot.add(new ItemStack(Material.SHULKER_BOX));
		for(int i = 0; i < 3; i++)
			loot.add(Collectible.generateProfessionResetDrop());
		
		for(Horde horde : Horde.values()) {
			if(rng.nextDouble() < horde.getDropRate() * 2) {
				loot.add(horde.generateDrop());
				break;
			}	
		}
		
		this.setDrops(loot);
		
		mount = (SkeletonHorse) loc.getWorld().spawnEntity(loc, EntityType.SKELETON_HORSE);
		mount.addPassenger(ent);
		AbilityUtils.setMaxHealth(mount, 300, Operation.ADD_NUMBER);
		setGear();
		
		abilities.add(new ChainLightning());
		abilities.add(new LightningVortex());
		abilities.add(new SummonRiders());
		//abilities.add(new PlagueUnleashed());
		
		this.startAbilityTick();
	}

	@Override
	protected void applyDefaults() {
		this.setMaxHealth(500);
		this.setAttackDamage(30);
		this.setSkillExpReward(120);
	}
	
	@Override
	protected EntityType getEntityType() {
		return EntityType.STRAY;
	}

	@Override
	public String getMetadataTag() {
		return "skeleton_lord";
	}

	private void setGear() {
		EntityEquipment equip = ent.getEquipment();
		
		ItemStack boots = new ItemStack(Material.IRON_BOOTS);
		boots.addEnchantment(Enchantment.BINDING_CURSE, 1);
		boots.addEnchantment(Enchantment.DURABILITY, 3);
		boots.addEnchantment(Enchantment.PROTECTION_FIRE, 4);
		boots = AbilityUtils.setDisplayName("Iron Boots of Conductivity", boots);
		ItemStack legs = new ItemStack(Material.IRON_LEGGINGS);
		legs.addEnchantment(Enchantment.BINDING_CURSE, 1);
		legs.addEnchantment(Enchantment.DURABILITY, 3);
		legs.addEnchantment(Enchantment.PROTECTION_FIRE, 4);
		legs = AbilityUtils.setDisplayName("Iron Leggings of Conductivity", legs);
		ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE);
		chest.addEnchantment(Enchantment.BINDING_CURSE, 1);
		chest.addEnchantment(Enchantment.DURABILITY, 3);
		chest.addEnchantment(Enchantment.PROTECTION_FIRE, 4);
		chest = AbilityUtils.setDisplayName("Iron Chestplate of Conductivity", chest);
		ItemStack helm = new ItemStack(Material.IRON_HELMET);
		helm.addEnchantment(Enchantment.BINDING_CURSE, 1);
		helm.addEnchantment(Enchantment.DURABILITY, 3);
		helm.addEnchantment(Enchantment.PROTECTION_FIRE, 4);
		helm = AbilityUtils.setDisplayName("Iron Helmet of Conductivity", helm);
		
		ItemStack bow = new ItemStack(Material.BOW);
		bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
		bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 5);
		
		equip.setBoots(boots);
		equip.setBootsDropChance(0.3f);
		equip.setLeggings(legs);
		equip.setBootsDropChance(0.3f);
		equip.setChestplate(chest);
		equip.setChestplateDropChance(0.3f);
		equip.setHelmet(helm);
		equip.setHelmetDropChance(0.3f);
		
		equip.setItemInMainHand(bow);
		equip.setItemInMainHandDropChance(0.4f);
	}
	
	private void addEntity(Entity e) {
		if(hitEntities.containsKey(e))
			hitEntities.put(e, hitEntities.get(e) + 1);
		else
			hitEntities.put(e, 1);
		
		if(e instanceof Player)
			((Player)e).sendMessage(ChatUtils.chat("&8You feel more... conductive. &6Lightning &8damage taken &4increased&8."));
	}
	
	@Override
	protected void targeted(EntityTargetEvent e) {
		if(e.getTarget() == null)
			return;
		
		if(skeletons.contains(e.getTarget().getType()))
				e.setCancelled(true);
	}
	
	public static List<EntityType> getSkeletons() {
		return skeletons;
	}

	public static List<DamageCause> getSkeletonimmunities() {
		return skeletonImmunities;
	}

	@Override
	protected void tookDamage(EntityDamageEvent e) {
		if(skeletonImmunities.contains(e.getCause()))
			e.setCancelled(true);
	}
	
	@Override
	protected void dealtDamage(EntityDamageByEntityEvent e) {
		if(e.getDamage() < 20.0)
			e.setDamage(20.0);
	}
	
	@Override
	protected void died(EntityDeathEvent e) {
		hitEntities.clear();
	}
	
	/*@EventHandler(priority = EventPriority.LOW)
	public void onProjectileHit(ProjectileHitEvent e) {
		if(e.isCancelled())
			return;
		
		if(e.getEntity().getShooter() != null && e.getEntity().getShooter().equals(ent)) {
			if(e.getHitEntity() != null)
				new BukkitRunnable() {

					@Override
					public void run() {
						if(e.getEntity().isDead())
							addEntity(e.getHitEntity());
					}
					
			}.runTaskLater(plugin, 1);
				
		}
	}
*/
	
	@EventHandler(priority = EventPriority.LOW)
	public void onDamageEvent(EntityDamageEvent e) {
		if(e.isCancelled())
			return;
		
		if(skeletons.contains(e.getEntity().getType()) && skeletonImmunities.contains(e.getCause())) {
			e.getEntity().setVisualFire(false);
			e.setCancelled(true);
			return;
		}
		
		if(skeletons.contains(e.getEntity().getType()))
			addEntity(e.getEntity());
		
		if(hitEntities.containsKey(e.getEntity())) {
			e.setDamage(lightningDmgMod * (hitEntities.get(e.getEntity()) + 1) * e.getDamage());
		}
		
		if(e.getEntity().equals(mount))
			e.setDamage(e.getDamage() / 10.0);
	}

}
