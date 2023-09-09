package me.thutson3876.fantasyclasses;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.thutson3876.fantasyclasses.commands.CommandManager;
import me.thutson3876.fantasyclasses.cooldowns.CooldownManager;
import me.thutson3876.fantasyclasses.custommobs.CustomMobManager;
import me.thutson3876.fantasyclasses.listeners.CursedItemsListener;
import me.thutson3876.fantasyclasses.listeners.CustomDamageListener;
import me.thutson3876.fantasyclasses.listeners.EnchantingListener;
import me.thutson3876.fantasyclasses.listeners.GeneralAbilityListener;
import me.thutson3876.fantasyclasses.listeners.MobSpawnListener;
import me.thutson3876.fantasyclasses.listeners.PlayerRegistryListener;
import me.thutson3876.fantasyclasses.listeners.ScalingListener;
import me.thutson3876.fantasyclasses.listeners.SkillPointExpListener;
import me.thutson3876.fantasyclasses.listeners.WitchesBrewListener;
import me.thutson3876.fantasyclasses.playermanagement.PlayerManager;
import me.thutson3876.fantasyclasses.professions.enchanter.customenchantments.Enchantments;
import me.thutson3876.fantasyclasses.status.StatusManager;

public class FantasyClasses extends JavaPlugin {

	protected static FantasyClasses plugin;

	private CommandManager commandManager;

	private CooldownManager cooldownManager;

	private PlayerManager playerManager;

	private CustomMobManager mobManager;
	
	private StatusManager statusManager;
	
	@Override
	public void onEnable() {
		plugin = this;
		statusManager = new StatusManager();
		playerManager = new PlayerManager();
		playerManager.init();
		cooldownManager = new CooldownManager();
		commandManager = new CommandManager();
		mobManager = new CustomMobManager();
		mobManager.init();
		
		loadCustomEnchantments();

		registerListeners();
		
		log("FantasyClasses has been loaded!");
	}

	@Override
	public void onDisable() {
		statusManager.deInit();
		playerManager.deInit();
		
		this.saveConfig();
		cooldownManager.deInit();
		mobManager.deInit();
		
		log("FantasyClasses has been disabled!");
	}

	private void registerListeners() {
		new PlayerRegistryListener();
		new EnchantingListener();
		new GeneralAbilityListener();
		new ScalingListener();
		new CustomDamageListener();
		new CursedItemsListener();
		new SkillPointExpListener();
		new MobSpawnListener();
		new WitchesBrewListener();
	}

	public void registerEvents(Listener listener) {
		if (listener == null)
			return;
		Bukkit.getPluginManager().registerEvents(listener, this);
	}
	
	private void loadCustomEnchantments() {
		if(!Enchantment.isAcceptingRegistrations())
			return;
		
		for(Enchantment ench : Enchantments.CUSTOM.getEnchants())
			Enchantment.registerEnchantment(ench);
	}

	public static FantasyClasses getPlugin() {
		return plugin;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public CooldownManager getCooldownManager() {
		return cooldownManager;
	}
	
	public CustomMobManager getMobManager() {
		return mobManager;
	}
	
	public StatusManager getStatusManager() {
		return statusManager;
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public void log(String message) {
		System.out.println(message);
	}

	public void log(String message, Throwable error) {
		System.out.println(message);
		error.printStackTrace();
	}

}
