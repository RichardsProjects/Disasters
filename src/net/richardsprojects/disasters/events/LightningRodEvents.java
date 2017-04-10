package net.richardsprojects.disasters.events;

import java.util.ArrayList;
import java.util.Iterator;

import net.richardsprojects.disasters.Config;
import net.richardsprojects.disasters.Disasters;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * An event handler for a collection of events related to the placement and
 * destruction of lightning rods.
 *
 * @author RichardB122
 * @version 4/6/17
 */
public class LightningRodEvents implements Listener {
	
	private Disasters plugin;
	
	public LightningRodEvents(Disasters plugin) {
		this.plugin = plugin;
	}

	/**
	 * An event for detecting signs are placed on 4 block tall iron pillars
	 * and creating a lightning rod in response.
	 *
	 * @param e the event
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPlace(PlayerInteractEvent e) {
		// stop right now if the event is not in the Disasters world
		if(!e.getPlayer().getWorld().getName().equals(Config.worldName)) return;

		// check if they had a sign in their hand and right clicked
		if(!(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getPlayer().getItemInHand() != null
				&& e.getPlayer().getItemInHand().getType() == Material.SIGN)) {
			return;
		}

		// check if there is iron behind sign
		if(!(e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.IRON_BLOCK)) {
			return;
		}

		Player player = e.getPlayer();

		Block ironBlock = e.getClickedBlock();
		final Location loc = ironBlock.getLocation();
		Location loc2 = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());
		Location loc3 = new Location(loc.getWorld(), loc2.getX(), loc2.getY() + 1, loc2.getZ());
		Location loc4 = new Location(loc.getWorld(), loc3.getX(), loc3.getY() + 1, loc3.getZ());

		// check that every point that should be a lighting rod is made of iron
		if(isBlockIron(loc2) && isBlockIron(loc3) && isBlockIron(loc4)) {

			// make sure they are not trying to make an underground lightning rod
			if(loc.getWorld().getHighestBlockAt((int) loc.getX(),(int) loc.getZ()).getY()
					== ((int) loc4.getY()) + 1) {
				// add the lightning rod and update the sign
				plugin.addLightningRod(loc, player);
				new UpdateSign(loc, "[Lightning Rod]").runTaskLater(plugin, 5);
			} else {
				// TODO: Make this message configurable
				String msg = ChatColor.RED + "If you are attempting to make a lightning rod the " +
						"highest iron block must be at the highest point on the map.";
				e.getPlayer().sendMessage(msg);
			}
		} else {
			// TODO: Make this message configurable
			String msg = ChatColor.RED + "A lightning rod must be exactly 4 blocks tall and the " +
					"sign must be placed on the bottom block";
			player.sendMessage(msg);
		}
		
	}

	/**
	 * Removes a lightning rod if the game created one when they placed iron
	 * blocks but they did not place a blank sign, meaning they didn't want
	 * a lightning rod there.
	 *
	 * @param e the SignChangeEvent
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onSignChange(SignChangeEvent e) {
		int signX = e.getBlock().getX();
		int signZ = e.getBlock().getZ();
		World w = e.getBlock().getWorld();
		Player player = e.getPlayer();

		// generate possible iron block locations
		Location loc = new Location(w, signX, e.getBlock().getY(), signZ + 1);
		Location loc2 = new Location(w, signX + 1, e.getBlock().getY(), signZ);
		Location loc3 = new Location(w, signX, e.getBlock().getY(), signZ - 1);
		Location loc4 = new Location(w, signX - 1, e.getBlock().getY(), signZ);

		// check if there is a lightning rod at the locations
		String key = "";
		if(isBlockIron(loc)) key = loc.getBlockX() + "|" + loc.getBlockZ();
		else if(isBlockIron(loc2)) key = loc2.getBlockX() + "|" + loc2.getBlockZ();
		else if(isBlockIron(loc3)) key = loc3.getBlockX() + "|" + loc3.getBlockZ();
		else if(isBlockIron(loc4)) key = loc4.getBlockX() + "|" + loc4.getBlockZ();

		if(!(e.getLine(0).equals("") && e.getLine(1).equals("") &&
			e.getLine(2).equals("") && e.getLine(3).equals(""))) {
			// remove the data about the lighting rod because they weren't intending to place one
			plugin.removeLightningRod(key);
		} else {
			// keep the lighting rod and inform the player it was created successfully
			if (plugin.isLightningRodHere(key)) {
				// TODO: Make this message configurable
				player.sendMessage(ChatColor.GREEN + "Lightning rod created successfully!");
			}
			e.setCancelled(true); // cancel event so text is not changed
		}
	}

	/**
	 * Handles removing a lightning rod from the list and updating the sign if
	 * an iron block in a lightning rod is broken.
	 *
	 * @param e the BlockBreakEvent
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onIronBlockBreak(BlockBreakEvent e) {
		if(e.getBlock().getType() == Material.IRON_BLOCK) {
			int ironX = e.getBlock().getX();
			int ironZ = e.getBlock().getZ();
			Player player = e.getPlayer();

			// check if there is a lightning rod at this location
			String key = ironX + "|" + ironZ;
			if (plugin.isLightningRodHere(key)) {
				int y = player.getWorld().getHighestBlockYAt(ironX, ironZ) - 1;
				Location bottom = new Location(e.getBlock().getWorld(), ironX, (y - 3), ironZ);

				// check to see that it is at the highest point
				if(e.getBlock().getY() == y || e.getBlock().getY() == y-1
						|| e.getBlock().getY() == y-2 || e.getBlock().getY() == y-3) {
					// TODO: Make this configurable
					String msg = ChatColor.RED + "You have just dismantled the lightning rod.";
					player.sendMessage(msg);
					new UpdateSign(bottom, "").runTaskLater(plugin, 5); // clear sign
					plugin.removeLightningRod(key);
				}
			}
		}
	}

	/**
	 * An event that handles signs breaking an checks if that sign belonged to
	 * a lightning rod. If it did it informs the player and removes the
	 * lightning rod.
	 *
	 * @param e
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onSignBreak(BlockBreakEvent e) {
		if(e.getBlock().getType() == Material.WALL_SIGN) {
			Player player = e.getPlayer();

			// calculate possible positions for iron block
			int signX = e.getBlock().getX();
			int signZ = e.getBlock().getZ();
			int signY = e.getBlock().getY();
			Integer ironBlockX = null;
			Integer ironBlockZ = null;
			World w = e.getBlock().getWorld();
			Location poss1 = new Location(w, signX, signY, signZ + 1);
			Location poss2 = new Location(w, signX + 1, signY, signZ);
			Location poss3 = new Location(w, signX - 1, signY, signZ);
			Location poss4 = new Location(w, signX, signY, signZ - 1);
			
			if(isBlockIron(poss1)) {
				ironBlockX = poss1.getBlockX();
				ironBlockZ = poss1.getBlockZ();
			} else if(isBlockIron(poss2)) {
				ironBlockX = poss2.getBlockX();
				ironBlockZ = poss2.getBlockZ();
			} else if(isBlockIron(poss3)) {
				ironBlockX = poss3.getBlockX();
				ironBlockZ = poss3.getBlockZ();
			} else if(isBlockIron(poss4)) {
				ironBlockX = poss4.getBlockX();
				ironBlockZ = poss4.getBlockZ();
			}

			// check if iron block is a lightning rod
			if(ironBlockX != null && ironBlockZ != null) {
				String key = ironBlockX + "|" + ironBlockZ;
				if (plugin.isLightningRodHere(key)) {

					plugin.removeLightningRod(ironBlockX + "|" + ironBlockZ); // remove

					// TODO: Make this configurable
					String msg = ChatColor.RED + "You have just dismantled the lightning rod.";
					player.sendMessage(msg); // inform player
				}
			}
		}
	}

	/**
	 * Helper method that returns if a block is an IRON_BLOCK.
	 *
	 * @param loc the Location to check
	 * @return whether or not it is an IRON_BLOCK
	 */
	private boolean isBlockIron(Location loc) {
		return (loc.getBlock() != null && loc.getBlock().getType() == Material.IRON_BLOCK);
	}

	/**
	 * A BukkitRunnable designed to update signs after a lightning rod is added.
	 *
	 * @author RichardB122
	 * @version 4/6/17
	 */
	private class UpdateSign extends BukkitRunnable {

		private Location loc;
		private String newText;

		public UpdateSign(Location loc, String newText) {
			this.loc = loc;
			this.newText = newText;
		}

		@Override
		public void run() {
			// possible sign locations
			Location poss1 = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() + 1);
			Location poss2 = new Location(loc.getWorld(), loc.getX() + 1, loc.getY(), loc.getZ());
			Location poss3 = new Location(loc.getWorld(), loc.getX() - 1, loc.getY(), loc.getZ());
			Location poss4 = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() - 1);

			Sign sign = null;
			if(poss1.getBlock().getType() == Material.WALL_SIGN) {
				sign = (Sign) poss1.getBlock().getState();
			} else if(poss2.getBlock().getType() == Material.WALL_SIGN) {
				sign = (Sign) poss2.getBlock().getState();
			} else if(poss3.getBlock().getType() == Material.WALL_SIGN) {
				sign = (Sign) poss3.getBlock().getState();
			} else if(poss4.getBlock().getType() == Material.WALL_SIGN) {
				sign = (Sign) poss4.getBlock().getState();
			}

			if (sign != null) {
				// clear text if it is empty
				if (newText.equals("")) {
					sign.setLine(0, "");
					sign.setLine(1, "");
					sign.setLine(2, "");
					sign.setLine(3, "");
				} else {
					sign.setLine(0, newText);
				}
				sign.update();
			}
		}
	}
}