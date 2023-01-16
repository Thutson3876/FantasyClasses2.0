package me.thutson3876.fantasyclasses.commands.commandexecutors;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.commands.AbstractCommand;
import me.thutson3876.fantasyclasses.playermanagement.FantasyPlayer;
import me.thutson3876.fantasyclasses.util.ChatUtils;

public class Command_BindAbility extends AbstractCommand implements Listener {

	public Command_BindAbility() {
		super("bindability", new String[] { "bind" });
	}

	@Override
	protected boolean onInternalCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatUtils.chat("&4Error: Must be a player to use this command"));
			return true;
		}
		if (args.length != 1) {
			return false;
		}

		Player player = (Player) sender;
		List<String> values = boundAbilityNames(player);
		if(values.isEmpty()) {
			sender.sendMessage(ChatUtils.chat("&4Error: You have no abilities that require binding"));
			return true;
		}
		
		for(String s : values) {
			if(s.equalsIgnoreCase(args[0])) {
				FantasyPlayer fplayer = plugin.getPlayerManager().getPlayer(player);
				for(Ability abil : fplayer.getClassAbilities()) {
					if(abil.getCurrentLevel() > 0 && s.equalsIgnoreCase(abil.getCommandName())) {
						Material type = player.getInventory().getItemInMainHand().getType();
						if(type == null || type.equals(Material.AIR)) {
							sender.sendMessage(ChatUtils.chat("&6" + s + " &bhas been unbound"));
							((Bindable)abil).setBoundType(null);
							return true;
						}
							
						((Bindable)abil).setBoundType(type);
						sender.sendMessage(ChatUtils.chat("&bSuccessfully bound &6" + s + " &bto &6" + type));
						return true;
					}
				}
				
				for(Ability abil : fplayer.getProfAbilities()) {
					if(abil.getCurrentLevel() > 0 && s.equalsIgnoreCase(abil.getCommandName())) {
						Material type = player.getInventory().getItemInMainHand().getType();
						if(type == null || type.equals(Material.AIR)) {
							sender.sendMessage(ChatUtils.chat("&6" + s + " &bhas been unbound"));
							((Bindable)abil).setBoundType(null);
							return true;
						}
							
						((Bindable)abil).setBoundType(type);
						sender.sendMessage(ChatUtils.chat("&bSuccessfully bound &6" + s + " &bto &6" + type));
						return true;
					}
				}
			}
		}

		sender.sendMessage(ChatUtils.chat("&4ERROR: Not a Bindable Ability"));
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> values = new LinkedList<>();
		if (!(sender instanceof Player))
			return values;
		Player player = (Player) sender;

		if (args.length != 1)
			return values;
		
		String arg = args[0].toLowerCase();
		for(String s : boundAbilityNames(player)) {
			if(s.toLowerCase().contains(arg))
				values.add(s);
		}

		return values;
	}

	private static List<String> boundAbilityNames(Player p) {
		List<String> values = new LinkedList<>();

		FantasyPlayer fplayer = plugin.getPlayerManager().getPlayer(p);

		for (Ability abil : fplayer.getClassAbilities()) {
			if (abil.getCurrentLevel() > 0 && abil instanceof Bindable)
				values.add(abil.getCommandName());
		}
		
		for (Ability abil : fplayer.getProfAbilities()) {
			if (abil.getCurrentLevel() > 0 && abil instanceof Bindable)
				values.add(abil.getCommandName());
		}

		return values;
	}
}
