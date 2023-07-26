package tools.redstone.redstonetools.utils.schematics;

import net.minecraft.nbt.NbtCompound;

public class SpongeSchematic extends Schematic{
    public int version;

    String author;

    String[] requiredMods;

    public SpongeSchematic(String filename, NbtCompound schemaNBT) {
        super(filename, schemaNBT);
        this.version = schemaNBT.getInt("Version");
        this.name = schemaNBT.getCompound("Metadata").getString("Name");
        this.requiredMods = new String[2];
        System.out.println("Byte Type: " + schemaNBT.getCompound("Metadata").get);
        System.out.println("Name:" + this.name);
        for(String x: this.requiredMods){
            System.out.println(x);
        }
    }


}
