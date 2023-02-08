package com.domain.redstonetools.service;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Standardized method of building and registering
 * commands per feature.
 */
public class CommandProvider implements CommandRegistrationCallback {

    {
        CommandRegistrationCallback.EVENT.register(this);
    }

    // the commands to register
    final List<LiteralArgumentBuilder<ServerCommandSource>> commands =
            new ArrayList<>();

    /**
     * Create a new command builder and schedule
     * it to be registered when the command registration
     * callback is invoked.
     *
     * @param name The command name.
     * @return The command builder.
     */
    public LiteralArgumentBuilder<ServerCommandSource> newCommand(String name) {
        LiteralArgumentBuilder<ServerCommandSource> builder =
                LiteralArgumentBuilder.literal(name);
        commands.add(builder);
        return builder;
    }

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        for (LiteralArgumentBuilder<ServerCommandSource> argumentBuilder : commands) {
            LiteralCommandNode<ServerCommandSource> node =
                    dispatcher.register(argumentBuilder);
        }
    }

}
