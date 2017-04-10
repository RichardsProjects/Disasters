package net.richardsprojects.disasters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;


public class Utils {

	private static Material[] flammable = {Material.WOOD, Material.LEAVES, Material.LEAVES_2,
			Material.LOG, Material.LOG_2, Material.WOOD_STEP, Material.ACACIA_FENCE, Material.FENCE,
			Material.BIRCH_FENCE, Material.DARK_OAK_FENCE, Material.SPRUCE_FENCE,
			Material.JUNGLE_FENCE, Material.VINE, Material.WOOL, Material.BOOKSHELF,
			Material.HAY_BLOCK, Material.ACACIA_STAIRS, Material.BIRCH_WOOD_STAIRS,
			Material.WOOD_STAIRS, Material.JUNGLE_WOOD_STAIRS, Material.DARK_OAK_STAIRS,
			Material.COAL_BLOCK, Material.YELLOW_FLOWER, Material.AIR};

	private static Material[] foliage = {Material.LONG_GRASS, Material.DEAD_BUSH,
			Material.YELLOW_FLOWER, Material.RED_ROSE, Material.DOUBLE_PLANT};

	/**
	 * Generates a random integer in the specified range.
	 *
	 * @param min minimum value
	 * @param max maximum value
	 * @return the generated random integer
	 */
	public static int randInt(int min, int max) {
	    return new Random().nextInt((max - min) + 1) + min;
	}

	/**
	 * Simple helper method that returns whether a Material is flammable.
	 *
	 * @param mat material to check
	 * @return whether or not it is flammable
	 */
	public static boolean isFlammable(Material mat) {
		ArrayList<Material> flammables = new ArrayList<>();
		Collections.addAll(flammables, flammable);
		return flammables.contains(mat);
	}

	/**
	 * Simple helper method that returns whether a Material is spreadable.
	 *
	 * @param mat material to check
	 * @return whether or not it is flammable
	 */
	public static boolean isSpreadable(Material mat) {
		ArrayList<Material> spreadables = new ArrayList<>();
		Collections.addAll(spreadables, flammable);
		spreadables.remove(Material.AIR);
		return spreadables.contains(mat);
	}
	
	public static boolean isFoliage(Material mat) {
		ArrayList<Material> foliages = new ArrayList<>();
		Collections.addAll(foliages, foliage);
		return foliages.contains(mat);
	}

	/**
	 * Simple helper method that translates messages that have color codes
	 * defined with an ampersand to traditional Minecraft formatting codes.
	 *
	 * @param msg message to translate codes on
	 * @return the newly formatted message
	 */
	public static String colorCodes(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	/**
	 * Returns the human readable name of a meteor by it's size int.
	 *
	 * @param size int 1-3
	 * @return human readable name of size
	 */
	public static String meteorSize(int size) {
		if (size == 1) {
			return "small-sized";
		} else if (size == 2) {
			return "medium-sized";
		} else if (size == 3) {
			return "large-sized";
		} else {
			return "";
		}
	}

	/**
	 * A utility method for calculating BlockData from a String.
	 *
	 * @return BlockData or null if there was a problem
	 */
	public static BlockData getBlockData(String str) {
		Material type = null;
		int typeData = 0;
		int intType;

		if (str.contains(":")) {
			String[] values = str.split(":");
			if (values.length == 2) {
				String material = values[0];
				String dataValue = values[1];

				// calculate Material
				try {
					intType = Integer.parseInt(material);
					type = Material.getMaterial(intType);
				} catch (NumberFormatException e) {
					type = Material.matchMaterial(material);
				}

				// determine data value (empty catch block is intended)
				try {
					typeData = Integer.parseInt(dataValue);
				} catch (NumberFormatException e) {}
			}
		} else {
			try {
				intType = Integer.parseInt(str);
				type = Material.getMaterial(intType);
			} catch (NumberFormatException e) {
				type = Material.matchMaterial(str);
			}
		}

		if (type != null) {
			return new BlockData(type, typeData);
		} else {
			return null;
		}
	}
}
