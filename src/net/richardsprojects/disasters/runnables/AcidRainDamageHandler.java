package net.richardsprojects.disasters.runnables;

import net.richardsprojects.disasters.Disasters;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AcidRainDamageHandler extends BukkitRunnable {

	private Disasters plugin;
	
	public AcidRainDamageHandler(Disasters plugin) {
		this.plugin = plugin;
	}
	
	public void run() {
		if(plugin.getServer().getWorld(Disasters.worldName) != null) {
			if(Disasters.currentlyRaining) {
				World world = plugin.getServer().getWorld(Disasters.worldName);
				for(Player player : world.getPlayers()) {
					//Check that they are not in a desert biome
					if(player.getLocation().getBlock().getBiome() != Biome.DESERT 
							&& player.getLocation().getBlock().getBiome() != Biome.DESERT_HILLS 
							&& player.getLocation().getBlock().getBiome() != Biome.DESERT_HILLS)
					{
						//Check if there is a block above them
						Location loc = player.getLocation();
						boolean blockAboveThem = false;
						int blockY = loc.getBlockY();
						while(blockY <= 256) {
							Block b = player.getLocation().getWorld().getBlockAt(loc.getBlockX(), blockY, loc.getBlockZ());
							if(b.getType() != Material.AIR) {
								blockAboveThem = true;
								break;
							}
							blockY = blockY + 1;
						}
						if(!blockAboveThem) player.damage(1);
					}
				}
			} else {
				this.cancel();
			}
		}
	}

}
