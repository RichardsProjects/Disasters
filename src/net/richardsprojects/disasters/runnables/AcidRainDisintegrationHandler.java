package net.richardsprojects.disasters.runnables;

import net.richardsprojects.disasters.BlockReplaceData;
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
		if(plugin.getServer().getWorld(Disasters.worldName) != null) {
			if(Disasters.currentlyRaining) {
				World world = plugin.getServer().getWorld(Disasters.worldName);
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
								for (BlockReplaceData data : Disasters.disintegrationData) {
									if (data.getType() == blockMat && block.getData() == data.getTypeData()) {
										String msg = "A block has been changed from ";
										msg = msg + data.getType().name() + " to " + data.getReplace().name();
										plugin.log.info(msg);
										block.setType(data.getReplace());
										block.setData((byte) data.getReplaceData());
										break;
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
