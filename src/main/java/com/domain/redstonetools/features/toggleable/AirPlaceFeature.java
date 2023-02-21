package com.domain.redstonetools.features.toggleable;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.arguments.Argument;
import com.domain.redstonetools.utils.ItemUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.royawesome.jlibnoise.module.combiner.Min;

import java.util.Random;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;

@Feature(name = "Air Place", description = "Allows you to place blocks in the air.", command = "airplace")
public class AirPlaceFeature extends ToggleableFeature {
    public static HitResult getAirPlacePosition(MinecraftClient client,
                                                float reach) {
        if (client.player == null)
            return null;
        ClientPlayerEntity player = client.player;

        return player.raycast(reach, 0, false);
    }

    public static final Argument<Boolean> showGhostBlock = Argument
            .ofType(bool())
            .withDefault(true);

    static final Random RANDOM = new Random(System.nanoTime() ^ System.currentTimeMillis());

    {
        // register ghost block renderer
        WorldRenderEvents.END.register(context -> {
            if (!isEnabled())
                return;
            if (!showGhostBlock.getValue())
                return;

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.interactionManager == null)
                return;

            HitResult hitResult = getAirPlacePosition(client, client.interactionManager.getReachDistance());
            if (hitResult == null)
                return;
            BlockState state = ItemUtils.getPlacementState(client.player.getInventory().getMainHandStack());
            if (state == null)
                return;
            BlockPos blockPos = new BlockPos(hitResult.getPos());

            BlockRenderManager blockRenderManager = client.getBlockRenderManager();
            blockRenderManager.renderBlock(
                    state, blockPos, context.world(), context.matrixStack(),
                    null, false, RANDOM
            );
        });
    }
}
