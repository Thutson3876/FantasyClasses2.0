package me.thutson3876.fantasyclasses.commands.commandexecutors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.thutson3876.fantasyclasses.commands.AbstractCommand;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;
import me.thutson3876.fantasyclasses.util.chat.ColorCode;

public class Command_ToggleStatusEffectMessages extends AbstractCommand implements Listener {

	public Command_ToggleStatusEffectMessages() {
		super("togglestatuseffectmessages", "statuseffects", "togglese");
	}

	@Override
	protected boolean onInternalCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			FantasyPlayer fplayer = plugin.getPlayerManager().getPlayer((Player)sender);
			
			if(fplayer != null) {
				boolean se = !fplayer.hasStatusMessages();
				
				if(se) {
					sender.sendMessage(ChatUtils.chat(ColorCode.SUCCESS + "Status Effect Messages have been toggled on!"));
				}
				else {
					sender.sendMessage(ChatUtils.chat(ColorCode.ERROR + "Status Effect Messages have been toggled off!"));
				}
				fplayer.setStatusMessages(se);
				return true;
			}
		}
		
		sender.sendMessage(ColorCode.ERROR + "Error: Must be player to use this command");
		return true;
	}

}
