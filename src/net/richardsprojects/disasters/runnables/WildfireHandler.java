package net.richardsprojects.disasters.runnables;

import net.md_5.bungee.api.ChatColor;
import net.richardsprojects.disasters.Disasters;
import net.richardsprojects.disasters.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class WildfireHandler extends BukkitRunnable {

	private Disasters plugin;
	private boolean random;
	
	public WildfireHandler(Disasters plugin, boolean random) {
		this.plugin = plugin;
		this.random = random;
	}
	
	public void run() {
		World world = plugin.getServer().getWorld(Disasters.worldName);
		if(world != null) { 
			if(world.getPlayers().size() > 0) {
				//Calculate chance
				int randomNumber = Utils.randInt(1, Disasters.wildfireChanceMax);
				if(randomNumber <= Disasters.wildfireChance || !random) {
					int playerNumber = Utils.randInt(0, world.getPlayers().size() - 1);
					final Player player = world.getPlayers().get(playerNumber);
					//Generate the wildfire
					int tries = 0;
					Location location = player.getLocation();
					while(tries < 100) {
						int xCoord = Utils.randInt(player.getLocation().getBlockX() - 50, player.getLocation().getBlockX() + 50);
						int zCoord = Utils.randInt(player.getLocation().getBlockZ() - 50, player.getLocation().getBlockZ() + 50);
						int yCoord = 250;
						Block block = new Location(player.getWorld(), xCoord, yCoord, zCoord).getBlock();
						while(block.getType() == Material.AIR) {
							yCoord = yCoord - 1;
							block = new Location(player.getWorld(), xCoord, yCoord, zCoord).getBlock();
						}
						if(Utils.isFlammable(block.getType())) {
							//Task must be run on main thread because it makes changes to blocks
							new StartWildfire(block, player).runTask(plugin);
							break;
						} else {
							tries = tries + 1;
							continue;
						}
					}
				}
			}			
		}
	}
}
