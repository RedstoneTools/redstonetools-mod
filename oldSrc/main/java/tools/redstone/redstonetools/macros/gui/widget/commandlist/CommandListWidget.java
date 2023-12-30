package tools.redstone.redstonetools.macros.gui.widget.commandlist;

import tools.redstone.redstonetools.macros.gui.screen.MacroEditScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;

import java.util.ArrayList;
import java.util.List;

public class CommandListWidget extends EntryListWidget<CommandEntry> {

    private final MacroEditScreen parent;

    public CommandListWidget(MinecraftClient client, MacroEditScreen parent, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
        this.parent = parent;
        addEntry(new CommandEntryPlaceHolder(client,this,""));
    }

    public void tick() {
        for (CommandEntry entry : children()) {
            entry.tick();
        }
    }



    public CommandEntry addCommand(String command) {
        CommandEntry entry = new CommandEntry(client,this,command);
        List<CommandEntry> entries = children();

        entries.add(entries.size()-1,entry);

        return entry;
    }

    protected void addCommandFromPlaceHolder(String command,CommandEntryPlaceHolder placeHolder) {
        CommandEntry entry = addCommand(command);
        placeHolder.setFocused(false);

        entry.command.x = placeHolder.command.x;
        entry.command.y = placeHolder.command.y;
        entry.setFocused(true);
    }

    public void centerScrollOn(CommandEntry entry) {
        super.centerScrollOn(entry);
    }



    protected void removeCommand(CommandEntry command) {
        removeEntry(command);
        setScrollAmount(getScrollAmount());
    }

    protected int getScrollbarPositionX() {
        return parent.width/2+this.width/2-4;
    }

    public List<String> getCommandList() {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < children().size(); i++){
            CommandEntry command = getEntry(i);
            if (command instanceof CommandEntryPlaceHolder) continue;

            list.add(command.getText());
        }

        return list;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        for (CommandEntry entry : children()) {
            entry.charTyped(chr,modifiers);
        }

        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (CommandEntry entry : children()) {
            entry.keyPressed(keyCode, scanCode, modifiers);
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        for (CommandEntry entry : children()) {
            entry.keyReleased(keyCode, scanCode, modifiers);
        }

        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        CommandEntry clickedEntry = getEntryAtPosition(mouseX,mouseY);
        for (CommandEntry entry : children()) {
            if (entry != clickedEntry) entry.setFocused(false);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    protected MacroEditScreen getParent() {
        return parent;
    }

    @Override
    public int getRowWidth() {
        return width;
    }

    public int getWidth() {
        return this.width;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {}
}
