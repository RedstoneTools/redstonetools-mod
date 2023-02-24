package com.domain.redstonetools.features.toggleable;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.arguments.Argument;
import com.domain.redstonetools.utils.ItemUtils;
import com.domain.redstonetools.utils.ReflectionUtils;
import com.domain.redstonetools.utils.render.CameraRelativeOverlayRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.Random;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;

@Feature(name = "Air Place", description = "Allows you to place blocks in the air.", command = "airplace")
public class AirPlaceFeature extends ToggleableFeature {
    static MethodHandle methodDrawBlockOutline;

    static {
        try {
            methodDrawBlockOutline = ReflectionUtils.getInternalLookup()
                    .findVirtual(WorldRenderer.class, "drawBlockOutline",
                            MethodType.methodType(void.class, MatrixStack.class, VertexConsumer.class, Entity.class,
                                    double.class, double.class, double.class, BlockPos.class, BlockState.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((context, blockOutlineContext) -> {
            if (!isEnabled())
                return true;
//            if (showGhostBlock.getValue() != Boolean.TRUE)
//                return;

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.interactionManager == null)
                return true;
            if (blockOutlineContext.getType() != HitResult.Type.MISS)
                return true;

            HitResult hitResult = getAirPlacePosition(client, client.interactionManager.getReachDistance());
            if (hitResult == null)
                return true;
            BlockPos blockPos = new BlockPos(hitResult.getPos());

            BlockState blockState = ItemUtils.getPlacementState(client.player.getMainHandStack());
            if (blockState == null)
                return true;

            /* render block outline */
            Camera camera = client.gameRenderer.getCamera();
            Vec3d camPos = camera.getPos();

            try {
                VertexConsumer consumer = context.consumers().getBuffer(RenderLayer.getLines());
                methodDrawBlockOutline.invoke(
                        context.worldRenderer(),
                        context.matrixStack(),
                        consumer,
                        (Entity)client.player,
                        camPos.x, camPos.y, camPos.z,
                        blockPos,
                        blockState
                );
            } catch (Throwable t) {
                throw new IllegalStateException(t);
            }

            return true;
        });
    }
}
