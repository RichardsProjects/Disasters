package net.richardsprojects.disasters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Logger;

import net.md_5.bungee.api.ChatColor;
import net.richardsprojects.disasters.commands.LightningStormCommand;
import net.richardsprojects.disasters.commands.MeteorCommand;
import net.richardsprojects.disasters.commands.WildfireCommand;
import net.richardsprojects.disasters.events.ImpactEvent;
import net.richardsprojects.disasters.events.PlayerJoinEvent;
import net.richardsprojects.disasters.events.WeatherEvents;
import net.richardsprojects.disasters.events.SPEvent;
import net.richardsprojects.disasters.runnables.AcidRainDamageHandler;
import net.richardsprojects.disasters.runnables.AcidRainDisintegrationHandler;
import net.richardsprojects.disasters.runnables.LightningStormStarter;
import net.richardsprojects.disasters.runnables.MeteorHandler;
import net.richardsprojects.disasters.runnables.WildfireHandler;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Disasters extends JavaPlugin {

	private FileConfiguration config;
	private FileConfiguration acidRain = new YamlConfiguration();
	private FileConfiguration lightningRod = new YamlConfiguration();
	private FileConfiguration lightning = new YamlConfiguration();
	private File dataFolder;
	public static String worldName = "world";
	
	public static int wildfireChance = 1;
	public static int wildfireChanceMax = 5;
	private static int wildfireTickTime = 12000;
	
	public static boolean currentlyRaining = false;
	public static String acidRainCurrent = "&2&lAn acid rain storm is currently going on!";
	public static String acidRainStart = "&2&lAn acid rain storm has started!";
	public static String acidRainStop = "&2&lThe acid rain storm has stopped.";
	public static String texturePack = "https://dl.dropboxusercontent.com/u/97875111/acidRain.zip";
	public static int acidRainDesintegrateTicks = 20;
	
	public static String lightningStormStart = "&7&lA lightning storm has started!";
	public static String lightningStormStop = "&7&lThe lightning storm has stopped.";
	public static int lightningStormChance = 1;
	public static int lightningStormChanceMax = 5;
	private static int lightningStormTickTime = 12000;
	public static int lightningStormDuration = 1200;
	
	public static int meteorChance = 1;
	public static int meteorChanceMax = 5;
	private static int meteorTickTime = 12000;
	public static String meteorMessage = "&4&lA [SIZE] meteor is falling near you."; 
	public static int smallMeteor = 2;
	public static int mediumMeteor = 4;
	public static int largeMeteor = 6;
	
	public static ArrayList<BlockReplaceData> disintegrationData = new ArrayList<>();
	public static ArrayList<BlockReplaceData> lightningData = new ArrayList<>();
	public static ArrayList<Location> lightningRodList = new ArrayList<>();
	
	public static Logger log;
	private PluginManager pm;
	
	public static boolean lightningStormInProgress = false;


	@Override
	public void onEnable() {
		//Basic Setup
		log = Logger.getLogger("Minecraft");
		pm = getServer().getPluginManager();
		
		dataFolder = getDataFolder();
		if(!dataFolder.exists()) {
			dataFolder.mkdirs();
		}
		
		loadConfig();
		loadAcidRainData();
		loadLightningRods();
		loadLightningData();
		
		
		
		// start handlers
		new WildfireHandler(this, true).runTaskTimerAsynchronously(this, wildfireTickTime,
				wildfireTickTime);
		new LightningStormStarter(this, true).runTaskTimerAsynchronously(this,
				lightningStormTickTime, lightningStormTickTime);
		new MeteorHandler(this, true).runTaskTimerAsynchronously(this, meteorTickTime,
				meteorTickTime);
		
		//Register Commands
		getCommand("wildfire").setExecutor(new WildfireCommand(this));
		getCommand("lightningstorm").setExecutor(new LightningStormCommand(this));
		getCommand("meteor").setExecutor(new MeteorCommand(this));
		
		//Setup Events
		pm.registerEvents(new WeatherEvents(this), this);
		pm.registerEvents(new SPEvent(this), this);
		pm.registerEvents(new PlayerJoinEvent(), this);
		pm.registerEvents(new ImpactEvent(this), this);
		
		// check if it is raining in the world and start the acid rain handlers if so.
		if(getServer().getWorld(worldName) != null) {
			World w = getServer().getWorld(worldName);
			if(w.hasStorm()) {
				new AcidRainDamageHandler(this).runTaskTimerAsynchronously(this, 0, 40);
				new AcidRainDisintegrationHandler(this).runTaskTimerAsynchronously(this,
						Disasters.acidRainDesintegrateTicks, Disasters.acidRainDesintegrateTicks);
				currentlyRaining = true;
			}
			for(Player p : w.getPlayers()) {
				p.setResourcePack(Disasters.texturePack);
			}
		}
	}
	
	public void loadConfig() {
		this.reloadConfig();
		
		File file = new File(getDataFolder() + File.separator + "config.yml");
		if(!file.exists()) {
			try {
				PrintWriter out = new PrintWriter(new File(dataFolder + File.separator + "config.yml"));
				BufferedReader reader = new BufferedReader(new InputStreamReader(
				Disasters.class.getResourceAsStream("/config.yml")));

				String currentLine;
				while ((currentLine = reader.readLine()) != null) {
					out.append(currentLine + "\n");
				}
				out.close();
				reader.close();
				
				config = new YamlConfiguration();
				config.load(new File(dataFolder + File.separator + "config.yml"));
			} catch (Exception e) {
				log.info("[Disasters] There was an error setting up the config file...");
				e.printStackTrace();
			}
			
			log.info("[Disasters] Generated config.yml");
		} else {
			config = this.getConfig();
			
			worldName = config.getString("worldName");
			wildfireChance = config.getInt("wildfireChance");
			wildfireChanceMax = config.getInt("wildfireChanceMax");
			wildfireTickTime = config.getInt("wildfireTickTime");
			acidRainStart = config.getString("acidRainStart");
			acidRainStop = config.getString("acidRainStop");
			acidRainDesintegrateTicks = config.getInt("acidRainDesintegrateTicks");
			lightningStormStart = config.getString("lightningStormStart");
			lightningStormStop = config.getString("lightningStormStop");
			lightningStormChance = config.getInt("lightningStormChance");
			lightningStormChanceMax = config.getInt("lightningStormChanceMax");
			lightningStormTickTime = config.getInt("lightningStormTickTime");
			lightningStormDuration = config.getInt("lightningStormDuration");
			texturePack = config.getString("texturePack");
			meteorMessage = config.getString("meteorMessage");
			meteorTickTime = config.getInt("meteorTickTime");
			meteorChance = config.getInt("meteorChance");
			meteorChanceMax = config.getInt("meteorChanceMax");
			smallMeteor = config.getInt("smallMeteor");
			mediumMeteor = config.getInt("mediumMeteor");
			largeMeteor = config.getInt("largeMeteor");
			acidRainCurrent = config.getString("acidRainCurrent");
		}
	}
	
	public void loadAcidRainData() {
		this.reloadConfig();
		
		File file = new File(getDataFolder() + File.separator + "acidRain.yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
				acidRain = new YamlConfiguration();
				acidRain.load(getDataFolder() + File.separator + "acidRain.yml");
			} catch (FileNotFoundException e) {
				log.info("[Disasters] There was a problem loading data from the acidRain.yml file");
				log.info("[Disasters] Plugin has been disabled...");
				pm.disablePlugin(this);
			} catch (IOException e) {
				log.info("[Disasters] There was a problem loading data from the acidRain.yml file");
				log.info("[Disasters] Plugin has been disabled...");
				pm.disablePlugin(this);
			} catch (InvalidConfigurationException e) {
				log.info("[Disasters] There was a problem loading data from the acidRain.yml file");
				log.info("[Disasters] Plugin has been disabled...");
				pm.disablePlugin(this);
			}
			
			acidRain.addDefault("4", "0");
			acidRain.options().copyDefaults(true);
			
			BlockReplaceData data = new BlockReplaceData(Material.getMaterial(4), 0, Material.getMaterial(0), 0);
			Disasters.disintegrationData.add(data);
			
			try {
				acidRain.save(getDataFolder() + File.separator + "acidRain.yml");
				log.info("[Disasters] Generated acidRain.yml");
			} catch (IOException e) {
				log.info("[Disasters] Error cannot generate acidRain.yml");
				log.info("[Disasters] Plugin has been disabled...");
				pm.disablePlugin(this);
			}
		} else {
			try {
				acidRain = new YamlConfiguration();
				acidRain.load(getDataFolder() + File.separator + "acidRain.yml");
			} catch (FileNotFoundException e) {
				log.info("[Disasters] There was a problem loading data from the acidRain.yml file");
				log.info("[Disasters] Plugin has been disabled...");
				pm.disablePlugin(this);
			} catch (IOException e) {
				log.info("[Disasters] There was a problem loading data from the acidRain.yml file");
				log.info("[Disasters] Plugin has been disabled...");
				pm.disablePlugin(this);
			} catch (InvalidConfigurationException e) {
				log.info("[Disasters] There was a problem loading data from the acidRain.yml file");
				log.info("[Disasters] Plugin has been disabled...");
				pm.disablePlugin(this);
			}
			
			for(String key : acidRain.getKeys(true)) {
				//Parse Materials
				Material type = null;
				Material replace = null;
				int typeData = 0;
				int replaceData = 0;
				int intType = 0;
				int returnType = 0;
				String str = acidRain.getString(key);
				if(key.contains(":")) {
					String[] values = key.split(":");
					if(values.length == 2) {
						String firstPart = values[0];
						String secondPart = values[1];
						//Calculate Material
						try {
							intType = Integer.parseInt(firstPart);
							type = Material.getMaterial(intType);
						} catch(NumberFormatException e) {
							type = Material.matchMaterial(firstPart);
						}
						//Calculate Data
						try {
							typeData = Integer.parseInt(secondPart);
						} catch(NumberFormatException e) {}
					}					
				} else {
					try {
						intType = Integer.parseInt(key);
						type = Material.getMaterial(intType);
					} catch(NumberFormatException e) {
						type = Material.matchMaterial(key);
					}
				}
				
				if(str.contains(":")) {
					String[] values = str.split(":");
					if(values.length == 2) {
						String firstPart = values[0];
						String secondPart = values[1];
						//Calculate Material
						try {
							returnType = Integer.parseInt(firstPart);
							replace = Material.getMaterial(returnType);
						} catch(NumberFormatException e) {
							replace = Material.matchMaterial(str);
						}
						//Calculate Data
						try {
							replaceData = Integer.parseInt(secondPart);
						} catch(NumberFormatException e) {}
					}
				} else {
					try {
						returnType = Integer.parseInt(str);
						replace = Material.getMaterial(returnType);
					} catch(NumberFormatException e) {
						replace = Material.matchMaterial(str);
					}
				}
				
				if(type == null || replace == null) {
					log.info("[Disasters] There was a problem parsing a section of the acidRain.yml file");
					log.info("[Disasters] Either '" + key + "' or '" + str + "' is not valid");
				} else {
					BlockReplaceData data = new BlockReplaceData(type, typeData, replace, replaceData);
					Disasters.disintegrationData.add(data);
				}
			}
		}
	}
	
	public void loadLightningRods() {
		File file = new File(getDataFolder() + File.separator + "lightningRods.yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (FileNotFoundException e) {
				log.info("[Disasters] There was a problem loading data from the lightningRods.yml file");
				log.info("[Disasters] Plugin has been disabled...");
				pm.disablePlugin(this);
			} catch (IOException e) {
				log.info("[Disasters] There was a problem loading data from the lightningRods.yml file");
				log.info("[Disasters] Plugin has been disabled...");
				pm.disablePlugin(this);
			}			
		} else {
			try {
				lightningRod.load(getDataFolder() + File.separator + "lightningRods.yml");
			} catch (FileNotFoundException e) {
				log.info("[Disasters] There was a problem loading data from the lightningRods.yml file");
				log.info("[Disasters] Plugin has been disabled...");
				pm.disablePlugin(this);
			} catch (IOException e) {
				log.info("[Disasters] There was a problem loading data from the lightningRods.yml file");
				log.info("[Disasters] Plugin has been disabled...");
				pm.disablePlugin(this);
			} catch (InvalidConfigurationException e) {
				log.info("[Disasters] There was a problem loading data from the lightningRods.yml file");
				log.info("[Disasters] Plugin has been disabled...");
				pm.disablePlugin(this);
			}
			
			for(String key : lightningRod.getKeys(true)) {
				try {
					String[] locationData = key.split("\\|");
					int x = Integer.parseInt(locationData[0]);
					int z = Integer.parseInt(locationData[1]);
					Location loc = new Location(getServer().getWorld(worldName), x, 0, z);
					lightningRodList.add(loc);
				} catch(NumberFormatException e) {
					log.info("[Disasters] There was a problem parsing a section of the lightningRods.yml file");
				}
			}
		}
	}
	
	public void addLightningRod(Location loc, Player p) {
		try {
			boolean rodAlreadyExists = false;
			String key = "";
			key = ((int) loc.getBlockX()) + "|" + ((int) loc.getBlockZ());
			String value = "lightingRod";
			lightningRod.load(getDataFolder() + File.separator + "lightningRods.yml");
			
			for(String cKey : lightningRod.getKeys(true)) {
				if(cKey.equals(key)) {
					rodAlreadyExists = true;
					break;
				}
			}
			
			if(!rodAlreadyExists) {
				lightningRod.set(key, value);
				lightningRod.save(getDataFolder() + File.separator + "lightningRods.yml");
				Location loc2 = new Location(getServer().getWorld(worldName), loc.getBlockX(), 0, loc.getBlockZ());
				lightningRodList.add(loc2);
				p.sendMessage(ChatColor.GREEN + "Lightning rod created successfully!");
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
			log.info("[Disasters] An error occured while attempting to remove a lighting rod.");
		}
	}
	
	public void loadLightningData() {
		this.reloadConfig();
		
		File file = new File(getDataFolder() + File.separator + "lightning.yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
				lightning = new YamlConfiguration();
				lightning.load(getDataFolder() + File.separator + "lightning.yml");
			} catch (FileNotFoundException e) {
				log.info("[Disasters] There was a problem loading data from the lightning.yml file");
				log.info("[Disasters] Plugin has been disabled...");
				pm.disablePlugin(this);
			} catch (IOException e) {
				log.info("[Disasters] There was a problem loading data from the lightning.yml file");
				log.info("[Disasters] Plugin has been disabled...");
				pm.disablePlugin(this);
			} catch (InvalidConfigurationException e) {
				log.info("[Disasters] There was a problem loading data from the lightning.yml file");
				log.info("[Disasters] Plugin has been disabled...");
				pm.disablePlugin(this);
			}
			
			lightning.addDefault("12", 20);
			lightning.options().copyDefaults(true);
			
			BlockReplaceData data = new BlockReplaceData(Material.getMaterial(4), 0, Material.getMaterial(0), 0);
			Disasters.lightningData.add(data);
			
			try {
				lightning.save(getDataFolder() + File.separator + "lightning.yml");
				log.info("[Disasters] Generated lightning.yml");
			} catch (IOException e) {
				log.info("[Disasters] Error cannot generate lightning.yml");
				log.info("[Disasters] Plugin has been disabled...");
				pm.disablePlugin(this);
			}
		} else {
			try {
				lightning = new YamlConfiguration();
				lightning.load(getDataFolder() + File.separator + "lightning.yml");
			} catch (FileNotFoundException e) {
				log.info("[Disasters] There was a problem loading data from the lightning.yml file");
				log.info("[Disasters] Plugin has been disabled...");
				pm.disablePlugin(this);
			} catch (IOException e) {
				log.info("[Disasters] There was a problem loading data from the lightning.yml file");
				log.info("[Disasters] Plugin has been disabled...");
				pm.disablePlugin(this);
			} catch (InvalidConfigurationException e) {
				log.info("[Disasters] There was a problem loading data from the lightning.yml file");
				log.info("[Disasters] Plugin has been disabled...");
				pm.disablePlugin(this);
			}
			
			for(String key : lightning.getKeys(true)) {
				//Parse Materials
				Material type = null;
				Material replace = null;
				int typeData = 0;
				int replaceData = 0;
				int intType = 0;
				int returnType = 0;
				String str = lightning.getString(key);
				
				if(key.contains(":")) {
					String[] values = key.split(":");
					if(values.length == 2) {
						String firstPart = values[0];
						String secondPart = values[1];
						//Calculate Material
						try {
							intType = Integer.parseInt(firstPart);
							type = Material.getMaterial(intType);
						} catch(NumberFormatException e) {
							type = Material.matchMaterial(firstPart);
						}
						//Calculate Data
						try {
							typeData = Integer.parseInt(secondPart);
						} catch(NumberFormatException e) {}
					}					
				} else {
					try {
						intType = Integer.parseInt(key);
						type = Material.getMaterial(intType);
					} catch(NumberFormatException e) {
						type = Material.matchMaterial(key);
					}
				}
				
				if(str.contains(":")) {
					String[] values = str.split(":");
					if(values.length == 2) {
						String firstPart = values[0];
						String secondPart = values[1];
						//Calculate Material
						try {
							returnType = Integer.parseInt(firstPart);
							replace = Material.getMaterial(returnType);
						} catch(NumberFormatException e) {
							replace = Material.matchMaterial(str);
						}
						//Calculate Data
						try {
							replaceData = Integer.parseInt(secondPart);
						} catch(NumberFormatException e) {}
					}
				} else {
					try {
						returnType = Integer.parseInt(str);
						replace = Material.getMaterial(returnType);
					} catch(NumberFormatException e) {
						replace = Material.matchMaterial(str);
					}
				}
				
				if(type == null || replace == null) {
					log.info("[Disasters] There was a problem parsing a section of the lightning.yml file");
					log.info("[Disasters] Either '" + key + "' or '" + str + "' is not valid");
				} else {
					BlockReplaceData data = new BlockReplaceData(type, typeData, replace, replaceData);
					Disasters.lightningData.add(data);
				}
			}
		}
	}
}