package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.utils.BlockInfo;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public abstract class BlockRaycastFeature extends CommandFeature {
    protected int execute(ServerCommandSource source) throws CommandSyntaxException {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) {
            throw new CommandSyntaxException(null, Text.of("This command is client-side only"));
        }

        if (client.crosshairTarget == null || client.crosshairTarget.getType() != HitResult.Type.BLOCK) {
            source.sendError(Text.of("You must be looking at a block to use this command"));

            return -1;
        }

        var blockPos = ((BlockHitResult) client.crosshairTarget).getBlockPos();
        var blockState = client.world.getBlockState(blockPos);
        var blockEntity = client.world.getBlockEntity(blockPos);
        var block = blockState.getBlock();

        return execute(source, new BlockInfo(block, blockPos, blockState, blockEntity));
    }

    protected abstract int execute(ServerCommandSource source, BlockInfo blockInfo) throws CommandSyntaxException;
}
