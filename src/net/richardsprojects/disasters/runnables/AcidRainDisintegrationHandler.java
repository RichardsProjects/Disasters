package net.richardsprojects.disasters.runnables;

import net.richardsprojects.disasters.BlockData;
import net.richardsprojects.disasters.Config;
import net.richardsprojects.disasters.Disasters;
import net.richardsprojects.disasters.Utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * A BukkitRunnable for handling acid rain disintegrating blocks.
 *
 * @author RichardB122
 * @version 4/11/17
 */
public class AcidRainDisintegrationHandler extends BukkitRunnable {

	private Disasters plugin;
	
	public AcidRainDisintegrationHandler(Disasters plugin) {
		this.plugin = plugin;
	}
	
	public void run() {
		if (plugin.getServer().getWorld(Config.worldName) == null) return;

		if (Disasters.currentlyRaining) {
			final World world = plugin.getServer().getWorld(Config.worldName);

			for (final Player p : world.getPlayers()) {
				new BukkitRunnable() {
					public void run() {
						// choose a block to disintegrate
						Location loc = p.getLocation();
						int x = Utils.randInt(loc.getBlockX() - 40, loc.getBlockX() + 40);
						int z = Utils.randInt(loc.getBlockZ() - 40, loc.getBlockZ() + 40);
						int y = world.getHighestBlockYAt(x, z);
						Block block = new Location(p.getWorld(), x, y, z).getBlock();

						Material blockMat = block.getType();
						for (BlockData source : Disasters.disintegrationData.keySet()) {
							if (source.getType() == blockMat) {
								BlockData replace = Disasters.disintegrationData.get(source);

								if (replace != null) {
									// log change
									String msg = "A block has been changed from " + source.getType().name()
											+ " to " + replace.getType().name();
									plugin.log.info(msg);

									// change block data
									block.setType(replace.getType());
									block.setData(new Integer(replace.getTypeData()).byteValue());

									break;
								}
							}
						}
					}
				}.runTask(plugin);
			}
		} else {
			this.cancel();
		}
	}
}
