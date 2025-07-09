package tools.redstone.redstonetools.macros.actions;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;

public class CommandAction extends Action {
    public String command;

    public CommandAction(String command) {
        this.command = command;
    }

    @Override
    public void run() {
        var player = MinecraftClient.getInstance().player;
        assert player != null;
        player.networkHandler.sendChatCommand(command.startsWith("/") ? command : "/" + command);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CommandAction commandAction) {
            return command.equals(commandAction.command);
        }

        return super.equals(obj);
    }

}
