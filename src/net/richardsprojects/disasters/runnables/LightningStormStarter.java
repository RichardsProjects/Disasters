package net.richardsprojects.disasters.runnables;

import net.richardsprojects.disasters.Config;
import net.richardsprojects.disasters.Disasters;
import net.richardsprojects.disasters.Utils;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * A BukkitRunnable that manages when lightning storms start and complete.
 *
 * @author RichardB122
 * @version 4/11/17
 */
public class LightningStormStarter extends BukkitRunnable {

	private boolean startRandomly;
	private Disasters plugin;
	
	public LightningStormStarter(Disasters plugin, boolean startRandomly) {
		this.plugin = plugin;
		this.startRandomly = startRandomly;
	}
	
	public void run() {
		World world = plugin.getServer().getWorld(Config.worldName);
		if (world == null) return;

		// only start a new storm if none is in progress and at least one player is online
		if(!Disasters.lightningStormInProgress && world.getPlayers().size() > 0) {
			// calculate chance
			int randomNumber = Utils.randInt(1, Config.lightningStormChanceMax);
			if(randomNumber <= Config.lightningStormChance || !startRandomly) {

				// notify all players in world
				for(Player p : world.getPlayers()) {
					p.sendMessage(Utils.colorCodes(Config.lightningStormStart));
				}

				// run Lightning storm stop task after configurable delay
				int duration = Config.lightningStormDuration;
				new LightningStormStop(plugin).runTaskLaterAsynchronously(plugin, duration);

				// reset and start strike method
				new LightningStrikeHandler(plugin).runTaskTimerAsynchronously(plugin, 0, 20);

				Disasters.lightningStormInProgress = true;
				world.setThundering(true);
			}
		}
	}
}
