package tools.redstone.redstonetools.features.commands;

import com.google.auto.service.AutoService;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.WorldEdit;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.registry.Registry;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.arguments.Argument;
import tools.redstone.redstonetools.features.feedback.Feedback;
import tools.redstone.redstonetools.utils.schematics.BlockPalette;
import tools.redstone.redstonetools.utils.schematics.Schematic;
import tools.redstone.redstonetools.utils.schematics.SpongeSchematic;


import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import static tools.redstone.redstonetools.features.arguments.serializers.IntegerSerializer.integer;
import static tools.redstone.redstonetools.features.arguments.serializers.StringSerializer.string;

@AutoService(AbstractFeature.class)
@Feature(name = "Schem Read", description = "Reads a schemfile", command = "schemread")
public class SchemNBTRead extends CommandFeature {

    public static final Argument<String> schematicName = Argument.ofType(string());

    public static final Argument<Integer> X = Argument.ofType(integer())
            .withDefault(0);
    public static final Argument<Integer> Y = Argument.ofType(integer())
            .withDefault(0);
    public static final Argument<Integer> Z = Argument.ofType(integer())
            .withDefault(0);

    @Override
    protected Feedback execute(ServerCommandSource source) {
        LocalConfiguration config = WorldEdit.getInstance().getConfiguration();
        File dir = WorldEdit.getInstance().getWorkingDirectoryPath(config.saveDir).toFile();
        File schem = new File(dir + "\\" + schematicName.getValue() + ".schem");
        if(!schem.exists()){
            return Feedback.error("Schematic doesn't exist.");
        }

        try {
            FileInputStream fis = new FileInputStream(schem);
            NbtCompound nb = NbtIo.readCompressed(fis);
            Schematic schema = new SpongeSchematic(schematicName.getValue(),nb);

            fis.close();


            for(int y = 0; y < schema.getHeight(); y++){
                for(int x = 0; x < schema.getLength(); x++){
                    for(int z = 0; z < schema.getWidth(); z++){
                        System.out.println("X: " + x + ",Y: " + y + ",Z: " + z + " - " + schema.blockAtCoords(x,y,z));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Feedback.success("File Exists: " +schem.exists());
    }

    public static Block toBlockFromPalette(BlockPalette palette, byte id){
        String blockName = null;

        Block result;
        Iterator<Block> blockRegistry = Registry.BLOCK.stream().iterator();

        //Searches Block Palette for id
        for(String x:palette.keySet()){
            if(palette.get(x) == id){
                blockName = x;
                break;
            }
        }

        if(blockName == null) return null;
        
        //Searches block registry for blockName
        while(blockRegistry.hasNext()){
            result = blockRegistry.next();
            if(result.toString().contains(blockName)){
                return result;
            }
        }
        return null;
    }
}
