package tools.redstone.redstonetools.macros.gui.screen;

import tools.redstone.redstonetools.macros.gui.widget.macrolist.MacroEntry;
import tools.redstone.redstonetools.macros.gui.widget.macrolist.MacroListWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class MacroSelectScreen extends GameOptionsScreen {


    private MacroListWidget macroList;

    public MacroSelectScreen(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Override
    public void init() {
        super.init();

        this.macroList = new MacroListWidget(this,client);
        this.addSelectableChild(this.macroList);

        this.addDrawableChild(new ButtonWidget(this.width / 2 +1, this.height - 29, 150, 20, Text.of("Create New..."), (button) -> {
            this.client.setScreen(new MacroEditScreen(this,gameOptions,Text.of("New Macro"), macroList));
        }));

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 151, this.height - 29, 150, 20, ScreenTexts.DONE, (button) -> {
            this.client.setScreen(this.parent);
        }));
    }

    @Override
    protected void addOptions() {}

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(0);
        macroList.render(matrices, mouseX, mouseY, delta);

        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 8, 16777215);
        super.render(matrices, mouseX, mouseY, delta);

    }

    public void openEditScreen(MacroEntry entry) {
        client.setScreen(new MacroEditScreen(this,gameOptions,Text.of("Edit Macro"), macroList, entry.macro));
    }

}
