package me.thutson3876.fantasyclasses.commands;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.thutson3876.fantasyclasses.FantasyClasses;

public abstract class AbstractCommand implements CommandInterface {
	protected final String commandName;
	  
	  protected final Collection<String> aliases = new HashSet<>();
	  
	  protected final static FantasyClasses plugin = FantasyClasses.getPlugin();
	  
	  protected String description = "No description provided.";
	  
	  public AbstractCommand(String commandName) {
	    this.commandName = commandName;
	  }
	  
	  public AbstractCommand(String commandName, String... aliases) {
	    this.commandName = commandName;
	    this.aliases.addAll(Arrays.asList(aliases));
	  }
	  
	  public String getCommandName() {
	    return this.commandName;
	  }
	  
	  public String[] getAliases() {
	    return this.aliases.<String>toArray(new String[this.aliases.size()]);
	  }
	  
	  public boolean hasAliases() {
	    return (this.aliases != null && this.aliases.size() > 0);
	  }
	  
	  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
	    return new LinkedList<>();
	  }
	  
	  public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	    try {
	      return onInternalCommand(sender, command, label, args);
	    } catch (Throwable exp) {
	      plugin.log("Error on command '" + label + "' for '" + sender.getName() + "' args: '" + 
	          StringUtils.join((Object[])args, " ") + "'", exp);
	      return true;
	    } 
	  }
	  
	  protected abstract boolean onInternalCommand(CommandSender sender, Command command, String label, String[] args);
	  
	  public String getDescription() {
	    return this.description;
	  }
	  
	  /*public void applyRemapping(Map<String, String> remaps) {
	    for (String key : remaps.keySet()) {
	      String replacement = remaps.get(key);
	        if (this.commandName.equalsIgnoreCase(key)) {
	          this.commandName = replacement;
	          break;
	        } 
	        for (String alias : this.aliases) {
		        if (alias.equalsIgnoreCase(key)) {
		          this.aliases.remove(alias);
		          this.aliases.add(replacement);
		          break;
		        } 
		     } 
	      } 
	  } */
}
