package me.thutson3876.fantasyclasses.abilities;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.abilities.skills.Skill;
import me.thutson3876.fantasyclasses.classes.AbstractFantasyClass;
import me.thutson3876.fantasyclasses.cooldowns.CooldownContainer;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;

@SerializableAs("Ability")
public interface Ability extends ConfigurationSerializable {

	void setDefaults();

	//<T extends Event> boolean trigger(T event);

	int getCooldown();
	
	void triggerCooldown(double cooldown, double cooldownReductionPerTick);

	CooldownContainer getCooldownContainer();
	
	Player getPlayer();

	ItemStack getItemStack();

	void setPlayer(Player p);
	
	void setFantasyPlayer(FantasyPlayer p);

	String getName();

	String getInstructions();

	String getDescription();

	String getCommandName();
	
	void setPrerequisite(String preq);
	
	String getPrerequisite();

	boolean getDealsDamage();
	
	Priority getPriority();

	boolean isEnabled();

	void enable();
	
	void disable();
	
	void resetLevel();

	int getCurrentLevel();
	
	int getMaxLevel();

	void setLevel(int level);

	void levelUp();

	int getSkillPointCost();

	void applyLevelModifiers();
	
	void triggerCooldown();

	void writeToConfig(ConfigurationSection section);
	
	static Ability clone(Ability ability) {
		Ability clone = null;
		try {
			Class<?> clazz = Class.forName(ability.getClass().getName());
			Constructor<?> ctor = clazz.getConstructor(Player.class);
			Object object = ctor.newInstance(ability.getPlayer());
			
			clone = (Ability) object;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return clone;
	}
	
	@Override
	Map<String, Object> serialize();

	static Ability deSerialize(Map<String, Object> map, FantasyPlayer p, boolean isProfession) {
		String name = null;
		int level = -1;
		String type = null;
		
		if (map.containsKey("name")) {
			name = (String) map.get("name");
		}
		if (map.containsKey("level")) {
			level = ((Integer) map.get("level")).intValue();
		}
		if (map.containsKey("type")) {
			type = ((String) map.get("type"));
		}
		
		if (name == null || level < 0) {
			return null;
		}

		Ability abil = null;
		
		List<AbstractFantasyClass> clazzList = isProfession ? p.getFantasyProfessions() : p.getFantasyClasses();
		
		for (AbstractFantasyClass clazz : clazzList) {
			for (Skill s : clazz.getSkillTree()) {
				abil = s.getAbility();
				if (abil.getCommandName().toLowerCase().equals(name)) {
					abil.setLevel(level);
					if(abil instanceof Bindable) {
						if(type == null || type.equalsIgnoreCase("null")) {
							((Bindable)abil).setBoundType(null);
						}
						else {
							((Bindable)abil).setBoundType(Material.getMaterial(type));
						}
						
					}
						
					s.replaceSkillAbility(abil);
					return abil;
				}
			}
		}
		

		return abil;
	}

	void deInit();
}
