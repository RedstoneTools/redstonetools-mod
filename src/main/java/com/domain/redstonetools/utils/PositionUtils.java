package com.domain.redstonetools.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class PositionUtils {
    private PositionUtils() {
    }

    // TODO: Test this
    public static Vec3d getFloorOfBlock(BlockPos position) {
        return Vec3d.ofCenter(position).subtract(0, 0.5, 0);
    }
}
