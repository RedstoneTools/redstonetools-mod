package com.domain.redstonetools.features.commands.airplace;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.commands.CommandFeature;
import com.domain.redstonetools.features.options.EmptyOptions;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

@Feature(name = "")
public class AirPlaceFeature extends CommandFeature<EmptyOptions> {
    public static boolean enabled = false;

    @Override
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register(
                CommandManager.literal("airplace")
                        .then(CommandManager.literal("enable").executes(this::enable))
                        .then(CommandManager.literal("disable").executes(this::disable)));
    }

    private int enable(CommandContext<ServerCommandSource> context) {
        enabled = true;

        context.getSource().sendFeedback(Text.of("Airplace has been enabled"), false);

        return Command.SINGLE_SUCCESS;
    }

    private int disable(CommandContext<ServerCommandSource> context) {
        enabled = false;

        context.getSource().sendFeedback(Text.of("Airplace has been disabled"), false);

        return Command.SINGLE_SUCCESS;
    }

    @Override
    protected int execute(ServerCommandSource source, EmptyOptions options) throws CommandSyntaxException {
        return 0;
    }
}
