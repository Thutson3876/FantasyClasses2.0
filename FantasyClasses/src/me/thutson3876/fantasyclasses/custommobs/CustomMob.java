package me.thutson3876.fantasyclasses.custommobs;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public enum CustomMob {

	LOST_GUARDIAN(EntityType.GUARDIAN, LostGuardian.class), PARASITE(EntityType.SILVERFISH, Parasite.class),
	UNDEAD_MINER(EntityType.WITHER_SKELETON, UndeadMiner.class), DROWNED_MINER(EntityType.DROWNED, DrownedMiner.class), 
	FAILED_EXPERIMENT(EntityType.ZOMBIE, FailedExperiment.class), SKELETAL_RIDER(EntityType.SKELETON, SkeletalRider.class);

	private EntityType type = null;
	private Class<? extends AbstractCustomMob> clazz;

	private CustomMob(EntityType type, Class<? extends AbstractCustomMob> clazz) {
		this.type = type;
		this.clazz = clazz;
	}
	
	public String getMetadataTag() {
		return this.name().toLowerCase();
	}
	
	public EntityType getEntityType() {
		return type;
	}
	
	public AbstractCustomMob newMob(Location loc) {
		try {
			return clazz.getConstructor(Location.class).newInstance(loc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String getMetadataTag(EntityType type) {
		for(CustomMob mob : CustomMob.values()) {
			if(mob.getEntityType().equals(type))
				return mob.getMetadataTag();
		}
		
		return "";
	}
}
