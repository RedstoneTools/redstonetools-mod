package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.arguments.Argument;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static com.domain.redstonetools.features.arguments.IntegerSerializer.integer;
import static com.domain.redstonetools.features.arguments.StringSerializer.word;

@Feature(id = "base-convert", name = "Base Convert", description = "Converts a number from one base to another.", command = "base")
public class BaseConvertFeature extends CommandFeature {
    public static final Argument<Integer> fromBase = Argument
            .ofType(integer(2, 36));
    public static final Argument<String> number = Argument
            .ofType(word());
    public static final Argument<Integer> toBase = Argument
            .ofType(integer(2, 36));

    @Override
    protected int execute(ServerCommandSource source) throws CommandSyntaxException {
        int input;
        try {
            input = Integer.parseInt(number.getValue(), fromBase.getValue());
        } catch (NumberFormatException e) {
            throw new CommandSyntaxException(null, Text.of("Inputted number does not match the specified base"));
        }

        var output = Integer.toString(input, toBase.getValue());
        source.sendFeedback(Text.of(output), false);

        return Command.SINGLE_SUCCESS;
    }

}
