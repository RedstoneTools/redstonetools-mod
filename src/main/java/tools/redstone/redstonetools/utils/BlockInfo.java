package tools.redstone.redstonetools.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockInfo {
	public final Block block;
	public final BlockPos pos;
	public final BlockState state;
	public final BlockEntity entity;

	public BlockInfo(Block block, BlockPos pos, BlockState state, BlockEntity entity) {
		this.block = block;
		this.pos = pos;
		this.state = state;
		this.entity = entity;
	}
}
