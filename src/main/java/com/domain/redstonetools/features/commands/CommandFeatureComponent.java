package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.FeatureComponent;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

/**
 * A command-based feature with one
 * primary command.
 */
public interface CommandFeatureComponent extends FeatureComponent {

    @Override
    default void register() {
        CommandRegistrationCallback.EVENT.register(this::registerCommand);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    default void registerCommand(CommandDispatcher dispatcher, boolean dedicated) {
        dispatcher.register(literal(getCommandName()).executes(this::executeCommand));
    }

    /**
     * Get the name of the primary command.
     */
    String getCommandName();

    /**
     * The Brigadier command handler.
     *
     * @param context The command context.
     * @return The execute return value.
     * @throws CommandSyntaxException Any syntax exceptions that may be thrown.
     */
    int executeCommand(CommandContext<Object> context) throws CommandSyntaxException;

}
