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
    private ButtonWidget editButton;
    private ButtonWidget deleteButton;

    public MacroSelectScreen(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Override
    public void init() {
        super.init();

        this.controlsList = new MacrosListWidget(this,client /*client, parent.width + 45, parent.height, 20, this.height - 64, 20*/);
        this.addSelectableChild(this.controlsList);

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 154, this.height - 51, 99, 20, Text.of("Create New"), (button) -> {

            this.client.setScreen(new MacroEditScreen(this,gameOptions,Text.of("Create New Macro"), controlsList));
        }));

        this.editButton = new ButtonWidget(this.width / 2 - 54, this.height - 51, 99, 20, Text.of("Edit"), (button) -> {
            MacrosListWidget.MacroEntry selected = controlsList.getSelectedOrNull();
            if (selected == null) return;

            this.client.setScreen(new MacroEditScreen(this,gameOptions,Text.of("Edit Macro"), controlsList, selected.macro));
        });
        this.addDrawableChild(editButton);

        this.deleteButton = new ButtonWidget(this.width / 2 + 46, this.height - 51, 99, 20, Text.of("Delete"), (button) -> {
            MacrosListWidget.MacroEntry selected = controlsList.getSelectedOrNull();
            if (selected == null) return;

            selected.deleteIfConfirmed();
        });
        this.addDrawableChild(deleteButton);

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 29, 200, 20, ScreenTexts.DONE, (button) -> {
            this.client.setScreen(this.parent);
        }));

        this.setFocused(controlsList);
        this.setActive(false);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        controlsList.render(matrices, mouseX, mouseY, delta);

        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 8, 16777215);
        super.render(matrices, mouseX, mouseY, delta);

    }


    public void setActive(boolean active){
        editButton.active = active;
        deleteButton.active = active;
    }

}
