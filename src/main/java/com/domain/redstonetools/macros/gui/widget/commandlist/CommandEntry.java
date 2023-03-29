package com.domain.redstonetools.macros.gui.widget.commandlist;

import com.domain.redstonetools.macros.gui.widget.IconButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class CommandEntry extends EntryListWidget.Entry<CommandEntry> {


    protected final CommandListWidget owner;

    protected final TextFieldWidget command;
    protected final ButtonWidget deleteButton;


    public CommandEntry(MinecraftClient client, CommandListWidget owner, String text) {
        this.owner = owner;

        command = new TextFieldWidget(client.textRenderer, 0, 0, 300, 20, Text.of(""));
        command.setMaxLength(32500);
        command.setText(text);

        deleteButton = new IconButtonWidget(IconButtonWidget.CROSS_ICON,0, 0, 20, 20, Text.of(""), (button) -> {
            owner.removeCommand(this);
        });
    }


    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        command.x = owner.getParent().width/2-owner.getWidth()/2+5;
        command.y = y;
        command.render(matrices,mouseX,mouseY,tickDelta);


        deleteButton.x = command.x + command.getWidth()+5;
        deleteButton.y = y;
        deleteButton.render(matrices,mouseX,mouseY,tickDelta);
    }

    public void tick() {
        command.tick();
    }

    public void setFocused(boolean focused){
        command.setTextFieldFocused(focused);
    }

    protected String getText() {
        return command.getText();
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        command.mouseClicked(mouseX,mouseY,button);
        deleteButton.mouseClicked(mouseX,mouseY,button);

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (command.isFocused()) return command.charTyped(chr,modifiers);

        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (command.isFocused()) return command.keyPressed(keyCode, scanCode, modifiers);

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (command.isFocused()) return command.keyReleased(keyCode, scanCode, modifiers);

        return super.keyReleased(keyCode, scanCode, modifiers);
    }
}
