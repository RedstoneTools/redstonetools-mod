package tools.redstone.redstonetools.mixin.telemetry;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.telemetry.TelemetryManager;
import tools.redstone.redstonetools.telemetry.gui.TelemetryPromptScreen;

import static tools.redstone.redstonetools.RedstoneToolsClient.INJECTOR;

@Mixin(TitleScreen.class)
public class ShowTelemetryPopupMixin extends Screen {
    public ShowTelemetryPopupMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        var manager = INJECTOR.getInstance(TelemetryManager.class);

        if (!manager.showTelemetryPrompt) {
            return;
        }

        MinecraftClient.getInstance().setScreen(new TelemetryPromptScreen(this));
    }
}
