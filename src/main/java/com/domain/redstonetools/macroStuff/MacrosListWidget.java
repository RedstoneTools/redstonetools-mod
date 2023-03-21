package com.domain.redstonetools.macroStuff;

import com.domain.redstonetools.RedstoneToolsClient;
import com.domain.redstonetools.macros.Macro;
import com.domain.redstonetools.macros.MacroManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;


import java.util.ArrayList;
import java.util.List;

import static com.domain.redstonetools.RedstoneToolsClient.INJECTOR;

public class MacrosListWidget extends AlwaysSelectedEntryListWidget<MacrosListWidget.MacroEntry> {

    private static final MacroManager macroManager = INJECTOR.getInstance(MacroManager.class);

    static {
        for (int i = 0; i < 30; i++) {
            macroManager.addMacro(new Macro("test" + i, true,-1,new ArrayList<>()));
        }
    }

    private final MacroSelectScreen parent;
    private final MinecraftClient client;


    public MacrosListWidget(MacroSelectScreen parent, MinecraftClient client) {
        super(client, parent.width, parent.height, 20, parent.height - 42, 20);
        this.parent = parent;
        this.client = client;

        for (Macro macro : macroManager.getMacros()) {
            addEntry(new MacroEntry(macro));
        }


        if (this.getSelectedOrNull() != null) {
            this.centerScrollOn(this.getEntry(0));
        }
        this.left = 0;

    }

    public void addMacro(Macro macro) {
        macroManager.addMacro(macro);
        addEntry(new MacroEntry(macro));
    }

    public boolean canAdd(String macroName) {
        Macro macro = macroManager.getMacro(macroName);

        return macro == null;
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
        MacroEntry entry = super.getEntryAtPosition(width/2,mouseY);
        if (entry != null) entry.mouseClickedInRow(mouseX,mouseY,button);

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public int getRowWidth() {
        return 120;
    }


    public class MacroEntry extends AlwaysSelectedEntryListWidget.Entry<MacroEntry>{

        private final CheckboxWidget buttonWidget;
        private final ButtonWidget deleteButton;
        private final ButtonWidget editButton;
        final Macro macro;


        public MacroEntry(Macro macro) {
            this.macro = macro;
            buttonWidget = new CheckboxWidget(0, 0, 20, 20, null, macro.enabled, false);
            deleteButton = new ButtonWidget(0, 0, 20, 20, Text.of("D"), (button) -> {
                deleteIfConfirmed();
            });
            editButton = new ButtonWidget(0, 0, 20, 20, Text.of("E"), (button) -> {
               MacrosListWidget.this.parent.openEditScreen(this);
            });
        }


        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            renderWidget(buttonWidget,matrices,mouseX,mouseY,tickDelta,x-30,y-2);
            renderWidget(editButton,matrices,mouseX,mouseY,tickDelta,x+entryWidth,y-2);
            renderWidget(deleteButton,matrices,mouseX,mouseY,tickDelta,x+entryWidth+22,y-2);

            String text = macro.name;

            if (client.textRenderer.getWidth(text) > getRowWidth()-2) {
                while (client.textRenderer.getWidth(text + "...") > getRowWidth()-2) {
                    text = text.substring(0,text.length()-1);
                }

                text += "...";
            }


            client.textRenderer.drawWithShadow(matrices, text, x, (float)(y + 1),macro.enabled?16777215:8355711, true);
        }

        private void renderWidget(PressableWidget widget,MatrixStack matrices, int mouseX, int mouseY, float tickDelta, int x, int y) {
            widget.x = x;
            widget.y = y;
            widget.render(matrices,mouseX,mouseY,tickDelta);
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) {
                this.onPressed();
                return true;
            } else {
                return false;
            }
        }

        public void mouseClickedInRow(double mouseX, double mouseY, int button) {
            if (button != 0) return;

            if (clickWidget(buttonWidget, mouseX, mouseY)) macro.enabled = buttonWidget.isChecked();
            clickWidget(editButton,mouseX,mouseY);
            clickWidget(deleteButton,mouseX,mouseY);


        }


        private boolean clickWidget(ClickableWidget widget, double mouseX, double mouseY) {
            if (widget.isMouseOver(mouseX,mouseY)) {
                client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                widget.onClick(mouseX,mouseY);

                return true;
            }
            return false;
        }

        private void onPressed() {
            MacrosListWidget.this.setSelected(this);
        }

        public Text getNarration() {
            return new TranslatableText("narrator.select");
        }

        //TODO add config deleting logic
        public void delete() {
            MacrosListWidget.this.removeEntry(this);
            macroManager.removeMacro(this.macro);
        }

        public void deleteIfConfirmed() {
            client.setScreen(new ConfirmScreen((confirmed) -> {
                if (confirmed) {
                    this.delete();
                }

                client.setScreen(parent);
            }, Text.of("Are you sure you want to delete '" + macro.name + "'?"), Text.of(""), new TranslatableText("selectWorld.deleteButton"), ScreenTexts.CANCEL));
        }


    }
}
