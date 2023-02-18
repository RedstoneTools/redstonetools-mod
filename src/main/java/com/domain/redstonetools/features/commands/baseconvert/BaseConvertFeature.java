package com.domain.redstonetools.features.commands.baseconvert;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.commands.CommandFeature;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

@Feature(name = "base")
public class BaseConvertFeature extends CommandFeature<BaseConvertOptions> {

    @Override
    protected int execute(ServerCommandSource source, BaseConvertOptions options) throws CommandSyntaxException {

        Integer input;

        try {

            input = Integer.parseInt(options.number.getValue(), options.fromBase.getValue());

        } catch (NumberFormatException e) {

            LiteralMessage message = new LiteralMessage("The input number does not match the input base");
            throw new CommandSyntaxException(new SimpleCommandExceptionType(message), message);

        }

        String result = Integer.toString(
                input,
                options.toBase.getValue());
        source.sendFeedback(Text.of(result), false);
        return Command.SINGLE_SUCCESS;

    }

}
