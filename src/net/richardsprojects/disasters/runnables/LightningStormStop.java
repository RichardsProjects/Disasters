package net.richardsprojects.disasters.runnables;

import net.richardsprojects.disasters.Config;
import net.richardsprojects.disasters.Disasters;
import net.richardsprojects.disasters.Utils;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LightningStormStop extends BukkitRunnable {

	private Disasters plugin;
	
	public LightningStormStop(Disasters plugin) {
		this.plugin = plugin;
	}
	
	public void run() {
		if(plugin.getServer().getWorld(Config.worldName) != null) {
			World world = plugin.getServer().getWorld(Config.worldName);
			
			// notify all players
			for(Player p : world.getPlayers()) {
				p.sendMessage(Utils.colorCodes(Config.lightningStormStop));
			}
			
			Disasters.lightningStormInProgress = false;
			world.setThundering(false);
		}		
	}
}
