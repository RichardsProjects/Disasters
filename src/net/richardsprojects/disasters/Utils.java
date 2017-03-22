package net.richardsprojects.disasters;

import java.util.Random;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;


public class Utils {
	
	public static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	public static boolean isFlammable(Material mat) {
		if(mat == Material.WOOD) {
			return true;
		} else if(mat == Material.LEAVES) {
			return true;			
		} else if(mat == Material.LEAVES_2) {
			return true;
		} else if(mat == Material.LOG) {
			return true;
		} else if(mat == Material.LOG_2) {
			return true;
		} else if(mat == Material.WOOD_STEP) {
			return true;
		} else if(mat == Material.ACACIA_FENCE) {
			return true;
		} else if(mat == Material.FENCE) {
			return true;
		} else if(mat == Material.BIRCH_FENCE) {
			return true;
		} else if(mat == Material.DARK_OAK_FENCE) {
			return true;
		} else if(mat == Material.SPRUCE_FENCE) {
			return true;
		} else if(mat == Material.JUNGLE_FENCE) {
			return true;
		} else if(mat == Material.VINE) {
			return true;
		} else if(mat == Material.WOOL) {
			return true;
		} else if(mat == Material.BOOKSHELF) {
			return true;
		} else if(mat == Material.HAY_BLOCK) {
			return true;
		} else if(mat == Material.ACACIA_STAIRS) {
			return true;
		} else if(mat == Material.BIRCH_WOOD_STAIRS) {
			return true;
		} else if(mat == Material.WOOD_STAIRS) {
			return true;
		} else if(mat == Material.JUNGLE_WOOD_STAIRS) {
			return true;
		} else if(mat == Material.DARK_OAK_STAIRS) {
			return true;
		} else if(mat == Material.COAL_BLOCK) {
			return true;
		} else if(mat == Material.YELLOW_FLOWER) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isSpreadable(Material mat) {
		if(mat == Material.WOOD) {
			return true;
		} else if(mat == Material.LEAVES) {
			return true;			
		} else if(mat == Material.LEAVES_2) {
			return true;
		} else if(mat == Material.LOG) {
			return true;
		} else if(mat == Material.LOG_2) {
			return true;
		} else if(mat == Material.WOOD_STEP) {
			return true;
		} else if(mat == Material.ACACIA_FENCE) {
			return true;
		} else if(mat == Material.FENCE) {
			return true;
		} else if(mat == Material.BIRCH_FENCE) {
			return true;
		} else if(mat == Material.DARK_OAK_FENCE) {
			return true;
		} else if(mat == Material.SPRUCE_FENCE) {
			return true;
		} else if(mat == Material.JUNGLE_FENCE) {
			return true;
		} else if(mat == Material.VINE) {
			return true;
		} else if(mat == Material.WOOL) {
			return true;
		} else if(mat == Material.BOOKSHELF) {
			return true;
		} else if(mat == Material.HAY_BLOCK) {
			return true;
		} else if(mat == Material.ACACIA_STAIRS) {
			return true;
		} else if(mat == Material.BIRCH_WOOD_STAIRS) {
			return true;
		} else if(mat == Material.WOOD_STAIRS) {
			return true;
		} else if(mat == Material.JUNGLE_WOOD_STAIRS) {
			return true;
		} else if(mat == Material.DARK_OAK_STAIRS) {
			return true;
		} else if(mat == Material.COAL_BLOCK) {
			return true;
		} else if(mat == Material.YELLOW_FLOWER) {
			return true;
		} else if(mat == Material.GRASS) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isFoliage(Material mat) {
		if(mat == Material.LONG_GRASS) {
			return true;
		} else if(mat == Material.DEAD_BUSH) {
			return true;
		} else if(mat == Material.YELLOW_FLOWER) {
			return true;
		} else if(mat == Material.RED_ROSE) {
			return true;
		} else if(mat == Material.DOUBLE_PLANT) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String colorCodes(String msg) {
		msg = msg.replace("&0", ChatColor.BLACK + "");
		msg = msg.replace("&1", ChatColor.DARK_BLUE + "");
		msg = msg.replace("&2", ChatColor.DARK_GREEN + "");
		msg = msg.replace("&3", ChatColor.DARK_AQUA + "");
		msg = msg.replace("&4", ChatColor.DARK_RED + "");
		msg = msg.replace("&5", ChatColor.DARK_PURPLE + "");
		msg = msg.replace("&6", ChatColor.GOLD + "");
		msg = msg.replace("&7", ChatColor.GRAY + "");
		msg = msg.replace("&8", ChatColor.DARK_GRAY + "");
		msg = msg.replace("&9", ChatColor.BLUE + "");
		msg = msg.replace("&a", ChatColor.GREEN + "");
		msg = msg.replace("&b", ChatColor.AQUA + "");
		msg = msg.replace("&c", ChatColor.RED + "");
		msg = msg.replace("&d", ChatColor.LIGHT_PURPLE + "");
		msg = msg.replace("&e", ChatColor.YELLOW + "");
		msg = msg.replace("&f", ChatColor.WHITE + "");
		msg = msg.replace("&l", ChatColor.BOLD + "");
		msg = msg.replace("&m", ChatColor.STRIKETHROUGH + "");
		msg = msg.replace("&n", ChatColor.UNDERLINE + "");
		msg = msg.replace("&o", ChatColor.ITALIC + "");
		msg = msg.replace("&r", ChatColor.RESET + "");
		return msg;
	}
	
	public static String meteorSize(int size) {
		if(size == 1) {
			return "small-sized";
		} else if(size == 2) {
			return "medium-sized";
		} else if(size == 3) {
			return "large-sized";
		} else {
			return "";
		}
	}
	
	public static String meteorNameSize(int size) {
		if(size == 1) {
			return "SmallMeteor";
		} else if(size == 2) {
			return "MediumMeteor";
		} else if(size == 3) {
			return "LargeMeteor";
		} else {
			return "";
		}
	}
}
