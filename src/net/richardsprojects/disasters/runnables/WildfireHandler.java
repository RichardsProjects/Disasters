package net.richardsprojects.disasters.runnables;

import net.richardsprojects.disasters.Config;
import net.richardsprojects.disasters.Disasters;
import net.richardsprojects.disasters.Utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * A BukkitRunnable that runs periodically and determines if it should start a
 * wildfire based on the config values.
 *
 * @author RichardB122
 * @version 4/9/17
 */
public class WildfireHandler extends BukkitRunnable {

	private Disasters plugin;
	private boolean random;
	
	public WildfireHandler(Disasters plugin, boolean random) {
		this.plugin = plugin;
		this.random = random;
	}
	
	public void run() {
		World world = plugin.getServer().getWorld(Config.worldName);
		if(world == null) return;
		if(world.getPlayers().size() == 0) return;

		// calculate chance
		int randomNumber = Utils.randInt(1, Config.wildfireChanceMax);
		if(randomNumber <= Config.wildfireChance || !random) {
			int playerNumber = Utils.randInt(0, world.getPlayers().size() - 1);
			final Player player = world.getPlayers().get(playerNumber);

			// generate the wildfire
			int tries = 0;
			Location loc = player.getLocation();
			while(tries < 50) {
				int x = Utils.randInt(loc.getBlockX() - 30, loc.getBlockX() + 30);
				int z = Utils.randInt(loc.getBlockZ() - 30, loc.getBlockZ() + 30);
				int y = world.getHighestBlockYAt(x, z);

				// check if the block at that location is flammable
				Block block = new Location(world, x, y, z).getBlock();
				if(Utils.isFlammable(block.getType())) {
					// task must be run on main thread because it makes changes to blocks
					new StartWildfire(block, player).runTask(plugin);
					break;
				} else {
					tries++;
				}
			}
		}
	}
}
