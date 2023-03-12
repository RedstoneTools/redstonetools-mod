package com.domain.redstonetools.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class PositionUtils {
    private PositionUtils() {
    }

    /**
     * Get the center of the bottom face of the
     * given block position.
     */
    public static Vec3d getBottomPositionOfBlock(BlockPos position) {
        return Vec3d.ofCenter(position).subtract(0, 0.5, 0);
    }
}
