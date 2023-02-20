package com.domain.redstonetools.mixin;

import com.domain.redstonetools.features.toggleable.AirPlaceFeature;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static com.domain.redstonetools.RedstoneToolsClient.INJECTOR;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    private final AirPlaceFeature airPlaceFeature = INJECTOR.getInstance(AirPlaceFeature.class);

    @Shadow
    public HitResult crosshairTarget;

    @Inject(method = "doItemUse", at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void doItemUse(CallbackInfo callbackInfo) {
        // If airplace is disabled
        if (!airPlaceFeature.isEnabled()) {
            return;
        }

        // If the hit result is already set
        if (crosshairTarget != null && crosshairTarget.getType() != HitResult.Type.MISS) {
            return;
        }

        var player = ((MinecraftClient) (Object) this).player;
        var interactionManager = ((MinecraftClient) (Object) this).interactionManager;
        if (player == null || interactionManager == null) {
            return;
        }

        var reach = interactionManager.getReachDistance();
        var hitResult = player.raycast(reach, 0, false);

        crosshairTarget = new BlockHitResult(hitResult.getPos(), Direction.UP, new BlockPos(hitResult.getPos()), false);
    }
}