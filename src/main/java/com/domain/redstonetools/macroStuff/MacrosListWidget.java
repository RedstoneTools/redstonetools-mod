package com.domain.redstonetools.macroStuff;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;


import java.util.ArrayList;
import java.util.List;

import static com.domain.redstonetools.RedstoneToolsClient.LOGGER;

public class MacrosListWidget extends AlwaysSelectedEntryListWidget<MacrosListWidget.MacroEntry> {

    private static final List<Macro> macros = new ArrayList<>();
    static {
        for (int i = 0; i < 30; i++) {
            macros.add(new Macro("test" + i));
        }
    }

    private final MacroSelectScreen parent;
    private final MinecraftClient client;

    private boolean leftClickPressed = false;

    public MacrosListWidget(MacroSelectScreen parent, MinecraftClient client) {
        super(client, parent.width + 45, parent.height, 20, parent.height - 64, 20);
        this.parent = parent;
        this.client = client;

        for (Macro macro : macros) {
            addEntry(new MacroEntry(macro));
        }


        if (this.getSelectedOrNull() != null) {
            this.centerScrollOn(this.getEntry(0));
        }
        this.left = - width/4+40;

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);

        leftClickPressed = false;
    }

    public void addMacro(Macro macro) {
        macros.add(macro);
        addEntry(new MacroEntry(macro));
    }

    public boolean canAdd(String macroName) {
        for (Macro macro1 : macros) {
            if (macro1.getName().equals(macroName)) return false;
        }

        return true;
    }

    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 20;
    }


    protected void renderBackground(MatrixStack matrices) {
        parent.renderBackground(matrices);
    }

    protected boolean isFocused() {
        return parent.getFocused() == this;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) leftClickPressed = true;

        return super.mouseClicked(mouseX, mouseY, button);
    }




    public class MacroEntry extends AlwaysSelectedEntryListWidget.Entry<MacroEntry> {

        private final CheckboxWidget buttonWidget;
        final Macro macro;


        public MacroEntry(Macro macro) {
            this.macro = macro;
            buttonWidget = new CheckboxWidget(0, 0, 20, 20, Text.of("Enabled"), macro.isEnabled());
        }


        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            String text = this.macro.getName();
            buttonWidget.x = x + entryWidth;
            buttonWidget.y = y-2;
            buttonWidget.render(matrices,mouseX,mouseY,tickDelta);

            if (buttonWidget.isHovered() && leftClickPressed) {
                client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                buttonWidget.onPress();
                macro.setEnabled(buttonWidget.isChecked());
            }




            client.textRenderer.drawWithShadow(matrices, text, (float)(MacrosListWidget.this.width / 8 ), (float)(y + 1), 16777215, true);
        }



        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            LOGGER.info("mouse");
            if (button == 0) {
                this.onPressed();
                return true;
            } else {
                return false;
            }
        }

        private void onPressed() {
            MacrosListWidget.this.setSelected(this);
            parent.setActive(true);
        }

        public Text getNarration() {
            return new TranslatableText("narrator.select");
        }

        //TODO add config deleting logic
        public void delete() {
            MacrosListWidget.this.removeEntry(this);
            macros.remove(this.macro);
        }

        public void deleteIfConfirmed() {
            client.setScreen(new ConfirmScreen((confirmed) -> {
                if (confirmed) {
                    this.delete();
                }

                client.setScreen(parent);
            }, Text.of("Are you sure you want to delete '" + macro.getName() + "'?"), Text.of(""), new TranslatableText("selectWorld.deleteButton"), ScreenTexts.CANCEL));
        }


    }
}
