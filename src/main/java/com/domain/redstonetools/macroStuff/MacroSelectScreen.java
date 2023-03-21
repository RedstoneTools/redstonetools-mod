package com.domain.redstonetools.macroStuff;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class MacroSelectScreen extends GameOptionsScreen {


    private MacrosListWidget controlsList;

    public MacroSelectScreen(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Override
    public void init() {
        super.init();

        this.controlsList = new MacrosListWidget(this,client);
        this.addSelectableChild(this.controlsList);

        this.addDrawableChild(new ButtonWidget(this.width / 2 +1, this.height - 29, 150, 20, Text.of("Create New..."), (button) -> {
            this.client.setScreen(new MacroEditScreen(this,gameOptions,Text.of("Create New Macro"), controlsList));
        }));

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 151, this.height - 29, 150, 20, ScreenTexts.DONE, (button) -> {
            this.client.setScreen(this.parent);
        }));

        this.setFocused(controlsList);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        controlsList.render(matrices, mouseX, mouseY, delta);

        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 8, 16777215);
        super.render(matrices, mouseX, mouseY, delta);

    }

    public void openEditScreen(MacrosListWidget.MacroEntry entry) {
        client.setScreen(new MacroEditScreen(this,gameOptions,Text.of("Edit Macro"), controlsList, entry.macro));
    }

}
