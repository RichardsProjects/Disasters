package net.richardsprojects.disasters.events;

import net.richardsprojects.disasters.Disasters;
import net.richardsprojects.disasters.Utils;
import net.richardsprojects.disasters.runnables.AcidRainDamageHandler;
import net.richardsprojects.disasters.runnables.AcidRainDisintegrationHandler;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherEvents implements Listener {

	private Disasters plugin;
	
	public WeatherEvents(Disasters plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onWeatherChange(WeatherChangeEvent e) {
		if(e.getWorld().getName().equals(Disasters.worldName)) {
			if(!e.toWeatherState()) {
				Disasters.currentlyRaining = false;
				World w = e.getWorld();
				for(Player player : w.getPlayers()) {
					player.sendMessage(Utils.colorCodes(Disasters.acidRainStop));
				}
			} else {
				Disasters.currentlyRaining = true;
				new AcidRainDisintegrationHandler(plugin).runTaskTimerAsynchronously(plugin, Disasters.acidRainDesintegrateTicks, Disasters.acidRainDesintegrateTicks);
				new AcidRainDamageHandler(plugin).runTaskTimer(plugin, 40, 40);
				World w = e.getWorld();
				for(Player player : w.getPlayers()) {
					player.setResourcePack(Disasters.texturePack);
					player.sendMessage(Utils.colorCodes(Disasters.acidRainStart));
				}
			}
		}

	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLightningStrike(LightningStrikeEvent e) {
		if(!Disasters.lightningStormInProgress) {
			e.setCancelled(true);
		}
	}
	
}
