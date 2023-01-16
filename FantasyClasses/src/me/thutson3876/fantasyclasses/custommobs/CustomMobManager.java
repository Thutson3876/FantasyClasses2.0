package me.thutson3876.fantasyclasses.custommobs;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import me.thutson3876.fantasyclasses.FantasyClasses;

public class CustomMobManager {

	private Set<AbstractCustomMob> mobs = new HashSet<>();
	
	public CustomMobManager() {
		
	}
	
	public void init() {
		for(World world : FantasyClasses.getPlugin().getServer().getWorlds()) {
			for(LivingEntity e : world.getLivingEntities()) {
				for(CustomMob customMob : CustomMob.values()) {
					if(e.getType().equals(customMob.getEntityType()) && e.hasMetadata(customMob.getMetadataTag())) {
						//mobs.add(customMob.newMob(e));
						Object obj = e.getMetadata(customMob.getMetadataTag());
						if(obj instanceof AbstractCustomMob) {
							mobs.add((AbstractCustomMob) obj);
							System.out.println(customMob.name() + " has been found");
						}
						
						break;
					}
				}
			}
		}
	}
	
	public void deInit() {
		for(AbstractCustomMob mob: mobs) {
			mob.deInit();
			
			mob.getEntity().remove();
			//mob.getEntity().setHealth(0.0);
		}
	}
	
	public void addMob(AbstractCustomMob mob) {
		mobs.add(mob);
	}
	
	public AbstractCustomMob removeMob(AbstractCustomMob mob) {
		mobs.remove(mob);
		mob.deInit();
		
		return mob;
	}

	public boolean contains(LivingEntity mob) {
		boolean hasMob = false;
		
		for(AbstractCustomMob m : this.mobs) {
			if(m.getEntity().equals(mob)) {
				hasMob = true;
				break;	
			}
		}
		
		return hasMob;
	}
	
	public int getExpDrop(LivingEntity mob) {
		int exp = 0;
		
		for(AbstractCustomMob m : this.mobs) {
			if(m.getEntity().equals(mob)) {
				exp = m.getSkillExpReward(); 
				break;
			}
		}
		
		return exp;
	}
	
}
