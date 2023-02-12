package com.domain.redstonetools.utils;

import com.domain.redstonetools.features.options.Argument;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandUtils {
    private CommandUtils() {
    }

    public static void register(String name, List<Argument<?>> arguments, Command<ServerCommandSource> executor, CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        var base = CommandManager.literal(name);

        if (arguments.stream().allMatch(arg -> arg.optional)) {
            base.executes(executor);
        }

        if (!arguments.isEmpty()) {
            base.then(createArgumentChain(arguments.stream()
                    .sorted((a, b) -> Boolean.compare(a.optional, b.optional))
                    .toList(), executor));
        }

        dispatcher.register(base);
    }

    private static ArgumentBuilder<ServerCommandSource, ?> createArgumentChain(List<Argument<?>> arguments, Command<ServerCommandSource> executor) {
        var reversedArguments = new ArrayList<>(arguments);
        Collections.reverse(reversedArguments);

        ArgumentBuilder<ServerCommandSource, ?> argument = null;
        for (var arg : reversedArguments) {
            if (argument == null) {
                argument = CommandManager.argument(arg.name, arg.type).executes(executor);
            } else {
                argument = CommandManager.argument(arg.name, arg.type).then(argument);

                // If the argument is optional or if this is the last required argument it should run the executor
                if (arg.optional || reversedArguments.get(reversedArguments.indexOf(arg) - 1).optional) {
                    argument.executes(executor);
                }
            }
        }

        return argument;
    }
}
