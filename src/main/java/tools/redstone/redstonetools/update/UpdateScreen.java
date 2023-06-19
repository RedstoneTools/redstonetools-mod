package tools.redstone.redstonetools.update;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.net.URI;

public class UpdateScreen extends ConfirmScreen {
    public UpdateScreen(Screen parentScreen, URI updateSource, String title, String message){
        super(confirmed -> {
            MinecraftClient.getInstance().setScreen(parentScreen);

            if (confirmed) {
                Util.getOperatingSystem().open(updateSource);
            }
        }, Text.of(title),Text.of(message),Text.of("Go to release"), Text.of("Ignore"));
    }
}
