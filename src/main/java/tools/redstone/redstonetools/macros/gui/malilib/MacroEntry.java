package tools.redstone.redstonetools.macros.gui.malilib;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.macros.Macro;

import java.util.List;
import net.minecraft.client.gui.Selectable;

public class MacroEntry extends ElementListWidget.Entry<MacroEntry> {
    private final MacrosScreen parent;
    private final Macro macro;

    private final ButtonWidget editButton;
    private final ButtonWidget deleteButton;

    public MacroEntry(MacrosScreen parent, Macro macro) {
        this.parent = parent;
        this.macro = macro;

        int btnWidth = 50;
        int btnHeight = 18;
        editButton = ButtonWidget.builder(Text.literal("Edit"), btn -> parent.openEditor(macro, false))
                .dimensions(0, 0, btnWidth, btnHeight).build();
        deleteButton = ButtonWidget.builder(Text.literal("Delete"), btn -> parent.deleteMacro(macro, this))
                .dimensions(0, 0, btnWidth, btnHeight).build();
    }

    @Override
    public List<? extends Element> children() {
        return List.of(editButton, deleteButton);
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return List.of(editButton, deleteButton);
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean hovered, float delta) {
        MinecraftClient mc = MinecraftClient.getInstance();

        // macro.name does have shit, doesnt get shown on screen though
        context.drawText(mc.textRenderer, macro.name, x + 4, y + (rowHeight - 9) / 2, 0xFFFFFFFF, false);


        int btnY = y + (rowHeight - 18) / 2;
        int deleteX = x + rowWidth - deleteButton.getWidth() - 4;
        int editX = deleteX - editButton.getWidth() - 4;

        editButton.setPosition(editX, btnY);
        deleteButton.setPosition(deleteX, btnY);

        editButton.render(context, mouseX, mouseY, delta);
        deleteButton.render(context, mouseX, mouseY, delta);
    }
} 