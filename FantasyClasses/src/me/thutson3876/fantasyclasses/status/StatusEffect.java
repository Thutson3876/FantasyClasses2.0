package me.thutson3876.fantasyclasses.status;

import org.bukkit.entity.LivingEntity;

public interface StatusEffect {
	
	public void run(LivingEntity host, double duration, int stacks);
	
}
