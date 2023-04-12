package com.domain.redstonetools.mixin;

import com.domain.redstonetools.telemetry.TelemetryClient;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.domain.redstonetools.RedstoneToolsClient.INJECTOR;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        INJECTOR.getInstance(TelemetryClient.class);
    }
}
