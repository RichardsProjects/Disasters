package net.richardsprojects.disasters.runnables;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.richardsprojects.disasters.BlockReplaceData;
import net.richardsprojects.disasters.Disasters;
import net.richardsprojects.disasters.Utils;

public class LightningStrikeHandler extends BukkitRunnable {

	private Disasters plugin;
	private int totalTimes;
	private int timesLeft;
	
	public LightningStrikeHandler(Disasters plugin) {
		this.plugin = plugin;
		this.totalTimes = Disasters.lightningStormDuration/20;
		this.timesLeft = this.totalTimes;
	}

	public void run() {
		//Loop through all players and strike lightning near them
		if(this.timesLeft > 0) {
			if(plugin.getServer().getWorld(Disasters.worldName) != null) {
				World w = plugin.getServer().getWorld(Disasters.worldName);
				if(w.getPlayers().size() > 0) {
					for(Player p : w.getPlayers()) {
						int xCoord = Utils.randInt(p.getLocation().getBlockX() - 25, p.getLocation().getBlockX() + 25);
						int zCoord = Utils.randInt(p.getLocation().getBlockZ() - 25, p.getLocation().getBlockZ() + 25);
						Location l = new Location(w, xCoord, w.getHighestBlockYAt(xCoord, zCoord), zCoord);
						
						//Check if there is a lightning rod in range
						for(Location rod : Disasters.lightningRodList) {
							if(!(rod.getX() == l.getX() && rod.getZ() == l.getZ())) {
								int topRightX = (int) (rod.getX() + 10);
								int bottomRightZ = (int) (rod.getZ() - 10);
								if(l.getX() <= topRightX && l.getX() >= topRightX - 20 && l.getZ() >= bottomRightZ && l.getZ() <= bottomRightZ + 20) {
									l = new Location(w, rod.getX(), w.getHighestBlockYAt((int) rod.getX(), (int) rod.getZ()), rod.getZ());
									break;
								}
							}
						}
						
						//Strike Lightning there
						w.strikeLightning(l);
						
						final Location l2 = l;
						
						new BukkitRunnable() {
							public void run() {
								//Change ground if applicable
								l2.setY(l2.getY() - 1);
								Material blockMat = l2.getBlock().getType();
								Block block = l2.getBlock();
								for(BlockReplaceData data : Disasters.lightningData) {
									if(data.getType() == blockMat) {
										String msg = "A block has been changed from ";
										msg = msg + data.getType().name() + " to " + data.getReplace().name();
										plugin.log.info(msg);
										block.setType(data.getReplace());
										break;
									}
								}
							}							
						}.runTask(plugin);
						
					}
				}
			}
			this.timesLeft--;
		} else {
			this.cancel();
		}
	}
	
	
	
}
