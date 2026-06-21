package tools.redstone.redstonetools.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class PositionUtils {
	private PositionUtils() {
	}

	public static Vec3 getBottomPositionOfBlock(BlockPos position) {
		return Vec3.atCenterOf(position).subtract(0, 0.5, 0);
	}
}
