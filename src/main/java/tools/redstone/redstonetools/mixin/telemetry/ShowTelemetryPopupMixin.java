package tools.redstone.redstonetools.mixin.telemetry;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.telemetry.TelemetryManager;

import static tools.redstone.redstonetools.RedstoneToolsClient.INJECTOR;

@Mixin(TitleScreen.class)
public class ShowTelemetryPopupMixin extends Screen {
    private static final String TELEMETRY_PROMPT = "Redstone Tools includes an optional telemetry feature that collects anonymous usage data to help us improve the mod. By enabling telemetry, you can help us better understand how the mod is being used and identify areas for improvement.\n\n\n\nWould you like to help us improve Redstone Tools by enabling anonymous usage data?"; //  TODO: Add "You can turn this off at any time in the settings menu." once you can actually turn it off in the settings menu

    public ShowTelemetryPopupMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        var manager = INJECTOR.getInstance(TelemetryManager.class);

        if (!manager.showTelemetryPrompt) {
            return;
        }

        var parentScreen = MinecraftClient.getInstance().currentScreen;
        var popup = new ConfirmScreen(accepted -> {
            manager.showTelemetryPrompt = false;
            manager.telemetryEnabled = accepted;

            manager.saveChanges();

            MinecraftClient.getInstance().setScreen(parentScreen);
        }, Text.of("Telemetry"), Text.of(TELEMETRY_PROMPT));

        MinecraftClient.getInstance().setScreen(popup);
    }
}
