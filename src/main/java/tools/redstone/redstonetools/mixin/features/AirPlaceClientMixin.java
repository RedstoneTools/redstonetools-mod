package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tools.redstone.redstonetools.RedstoneToolsClient;
import tools.redstone.redstonetools.features.toggleable.AirPlaceFeature;
import tools.redstone.redstonetools.utils.RaycastUtils;

@Mixin(MinecraftClient.class)
public class AirPlaceClientMixin {
    private final AirPlaceFeature airPlaceFeature = RedstoneToolsClient.INJECTOR.getInstance(AirPlaceFeature.class);

    @Shadow
    public HitResult crosshairTarget;

    @Inject(method = "doItemUse", at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void doItemUse(CallbackInfo callbackInfo) {
        if (!isAirPlaceAllowed()) {
            return;
        }

        crosshairTarget = AirPlaceFeature.findAirPlaceBlockHit(getPlayer());
    }

    @Inject(method = "doAttack", at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void doAttack(CallbackInfoReturnable<Boolean> cir) {
        if (!isAirPlaceAllowed()) {
            return;
        }

        // Call interactionManager directly because the block is air, with which the player cannot interact
        var hit = AirPlaceFeature.findAirPlaceBlockHit(getPlayer());
        getInteractionManager().attackBlock(hit.getBlockPos(), hit.getSide());
    }

    private boolean isAirPlaceAllowed() {
        // If airplace is disabled
        if (!airPlaceFeature.isEnabled()) {
            return false;
        }

        // If the hit result is already set
        if (crosshairTarget != null && crosshairTarget.getType() != HitResult.Type.MISS) {
            return false;
        }

        // If the player or interactionManager not initialized
        if (getPlayer() == null || getInteractionManager() == null) {
            return false;
        }

        return true;
    }

    private MinecraftClient getMinecraftClient() {
        return (MinecraftClient) (Object) this;
    }

    private ClientPlayerEntity getPlayer() {
        return getMinecraftClient().player;
    }

    private ClientPlayerInteractionManager getInteractionManager() {
        return getMinecraftClient().interactionManager;
    }

}
