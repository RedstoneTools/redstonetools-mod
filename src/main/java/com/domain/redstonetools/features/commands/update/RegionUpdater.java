package com.domain.redstonetools.features.commands.update;

import com.domain.redstonetools.feedback.Feedback;
import com.sk89q.worldedit.math.BlockVector3;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RegionUpdater {


    public static Feedback updateRegion(World world, BlockVector3 minPos, BlockVector3 maxPos) {
        long blockCount = 0;

        for (int x = minPos.getX() - 1; x <= maxPos.getX() + 1; x++) {
            for (int y = minPos.getY() - 1; y <= maxPos.getY() + 1; y++) {
                for (int z = minPos.getZ() - 1; z <= maxPos.getZ() + 1; z++) {
                    BlockPos pos = new BlockPos(x, y, z);

                    world.updateNeighbors(new BlockPos(x, y, z), world.getBlockState(pos).getBlock());
                    blockCount++;

                }
            }
        }
        return Feedback.success("Successfully forced block updates for " + blockCount + " blocks");
    }

}
