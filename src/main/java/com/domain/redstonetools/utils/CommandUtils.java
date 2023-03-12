package com.domain.redstonetools.utils;

import com.domain.redstonetools.features.arguments.Argument;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandUtils {
    private CommandUtils() { }

    /**
     * Register a new command by name with the appropriate
     * arguments. This handles optionals and other behaviours
     * automatically.
     *
     * @param name The name of the command.
     * @param arguments The list of arguments.
     * @param executor The command executor.
     * @param dispatcher The {@link CommandDispatcher} instance.
     * @param dedicated todo: Unused?
     */
    public static void register(String name,
                                List<Argument<?>> arguments,
                                Command<ServerCommandSource> executor,
                                CommandDispatcher<ServerCommandSource> dispatcher,
                                boolean dedicated) {
        var base = CommandManager.literal(name);

        if (arguments.stream().allMatch(Argument::isOptional)) {
            base.executes(executor);
        }

        if (!arguments.isEmpty()) {
            base.then(createArgumentChain(arguments.stream()
                    .sorted((a, b) -> Boolean.compare(a.isOptional(), b.isOptional()))
                    .toList(), executor));
        }

        dispatcher.register(base);
    }

    // builds the command argument chain,
    // handling optionals by injecting the
    // executor before optional arguments
    private static ArgumentBuilder<ServerCommandSource, ?> createArgumentChain(List<Argument<?>> arguments, Command<ServerCommandSource> executor) {
        var reversedArguments = new ArrayList<>(arguments);
        Collections.reverse(reversedArguments);

        ArgumentBuilder<ServerCommandSource, ?> argument = null;
        for (var arg : reversedArguments) {
            if (argument == null) {
                argument = CommandManager.argument(arg.getName(), arg.getType()).executes(executor);
            } else {
                argument = CommandManager.argument(arg.getName(), arg.getType()).then(argument);

                // If the argument is optional or if this is the last required argument it should run the executor
                if (arg.isOptional() || reversedArguments.get(reversedArguments.indexOf(arg) - 1).isOptional()) {
                    argument.executes(executor);
                }
            }
        }

        return argument;
    }
}
