package com.domain.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

public interface CommandFeature {
    default void registerCommand(CommandDispatcher dispatcher, boolean dedicated) {
        dispatcher.register(literal(getCommandName()).executes(this::executeCommand));
    }

    String getCommandName();

    int executeCommand(CommandContext<Object> context) throws CommandSyntaxException;
}
