package me.thutson3876.soundplayer.commands;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import me.thutson3876.soundplayer.SoundPlayer;

public class CommandManager {
	private List<AbstractCommand> commands = new LinkedList<>();
	
	public CommandManager() {
		commands.add(new SoundPlayCommand());
		
		this.registerCommands();
	}
	
	
	public void registerCommands() {
		SoundPlayer plugin = SoundPlayer.getPlugin();
		for(AbstractCommand command : this.commands) {
			plugin.getCommand(command.getCommandName()).setDescription(command.getDescription());
			plugin.getCommand(command.getCommandName()).setAliases(Arrays.asList(command.getAliases()));
			plugin.getCommand(command.getCommandName()).setTabCompleter(command);
			plugin.getCommand(command.getCommandName()).setExecutor(command);
		}
	}
}
