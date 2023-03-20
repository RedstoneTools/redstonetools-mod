package com.domain.redstonetools.macros.actions;

import com.domain.redstonetools.utils.CommandSourceUtils;
import com.domain.redstonetools.utils.CommandUtils;
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

        CommandSourceUtils.executeCommand(player.getCommandSource(), command);
    }
}
