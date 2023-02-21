package com.domain.redstonetools.features.toggleable;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.arguments.Argument;
import com.domain.redstonetools.utils.render.CameraRelativeRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

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
            .withDefault(true)
            .build();

    static final Random RANDOM = new Random(System.nanoTime() ^ System.currentTimeMillis());

    {
        // register ghost block renderer
        WorldRenderEvents.END.register(context -> {
            if (!isEnabled())
                return;
            if (showGhostBlock.getValue() != Boolean.TRUE)
                return;

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.interactionManager == null)
                return;

            HitResult hitResult = getAirPlacePosition(client, client.interactionManager.getReachDistance());
            if (hitResult == null)
                return;
            BlockPos blockPos = new BlockPos(hitResult.getPos());

            /* render block outline */
            Entity cam = client.cameraEntity;
            if (cam == null)
                return;
            CameraRelativeRenderer renderer = new CameraRelativeRenderer(Tessellator.getInstance());
            renderer.camera(cam.getX(), cam.getY(), cam.getZ());
            renderer.beginLines(4f);
            renderer.cuboidOutline(blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                    blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1,
                    v -> v.color(1.0f, 0, 0, 1.0f).next());
            renderer.draw();
        });
    }
}
