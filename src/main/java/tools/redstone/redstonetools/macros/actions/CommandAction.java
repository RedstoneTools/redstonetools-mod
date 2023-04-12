package tools.redstone.redstonetools.macros.actions;

import net.minecraft.client.MinecraftClient;

public class CommandAction extends Action {
    public String command;

    public CommandAction(String command) {
        this.command = command;
    }

    @Override
    public void run() {
        var player = MinecraftClient.getInstance().player;
        assert player != null;

        player.sendChatMessage(command.startsWith("/") ? command : "/" + command);
    }
}
