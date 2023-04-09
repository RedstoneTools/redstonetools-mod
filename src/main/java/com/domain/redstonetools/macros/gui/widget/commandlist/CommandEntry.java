package com.domain.redstonetools.macros.gui.widget.commandlist;

import com.domain.redstonetools.macros.gui.commandsuggestor.WorldlessCommandSuggestor;
import com.domain.redstonetools.macros.gui.widget.IconButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class CommandEntry extends EntryListWidget.Entry<CommandEntry> {


    protected CommandListWidget owner;
    private final WorldlessCommandSuggestor commandSuggestor;

    protected final TextFieldWidget command;
    protected final ButtonWidget deleteButton;


    public CommandEntry(MinecraftClient client, CommandListWidget owner, String text) {
        this.owner = owner;

        command = new TextFieldWidget(client.textRenderer, 0, 0, 300, 20, Text.of(""));
        command.setMaxLength(32500);
        command.setText(text);

        deleteButton = new IconButtonWidget(IconButtonWidget.CROSS_ICON,0, 0, 20, 20, Text.of(""), (button) -> {
            this.owner.removeCommand(this);
        });

        this.commandSuggestor = new WorldlessCommandSuggestor(client, owner.getParent(), command,client.textRenderer,true,false, 0,0,true,0);
        commandSuggestor.refresh(false);
    }


    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        command.x = owner.getParent().width/2-owner.getWidth()/2+5;
        command.y = y;
        command.render(matrices,mouseX,mouseY,tickDelta);


        deleteButton.x = command.x + command.getWidth()+5;
        deleteButton.y = y;
        deleteButton.render(matrices,mouseX,mouseY,tickDelta);


        if (command.isFocused()) {
            commandSuggestor.refresh(true);
            if (!command.getText().isEmpty()) commandSuggestor.showSuggestions(false);
        }

    }

    public void tick() {
        command.tick();
    }

    public void setFocused(boolean focused){
        command.setTextFieldFocused(focused);
        if (!focused) {
            commandSuggestor.refresh(false);
        }
    }

    protected String getText() {
        return command.getText();
    }

    public void setOwner(CommandListWidget owner) {
        this.owner = owner;
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
        if (command.isFocused()) {
            commandSuggestor.keyPressed(keyCode, scanCode, modifiers);
            return command.keyPressed(keyCode, scanCode, modifiers);
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (command.isFocused()) return command.keyReleased(keyCode, scanCode, modifiers);

        return super.keyReleased(keyCode, scanCode, modifiers);
    }
}
