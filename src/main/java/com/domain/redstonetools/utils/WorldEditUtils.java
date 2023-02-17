package com.domain.redstonetools.utils;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;

import java.util.function.Consumer;

public class WorldEditUtils {

    /**
     * Execute a function for each block in
     * the provided region.
     *
     * Iterates the bounding box of the region
     * and checks if the position is contained
     * for each block.
     *
     * TODO: maybe make an async version of this somehow
     *
     * @param region The region.
     * @param consumer The function to run.
     */
    public static void forEachBlockInRegion(Region region,
                                            Consumer<BlockVector3> consumer) {
        CuboidRegion bb = region.getBoundingBox();
        BlockVector3 min = bb.getMinimumPoint();
        BlockVector3 max = bb.getMaximumPoint();
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    BlockVector3 vec = BlockVector3.at(x, y, z);
                    if (!region.contains(vec))
                        continue;
                    consumer.accept(vec);
                }
            }
        }
    }

}
