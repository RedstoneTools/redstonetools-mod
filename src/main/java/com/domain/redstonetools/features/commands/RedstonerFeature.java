package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.RedstoneToolsGameRules;
import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.commands.CommandFeature;
import com.domain.redstonetools.features.options.EmptyOptions;
import com.mojang.brigadier.Command;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;

@Feature(name = "Redstoner", description = "Sets the game rules to be more redstone friendly.", command = "redstoner")
public class RedstonerFeature extends CommandFeature {
    @Override
    protected int execute(ServerCommandSource source) {
        GameRules rules = source.getWorld().getGameRules();
        MinecraftServer server = source.getServer();

        rules.get(GameRules.DO_TILE_DROPS).set(false, server);
        rules.get(GameRules.DO_TRADER_SPAWNING).set(false, server);
        rules.get(GameRules.DO_WEATHER_CYCLE).set(false, server);
        rules.get(GameRules.DO_DAYLIGHT_CYCLE).set(false, server);
        rules.get(GameRules.DO_MOB_SPAWNING).set(false, server);
        rules.get(RedstoneToolsGameRules.DO_CONTAINER_DROPS).set(false, server);

        source.getWorld().setTimeOfDay(6000); // Noon

        return Command.SINGLE_SUCCESS;
    }
}
