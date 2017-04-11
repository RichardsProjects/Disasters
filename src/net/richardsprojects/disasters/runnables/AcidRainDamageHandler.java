package net.richardsprojects.disasters.runnables;

import net.richardsprojects.disasters.Config;
import net.richardsprojects.disasters.Disasters;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * A BukkitRunnable that damages players if they are in a Disasters world and
 * exposed to the acid rain.
 *
 * @author RichardB122
 * @version 4/11/17
 */
public class AcidRainDamageHandler extends BukkitRunnable {

	private Disasters plugin;
	
	public AcidRainDamageHandler(Disasters plugin) {
		this.plugin = plugin;
	}
	
	public void run() {
		if (plugin.getServer().getWorld(Config.worldName) == null) return;

		if(Disasters.currentlyRaining) {
			World world = plugin.getServer().getWorld(Config.worldName);

			// damage players only in the Disasters world
			for(Player player : world.getPlayers()) {
				// check that they are not in a desert biome
				if(player.getLocation().getBlock().getBiome() != Biome.DESERT
						&& player.getLocation().getBlock().getBiome() != Biome.DESERT_HILLS
						&& player.getLocation().getBlock().getBiome() != Biome.DESERT_HILLS)
				{
					// check if there is a block above them
					Location loc = player.getLocation();
					boolean blockAboveThem = false;
					int blockY = loc.getBlockY();
					int highestBlockY = world.getHighestBlockYAt(loc) - 1;
					blockAboveThem = highestBlockY > blockY;
					if(!blockAboveThem) player.damage(1);
				}
			}
		} else {
			this.cancel();
		}
	}

}
