package com.domain.redstonetools;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

import static com.domain.redstonetools.RedstoneToolsClient.LOGGER;

public class RedstoneToolsGameRules {

    private RedstoneToolsGameRules() {
    }

    public static GameRules.Key<GameRules.BooleanRule> DO_CONTAINER_DROPS;

    public static void register() {
        DO_CONTAINER_DROPS = GameRuleRegistry.register("doContainerDrops",GameRules.Category.DROPS,GameRuleFactory.createBooleanRule(true));
    }

}
