package me.thutson3876.fantasyclasses.custommobs.boss;

import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class TargetDummy extends AbstractBoss {

	int count = 0;
	int timer = 10;
	boolean tookDamageWithinTimer = false;
	BukkitTask task = null;
	
	public TargetDummy(Location loc) {
		super(loc, "&6Target Dummy");
		this.setBossBar("&6Target Dummy", BarColor.GREEN, BarStyle.SOLID, new BarFlag[0]);
		
		((Slime)ent).setSize(3);
		
		task = new BukkitRunnable() {

			@Override
			public void run() {
				if(ent.isDead())
					cancel();
				
				if(count >= timer) {
					AbilityUtils.heal(ent, 500, ent);
					count = 0;
				}
				count++;
			}
			
		}.runTaskTimer(plugin, 20, 20);
	}
	
	@Override
	public void deInit() {
		if(task != null) {
			if(!task.isCancelled())
				task.cancel();
		}
	}
	
	@Override
	protected void applyDefaults() {
		this.setMaxHealth(500);
		this.setAttackDamage(0);
		this.setMoveSpeed(0.0f);
		this.setSkillExpReward(0);
	}

	@Override
	protected void tookDamage(EntityDamageByEntityEvent e) {
		count = 0;
	}
	
	@Override
	protected void targeted(EntityTargetEvent e) {
		e.setCancelled(true);
	}
	
	@Override
	public String getMetadataTag() {
		return "target_dummy";
	}

	@Override
	protected EntityType getEntityType() {
		return EntityType.SLIME;
	}
	
}
