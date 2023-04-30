package tools.redstone.redstonetools.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class UpdatePopupScreen extends PopupScreen {

    public UpdatePopupScreen(Screen parent) {
        super(parent, "Update Available",
                "An update is available for redstone tools! You are on version {currentVersion} but version {newVersion} is available.");
    }

    @Override
    protected void addButtons(int y) {
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 5, y, 150, 20, Text.of("Go to release"), (button) -> {
            this.close();
        }));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155, y, 150, 20, Text.of("Ignore"), (button) -> {
            this.close();
        }));
    }

}
