package net.richardsprojects.disasters.runnables;

import net.md_5.bungee.api.ChatColor;
import net.richardsprojects.disasters.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StartWildfire extends BukkitRunnable {

	private Block block;
	private Player player;
	
	public StartWildfire(Block block, Player player) {
		this.block = block;
		this.player = player;
	}
	
	public void run() {
		Location loc = block.getLocation();
		loc.setY(loc.getY() + 1);
		block = loc.getBlock();
		block.setType(Material.FIRE);
		
		//TODO: Fix where fires start
		
		//Start fire at z - 1
		loc = block.getLocation();
		loc.setZ(loc.getZ() - 1);
		Location loc2 = loc;
		loc2.setY(loc2.getY() - 1);							
		if((loc.getBlock().getType() == Material.AIR || Utils.isFoliage(loc.getBlock().getType())) && Utils.isSpreadable(loc2.getBlock().getType())) {
			loc.getBlock().setType(Material.FIRE);
		}
		
		//Start fire at z + 1
		loc = block.getLocation();
		loc.setZ(loc.getZ() + 1);
		loc2 = loc;
		loc2.setY(loc2.getY() - 1);							
		if((loc.getBlock().getType() == Material.AIR || Utils.isFoliage(loc.getBlock().getType())) && Utils.isSpreadable(loc2.getBlock().getType())) {
			loc.getBlock().setType(Material.FIRE);
		}
		
		//Start fire at z - 2
		loc = block.getLocation();
		loc.setZ(loc.getZ() - 2);
		loc2 = loc;
		loc2.setY(loc2.getY() - 1);							
		if((loc.getBlock().getType() == Material.AIR || Utils.isFoliage(loc.getBlock().getType())) && Utils.isSpreadable(loc2.getBlock().getType())) {
			loc.getBlock().setType(Material.FIRE);
		}
		
		//Start fire at z + 2
		loc = block.getLocation();
		loc.setZ(loc.getZ() + 2);
		loc2 = loc;
		loc2.setY(loc2.getY() - 1);							
		if((loc.getBlock().getType() == Material.AIR || Utils.isFoliage(loc.getBlock().getType())) && Utils.isSpreadable(loc2.getBlock().getType())) {
			loc.getBlock().setType(Material.FIRE);
		}
		
		//Start fire at x - 1
		loc = block.getLocation();
		loc.setX(loc.getX() - 1);
		loc2 = loc;
		loc2.setY(loc2.getY() - 1);							
		if((loc.getBlock().getType() == Material.AIR || Utils.isFoliage(loc.getBlock().getType())) && Utils.isSpreadable(loc2.getBlock().getType())) {
			loc.getBlock().setType(Material.FIRE);
		}
		
		//Start fire at x + 1
		loc = block.getLocation();
		loc.setX(loc.getX() + 1);
		loc2 = loc;
		loc2.setY(loc2.getY() - 1);							
		if((loc.getBlock().getType() == Material.AIR || Utils.isFoliage(loc.getBlock().getType())) && Utils.isSpreadable(loc2.getBlock().getType())) {
			loc.getBlock().setType(Material.FIRE);
		}

		//Start fire at x - 2
		loc = block.getLocation();
		loc.setX(loc.getX() - 2);
		loc2 = loc;
		loc2.setY(loc2.getY() - 1);							
		if((loc.getBlock().getType() == Material.AIR || Utils.isFoliage(loc.getBlock().getType())) && Utils.isSpreadable(loc2.getBlock().getType())) {
			loc.getBlock().setType(Material.FIRE);
		}
		
		//Start fire at x + 2
		loc = block.getLocation();
		loc.setX(loc.getX() + 2);
		loc2 = loc;
		loc2.setY(loc2.getY() - 1);							
		if((loc.getBlock().getType() == Material.AIR || Utils.isFoliage(loc.getBlock().getType())) && Utils.isSpreadable(loc2.getBlock().getType())) {
			loc.getBlock().setType(Material.FIRE);
		}
		
		//Update Fire spread game rule
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doFireTick true");
		
		//Message player that a fire has started near them
		player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "A wildfire has started near your position.");	
	}

}
