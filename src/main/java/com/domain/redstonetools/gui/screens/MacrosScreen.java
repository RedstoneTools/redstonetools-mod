package com.domain.redstonetools.gui.screens;

import com.domain.redstonetools.gui.widgets.MacroWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;

public class MacrosScreen extends GameOptionsScreen {
    private final Screen parent;
    private final GameOptions gameOptions;

    public MacrosScreen(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, Text.of("Macros"));
        this.parent = parent;
        this.gameOptions = gameOptions;
    }

    @Override
    protected void init() {
        this.addDrawableChild(new MacroWidget(100, 100, 30, 30, true));
    }
}
