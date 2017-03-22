package net.richardsprojects.disasters;

import org.bukkit.Material;

public class BlockReplaceData {

	private Material type;
	private int typeData;
	private Material replace;
	private int replaceData;
	
	public BlockReplaceData(Material type, int typeData, Material replace, int replaceData) {
		this.type = type;
		this.replace = replace;
		if(!(typeData > 15 || typeData < 0)) this.typeData = typeData;
		if(!(replaceData > 15 || replaceData < 0)) this.replaceData = replaceData;
	}
	
	public Material getType() {
		return type;
	}
	
	public Material getReplace() {
		return replace;
	}
	
	public int getReplaceData() {
		return replaceData;
	}
	
	public int getTypeData() {
		return typeData;
	}
	
}
