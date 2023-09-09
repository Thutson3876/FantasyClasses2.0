package me.thutson3876.fantasyclasses.abilities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.cooldowns.CooldownContainer;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;

public abstract class AbstractAbility implements Ability, Listener {

	protected static final FantasyClasses plugin = FantasyClasses.getPlugin();

	protected FantasyPlayer fplayer;
	protected Player player;
	
	protected final Priority priority;

	protected String displayName;

	protected int coolDowninTicks = 0;

	protected boolean isEnabled = false;
	
	protected int currentLevel = 0;
	protected int maximumLevel = 90000000;

	protected int skillPointCost = 1;

	protected ItemStack itemStack;
	protected String prerequisite = "None";

	public AbstractAbility(Player p) {
		setDefaults();
		if (p != null) {
			player = p;
			fplayer = plugin.getPlayerManager().getPlayer(p);
		}
		else {
			System.out.println(this.getCommandName() + " has null player");
		}
		
		priority = Priority.NORMAL;
	}
	
	public AbstractAbility(Player p, Priority priority) {
		setDefaults();
		if (p != null) {
			player = p;
			fplayer = plugin.getPlayerManager().getPlayer(p);
		}
		else {
			System.out.println(this.getCommandName() + " has null player");
		}
		
		this.priority = priority;
	}
	
	/*public AbstractAbility(Ability abil) {
		this.player = abil.getPlayer();
		if(this.player != null) {
			this.fplayer = plugin.getPlayerManager().getPlayer(player);
		}
		
		this.coolDowninTicks = abil.getCooldown();
		this.displayName = abil.getName();
		this.skillPointCost = abil.getSkillPointCost();
		this.maximumLevel = abil.getMaxLevel();

		this.itemStack = abil.getItemStack();
		
		this.currentLevel = abil.getCurrentLevel();
		this.priority = abil.getPriority();
		
	}*/

	@Override
	public Player getPlayer() {
		return player;
	}
	
	protected FantasyPlayer getFantasyPlayer() {
		return fplayer = plugin.getPlayerManager().getPlayer(player);
	}

	@Override
	public void setPlayer(Player p) {
		if (p != null) {
			player = p;
			fplayer = plugin.getPlayerManager().getPlayer(p);
		}
	}
	
	@Override
	public void setFantasyPlayer(FantasyPlayer p) {
		if (p != null) {
			fplayer = p;
		}
	}

	@Override
	public String getCommandName() {
		return this.getName().replaceAll(" ", "_");
	}
	
	@Override
	public String getName() {
		return displayName;
	}
	
	@Override
	public String getPrerequisite() {
		return prerequisite;
	}
	
	@Override
	public void setPrerequisite(String preq) {
		this.prerequisite = preq;
		updateItemStack();
	}

	@Override
	public int getCurrentLevel() {
		return this.currentLevel;
	}

	@Override
	public void setLevel(int level) {
		this.currentLevel = level;
		if(level == 0)
			disable();
		
		this.applyLevelModifiers();
		this.updateItemStack();
	}

	@Override
	public void levelUp() {
		if(this.fplayer == null)
			plugin.getPlayerManager().getPlayer(player);
		this.currentLevel++;
		applyLevelModifiers();
		this.updateItemStack();
		this.player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.7F, 1F);
	}

	@Override
	public void resetLevel() {
		disable();
		isEnabled = false;
	}
	
	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

	protected void onTrigger(boolean triggerCooldown) {
		AbilityTriggerEvent event = new AbilityTriggerEvent(fplayer, this);
		Bukkit.getPluginManager().callEvent(event);
		
		if(triggerCooldown)
			this.triggerCooldown(event.getCooldown(), event.getCooldownReductionPerTick());
	}
	
	protected AbilityTriggerEvent callEvent() {
		AbilityTriggerEvent event = new AbilityTriggerEvent(fplayer, this);
		Bukkit.getPluginManager().callEvent(event);
		return event;
	}
	
	@Override
	public Priority getPriority() {
		return priority;
	}
	
	@Override
	public void enable() {
		if(this.fplayer == null)
			this.fplayer = plugin.getPlayerManager().getPlayer(player);
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		isEnabled = true;
		
		init();
	}

	@Override
	public void disable() {
		if(this.currentLevel != 0) {
			this.setLevel(0);
			applyLevelModifiers();
		}
		
		//plugin.getPlayerManager().getPlayer(this.player).removeAndResetClassAbility(this);
		//plugin.getPlayerManager().getPlayer(this.player).removeAndResetProfAbility(this);
		HandlerList.unregisterAll(this);
	}
	
	protected ItemStack createItemStack(Material mat) {
		ItemStack item = new ItemStack(mat);
		List<String> lore = new ArrayList<>();
		List<String> temp = new ArrayList<>();
		lore.add("Level: &6" + this.currentLevel + "&r/&6" + this.maximumLevel);
		lore.add("Cost: &6" + this.skillPointCost);
		temp.add(this.getDescription());
		lore.addAll(ChatUtils.splitStringAtLength(temp, ChatUtils.getDefaultSplitLength(), "&r"));
		temp = new ArrayList<>();
		temp.add(this.getInstructions());
		lore.addAll(ChatUtils.splitStringAtLength(temp, ChatUtils.getDefaultSplitLength(), "&r&3"));
		
		if(this instanceof Bindable) {
			Bindable bindable = (Bindable)this;
			if(bindable.getBoundType() == null) {
				lore.add("&bBindable");
			}
			else {
				lore.add("&bBound to: &6" + bindable.getBoundType().name());
			}
		}
		lore.add("Prerequisite: &6" + this.prerequisite);
		
		List<String> loreFinal = new ArrayList<>();
		
		for(String s : lore) {
			loreFinal.add(ChatUtils.chat(s));
		}
		

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatUtils.chat("&6" + this.displayName));
		meta.setLore(loreFinal);
		item.setItemMeta(meta);
		this.itemStack = item;
		return this.itemStack;
	}

	public int getMaxLevel() {
		return this.maximumLevel;
	}

	@Override
	public int getSkillPointCost() {
		return this.skillPointCost;
	}

	public int getCooldownInTicks() {
		return this.coolDowninTicks;
	}

	public double getCooldownInSeconds() {
		return this.coolDowninTicks / 20;
	}
	
	@Override
	public int getCooldown() {
		return this.coolDowninTicks;
	}
	
	@Override
	public CooldownContainer getCooldownContainer() {
		return plugin.getCooldownManager().getCooldownContainer(player, this);
	}

	@Override
	public void triggerCooldown() {
		this.triggerCooldown(this.coolDowninTicks, 1.00);
	}
	
	@Override
	public ItemStack getItemStack() {
		updateItemStack();
		return itemStack;
	}

	private void updateItemStack() {
		if(this.itemStack == null) {
			System.out.println("Ability ItemStack was null: " + this.getClass().getName());
			return;
		}
		
		this.itemStack = createItemStack(this.itemStack.getType());
	}

	@Override
	public void triggerCooldown(double cooldown, double cooldownReductionPerTick) {
		plugin.getCooldownManager().setCooldown(player, this, cooldown, cooldownReductionPerTick);
	}

	public int getMaxCooldown() {
		return this.coolDowninTicks;
	}

	public boolean isOnCooldown() {
		return (-1 != plugin.getCooldownManager().stillHasCooldown(player, this));
	}

	@Override
	public void writeToConfig(ConfigurationSection section) {
		section.set(this.getCommandName().toLowerCase() + "level", this.currentLevel);
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("name", this.getCommandName().toLowerCase());
		map.put("level", Integer.valueOf(this.currentLevel));
		if(this instanceof Bindable) {
			if(((Bindable)this).getBoundType() == null) {
				map.put("type", "null");
			}
			else {
				map.put("type", ((Bindable)this).getBoundType().name());
			}
		}

		return map;
	}

	@Override
	public String toString() {
		return "\nName: " + this.getCommandName().toLowerCase() + "\nLevel: " + this.currentLevel + "\n";
	}
	
	protected void init() {
		
	}
	
	@Override
	public void deInit() {
		
	}

}
