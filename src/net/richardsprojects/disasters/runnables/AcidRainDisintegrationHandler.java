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

public class AcidRainDisintegrationHandler extends BukkitRunnable {

	private Disasters plugin;
	
	public AcidRainDisintegrationHandler(Disasters plugin) {
		this.plugin = plugin;
	}
	
	public void run() {
		if(plugin.getServer().getWorld(Config.worldName) != null) {
			if(Disasters.currentlyRaining) {
				World world = plugin.getServer().getWorld(Config.worldName);
				if(world.getPlayers().size() > 0) {
					for(final Player p : world.getPlayers()) {
						final BukkitTask bukkitTask = new BukkitRunnable() {
							public void run() {
								Location location = p.getLocation();
								int xCoord = Utils.randInt(p.getLocation().getBlockX() - 40, p.getLocation().getBlockX() + 40);
								int zCoord = Utils.randInt(p.getLocation().getBlockZ() - 40, p.getLocation().getBlockZ() + 40);
								int yCoord = 250;
								Block block = new Location(p.getWorld(), xCoord, yCoord, zCoord).getBlock();
								while (block.getType() == Material.AIR) {
									yCoord = yCoord - 1;
									block = new Location(p.getWorld(), xCoord, yCoord, zCoord).getBlock();
								}
								Material blockMat = block.getType();
								for(BlockData source : Disasters.disintegrationData.keySet()) {
									if(source.getType() == blockMat) {
										BlockData replace = Disasters.disintegrationData.get(source);

										if (replace != null) {
											String msg = "A block has been changed from " + source.getType().name()
													+ " to " + replace.getType().name();
											plugin.log.info(msg);
											block.setType(replace.getType());
											block.setData(new Integer(replace.getTypeData()).byteValue());
											break;
										}
									}
								}
							}
						}.runTask(plugin);
					}
				}
			} else {
				this.cancel();
			}
		}
	}
}
