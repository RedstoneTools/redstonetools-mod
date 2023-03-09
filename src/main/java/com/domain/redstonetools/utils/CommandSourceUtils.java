package com.domain.redstonetools.utils;

import net.minecraft.server.command.ServerCommandSource;

public class CommandSourceUtils {
    private CommandSourceUtils() {
    }

    public static void executeCommand(ServerCommandSource source, String command) {
        source.getServer().getCommandManager().execute(source, command);
    }
}
