package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.features.FeatureComponent;
import com.domain.redstonetools.service.CommandProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

/**
 * A feature component providing a primary
 * command for each feature, which also allows
 * for configuration of the feature.
 */
public interface PrimaryCommand extends FeatureComponent {

    @Override
    default void register() {
        // register primary command
        CommandProvider commandProvider = getService(CommandProvider.class);
        LiteralArgumentBuilder<ServerCommandSource> builder =
                commandProvider.newCommand(getPrimaryCommandName());
        configurePrimaryCommand(builder);
        // todo: configure primary command
        //  with configuration
    }

    /**
     * Configures the primary command.
     *
     * @param builder The command builder.
     */
    default void configurePrimaryCommand(LiteralArgumentBuilder<ServerCommandSource> builder) {

    }

    /**
     * Get the name of the primary command.
     */
    String getPrimaryCommandName();

}
