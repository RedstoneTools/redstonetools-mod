package com.domain.redstonetools.features.commands.baseconvert;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.commands.CommandFeature;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

@Feature(name = "base")
public class BaseConvertFeature extends CommandFeature<BaseConvertOptions> {

    @Override
    protected int execute(ServerCommandSource source, BaseConvertOptions options) throws CommandSyntaxException {

        String result = Integer.toString(
                Integer.parseInt(options.number.getValue().toString(), options.fromBase.getValue()),
                options.toBase.getValue());

        source.sendFeedback(Text.of(result), false);

        return Command.SINGLE_SUCCESS;
    }

}
