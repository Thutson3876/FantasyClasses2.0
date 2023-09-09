package me.thutson3876.fantasyclasses.commands.commandexecutors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.thutson3876.fantasyclasses.commands.AbstractCommand;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;
import me.thutson3876.fantasyclasses.util.chat.ColorCode;

public class Command_ToggleArrowVelocityTracker extends AbstractCommand implements Listener {

	public Command_ToggleArrowVelocityTracker() {
		super("togglearrowtracker", "arrowtracker", "toggleat");
	}

	@Override
	protected boolean onInternalCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			FantasyPlayer fplayer = plugin.getPlayerManager().getPlayer((Player)sender);
			
			if(fplayer != null) {
				boolean avt = !fplayer.hasArrowVelocityTracker();
				
				if(avt) {
					sender.sendMessage(ChatUtils.chat(ColorCode.SUCCESS + "Arrow Velocity Tracker has been toggled on!"));
				}
				else {
					sender.sendMessage(ChatUtils.chat(ColorCode.ERROR + "Status Effect Messages has been toggled off!"));
				}
				fplayer.setArrowVelocityTracker(avt);
				return true;
			}
		}
		
		sender.sendMessage(ColorCode.ERROR + "Error: Must be player to use this command");
		return true;
	}
}
