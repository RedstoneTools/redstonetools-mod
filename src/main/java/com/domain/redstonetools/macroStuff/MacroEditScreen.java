package com.domain.redstonetools.macroStuff;

import com.domain.redstonetools.macros.Macro;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;


public class MacroEditScreen extends GameOptionsScreen {

    private final MacrosListWidget macrosListWidget;
    private final Macro editing;
    private TextFieldWidget nameField;
    private ButtonWidget doneButton;

    public MacroEditScreen(Screen parent, GameOptions gameOptions, Text title, MacrosListWidget macrosListWidget) {
        super(parent, gameOptions, title);
        this.macrosListWidget = macrosListWidget;
        this.editing = Macro.buildEmpty();
    }

    public MacroEditScreen(Screen parent, GameOptions gameOptions, Text title, MacrosListWidget macrosListWidget, Macro macro) {
        super(parent, gameOptions, title);
        this.macrosListWidget = macrosListWidget;
        this.editing = macro;
    }


    @Override
    public void init() {
        super.init();
        nameField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 22, 200, 20, Text.of(""));

        if (editing != null) nameField.setText(editing.name);

        doneButton = this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 144 + 5, 98, 20, Text.of("Done"), (button) -> {
           String name = nameField.getText();
           if (name.isEmpty()) return;

           if (editing == null) macrosListWidget.addMacro(Macro.buildEmpty());
           else editing.name = name;

           client.setScreen(parent);
        }));
        doneButton.active = false;

        nameField.setChangedListener(s -> doneButton.active = !nameField.getText().isEmpty() && !nameField.getText().contains(" ") && macrosListWidget.canAdd(s));
        addSelectableChild(nameField);


        this.addDrawableChild(new ButtonWidget(this.width / 2 + 2, this.height / 4 + 144 + 5, 98, 20, ScreenTexts.CANCEL, (button) -> {
            client.setScreen(parent);
        }));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 8, 16777215);
        nameField.render(matrices, mouseX, mouseY, delta);


        super.render(matrices, mouseX, mouseY, delta);
    }

}
