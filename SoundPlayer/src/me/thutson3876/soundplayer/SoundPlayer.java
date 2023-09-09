package me.thutson3876.soundplayer;

import org.bukkit.plugin.java.JavaPlugin;

import me.thutson3876.soundplayer.commands.CommandManager;

public class SoundPlayer extends JavaPlugin {
	
	protected static SoundPlayer plugin;
	
	protected CommandManager commandManager;
	
	@Override
	public void onEnable() {
		plugin = this;
		this.commandManager = new CommandManager();
		
		this.log("SoundPlayer has been loaded!");
	}
	
	@Override
	public void onDisable() {
		this.log("SoundPlayer has been disabled!");
	}
	
	public static SoundPlayer getPlugin() {
		return plugin;
	}

	public CommandManager getCommandManager() {
		return this.commandManager;
	}
	
	public void log(String message) {
		System.out.println(message);
	}
	
	public void log(String message, Throwable error) {
		System.out.println(message);
		error.printStackTrace();
	}
}
