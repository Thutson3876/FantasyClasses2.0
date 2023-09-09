package me.thutson3876.fantasyclasses.commands.commandexecutors;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.thutson3876.fantasyclasses.collectible.Collectible;
import me.thutson3876.fantasyclasses.commands.AbstractCommand;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;
import me.thutson3876.fantasyclasses.util.chat.ColorCode;

public class Command_GenerateRandomLore extends AbstractCommand implements Listener {

	public Command_GenerateRandomLore() {
		super("generaterandomlore", "lore");
	}

	@Override
	protected boolean onInternalCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatUtils.chat(ColorCode.ERROR + "Error: Must be a player to use this command"));
			return true;
		}
		if (!sender.isOp()) {
			sender.sendMessage(ChatUtils.chat(ColorCode.ERROR + "Error: Must be a server operator to use this command"));
			return true;
		}
		
		//Remove when testing is over vvv
		if(sender instanceof HumanEntity) {
			sender.sendMessage(ChatUtils.chat(ColorCode.ERROR + "Nice try"));
			return true;
			
		}
		//Remove when testing is over ^^^
		
		if (args.length < 1) {
			return false;
		}
		Player player = (Player) sender;

		if (args.length >= 1) {
			Collectible c = null;
			for(Collectible col : Collectible.values()) {
				if(args[0].equalsIgnoreCase(col.name())) {
					c = col;
					break;
				}
			}
			if(c == null) {
				sender.sendMessage(ChatUtils.chat(ColorCode.ERROR + "Error: Invalid collectible type"));
				return true;
			}
			if(args.length > 1) {
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(p != null && p.getDisplayName().equalsIgnoreCase(args[1])) {
						player = p;
						break;
					}
				}
			}
			
			player.getWorld().dropItemNaturally(player.getLocation(), Collectible.generateDrop());
			player.sendMessage(c.getRandomLore());
			
			return true;
		}

		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> values = new LinkedList<>();
		if (!(sender instanceof Player))
			return values;
		
		if(args.length == 1) {
			for(Collectible c : Collectible.values())
				values.add(c.name());
		}
		else if(args.length == 2) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(p != null) 
					values.add(p.getDisplayName());
			}
		}

		return values;
	}
}
