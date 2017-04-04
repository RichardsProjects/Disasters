package net.richardsprojects.disasters.events;

import java.util.ArrayList;
import java.util.Iterator;

import net.richardsprojects.disasters.BlockData;
import net.richardsprojects.disasters.Config;
import net.richardsprojects.disasters.Disasters;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class SPEvent implements Listener {
	
	private Disasters plugin;
	
	public SPEvent(Disasters plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPlace(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getClickedBlock().getWorld().getName().equals(Config.worldName)) {
				if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getPlayer().getItemInHand() != null) {
					if(e.getPlayer().getItemInHand().getType() == Material.SIGN) {
						//Check if there is iron behind sign
						if(e.getClickedBlock() != null) {
							if(e.getClickedBlock().getType() == Material.IRON_BLOCK) {
								Block ironBlock = e.getClickedBlock();
								final Location loc = ironBlock.getLocation();
								Location loc2 = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());
								Location loc3 = new Location(loc.getWorld(), loc2.getX(), loc2.getY() + 1, loc2.getZ());
								Location loc4 = new Location(loc.getWorld(), loc3.getX(), loc3.getY() + 1, loc3.getZ());
								if(loc2.getBlock().getType() == Material.IRON_BLOCK &&
									loc3.getBlock().getType() == Material.IRON_BLOCK &&
									loc4.getBlock().getType() == Material.IRON_BLOCK) {
									if(loc.getWorld().getHighestBlockAt((int) loc.getX(),(int) loc.getZ()).getY() == ((int) loc4.getY()) + 1) {
										plugin.addLightningRod(loc, e.getPlayer());
										
										final Player player = e.getPlayer();
										new BukkitRunnable() {
											public void run() {
												//Possible sign locations
												Location poss1 = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() + 1);
												Location poss2 = new Location(loc.getWorld(), loc.getX() + 1, loc.getY(), loc.getZ());
												Location poss3 = new Location(loc.getWorld(), loc.getX() - 1, loc.getY(), loc.getZ());
												Location poss4 = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() - 1);
																							
												if(poss1.getBlock().getType() == Material.WALL_SIGN) {
													Sign sign = (Sign) poss1.getBlock().getState();
													sign.setLine(0, "[Lightning Rod]");
													sign.update();
												} else if(poss2.getBlock().getType() == Material.WALL_SIGN) {
													Sign sign = (Sign) poss2.getBlock().getState();
													sign.setLine(0, "[Lightning Rod]");
													sign.update();										
												} else if(poss3.getBlock().getType() == Material.WALL_SIGN) {
													Sign sign = (Sign) poss3.getBlock().getState();
													sign.setLine(0, "[Lightning Rod]");
													sign.update();										
												} else if(poss4.getBlock().getType() == Material.WALL_SIGN) {
													Sign sign = (Sign) poss4.getBlock().getState();
													sign.setLine(0, "[Lightning Rod]");
													sign.update();										
												}
											}
										}.runTaskLater(plugin, 5);
									} else {
										e.getPlayer().sendMessage(ChatColor.RED + "If you are attempting to make " +
										"a lightning rod the highest iron block must be at the highest point on the map.");
									}
								} else {
									e.getPlayer().sendMessage(ChatColor.RED + "A lightning rod must be exactly 4" +
										" blocks tall and the sign must be placed on the bottom block");
								}
							}
						}
					}
				}
			}
		}
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onSignChange(SignChangeEvent e) {
		int signX = e.getBlock().getX();
		int signZ = e.getBlock().getZ();
		World w = e.getBlock().getWorld();
		if(e.getLine(0).equals("") && e.getLine(1).equals("") &&
			e.getLine(2).equals("") && e.getLine(3).equals("")) {
			//Possible iron block locations
			int poss1X = signX;
			int poss1Z = signZ + 1;				
			int poss2X = signX + 1;
			int poss2Z = signZ;				
			int poss3X = signX;
			int poss3Z = signZ - 1;				
			int poss4X = signX - 1;
			int poss4Z = signZ;				
				
			if(new Location(w, poss1X, e.getBlock().getY(), poss1Z).getBlock().getType() == Material.IRON_BLOCK) {
				e.setCancelled(true);
			} else if(new Location(w, poss2X, e.getBlock().getY(), poss2Z).getBlock().getType() == Material.IRON_BLOCK) {
				e.setCancelled(true);
			} else if(new Location(w, poss3X, e.getBlock().getY(), poss3Z).getBlock().getType() == Material.IRON_BLOCK) {
				e.setCancelled(true);
			} else if(new Location(w, poss4X, e.getBlock().getY(), poss4Z).getBlock().getType() == Material.IRON_BLOCK) {
				e.setCancelled(true);
			}
		} else {
			//Possible iron block locations
			int poss1X = signX;
			int poss1Z = signZ + 1;				
			int poss2X = signX + 1;
			int poss2Z = signZ;				
			int poss3X = signX;
			int poss3Z = signZ - 1;				
			int poss4X = signX - 1;
			int poss4Z = signZ;
			
			if(new Location(w, poss1X, e.getBlock().getY(), poss1Z).getBlock().getType() == Material.IRON_BLOCK) {
				String key = poss1X + "|" + poss1Z;
				plugin.removeLightningRod(key);
			} else if(new Location(w, poss2X, e.getBlock().getY(), poss2Z).getBlock().getType() == Material.IRON_BLOCK) {
				String key = poss2X + "|" + poss2Z;
				plugin.removeLightningRod(key);
			} else if(new Location(w, poss3X, e.getBlock().getY(), poss3Z).getBlock().getType() == Material.IRON_BLOCK) {
				String key = poss3X + "|" + poss3Z;
				plugin.removeLightningRod(key);
			} else if(new Location(w, poss4X, e.getBlock().getY(), poss4Z).getBlock().getType() == Material.IRON_BLOCK) {
				String key = poss4X + "|" + poss4Z;
				plugin.removeLightningRod(key);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onIronBlockBreak(BlockBreakEvent e) {
		if(e.getBlock().getType() == Material.IRON_BLOCK) {
			int ironBlockX = e.getBlock().getX();
			int ironBlockZ = e.getBlock().getZ();


			Iterator<Location> it = Disasters.lightningRodList.iterator();
			while (it.hasNext()) {
				Location loc = it.next();
				if(loc.getBlockX() == ironBlockX && loc.getBlockZ() == ironBlockZ) {
					int y = loc.getWorld().getHighestBlockYAt(ironBlockX, ironBlockZ) - 1;
					if(e.getBlock().getY() == y || e.getBlock().getY() == y-1
							|| e.getBlock().getY() == y-2 || e.getBlock().getY() == y-3) {
						// remove the lightning rod data and find and change the sign
						plugin.removeLightningRod(ironBlockX + "|" + ironBlockZ);
						Location bottom = new Location(e.getBlock().getWorld(), ironBlockX, (y-3), ironBlockZ);
						
						e.getPlayer().sendMessage(ChatColor.RED + "You have just dismantled the lightning rod.");
						
						// possible sign locations
						Location poss1 = new Location(bottom.getWorld(), bottom.getX(), bottom.getY(), bottom.getZ() + 1);
						Location poss2 = new Location(bottom.getWorld(), bottom.getX() + 1, bottom.getY(), bottom.getZ());
						Location poss3 = new Location(bottom.getWorld(), bottom.getX() - 1, bottom.getY(), bottom.getZ());
						Location poss4 = new Location(bottom.getWorld(), bottom.getX(), bottom.getY(), bottom.getZ() - 1);
																	
						if(poss1.getBlock().getType() == Material.WALL_SIGN) {
							Sign sign = (Sign) poss1.getBlock().getState();
							sign.setLine(0, "");
							sign.update();
						} else if(poss2.getBlock().getType() == Material.WALL_SIGN) {
							Sign sign = (Sign) poss2.getBlock().getState();
							sign.setLine(0, "");
							sign.update();										
						} else if(poss3.getBlock().getType() == Material.WALL_SIGN) {
							Sign sign = (Sign) poss3.getBlock().getState();
							sign.setLine(0, "");
							sign.update();										
						} else if(poss4.getBlock().getType() == Material.WALL_SIGN) {
							Sign sign = (Sign) poss4.getBlock().getState();
							sign.setLine(0, "");
							sign.update();										
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onSignBreak(BlockBreakEvent e) {
		if(e.getBlock().getType() == Material.WALL_SIGN) {
			int signX = e.getBlock().getX();
			int signZ = e.getBlock().getZ();
			int signY = e.getBlock().getY();
			
			int ironBlockX = 0;
			int ironBlockZ = 0;
			
			//Calculate possible positions for iron block
			boolean ironBlock = false;
			World w = e.getBlock().getWorld();
			Location poss1 = new Location(w, signX, signY, signZ + 1);
			Location poss2 = new Location(w, signX + 1, signY, signZ);
			Location poss3 = new Location(w, signX - 1, signY, signZ);
			Location poss4 = new Location(w, signX, signY, signZ - 1);
			
			if(poss1.getBlock().getType() == Material.IRON_BLOCK) {
				ironBlock = true;
				ironBlockX = poss1.getBlockX();
				ironBlockZ = poss1.getBlockZ();
			} else if(poss2.getBlock().getType() == Material.IRON_BLOCK) {
				ironBlock = true;
				ironBlockX = poss2.getBlockX();
				ironBlockZ = poss2.getBlockZ();
			} else if(poss3.getBlock().getType() == Material.IRON_BLOCK) {
				ironBlock = true;
				ironBlockX = poss3.getBlockX();
				ironBlockZ = poss3.getBlockZ();
			} else if(poss4.getBlock().getType() == Material.IRON_BLOCK) {
				ironBlock = true;
				ironBlockX = poss4.getBlockX();
				ironBlockZ = poss4.getBlockZ();
			}
			
			if(ironBlock) {
				// check if iron block is a lightning rod
				Iterator<Location> it = Disasters.lightningRodList.iterator();
				while (it.hasNext()) {
					Location loc = it.next();
					if(loc.getBlockX() == ironBlockX && loc.getBlockZ() == ironBlockZ) {
						plugin.removeLightningRod(ironBlockX + "|" + ironBlockZ);
						e.getPlayer().sendMessage(ChatColor.RED + "You have just dismantled the lightning rod.");
						return;
					}
				}
			}
		}
	}
}
