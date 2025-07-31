package tools.redstone.redstonetools.macros.gui.malilib;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.macros.Macro;
import tools.redstone.redstonetools.macros.MacroManager;

import java.util.List;

public class MacrosScreen extends Screen {
    private final Screen parent;
    private final List<Macro> macros;

    private MacroListWidget listWidget;
    private ButtonWidget addButton;

    public MacrosScreen(Screen parent, List<Macro> macros) {
        super(Text.literal("Macros"));
        this.parent = parent;
        this.macros = macros;
    }

    @Override
    protected void init() {
        super.init();

        int topMargin = 60;
        int bottomMargin = 30;
        int rowHeight = 20;

        int listWidth = this.width - 20;
        int listHeight = this.height - topMargin - bottomMargin;

        listWidget = new MacroListWidget(this.client, listWidth, listHeight, topMargin, rowHeight);
        listWidget.setPosition(10, topMargin);

        macros.forEach(macro -> listWidget.addMacro(new MacroEntry(this, macro)));
        this.addSelectableChild(listWidget);

        int btnWidth = 120;
        int btnY = 25; // near top
        addButton = ButtonWidget.builder(Text.literal("Add Macro"), btn -> {
            Macro m = Macro.buildEmpty();
            macros.add(m);
            listWidget.addMacro(new MacroEntry(this, m));
            openEditor(m, true);
        }).dimensions(this.width / 2 - btnWidth / 2, btnY, btnWidth, 20).build();
        this.addDrawableChild(addButton);
        this.addSelectableChild(addButton);

        ButtonWidget backButton = ButtonWidget.builder(Text.literal("Back"), btn -> this.close()).dimensions(10, btnY, 60, 20).build();
        this.addDrawableChild(backButton);
        this.addSelectableChild(backButton);
    }

    public void openEditor(Macro macro, boolean isNew) {
        this.client.setScreen(new MacroEditScreen(this, macro, isNew));
    }

    public void deleteMacro(Macro macro, MacroEntry entry) {
        macros.remove(macro);
        if (entry != null) {
            listWidget.removeMacro(entry);
        }
        MacroManager.saveChanges();
    }

    @Override
    public void close() {
        super.close();
        MacroManager.saveChanges();
        MacroManager.updateMacroKeys();
        this.client.setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
//        this.renderBackground(context, mouseX, mouseY, delta);
        listWidget.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 12, 0xffffffff);
    }

    private static class MacroListWidget extends ElementListWidget<MacroEntry> {
        public MacroListWidget(MinecraftClient mc, int width, int height, int y, int itemHeight) {
            super(mc, width, height, y, itemHeight);
        }

        @Override
        public int getRowWidth() {
            return this.width - 20;
        }

        public void addMacro(MacroEntry entry) {
            this.addEntry(entry);
        }

        public void removeMacro(MacroEntry entry) {
            this.removeEntry(entry);
        }
    }
}