package net.richardsprojects.disasters.runnables;

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
		World world = plugin.getServer().getWorld(Disasters.worldName);
		if(world != null) {
			if(!Disasters.lightningStormInProgress) {
				if(world.getPlayers().size() > 0) {
					//Calculate chance
					int randomNumber = Utils.randInt(1, Disasters.lightningStormChanceMax);
					if(randomNumber <= Disasters.lightningStormChance || !startRandomly) {
						//Notify all players
						for(Player p : world.getPlayers()) {
							p.sendMessage(Utils.colorCodes(Disasters.lightningStormStart));
						}
						
						//Run Lightning storm stop task after configurable delay
						new LightningStormStop(plugin).runTaskLaterAsynchronously(plugin, Disasters.lightningStormDuration);
						
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
