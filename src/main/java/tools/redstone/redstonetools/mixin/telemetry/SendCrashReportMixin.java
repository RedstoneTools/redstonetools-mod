package tools.redstone.redstonetools.mixin.telemetry;

import tools.redstone.redstonetools.utils.TelemetryUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class SendCrashReportMixin {
    @Inject(method = "printCrashReport", at = @At("TAIL"))
    private static void printCrashReport(CrashReport report, CallbackInfo ci) {
        TelemetryUtils.sendCrash(report);
    }
}
