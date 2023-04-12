package tools.redstone.redstonetools.macros.gui.widget.commandlist;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class CommandEntryPlaceHolder extends CommandEntry{
    public CommandEntryPlaceHolder(MinecraftClient client, CommandListWidget owner, String text) {
        super(client, owner, text);
        super.deleteButton.visible = false;
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        if (!super.command.getText().isEmpty()) {
            super.owner.addCommandFromPlaceHolder(command.getText(),this);
            command.setText("");
        }

        super.render(matrices, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);
    }

}
