package tools.redstone.redstonetools.macros.gui.widget.macrolist;

import tools.redstone.redstonetools.macros.Macro;
import tools.redstone.redstonetools.macros.gui.widget.IconButtonWidget;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class MacroEntry extends AlwaysSelectedEntryListWidget.Entry<MacroEntry>{

    private final MacroListWidget owner;

    private final CheckboxWidget buttonWidget;
    private final ButtonWidget deleteButton;
    private final ButtonWidget editButton;
    public final Macro macro;


    public MacroEntry(Macro macro, MacroListWidget owner) {
        this.macro = macro;
        this.owner = owner;

        buttonWidget = new CheckboxWidget(0, 0, 20, 20, null, macro.enabled, false);
        deleteButton = new IconButtonWidget(IconButtonWidget.CROSS_ICON,0, 0, 20, 20 ,Text.of(""), (button) -> {
            deleteIfConfirmed();
        });
        editButton = new IconButtonWidget(IconButtonWidget.PENCIL_ICON,0, 0, 20, 20, Text.of(""), (button) -> {
            owner.parent.openEditScreen(this);
        });
    }


    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        renderWidget(buttonWidget,matrices,mouseX,mouseY,tickDelta,x-30,y-2);
        renderWidget(editButton,matrices,mouseX,mouseY,tickDelta,x+entryWidth,y-2);
        renderWidget(deleteButton,matrices,mouseX,mouseY,tickDelta,x+entryWidth+22,y-2);

        String text = macro.name;

        if (owner.client.textRenderer.getWidth(text) > owner.getRowWidth()-2) {
            while (owner.client.textRenderer.getWidth(text + "...") > owner.getRowWidth()-2) {
                text = text.substring(0,text.length()-1);
            }

            text += "...";
        }


        owner.client.textRenderer.drawWithShadow(matrices, text, x, y+3,macro.enabled?16777215:8355711, true);
    }

    private void renderWidget(PressableWidget widget, MatrixStack matrices, int mouseX, int mouseY, float tickDelta, int x, int y) {
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
        return new TranslatableText("narrator.select");
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
