package com.domain.redstonetools.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

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
