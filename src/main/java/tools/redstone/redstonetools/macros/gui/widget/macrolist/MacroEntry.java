package tools.redstone.redstonetools.macros.gui.widget.macrolist;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import tools.redstone.redstonetools.macros.Macro;
import tools.redstone.redstonetools.macros.gui.widget.IconButtonWidget;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class MacroEntry extends AlwaysSelectedEntryListWidget.Entry<MacroEntry>{

    private final MacroListWidget owner;

    private final CheckboxWidget buttonWidget;
    private final ButtonWidget deleteButton;
    private final ButtonWidget editButton;
    public final Macro macro;


    public MacroEntry(Macro macro, MacroListWidget owner) {
        this.macro = macro;
        this.owner = owner;

        buttonWidget = CheckboxWidget.builder(Text.literal(""), MinecraftClient.getInstance().textRenderer)
                .pos(0, 0)                                    // x, y
                .checked(macro.enabled)                       // initial checked state
                .callback((cb, isChecked) -> {
                    macro.enabled = isChecked;                // update your flag on change
                })
                // (optional) .tooltip(Tooltip.of(Text.of("…")))  // if you want a hover-tooltip
                .build();                                     // finally produce the CheckboxWidget

//        buttonWidget = new CheckboxWidget(0, 0, 20, 20, null, macro.enabled, false);
        deleteButton = new IconButtonWidget(IconButtonWidget.CROSS_ICON,0, 0, 20, 20 ,Text.of(""), (button) -> {
            deleteIfConfirmed();
        });
        editButton = new IconButtonWidget(IconButtonWidget.PENCIL_ICON,0, 0, 20, 20, Text.of(""), (button) -> {
            owner.parent.openEditScreen(this);
        });
    }


    @Override
    public void render(DrawContext context,
                       int index,
                       int y,
                       int x,
                       int entryWidth,
                       int entryHeight,
                       int mouseX,
                       int mouseY,
                       boolean hovered,
                       float tickDelta) {
        // 1) render each child widget
        buttonWidget .render(context, mouseX, mouseY, tickDelta);
        editButton   .render(context, mouseX, mouseY, tickDelta);
        deleteButton .render(context, mouseX, mouseY, tickDelta);

        TextRenderer tr = owner.client.textRenderer;
        String text = macro.name;
        int maxWidth = owner.getRowWidth() - 2;
        if (tr.getWidth(text) > maxWidth) {
            while (tr.getWidth(text + "...") > maxWidth) {
                text = text.substring(0, text.length() - 1);
            }
            text += "...";
        }

        context.drawText(tr, text, x, y + 3, macro.enabled ? 0xFFFFFE : 0x7F7F7F, true);
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
            owner.client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            widget.onClick(mouseX,mouseY);

            return true;
        }
        return false;
    }

    private void onPressed() {
        owner.setSelected(this);
    }

    public Text getNarration() {
        return Text.translatable("narrator.select");
    }


    public void delete() {
        owner.removeEntry(this);
        macro.unregisterKeyBinding();
        MacroListWidget.macroManager.removeMacro(this.macro);
        MacroListWidget.macroManager.updateMacroKeys();
    }

    public void deleteIfConfirmed() {
        owner.client.setScreen(new ConfirmScreen((confirmed) -> {
            if (confirmed) {
                this.delete();
            }

            owner.client.setScreen(owner.parent);
        }, Text.of("Delete macro"), Text.of("Are you sure you want to delete '" + macro.name + "'?")));
    }
}
