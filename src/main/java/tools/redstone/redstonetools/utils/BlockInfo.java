package tools.redstone.redstonetools.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public record BlockInfo(Block block, BlockPos pos, BlockState state, BlockEntity entity) {
}
