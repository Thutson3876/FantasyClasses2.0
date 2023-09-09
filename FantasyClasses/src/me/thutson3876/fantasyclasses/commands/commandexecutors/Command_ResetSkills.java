package me.thutson3876.fantasyclasses.commands.commandexecutors;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import me.thutson3876.fantasyclasses.commands.AbstractCommand;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;
import me.thutson3876.fantasyclasses.util.chat.ColorCode;

public class Command_ResetSkills extends AbstractCommand implements Listener {

	public Command_ResetSkills() {
		super("resetskills", new String[] { "rskills" });
	}

	@Override
	protected boolean onInternalCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatUtils.chat(ColorCode.ERROR + "Error: Must be a player to use this command"));
			return true;
		}
		if (args.length != 0) {
			sender.sendMessage(ChatUtils.chat(ColorCode.ERROR + "Error: Improper use of command"));
			return false;
		}

		Player player = (Player) sender;

		FantasyPlayer fplayer = plugin.getPlayerManager().getPlayer(player);
		if (fplayer == null) {
			sender.sendMessage(ChatUtils.chat(ColorCode.ERROR + "Error: Must be a player to use this command"));
			return false;
		}
		player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1.2f, 1.2f);
		fplayer.resetAllClassAbilities();

		// REMOVE THIS UPON SERVER LAUNCH v
			fplayer.resetChosenClass();
			fplayer.resetAllProfAbilities();
			fplayer.resetChosenProfessions();
		// REMOVE THIS UPON SERVER LAUNCH ^

		player.sendMessage(ChatUtils.chat(ColorCode.SUCCESS + "Successfully reset skills!"));
		return true;
	}
}
