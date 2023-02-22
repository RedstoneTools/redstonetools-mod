package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.features.options.Options;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public abstract class RayCastFeature extends CommandFeature {
    protected int execute(ServerCommandSource source) throws CommandSyntaxException {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) {
            throw new CommandSyntaxException(null, Text.of("This command is client-side only"));
        }

        if (client.crosshairTarget == null || client.crosshairTarget.getType() != HitResult.Type.BLOCK) {
            source.sendError(Text.of("You must be looking at a block to use this command"));

            return -1;
        }

        return execute(source, (BlockHitResult) client.crosshairTarget);
    }

    protected abstract int execute(ServerCommandSource source, BlockHitResult blockHit) throws CommandSyntaxException;
}
