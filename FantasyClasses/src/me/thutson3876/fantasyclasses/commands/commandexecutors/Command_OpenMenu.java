package me.thutson3876.fantasyclasses.commands.commandexecutors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.thutson3876.fantasyclasses.FantasyClasses;
import me.thutson3876.fantasyclasses.commands.AbstractCommand;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;

public class Command_OpenMenu extends AbstractCommand implements Listener {

	public Command_OpenMenu() {
		super("openmenu", "chooseclass");
	}

	@Override
	protected boolean onInternalCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			return false;
		}
		if(args.length != 0) {
			return false;
		}
		
		FantasyPlayer p = FantasyClasses.getPlugin().getPlayerManager().getPlayer((Player)sender);
		p.openMainMenu();
		
		return true;
	}

}
