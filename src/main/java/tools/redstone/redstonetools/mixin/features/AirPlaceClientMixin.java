package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tools.redstone.redstonetools.RedstoneToolsClient;
import tools.redstone.redstonetools.features.commands.AirPlaceReachFeature;
import tools.redstone.redstonetools.features.toggleable.AirPlaceFeature;

@Mixin(MinecraftClient.class)
public class AirPlaceClientMixin {
    private final AirPlaceFeature airPlaceFeature = RedstoneToolsClient.INJECTOR.getInstance(AirPlaceFeature.class);
    private final AirPlaceReachFeature airPlaceReachFeature = RedstoneToolsClient.INJECTOR.getInstance(AirPlaceReachFeature.class);

    @Shadow
    public HitResult crosshairTarget;

    @Inject(method = "doItemUse", at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void doItemUse(CallbackInfo callbackInfo) {
        if (!isAirplaceAllowed()) {
            return;
        }

        var hitResult = getAirplaceHitResult();

        crosshairTarget = new BlockHitResult(hitResult, Direction.UP, new BlockPos(hitResult), false);
    }

    @Inject(method = "doAttack", at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void doAttack(CallbackInfoReturnable<Boolean> cir) {
        if (!isAirplaceAllowed()) {
            return;
        }

        //Call interactionManager directly because the block is air, with which the player cannot interact
        getInteractionManager().attackBlock(new BlockPos(getAirplaceHitResult()), Direction.UP);
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
        if (getPlayer() == null || getInteractionManager() == null) {
            return false;
        }

        return true;
    }

    private Vec3d getAirplaceHitResult() {
        // Asserting values previously tested by isAirplaceAllowed()
        assert getInteractionManager() != null;
        assert getPlayer() != null;

        return getPlayer().raycast(airPlaceReachFeature.reach, 0, false).getPos();
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
