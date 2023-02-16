package com.domain.redstonetools.features.commands.redstoner;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.commands.CommandFeature;
import com.domain.redstonetools.features.options.EmptyOptions;
import com.domain.redstonetools.utils.CommandSourceUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;

import java.util.List;

@Feature(name = "redstoner")
public class RedstonerFeature extends CommandFeature<EmptyOptions> {
    @Override
    protected int execute(ServerCommandSource source, EmptyOptions options) {
        List.of(
                "gamerule doTileDrops false",
                "gamerule doTraderSpawning false",
                "gamerule doWeatherCycle false",
                "gamerule doDaylightCycle false",
                "gamerule doMobSpawning false",
                "gamerule doContainerDrops false",
                "time set noon"
                ).forEach(cmd -> CommandSourceUtils.executeCommand(source, cmd));

        return Command.SINGLE_SUCCESS;
    }
}
