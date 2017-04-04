package net.richardsprojects.disasters;

import org.bukkit.Material;

/**
 * A simple wrapper class for block Material and type.
 *
 * @author RichardB122
 * @version 4/3/17
 */
public class BlockData {

    private Material type;
    private int typeData;

    /**
     * Creates a new instance of BlockData with a type and type data. This
     * constructor automatically alters typeData to range 0 to 15 inclusive.
     *
     * @param type the block Material
     * @param typeData the data of the block for blocks like wool
     */
    public BlockData(Material type, int typeData) {
        // validate data
        if (typeData > 15) typeData = 15;
        if (typeData < 0) typeData = 0;

        this.type = type;
        this.typeData = typeData;
    }

    public Material getType() {
        return this.type;
    }

    public int getTypeData() {
        return this.typeData;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof BlockData) {
            BlockData compare = (BlockData) object;
            return compare.getType() == getType() && compare.getTypeData() == getTypeData();
        } else {
            return false;
        }
    }

}
