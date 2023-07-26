package tools.redstone.redstonetools.utils.schematics;

import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;

import static tools.redstone.redstonetools.features.commands.SchemNBTRead.toBlockFromPalette;

public class Schematic {

    String name,filename;
    short width, length, height;

    byte[] blockIDs;
    Block[] blocks;
    BlockPalette palette;

    public Schematic(String filename, NbtCompound schemaNBT){
            this(filename,
                    schemaNBT.getByteArray("BlockData"),
                    schemaNBT.getShort("Width"),
                    schemaNBT.getShort("Length"),
                    schemaNBT.getShort("Height"),
                    schemaNBT.getCompound("Palette"));
    }
    public Schematic(String filename, byte[] blockIDs, short width, short length, short height, NbtCompound palette){
        this(filename,blockIDs,width,length,height,BlockPalette.fromNBT(palette));
    }
    public Schematic(String filename, byte[] blockIDs, short width, short length, short height, BlockPalette palette){
        this.filename = filename;
        this.width = width;
        this.length = length;
        this.height = height;
        this.blockIDs = blockIDs;
        this.palette = palette;
        this.blocks = new Block[blockIDs.length];
        for(int i = 0; i < blockIDs.length;i++){
            this.blocks[i] = toBlockFromPalette(palette,blockIDs[i]);
        }
    }

    public Block blockAtCoords(int x, int y, int z){
        int index = (y*length + z)*width + x;
        index = index >= this.blocks.length ? this.blocks.length-1 : index;
        return this.blocks[index];
    }
    public BlockPalette getPalette(){
        return this.palette;
    }

    public Block[] getBlocks(){
        return this.blocks;
    }

    public int getWidth(){
        return this.width;
    }

    public int getLength(){
        return this.length;
    }

    public int getHeight(){
        return this.height;
    }
}

