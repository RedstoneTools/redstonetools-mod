package tools.redstone.redstonetools.mixin.features;

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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tools.redstone.redstonetools.RedstoneToolsClient;
import tools.redstone.redstonetools.features.toggleable.AirPlaceFeature;

@Mixin(MinecraftClient.class)
public class AirplaceMixin {
    private final AirPlaceFeature airPlaceFeature = RedstoneToolsClient.INJECTOR.getInstance(AirPlaceFeature.class);

    @Shadow
    public HitResult crosshairTarget;

    @Inject(method = "doItemUse", at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void doItemUse(CallbackInfo callbackInfo) {
        if (!isAirplaceAllowed()) {
            return;
        }

        var hitResult = getAirplaceHitResult();

        crosshairTarget = new BlockHitResult(hitResult.getPos(), Direction.UP, new BlockPos(hitResult.getPos()), false);
    }

    @Inject(method = "doAttack", at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void doAttack(CallbackInfoReturnable<Boolean> cir) {
        if (!isAirplaceAllowed()) {
            return;
        }

        var hitResult = getAirplaceHitResult();
        var minecraftClient = (MinecraftClient) (Object) this;

        //Call interactionManager directly because the block is air, with which the player cannot interact
        minecraftClient.interactionManager.attackBlock(new BlockPos(hitResult.getPos()), Direction.UP);
    }

    private boolean isAirplaceAllowed() {
        // If airplace is disabled
        if (!airPlaceFeature.isEnabled()) {
            return false;
        }

        // If the hit result is already set
        if (crosshairTarget != null && crosshairTarget.getType() != HitResult.Type.MISS) {
            return false;
        }

        // If the player or interactionManager not initialized
        var minecraftClient = (MinecraftClient) (Object) this;
        var player = minecraftClient.player;
        var interactionManager = minecraftClient.interactionManager;
        if (player == null || interactionManager == null) {
            return false;
        }

        return true;
    }

    private HitResult getAirplaceHitResult() {
        var minecraftClient = (MinecraftClient) (Object) this;

        // Asserting values previously tested by isAirplaceAllowed()
        assert minecraftClient.interactionManager != null;
        assert minecraftClient.player != null;

        var reach = minecraftClient.interactionManager.getReachDistance();
        var hitResult = minecraftClient.player.raycast(reach, 0, false);

        return hitResult;
    }
}
