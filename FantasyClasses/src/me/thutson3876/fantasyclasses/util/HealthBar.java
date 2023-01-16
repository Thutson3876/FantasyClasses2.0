package me.thutson3876.fantasyclasses.util;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

public class HealthBar {
	
	private static final int HEALTHBAR_LIMIT = 30;
	
	public static void setHealthBar(LivingEntity ent) {
		String name = "&a";
		int health = (int) ent.getHealth() / 2;
		int displayedMaxHealth = (int) ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() / 2;

		if(displayedMaxHealth > HEALTHBAR_LIMIT) {
			health = (health / displayedMaxHealth) * HEALTHBAR_LIMIT;
			displayedMaxHealth = HEALTHBAR_LIMIT;
		}
		
		int i = 0;
		while(i < health) {
			name += "▮";
			i++;
		}
		if(health < displayedMaxHealth) {
			name += "&8";
			while(i < displayedMaxHealth) {
				name += "▮";
				i++;
			}
		}
		
		//name.replaceAll(" ","");
		ent.setCustomName(ChatUtils.chat(name));
		ent.setCustomNameVisible(true);
	}
}
