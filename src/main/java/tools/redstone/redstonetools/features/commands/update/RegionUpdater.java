package tools.redstone.redstonetools.features.commands.update;

import tools.redstone.redstonetools.features.feedback.Feedback;
import com.sk89q.worldedit.math.BlockVector3;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RegionUpdater {


    public static Feedback updateRegion(World world, BlockVector3 minPos, BlockVector3 maxPos) {
        long blockCount = 0;

        for (int x = minPos.x() - 1; x <= maxPos.x() + 1; x++) {
            for (int y = minPos.y() - 1; y <= maxPos.y() + 1; y++) {
                for (int z = minPos.z() - 1; z <= maxPos.z() + 1; z++) {
                    BlockPos pos = new BlockPos(x, y, z);

                    world.updateNeighbors(new BlockPos(x, y, z), world.getBlockState(pos).getBlock());
                    blockCount++;

                }
            }
        }
        return Feedback.success("Successfully forced block updates for {} block(s).", blockCount);
    }

}
