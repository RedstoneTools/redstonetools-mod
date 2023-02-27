package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.RedstoneToolsGameRules;
import com.domain.redstonetools.features.Feature;
import com.mojang.brigadier.Command;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanRule;
import net.minecraft.world.GameRules.Key;

@Feature(name = "Redstoner", description = "Sets the game rules to be more redstone friendly.", command = "redstoner")
public class RedstonerFeature extends CommandFeature {
    @Override
    protected int execute(ServerCommandSource source) {
        GameRules rules = source.getWorld().getGameRules();
        MinecraftServer server = source.getServer();

        setRule(GameRules.DO_TILE_DROPS, false, rules, server, source);
        setRule(GameRules.DO_TRADER_SPAWNING, false, rules, server, source);
        setRule(GameRules.DO_WEATHER_CYCLE, false, rules, server, source);
        setRule(GameRules.DO_DAYLIGHT_CYCLE, false, rules, server, source);
        setRule(GameRules.DO_MOB_SPAWNING, false, rules, server, source);
        setRule(RedstoneToolsGameRules.DO_CONTAINER_DROPS, false, rules, server, source);

        source.getWorld().setTimeOfDay(6000); // Noon

        return Command.SINGLE_SUCCESS;
    }

    private void setRule(Key<BooleanRule> rule, Boolean value, GameRules rules, MinecraftServer server, ServerCommandSource source) {
        rules.get(rule).set(value, server);
        source.sendFeedback(Text.of(String.format("Gamerule %s is now set to: %s", rule.getName(), value.toString())), true);
    }
}
