package net.richardsprojects.disasters.commands;

import net.md_5.bungee.api.ChatColor;
import net.richardsprojects.disasters.Disasters;
import net.richardsprojects.disasters.runnables.MeteorHandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MeteorCommand implements CommandExecutor {

private Disasters plugin;
	
	public MeteorCommand(Disasters plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(sender.hasPermission("disasters.meteor.start")) {
			new MeteorHandler(plugin, false).runTask(plugin);
		} else {
			sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to perform that command.");
		}
		return true;
	}
}
