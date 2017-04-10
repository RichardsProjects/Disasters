package net.richardsprojects.disasters.runnables;

import net.richardsprojects.disasters.Config;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.richardsprojects.disasters.Disasters;
import net.richardsprojects.disasters.Utils;

/**
 * A BukkitTask that runs periodically and determines whether to launch a
 * meteor based on the config values.
 *
 * @author RichardB122
 * @version 4/9/17
 */
public class MeteorHandler extends BukkitRunnable {

	private Disasters plugin;
	private boolean random;
	
	public MeteorHandler(Disasters plugin, boolean random) {
		this.plugin = plugin;
		this.random = random;
	}

	public void run() {
		final World w = plugin.getServer().getWorld(Config.worldName);
		if (w == null) return;

		if(w.getPlayers().size() > 0) {
			// decide whether to shoot a meteor or not
			int randomNumber = Utils.randInt(1, Config.meteorChanceMax);
			if(randomNumber <= Config.meteorChance || !random) {
				// select random player and determine meteor size
				int playerNumber = Utils.randInt(0, w.getPlayers().size() - 1);
				final Player p = w.getPlayers().get(playerNumber);
				final int meteorSize = Utils.randInt(1, 3);

				// pick location within 20 blocks of the player
				Location loc = p.getLocation();
				int x = Utils.randInt(loc.getBlockX() - 20, loc.getBlockX() + 20);
				int z = Utils.randInt(loc.getBlockZ() - 20, loc.getBlockZ() + 20);
				final Location yawLoc = new Location(w, x, 245, z, 270F, 20F);
				final Location twoBlocks = new Location(w, x, 254, z);

				// warn the player of the impending meteor
				String msg = Config.meteorMessage;
				msg = msg.replace("[SIZE]", Utils.meteorSize(meteorSize));
				msg = msg.replace("[COORDS]", "X: " + x + ", Z: " + z);
				p.sendMessage(Utils.colorCodes(msg));

				// launch the meteor
				new MeteorLaunchTask(w, twoBlocks, yawLoc, meteorSize).runTaskLater(plugin, 240);
			}
		}
	}

	private class MeteorLaunchTask extends BukkitRunnable {

		private World w;
		private Location twoBlocks;
		private Location yawLoc;
		private int meteorSize;

		private MeteorLaunchTask(World w, Location twoBlocks, Location yawLoc, int meteorSize) {
			this.w = w;
			this.twoBlocks = twoBlocks;
			this.yawLoc = yawLoc;
			this.meteorSize = meteorSize;
		}

		public void run() {
			// create required entities
			LargeFireball fb = w.spawn(twoBlocks, LargeFireball.class);
			Arrow e = w.spawn(yawLoc, Arrow.class);
			e.setCustomName(Utils.meteorSize(meteorSize));

			// generate and setup vector
			int angleX = Utils.randInt(0, 360);
			int angleY = Utils.randInt(0, 360);
			int angleZ = Utils.randInt(0, 360);
			Vector angle = new Vector(angleX, angleY, angleZ);
			Vector dir = new Vector(0, -2, 0);
			dir.angle(angle);
			fb.setVelocity(dir);

			// setup entities
			e.setPassenger(fb);
			e.setVelocity(dir);
		}
	}
		
}
