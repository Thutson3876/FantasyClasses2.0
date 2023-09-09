package me.thutson3876.soundplayer.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.thutson3876.soundplayer.utils.ChatUtils;

public class SoundPlayCommand extends AbstractCommand {

	private static final List<String> SOUND_NAMES;
	
	static {
		List<String> soundNames = new ArrayList<>();
		for(Sound sound : Sound.values()) {
			soundNames.add(sound.name());
		}
		
		SOUND_NAMES = soundNames;
	}
	
	public SoundPlayCommand() {
		super("playsound");
	}

	@Override
	protected boolean onInternalCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatUtils.chat("&4Error: Must be a player to use this command"));
			return true;
		}
		if (args.length != 3) {
			return false;
		}
		
		Player player = (Player) sender;
		
		Sound sound = getSound(args[0]);
		float volume = 0.0f;
		float pitch = 0.0f;
		
		if(sound == null) {
			sender.sendMessage(ChatUtils.chat("&4Error: Invalid sound name"));
			return true;
		}
		
		try {
			volume = (float) Double.parseDouble(args[1]);
			pitch = (float) Double.parseDouble(args[2]);
			
		} catch (NumberFormatException e) {
			return false;
		}
		
		player.playSound(player, sound, volume, pitch);
		return true;
	}

	private Sound getSound(String name) {
		for(Sound sound : Sound.values()) {
			if(sound.name().equalsIgnoreCase(name))
				return sound;
		}
			
		return null;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> values = new ArrayList<>();
		
		if (args.length != 1)
			return values;
		
		String arg = args[0].toUpperCase();
		for(String s : SOUND_NAMES) {
			if(s.contains(arg))
				values.add(s);
		}
		
		return values;
	}
}
