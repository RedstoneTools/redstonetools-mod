package com.domain.redstonetools.utils;

import net.minecraft.util.hit.BlockHitResult;

public class RaycastUtils {
    private RaycastUtils() {
    }

    // TODO: Test this
    public static BlockHitResult getBlockHitNeighbor(BlockHitResult hit) {
        var sideOffset = hit.getSide().getUnitVector();

        var position = hit.getBlockPos().add(sideOffset.getX(), sideOffset.getY(), sideOffset.getZ());

        return hit.withBlockPos(position);
    }
}
