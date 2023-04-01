package com.domain.redstonetools.macros.gui.screen;

import com.domain.redstonetools.macros.Macro;
import com.domain.redstonetools.macros.MacroManager;
import com.domain.redstonetools.macros.actions.Action;
import com.domain.redstonetools.macros.actions.CommandAction;
import com.domain.redstonetools.macros.gui.widget.commandlist.CommandEntry;
import com.domain.redstonetools.macros.gui.widget.commandlist.CommandListWidget;
import com.domain.redstonetools.macros.gui.widget.macrolist.MacroListWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.InputUtil.Key;
import net.minecraft.client.util.InputUtil.Type;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

import static com.domain.redstonetools.RedstoneToolsClient.INJECTOR;


public class MacroEditScreen extends GameOptionsScreen {

    private final MacroListWidget macroListWidget;
    private final Macro macro;

    private CommandListWidget commandList;
    private TextFieldWidget nameField;
    private ButtonWidget doneButton;
    private ButtonWidget keyBindButton;

    private boolean detectingKeycodeKey = false;

    public MacroEditScreen(Screen parent, GameOptions gameOptions, Text title, MacroListWidget macroListWidget) {
        super(parent, gameOptions, title);
        this.macroListWidget = macroListWidget;
        this.macro = Macro.buildEmpty();
    }

    public MacroEditScreen(Screen parent, GameOptions gameOptions, Text title, MacroListWidget macroListWidget, Macro macro) {
        super(parent, gameOptions, title);
        this.macroListWidget = macroListWidget;
        this.macro = macro.createCopy();
    }


    @Override
    public void init() {
        super.init();
        nameField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 22, 200, 20, Text.of(""));
        nameField.setText(macro.name);

        doneButton = this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 144 + 5, 98, 20, Text.of("Done"), (button) -> {
            String name = nameField.getText();
            if (name.isEmpty()) return;

            macro.actions.clear();

            for (String command : commandList.getCommandList()) {
                macro.actions.add(new CommandAction(command));
            }

            if (!macro.isCopy()) macroListWidget.addMacro(macro);
            else macro.applyChangesToOriginal();

            INJECTOR.getInstance(MacroManager.class).saveChanges();

            client.setScreen(parent);
        }));

        nameField.setChangedListener(s -> {
            macro.name = s;
            doneButton.active = !nameField.getText().isEmpty() && !nameField.getText().contains(" ") && macroListWidget.canAdd(macro);
        });
        addSelectableChild(nameField);


        this.addDrawableChild(new ButtonWidget(this.width / 2 + 2, this.height / 4 + 144 + 5, 98, 20, ScreenTexts.CANCEL, (button) -> {
            client.setScreen(parent);
        }));

        Key keyCode = macro.getKey();
        Text text = keyCode.getLocalizedText();
        if (keyCode == InputUtil.UNKNOWN_KEY) text = Text.of("Not Bound");

        keyBindButton = new ButtonWidget(this.width / 2 + 26, 55, 75, 20, text, (button) -> {
            detectingKeycodeKey = true;
            keyBindButton.setMessage((new LiteralText("> ")).append(keyBindButton.getMessage().shallowCopy().formatted(Formatting.YELLOW)).append(" <").formatted(Formatting.YELLOW));
        });
        if (detectingKeycodeKey) keyBindButton.onPress();

        this.addDrawableChild(keyBindButton);

        int widgetWidth = 339;
        List<CommandEntry> entries = null;
        if (commandList != null) entries = commandList.children();

        commandList = new CommandListWidget(client, this, widgetWidth, height, 85, this.height / 4 + 144 + 5 - 10, 24);
        commandList.setLeftPos(width / 2 - widgetWidth / 2);


        if (entries != null) {
            commandList.children().clear();
            commandList.children().addAll(entries);
        } else {
            for (Action action : macro.actions) {
                if (action instanceof CommandAction commandAction) {
                    commandList.addCommand(commandAction.command);
                }
            }
        }

        this.addSelectableChild(commandList);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(0);
        commandList.render(matrices, mouseX, mouseY, delta);

        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 8, 16777215);

        drawCenteredText(matrices, this.textRenderer, "KeyBind", width / 2 - (99 - textRenderer.getWidth("KeyBind") / 2), 55 + textRenderer.fontHeight / 2, 16777215);
        nameField.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void tick() {
        nameField.tick();
        commandList.tick();

        super.tick();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        Key key = InputUtil.fromKeyCode(keyCode, scanCode);
        if (keyCode == InputUtil.GLFW_KEY_ESCAPE) key = InputUtil.UNKNOWN_KEY;

        if (updateKeybinding(key)) return false;
        if (commandList.keyPressed(keyCode, scanCode, modifiers)) return false;
        if (keyCode == InputUtil.GLFW_KEY_TAB || keyCode == InputUtil.GLFW_KEY_SPACE) return false;

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (commandList.keyReleased(keyCode, scanCode, modifiers)) return false;

        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (commandList.charTyped(chr, modifiers)) return false;

        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!commandList.isMouseOver(mouseX, mouseY)) commandList.mouseClicked(mouseX, mouseY, button);
        if (updateKeybinding(Type.MOUSE.createFromCode(button))) return false;


        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean updateKeybinding(Key key) {
        if (detectingKeycodeKey) {
            detectingKeycodeKey = false;
            Text text = key.getLocalizedText();
            if (key == InputUtil.UNKNOWN_KEY) text = Text.of("Not Bound");

            keyBindButton.setMessage(text);
            macro.setKey(key);
            return true;
        }

        return false;
    }


}
