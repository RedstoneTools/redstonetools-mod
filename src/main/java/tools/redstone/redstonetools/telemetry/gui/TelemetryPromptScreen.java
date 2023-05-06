package tools.redstone.redstonetools.telemetry.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.gui.PopupScreen;
import tools.redstone.redstonetools.telemetry.TelemetryManager;

import static tools.redstone.redstonetools.RedstoneToolsClient.INJECTOR;

public class TelemetryPromptScreen extends PopupScreen {
    private static final String TELEMETRY_PROMPT = "Redstone Tools includes an optional telemetry feature that collects anonymous usage data to help us improve the mod. By enabling telemetry, you can help us better understand how the mod is being used and identify areas for improvement.\n\n\n\nWould you like to help us improve Redstone Tools by enabling anonymous usage data?"; //  TODO: Add "You can turn this off at any time in the settings menu." once you can actually turn it off in the settings menu

    public TelemetryPromptScreen(Screen parent) {
        super(parent, "Telemetry", TELEMETRY_PROMPT);
    }

    @Override
    protected void addButtons(int y) {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155, y, 150, 20, Text.of("Yes"), (button) -> {
            var manager = INJECTOR.getInstance(TelemetryManager.class);

            manager.showTelemetryPrompt = false;
            manager.telemetryEnabled = true;

            manager.saveChanges();

            this.client.setScreen(this.parent);
        }));

        this.addDrawableChild(new ButtonWidget(this.width / 2 + 5, y, 150, 20, Text.of("No"), (button) -> {
            var manager = INJECTOR.getInstance(TelemetryManager.class);

            manager.showTelemetryPrompt = false;
            manager.telemetryEnabled = false;

            manager.saveChanges();

            this.client.setScreen(this.parent);
        }));
    }
}
