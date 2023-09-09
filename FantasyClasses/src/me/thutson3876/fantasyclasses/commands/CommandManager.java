package me.thutson3876.fantasyclasses.commands;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.commands.commandexecutors.Command_AddSkillExp;
import me.thutson3876.fantasyclasses.commands.commandexecutors.Command_BindAbility;
import me.thutson3876.fantasyclasses.commands.commandexecutors.Command_GenerateRandomLore;
import me.thutson3876.fantasyclasses.commands.commandexecutors.Command_Help;
import me.thutson3876.fantasyclasses.commands.commandexecutors.Command_OpenMenu;
import me.thutson3876.fantasyclasses.commands.commandexecutors.Command_ResetSkills;
import me.thutson3876.fantasyclasses.commands.commandexecutors.Command_ToggleArrowVelocityTracker;
import me.thutson3876.fantasyclasses.commands.commandexecutors.Command_ToggleDamageMeters;
import me.thutson3876.fantasyclasses.commands.commandexecutors.Command_ToggleDetailedDamageMeters;
import me.thutson3876.fantasyclasses.commands.commandexecutors.Command_ToggleFriendlyFire;
import me.thutson3876.fantasyclasses.commands.commandexecutors.Command_ToggleStatusEffectMessages;

public class CommandManager {
	private List<AbstractCommand> commands = new LinkedList<>();
	
	public CommandManager() {
		commands.add(new Command_OpenMenu());
		commands.add(new Command_BindAbility());
		commands.add(new Command_ResetSkills());
		commands.add(new Command_Help());
		commands.add(new Command_AddSkillExp());
		commands.add(new Command_ToggleDamageMeters());
		commands.add(new Command_ToggleDetailedDamageMeters());
		commands.add(new Command_ToggleFriendlyFire());
		commands.add(new Command_ToggleArrowVelocityTracker());
		commands.add(new Command_ToggleStatusEffectMessages());
		commands.add(new Command_GenerateRandomLore());
		
		this.registerCommands();
	}
	
	
	public void registerCommands() {
		FantasyClasses plugin = FantasyClasses.getPlugin();
		for(AbstractCommand command : this.commands) {
			plugin.getCommand(command.getCommandName()).setDescription(command.getDescription());
			plugin.getCommand(command.getCommandName()).setAliases(Arrays.asList(command.getAliases()));
			plugin.getCommand(command.getCommandName()).setTabCompleter(command);
			plugin.getCommand(command.getCommandName()).setExecutor(command);
		}
	}
}
