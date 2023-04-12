package tools.redstone.redstonetools.utils;

import net.minecraft.util.hit.BlockHitResult;

public class RaycastUtils {
    private RaycastUtils() {
    }

    public static BlockHitResult getBlockHitNeighbor(BlockHitResult hit) {
        var sideOffset = hit.getSide().getUnitVector();

        var newBlockPos = hit.getBlockPos().add(sideOffset.getX(), sideOffset.getY(), sideOffset.getZ());

        return hit.withBlockPos(newBlockPos);
    }
}
