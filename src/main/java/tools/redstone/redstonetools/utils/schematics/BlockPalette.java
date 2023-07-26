package tools.redstone.redstonetools.utils.schematics;

import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;

public class BlockPalette extends HashMap<String,Integer> {

    public BlockPalette(){};
    public BlockPalette(NbtCompound palette){
        for(String x: palette.getKeys()){
            this.put(x,palette.getInt(x));
        }
    }

    public static BlockPalette fromNBT(NbtCompound nbt){
        BlockPalette result = new BlockPalette();
        for(String x: nbt.getKeys()){
            result.put(x,nbt.getInt(x));
        }
        return result;
    }
}
