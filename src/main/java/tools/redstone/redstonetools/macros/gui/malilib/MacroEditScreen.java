package tools.redstone.redstonetools.macros.gui.malilib;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.client.util.InputUtil;
import tools.redstone.redstonetools.macros.Macro;
import tools.redstone.redstonetools.macros.MacroManager;
import tools.redstone.redstonetools.macros.config.StringListWidget;

public class MacroEditScreen extends Screen {
    private final MacrosScreen parent;
    private final Macro macro;
    private final boolean isNew;

    private TextFieldWidget nameField;
    private ButtonWidget enabledButton;
    private ButtonWidget keyButton;
    private StringListWidget commandsWidget;

    private boolean capturingKey = false;

    public MacroEditScreen(MacrosScreen parent, Macro macro, boolean isNew) {
        super(Text.literal("Edit Macro"));
        this.parent = parent;
        this.macro = macro;
        this.isNew = isNew;
    }

    @Override
    protected void init() {
        int margin = 10;
        int y = margin + 20;

        nameField = new TextFieldWidget(this.textRenderer, margin, y, 200, 18, Text.literal("Name"));
        nameField.setText(macro.name);
        this.addSelectableChild(nameField);
        y += 24;

        enabledButton = ButtonWidget.builder(getEnabledText(), btn -> {
            macro.enabled = !macro.enabled;
            btn.setMessage(getEnabledText());
        }).dimensions(margin, y, 100, 18).build();
        this.addDrawableChild(enabledButton);

        keyButton = ButtonWidget.builder(Text.literal("Key: " + macro.getKey().getTranslationKey().replace("key.keyboard.", "")), btn -> {
            capturingKey = true;
            btn.setMessage(Text.literal("Press a key..."));
        }).dimensions(margin + 110, y, 120, 18).build();
        this.addDrawableChild(keyButton);
        y += 26;

        int listTop = y;
        commandsWidget = new StringListWidget(this.width, this.height-y-50, listTop, 26, macro.actionsAsStringList);
        this.addSelectableChild(commandsWidget);

        int btnY = this.height - 40;

        ButtonWidget addCmdButton = ButtonWidget.builder(Text.literal("Add Command"), btn -> commandsWidget.addEmptyRow()).dimensions(margin, btnY, 110, 18).build();
        this.addDrawableChild(addCmdButton);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Save"), btn -> saveAndClose())
                .dimensions(this.width - 100, btnY, 80, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Cancel"), btn -> cancel())
                .dimensions(this.width - 220, btnY, 80, 20).build());
    }

    private Text getEnabledText() {
        return Text.literal("Enabled: " + (macro.enabled ? "Yes" : "No"));
    }

    private void saveAndClose() {
        String newName = nameField.getText().trim();
        if (newName.isEmpty()) newName = "unnamed";

        // ensure uniqueness
        if (MacroManager.nameExists(newName, macro)) {
            int idx = 1;
            String base = newName;
            while (MacroManager.nameExists(base + "_" + idx, macro)) idx++;
            newName = base + "_" + idx;
        }
        macro.setName(newName);
        commandsWidget.rebuild();
        macro.fromStringList(macro.actionsAsStringList);
        MacroManager.saveChanges();
        MacroManager.updateMacroKeys();
        assert this.client != null;
        this.client.setScreen(parent);
    }

    private void cancel() {
        if (isNew) {
            parent.deleteMacro(macro, null);
        }
        this.close();
    }

    @Override
    public void close() {
        saveAndClose();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (capturingKey) {
            capturingKey = false;
            InputUtil.Key key = InputUtil.fromKeyCode(keyCode, scanCode);
            macro.setKey(key);
            keyButton.setMessage(Text.literal("Key: " + macro.getKey().getTranslationKey().replace("key.keyboard.", "")));
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFFF);
        nameField.render(context, mouseX, mouseY, delta);
        commandsWidget.render(context, mouseX, mouseY, delta);
    }
} 