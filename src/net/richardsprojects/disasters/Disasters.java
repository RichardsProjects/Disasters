package net.richardsprojects.disasters;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import net.md_5.bungee.api.ChatColor;
import net.richardsprojects.disasters.commands.LightningStormCommand;
import net.richardsprojects.disasters.commands.MeteorCommand;
import net.richardsprojects.disasters.commands.WildfireCommand;
import net.richardsprojects.disasters.events.ImpactEvent;
import net.richardsprojects.disasters.events.PlayerJoinEvent;
import net.richardsprojects.disasters.events.WeatherEvents;
import net.richardsprojects.disasters.events.LightningRodEvents;
import net.richardsprojects.disasters.runnables.AcidRainDamageHandler;
import net.richardsprojects.disasters.runnables.AcidRainDisintegrationHandler;
import net.richardsprojects.disasters.runnables.LightningStormStarter;
import net.richardsprojects.disasters.runnables.MeteorHandler;
import net.richardsprojects.disasters.runnables.WildfireHandler;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Disasters extends JavaPlugin {

	private FileConfiguration lightningRod = new YamlConfiguration();
	private FileConfiguration lightning = new YamlConfiguration();
	private PluginManager pm;

	public static boolean currentlyRaining = false;
	public static boolean lightningStormInProgress = false;

	public static HashMap<BlockData, BlockData> disintegrationData = new HashMap<>();
	public static HashMap<BlockData, BlockData> lightningData = new HashMap<>();
	public static CopyOnWriteArrayList<Location> lightningRodList = new CopyOnWriteArrayList<>();
	public static Disasters instance;
	public static Logger log;

	public File dataFolder;

	@Override
	public void onEnable() {
		instance = this;
		log = this.getLogger();
		pm = getServer().getPluginManager();
		
		dataFolder = getDataFolder();
		if(!dataFolder.exists()) {
			if (!dataFolder.mkdirs()) {
				log.info("There was an error setting up the data folder.");
				log.info("Plugin has been disabled...");
				pm.disablePlugin(this);
			}
		}
		
		Config.loadConfig();

		// attempt to load acid rain desintegration data
		if (!loadAcidRainData()) {
			log.info("There was a problem accessing the file acidRain.yml");
			log.info("Plugin has been disabled...");
			pm.disablePlugin(this);
		}

		// attempt to load lightning rod list
		if (!loadLightningRods()) {
			log.info("There was a problem accessing the file lightningRods.yml");
			log.info("Plugin has been disabled...");
			pm.disablePlugin(this);
		}

		// attempt to load lightning rod data
		if (!loadLightningData()) {
			log.info("There was a problem accessing the lightning.yml file");
			log.info("Plugin has been disabled...");
			pm.disablePlugin(this);
		}

		// start handlers
		if (Config.wildfiresEnabled) {
			new WildfireHandler(this, true).runTaskTimerAsynchronously(this, Config.wildfireTickTime,
					Config.wildfireTickTime);
		}
		if (Config.lightningstormsEnabled) {
			new LightningStormStarter(this, true).runTaskTimerAsynchronously(this,
					Config.lightningStormTickTime, Config.lightningStormTickTime);
		}
		if (Config.meteorsEnabled) {
			new MeteorHandler(this, true).runTaskTimerAsynchronously(this, Config.meteorTickTime,
					Config.meteorTickTime);
		}
		
		// register commands
		getCommand("wildfire").setExecutor(new WildfireCommand(this));
		getCommand("lightningstorm").setExecutor(new LightningStormCommand(this));
		getCommand("meteor").setExecutor(new MeteorCommand(this));
		
		// setup events
		pm.registerEvents(new WeatherEvents(this), this);
		pm.registerEvents(new LightningRodEvents(this), this);
		pm.registerEvents(new PlayerJoinEvent(), this);
		pm.registerEvents(new ImpactEvent(this), this);
		
		// check if it is raining in the world and start the acid rain handlers if so.
		if(getServer().getWorld(Config.worldName) != null) {
			World w = getServer().getWorld(Config.worldName);
			if(w.hasStorm() && Config.acidRainEnabled) {
				new AcidRainDamageHandler(this).runTaskTimerAsynchronously(this, 0, 40);
				new AcidRainDisintegrationHandler(this).runTaskTimerAsynchronously(this,
						Config.acidRainDesintegrateTicks, Config.acidRainDesintegrateTicks);
				currentlyRaining = true;
			}
			for(Player p : w.getPlayers()) {
				p.setResourcePack(Config.texturePack);
			}
		}
	}

	/**
	 * A helper method that loads the data on what blocks are changed to by
	 * acid rain from the acidRain.yml file. Returns whether it was successful
	 * or not.
	 *
	 * @return whether it was successful or not
	 */
	private boolean loadAcidRainData() {
		YamlConfiguration acidRain = new YamlConfiguration();
		
		File file = new File(getDataFolder() + File.separator + "acidRain.yml");
		if(!file.exists()) {
			try {
				if (!file.createNewFile()) return false;
				acidRain.load(getDataFolder() + File.separator + "acidRain.yml");
			} catch (Exception e) {
				return false;
			}
			
			acidRain.addDefault("4", "0");
			acidRain.options().copyDefaults(true);
			
			BlockData source = new BlockData(Material.getMaterial(4), 0);
			BlockData replace = new BlockData(Material.getMaterial(0), 0);
			Disasters.disintegrationData.put(source, replace);
			
			try {
				acidRain.save(getDataFolder() + File.separator + "acidRain.yml");
				log.info("Generated acidRain.yml");
			} catch (IOException e) {
				return false;
			}
		} else {
			try {
				acidRain = new YamlConfiguration();
				acidRain.load(getDataFolder() + File.separator + "acidRain.yml");
			} catch (Exception e) {
				return false;
			}
			
			for(String key : acidRain.getKeys(true)) {
				BlockData source = Utils.getBlockData(key);
				String value = acidRain.getString(key);
				BlockData replace = Utils.getBlockData(value);

				if(source == null || replace == null) {
					log.info("There was a problem parsing a section of the acidRain.yml file");
					log.info("Either '" + key + "' or '" + value + "' is not valid");
				} else {
					Disasters.disintegrationData.put(source, replace);
				}
			}
		}

		return true;
	}

	/**
	 * A helper method that loads lightning rod positions from the
	 * lightningRods.yml file. Returns whether it was successful or not.
	 *
	 * @return whether it was successful or not
	 */
	private boolean loadLightningRods() {
		File file = new File(getDataFolder() + File.separator + "lightningRods.yml");
		if(!file.exists()) {
			try {
				if (!file.createNewFile()) return false;
			} catch (Exception e) {
				return false;
			}
		} else {
			try {
				lightningRod.load(getDataFolder() + File.separator + "lightningRods.yml");
			} catch (Exception e) {
				return false;
			}
			
			for(String key : lightningRod.getKeys(true)) {
				try {
					String[] locationData = key.split("\\|");
					int x = Integer.parseInt(locationData[0]);
					int z = Integer.parseInt(locationData[1]);
					Location loc = new Location(getServer().getWorld(Config.worldName), x, 0, z);
					lightningRodList.add(loc);
				} catch(NumberFormatException e) {
					log.info("There was a problem parsing a section of the lightningRods.yml file");
				}
			}
		}

		return true;
	}

	public boolean isLightningRodHere(String loc) {
		return lightningRod.getKeys(true).contains(loc);
	}


	public void addLightningRod(Location loc, Player p) {
		try {
			String key = loc.getBlockX() + "|" + loc.getBlockZ();
			String value = "lightingRod";
			lightningRod.load(getDataFolder() + File.separator + "lightningRods.yml");
			
			if(!isLightningRodHere(key)) {
				lightningRod.set(key, value);
				lightningRod.save(getDataFolder() + File.separator + "lightningRods.yml");
				Location loc2 = new Location(getServer().getWorld(Config.worldName), loc.getBlockX(), 0, loc.getBlockZ());
				lightningRodList.add(loc2);
			} else {
				p.sendMessage(ChatColor.RED + "There is already a lightning rod there.");
			}
		} catch (Exception e) {
			String msg = "An unexpected error occured while saving your lightning rod";
			msg = msg + " data. Please make sure the plugin is up to date.";
			String msg2 = "An unexpected error occured while saving lightning rod";
			msg2 = msg2 + " data. Please make sure the plugin is up to date.";
			p.sendMessage(ChatColor.RED + msg);
			log.info(ChatColor.RED + msg2);
		}
	}
	
	public void removeLightningRod(String key) {
		try {
			lightningRod.load(getDataFolder() + File.separator + "lightningRods.yml");
			lightningRod.set(key, null);
			lightningRod.save(getDataFolder() + File.separator + "lightningRods.yml");
			Disasters.lightningRodList.clear();
			loadLightningRods();
		} catch(Exception e) {
			e.printStackTrace();
			log.info("An error occured while attempting to remove a lighting rod.");
		}
	}

	/**
	 * A helper method that loads information on what blocks are changed to and
	 * from when struck by lightning from the lightning.yml file on disk. It
	 * will return false if it encounters a problem while attempting to load
	 * the data.
	 *
	 * @return whether or not it was able to load the data
	 */
	private boolean loadLightningData() {
		this.reloadConfig();
		
		File file = new File(getDataFolder() + File.separator + "lightning.yml");
		if(!file.exists()) {
			boolean failure = false;
			try {
				if (file.createNewFile()) {
					lightning = new YamlConfiguration();
					lightning.load(getDataFolder() + File.separator + "lightning.yml");
				} else {
					failure = true;
				}
			} catch (Exception e) {
				failure = true;
			}

			if (failure) {
				return false;
			}
			
			lightning.addDefault("12", 20);
			lightning.options().copyDefaults(true);
			
			BlockData source = new BlockData(Material.getMaterial(4), 0);
			BlockData replace = new BlockData(Material.getMaterial(0), 0);
			Disasters.lightningData.put(source, replace);
			
			try {
				lightning.save(getDataFolder() + File.separator + "lightning.yml");
				log.info("Generated lightning.yml");
			} catch (IOException e) {
				return false;
			}
		} else {
			try {
				lightning = new YamlConfiguration();
				lightning.load(getDataFolder() + File.separator + "lightning.yml");
			} catch (Exception e) {
				log.info("There was a problem loading data from the lightning.yml file");
				log.info("Plugin has been disabled...");
				pm.disablePlugin(this);
			}
			
			for(String key : lightning.getKeys(true)) {
				BlockData source = Utils.getBlockData(key);
				String value = lightning.getString(key);
				BlockData replace = Utils.getBlockData(value);
				
				if(source == null || replace == null) {
					log.info("There was a problem parsing a section of the lightning.yml file");
					log.info("Either '" + key + "' or '" + value + "' is not valid");
				} else {
					Disasters.lightningData.put(source, replace);
				}
			}
		}

		return true;
	}
}