package tools.redstone.redstonetools.mixin;

import tools.redstone.redstonetools.telemetry.TelemetryClient;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static tools.redstone.redstonetools.RedstoneToolsClient.INJECTOR;

@Mixin(MinecraftClient.class)
public class TelemetryInitializeMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        INJECTOR.getInstance(TelemetryClient.class);
    }
}
