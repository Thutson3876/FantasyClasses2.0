package me.thutson3876.fantasyclasses.commands.commandexecutors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.thutson3876.fantasyclasses.commands.AbstractCommand;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;
import me.thutson3876.fantasyclasses.util.chat.ColorCode;

public class Command_ToggleFriendlyFire extends AbstractCommand implements Listener {

	public Command_ToggleFriendlyFire() {
		super("togglefriendlyfire", "friendlyfire", "toggleff");
	}

	@Override
	protected boolean onInternalCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			FantasyPlayer fplayer = plugin.getPlayerManager().getPlayer((Player)sender);
			
			if(fplayer != null) {
				boolean ff = !fplayer.hasFriendlyFire();
				
				if(ff) {
					sender.sendMessage(ChatUtils.chat(ColorCode.SUCCESS + "Friendly Fire has been toggled on!"));
				}
				else {
					sender.sendMessage(ChatUtils.chat(ColorCode.ERROR + "Friendly Fire has been toggled off!"));
				}
				fplayer.setFriendlyFire(ff);
				return true;
			}
		}
		
		sender.sendMessage(ColorCode.ERROR + "Error: Must be player to use this command");
		return true;
	}

}
