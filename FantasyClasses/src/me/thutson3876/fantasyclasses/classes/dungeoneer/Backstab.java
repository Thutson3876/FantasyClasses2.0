package me.thutson3876.fantasyclasses.classes.dungeoneer;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class Backstab extends AbstractAbility {

	private double dmgMod = 0.05;
	private int duration = 30;
	private int amp = 0;

	private int maxDuration = 20 * 30;
	private int maxAmp = 9;

	private PotionEffect blindness;
	private PotionEffect weakness;
	//private PotionEffect unluck;

	public Backstab(Player p) {
		super(p, Priority.LOW);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 20;
		this.displayName = "Backstab";
		this.skillPointCost = 1;
		this.maximumLevel = 6;

		this.createItemStack(Material.NETHERITE_SWORD);
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		if (!e.getDamager().equals(player))
			return;
		
		Entity damaged = e.getEntity();
		Entity damager = e.getDamager();
		float damagedYaw = (damaged.getLocation().getYaw() >= 0.0F) ? damaged.getLocation().getYaw()
				: (180.0F + -damaged.getLocation().getYaw());
		float damagerYaw = (damager.getLocation().getYaw() >= 0.0F) ? damager.getLocation().getYaw()
				: (180.0F + -damager.getLocation().getYaw());
		float angle = (damagedYaw - damagerYaw >= 0.0F) ? (damagedYaw - damagerYaw) : (damagerYaw - damagedYaw);
		if (angle <= 50.0F) {
			AbilityTriggerEvent thisEvent = this.callEvent();
			e.setDamage(e.getDamage() * (1 + dmgMod));
			if (e.getEntity() instanceof LivingEntity) {
				applyPotionEffects((LivingEntity) damaged);
				if (e.getFinalDamage() >= ((LivingEntity) damaged).getHealth())
					dropPlayerHead(e.getEntity());
			}
			this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
		}
	}

	private void dropPlayerHead(Entity victim) {
		if (!(victim instanceof Player))
			return;
		Player p = (Player) victim;
		ItemStack head = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
		skullMeta.setOwningPlayer((OfflinePlayer) p);
		head.setItemMeta((ItemMeta) skullMeta);
		p.getWorld().dropItemNaturally(p.getLocation(), head);
	}

	private void applyPotionEffects(LivingEntity ent) {
		if (ent.isDead())
			return;

		AbilityUtils.applyStackingPotionEffect(blindness, ent, maxAmp, maxDuration);
		AbilityUtils.applyStackingPotionEffect(weakness, ent, maxAmp, maxDuration);
		//AbilityUtils.applyStackingPotionEffect(unluck, ent, maxAmp, maxDuration);
	}

	@Override
	public String getInstructions() {
		return "Attack a target from behind";
	}

	@Override
	public String getDescription() {
		return "Deal &6" + AbilityUtils.doubleRoundToXDecimals(dmgMod, 2)
				+ "% &rmore damage and apply stacking blindness and weakness effects when damaging a target from behind";
	}

	@Override
	public boolean getDealsDamage() {
		return true;
	}

	@Override
	public void applyLevelModifiers() {
		dmgMod = 0.05 * currentLevel;
		amp = currentLevel - 1;
		duration = 20 + 10 * currentLevel;

		blindness = new PotionEffect(PotionEffectType.BLINDNESS, duration, amp);
		weakness = new PotionEffect(PotionEffectType.WEAKNESS, duration, amp);
		//unluck = new PotionEffect(PotionEffectType.UNLUCK, duration, amp);
	}

}
