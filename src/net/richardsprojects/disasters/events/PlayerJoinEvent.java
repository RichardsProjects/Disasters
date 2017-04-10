package net.richardsprojects.disasters.events;

import net.richardsprojects.disasters.Config;
import net.richardsprojects.disasters.Disasters;
import net.richardsprojects.disasters.Utils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * The event handler for a player logging into the world affected by this
 * plugin.
 *
 *  @author RichardB122
 *  @version 4/6/17
 */
public class PlayerJoinEvent implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent e) {
		// check to make sure they joined the plugin world
		if(!e.getPlayer().getWorld().getName().equals(Config.worldName)) return;

		// send them a message & the resource pack if it's raining
		if(Disasters.currentlyRaining) {
			e.getPlayer().sendMessage(Utils.colorCodes(Config.acidRainCurrent));
			e.getPlayer().setResourcePack(Config.texturePack);
		}
	}
	
}
