package net.richardsprojects.disasters.runnables;

import net.md_5.bungee.api.ChatColor;
import net.richardsprojects.disasters.Config;
import net.richardsprojects.disasters.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * A BukkitRunnable that starts a wildfire at the position passed.
 *
 * @author RichardB122
 * @version 4/9/17
 */
public class StartWildfire extends BukkitRunnable {

	private Block block;
	private Player player;

	public StartWildfire(Block block, Player player) {
		this.block = block;
		this.player = player;
	}

	public void run() {
		Location loc = block.getLocation();
		World w = loc.getWorld();

		// start a fire at blocks within proximity
		for (int x = (int) loc.getX() - 2; x <= (int) loc.getX() + 2; x++) {
			for (int z = (int) loc.getZ() - 2; z <= (int) loc.getZ() + 2; z++) {
				Block cBlock = w.getBlockAt(x, w.getHighestBlockYAt(x, z) - 1, z);
				Block blockAbove = w.getBlockAt(cBlock.getX(), cBlock.getY() + 1, cBlock.getZ());
				blockAbove.setType(Material.FIRE);
			}
		}

		// update Fire spread gamerule based on Config value
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doFireTick " +
				Config.wildfiresSpread);

		// message player that a fire has started near them
		// TODO: Make message configurable
		String msg = ChatColor.GOLD + "" + ChatColor.BOLD + "A wildfire has started near your " +
				"position.";
		player.sendMessage(msg);
	}

}
