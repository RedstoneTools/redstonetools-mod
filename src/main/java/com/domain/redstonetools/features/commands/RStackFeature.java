package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.arguments.Argument;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;

import static com.domain.redstonetools.features.arguments.DirectionArgumentType.directionArgument;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;

@Feature(name = "rstack", description = "Stacks with custom distance", command = "/rstack")
public class RStackFeature extends CommandFeature {
    public static final Argument<Integer> count = Argument
            .ofType(integer())
            .withDefault(1);

    public static final Argument<String> direction = Argument
            .ofType(directionArgument())
            .withDefault("me");

    public static final Argument<Integer> spacing = Argument
            .ofType(integer())
            .withDefault(2);



    @Override
    protected int execute(ServerCommandSource source) throws CommandSyntaxException {


        return 0;
    }
}
