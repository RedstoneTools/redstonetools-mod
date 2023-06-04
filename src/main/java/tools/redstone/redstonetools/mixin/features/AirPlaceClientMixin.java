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

        // If airplace is disabled
        if (!airPlaceFeature.isEnabled()) {
            return;
        }

        // If the hit result is already set
        if (crosshairTarget != null && crosshairTarget.getType() != HitResult.Type.MISS) {
            return;
        }

        var minecraftClient = (MinecraftClient) (Object) this;
        var playerEntity = minecraftClient.player;
        var interactionManager = minecraftClient.interactionManager;
        if (playerEntity == null || interactionManager == null) {
            return;
        }

        var hitResult = playerEntity.raycast(airPlaceReachFeature.reach, 0, false);

        crosshairTarget = new BlockHitResult(hitResult.getPos(), Direction.UP, new BlockPos(hitResult.getPos()), false);

    }
}
