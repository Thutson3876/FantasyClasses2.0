package me.thutson3876.fantasyclasses.commands.commandexecutors;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.thutson3876.fantasyclasses.commands.AbstractCommand;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;
import me.thutson3876.fantasyclasses.util.chat.ColorCode;

public class Command_AddSkillExp extends AbstractCommand implements Listener {

	public Command_AddSkillExp() {
		super("addskillexp");
	}

	@Override
	protected boolean onInternalCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatUtils.chat(ColorCode.ERROR + "Error: Must be a player to use this command"));
			return true;
		}
		if(!sender.isOp()) {
			sender.sendMessage(ChatUtils.chat(ColorCode.ERROR + "Error: Must be a server operator to use this command"));
			return true;
		}
		if (args.length < 1) {
			return false;
		}
		Player player = (Player) sender;
		int amt = 0;
		if (args.length == 1) {
			try {
				amt = Integer.parseInt(args[0]);
				plugin.getPlayerManager().getPlayer(player).addSkillExp(amt);
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.7f, 1.0f);
			} catch (NumberFormatException e) {
				return false;
			}
			return true;
		}
		if (args.length == 2) {
			try {
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(args[1].equalsIgnoreCase(p.getDisplayName())) {
						player = p;
						break;
					}
				}
				
				amt = Integer.parseInt(args[0]);
				plugin.getPlayerManager().getPlayer(player).addSkillExp(amt);
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.7f, 1.0f);
			} catch (NumberFormatException e) {
				return false;
			}
			return true;
		}
		return false;
	}
}
