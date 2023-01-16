package me.thutson3876.fantasyclasses.util;

import java.util.List;

import org.bukkit.entity.EntityType;

public enum EntityTypeCategory {

	BASIC, NETHER, END, ILLAGER, EASY, MEDIUM, HARD, VERY_HARD, MINI_BOSS, BOSS;
	
	private List<EntityType> types;
	private int expReward = 0;
	
	static {
		
	}
	
	public List<EntityType> getTypes() {
		return types;
	}
	
	public int getExpDrop() {
		return expReward;
	}
}
