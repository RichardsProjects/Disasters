package net.richardsprojects.disasters.runnables;

import net.richardsprojects.disasters.BlockData;
import net.richardsprojects.disasters.Config;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.richardsprojects.disasters.Disasters;
import net.richardsprojects.disasters.Utils;

/**
 * BukkitRunnable for striking the Disaster's world with lightning during a
 * lightning storm.
 *
 * @author RichardB122
 * @version 4/11/17
 */
public class LightningStrikeHandler extends BukkitRunnable {

	private Disasters plugin;
	private int timesLeft;
	
	LightningStrikeHandler(Disasters plugin) {
		this.plugin = plugin;
		timesLeft = Config.lightningStormDuration / 20;
	}

	public void run() {

		if(this.timesLeft <= 0) this.cancel();

		World w = plugin.getServer().getWorld(Config.worldName);
		if(w == null) return;

		// loop through all players and strike lightning near them
		for (Player p : w.getPlayers()) {
			// pick a random location within 25 blocks of the player
			Location loc = p.getLocation();
			int x = Utils.randInt(loc.getBlockX() - 25, loc.getBlockX() + 25);
			int z = Utils.randInt(loc.getBlockZ() - 25, loc.getBlockZ() + 25);
			Location l = new Location(w, x, w.getHighestBlockYAt(x, z), z);

			// check if there is a lightning rod in range
			for (Location rod : Disasters.lightningRodList) {
				if (!(rod.getX() == l.getX() && rod.getZ() == l.getZ())) {
					int topRightX = (int) (rod.getX() + 10);
					int bottomRightZ = (int) (rod.getZ() - 10);
					if (l.getX() <= topRightX && l.getX() >= topRightX - 20
							&& l.getZ() >= bottomRightZ && l.getZ() <= bottomRightZ + 20) {
						// change the location to the lightning rod
						l = new Location(w, rod.getX(), w.getHighestBlockYAt((int) rod.getX(),
								(int) rod.getZ()), rod.getZ());
						break;
					}
				}
			}

			// strike location with lightning & change ground
			w.strikeLightning(l);
			new LightningChangeGround(l).runTask(plugin);
		}
		this.timesLeft--;
	}

	/**
	 * A task that checks if the ground where the lightning struck needs to be
	 * changed and changes it.
	 *
	 * @author RichardB122
	 * @version 4/8/17
	 */
	private class LightningChangeGround extends BukkitRunnable {

		private Location loc;

		private LightningChangeGround(Location loc) {
			this.loc = loc;
		}

		@Override
		public void run() {
			loc.setY(loc.getY() - 1);
			Material blockMat = loc.getBlock().getType();
			Block block = loc.getBlock();

			// change ground if applicable
			for(BlockData source : Disasters.lightningData.keySet()) {
				if(source.getType() == blockMat) {
					BlockData replace = Disasters.lightningData.get(source);

					if (replace != null) {
						// log change
						String msg = "A block has been changed from " + source.getType().name()
								+ " to " + replace.getType().name();
						Disasters.log.info(msg);

						// perform change
						block.setType(replace.getType());
						block.setData(new Integer(replace.getTypeData()).byteValue());

						break; // quit loop
					}
				}
			}
		}
	}
	
}
