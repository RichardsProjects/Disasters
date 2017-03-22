package net.richardsprojects.disasters.events;

import net.richardsprojects.disasters.Disasters;
import net.richardsprojects.disasters.Utils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PlayerJoinEvent implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent e) {
		if(e.getPlayer().getWorld().getName().equals(Disasters.worldName)) {
			if(Disasters.currentlyRaining) {
				e.getPlayer().sendMessage(Utils.colorCodes(Disasters.acidRainCurrent));
				e.getPlayer().setResourcePack(Disasters.texturePack);
			}
		}
	}
	
}
