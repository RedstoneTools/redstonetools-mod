package com.domain.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public interface CommandFeature {
    default void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register(CommandManager.literal(getCommandName()).executes(this::executeCommand));
    }

    String getCommandName();

    int executeCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException;
}
