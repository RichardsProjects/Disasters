package net.richardsprojects.disasters.runnables;

import net.richardsprojects.disasters.Config;
import net.richardsprojects.disasters.Disasters;
import net.richardsprojects.disasters.Utils;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LightningStormStarter extends BukkitRunnable {

	private boolean startRandomly;
	private Disasters plugin;
	
	public LightningStormStarter(Disasters plugin, boolean startRandomly) {
		this.plugin = plugin;
		this.startRandomly = startRandomly;
	}
	
	public void run() {
		World world = plugin.getServer().getWorld(Config.worldName);
		if(world != null) {
			if(!Disasters.lightningStormInProgress) {
				if(world.getPlayers().size() > 0) {
					//Calculate chance
					int randomNumber = Utils.randInt(1, Config.lightningStormChanceMax);
					if(randomNumber <= Config.lightningStormChance || !startRandomly) {
						//Notify all players
						for(Player p : world.getPlayers()) {
							p.sendMessage(Utils.colorCodes(Config.lightningStormStart));
						}
						
						//Run Lightning storm stop task after configurable delay
						new LightningStormStop(plugin).runTaskLaterAsynchronously(plugin, Config.lightningStormDuration);
						new LightningStormStop(plugin).runTaskLaterAsynchronously(plugin, Config.lightningStormDuration);

						//Reset and start strike method
						new BukkitRunnable() {
							public void run() {
								new LightningStrikeHandler(plugin).runTaskTimerAsynchronously(plugin, 0, 20);
							}
						}.runTask(plugin);
						
						Disasters.lightningStormInProgress = true;
						world.setThundering(true);
					}
				}
			}
		}	
	}
}
