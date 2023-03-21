package com.domain.redstonetools.gui.widgets;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class MacroWidget implements Drawable, Element, Selectable {
    private final CheckboxWidget enabledCheckbox;

    public MacroWidget(int x, int y, int width, int height, boolean enabled) {
        enabledCheckbox = new CheckboxWidget(x, y, width, height, Text.of("Enabled"), enabled, false);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        enabledCheckbox.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        enabledCheckbox.appendNarrations(builder);
    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }
}
