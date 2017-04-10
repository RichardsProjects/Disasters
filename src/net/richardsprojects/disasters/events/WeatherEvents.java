package net.richardsprojects.disasters.events;

import net.richardsprojects.disasters.Config;
import net.richardsprojects.disasters.Disasters;
import net.richardsprojects.disasters.Utils;
import net.richardsprojects.disasters.runnables.AcidRainDamageHandler;
import net.richardsprojects.disasters.runnables.AcidRainDisintegrationHandler;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * An event listener that listens for WeatherEvents.
 *
 * @author RichardB122
 * @version 4/8/17
 */
public class WeatherEvents implements Listener {

	private Disasters plugin;
	
	public WeatherEvents(Disasters plugin) {
		this.plugin = plugin;
	}

	/**
	 * This event runs when the weather changes and if the weather changed in a
	 * Disasters world the plugin updates the status of acid rain and notifies
	 * all players of the change.
	 *
	 * @param e WeatherChangeEvent
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onWeatherChange(WeatherChangeEvent e) {
		if(!(e.getWorld().getName().equals(Config.worldName) && Config.acidRainEnabled)) return;

		if(!e.toWeatherState()) {
			Disasters.currentlyRaining = false;
			World w = e.getWorld();
			for(Player player : w.getPlayers()) {
				player.sendMessage(Utils.colorCodes(Config.acidRainStop));
			}
		} else {
			Disasters.currentlyRaining = true;
			new AcidRainDisintegrationHandler(plugin).runTaskTimerAsynchronously(plugin,
					Config.acidRainDesintegrateTicks, Config.acidRainDesintegrateTicks);
			new AcidRainDamageHandler(plugin).runTaskTimer(plugin, 40, 40);
			World w = e.getWorld();
			for(Player player : w.getPlayers()) {
				player.setResourcePack(Config.texturePack);
				player.sendMessage(Utils.colorCodes(Config.acidRainStart));
			}
		}

	}

	/**
	 * Cancels lightning strikes in the Disasters world unless a lightning
	 * storm is occurring.
	 *
	 * @param e LightningStrikeEvent
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLightningStrike(LightningStrikeEvent e) {
		if(!Disasters.lightningStormInProgress) {
			e.setCancelled(true);
		}
	}
	
}
