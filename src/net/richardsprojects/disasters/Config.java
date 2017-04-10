package net.richardsprojects.disasters;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Handles methods and storing data related to the plugin's configuration.
 *
 * @author RichardB122
 * @version 4/3/17
 */
public class Config {

    public static boolean meteorDamageTerrain = false;
    public static boolean lightningstormsEnabled = true;
    public static boolean acidRainEnabled = true;
    public static boolean wildfiresEnabled = true;
    public static boolean meteorsEnabled = true;

    public static String acidRainCurrent = "&2&lAn acid rain storm is currently going on!";
    public static String acidRainStart = "&2&lAn acid rain storm has started!";
    public static String acidRainStop = "&2&lThe acid rain storm has stopped.";
    public static String texturePack = "https://dl.dropboxusercontent.com/s/boafxuxo7zcwrq3/acidRain.zip?dl=0";
    public static int acidRainDesintegrateTicks = 20;

    public static String lightningStormStart = "&7&lA lightning storm has started!";
    public static String lightningStormStop = "&7&lThe lightning storm has stopped.";
    public static int lightningStormChance = 1;
    public static int lightningStormChanceMax = 5;
    public static int lightningStormTickTime = 12000;
    public static int lightningStormDuration = 1200;

    public static int meteorChance = 1;
    public static int meteorChanceMax = 5;
    public static int meteorTickTime = 12000;
    public static String meteorMessage = "&4&lA [SIZE] meteor is falling near you.";
    public static int smallMeteor = 2;
    public static int mediumMeteor = 4;
    public static int largeMeteor = 6;

    public static String worldName = "world";

    public static int wildfireChance = 1;
    public static int wildfireChanceMax = 5;
    public static int wildfireTickTime = 12000;
    public static boolean wildfiresSpread = false;

    /**
     * Helper method that loads data from the configuration.
     */
    public static void loadConfig() {
        File file = new File(Disasters.instance.dataFolder + File.separator + "config.yml");
        YamlConfiguration config;

        if(!file.exists()) {
            try {
                PrintWriter out = new PrintWriter(new File(Disasters.instance.dataFolder
                        + File.separator + "config.yml"));
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        Disasters.class.getResourceAsStream("/config.yml")));

                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    currentLine = currentLine + "\n";
                    out.append(currentLine);
                }
                out.close();
                reader.close();

                config = new YamlConfiguration();
                config.load(new File(Disasters.instance.dataFolder + File.separator + "config.yml"));
            } catch (Exception e) {
                Disasters.log.info("There was an error setting up the config file...");
                e.printStackTrace();
            }

            Disasters.log.info("Generated config.yml");
        } else {
            config = (YamlConfiguration) Disasters.instance.getConfig();

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
            meteorDamageTerrain = config.getBoolean("meteorsDamageTerrain");
            meteorsEnabled = config.getBoolean("meteorsEnabled");
            lightningstormsEnabled = config.getBoolean("lightningstormsEnabled");
            acidRainEnabled = config.getBoolean("acidRainEnabled");
            wildfiresEnabled = config.getBoolean("wildfiresEnabled");
            wildfiresSpread = config.getBoolean("wildfiresSpread");
        }
    }
}
