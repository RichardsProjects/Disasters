package net.richardsprojects.disasters.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;
import net.richardsprojects.disasters.Disasters;
import net.richardsprojects.disasters.runnables.WildfireHandler;

public class WildfireCommand implements CommandExecutor {
	
	private Disasters plugin;
	
	public WildfireCommand(Disasters plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(sender.hasPermission("disasters.wildfire.start")) {
			new WildfireHandler(plugin, false).runTaskAsynchronously(plugin);
		} else {
			sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to perform that command.");
		}
		return true;
	}
}
