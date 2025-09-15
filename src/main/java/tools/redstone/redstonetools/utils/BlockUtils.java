package tools.redstone.redstonetools.utils;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

public class BlockUtils {
	public static BlockState rotate(BlockState original) {
		if (original.contains(Properties.FACING))
			original = original.with(Properties.FACING, original.get(Properties.FACING).getOpposite());

		if (original.contains(Properties.HORIZONTAL_FACING))
			original = original.with(Properties.HORIZONTAL_FACING, original.get(Properties.HORIZONTAL_FACING).getOpposite());

		if (original.contains(Properties.HOPPER_FACING))
			if (original.get(Properties.HOPPER_FACING).getOpposite() != Direction.UP)
				original = original.with(Properties.HOPPER_FACING, original.get(Properties.HOPPER_FACING).getOpposite());
		return original;
	}
}
