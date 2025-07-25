package tools.redstone.redstonetools.utils;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class WorldEditUtils {
    /**
     * Execute a function for each block in
     * the provided region.
     * <p>
     * Iterates the bounding box of the region
     * and checks if the position is contained
     * for each block.
     * <p>
     * TODO: maybe make an async version of this somehow
     *
     * @param region The region.
     * @param consumer The function to run.
     */
    public static void forEachBlockInRegion(Region region,
                                            Consumer<BlockVector3> consumer) {
        if (!DependencyLookup.WORLDEDIT_PRESENT) {
            throw new IllegalStateException("WorldEdit is not loaded.");
        }

        CuboidRegion bb = region.getBoundingBox();
        BlockVector3 min = bb.getMinimumPoint();
        BlockVector3 max = bb.getMaximumPoint();
        for (int x = min.x(); x <= max.x(); x++) {
            for (int y = min.y(); y <= max.y(); y++) {
                for (int z = min.z(); z <= max.z(); z++) {
                    BlockVector3 vec = BlockVector3.at(x, y, z);
                    if (!region.contains(vec))
                        continue;
                    consumer.accept(vec);
                }
            }
        }
    }

    public static Region getSelection(ServerPlayerEntity player) throws CommandSyntaxException {
        if (!DependencyLookup.WORLDEDIT_PRESENT) {
            throw new IllegalStateException("WorldEdit is not loaded.");
        }

        var actor = FabricAdapter.adaptPlayer(player);

        var localSession = WorldEdit.getInstance()
                .getSessionManager()
                .get(actor);

        var selectionWorld = localSession.getSelectionWorld();

        try {
            return localSession.getSelection(selectionWorld);
        } catch (IncompleteRegionException ex) {
            throw new SimpleCommandExceptionType(Text.literal("Please make a selection with WorldEdit first")).create();
        }
    }
}
