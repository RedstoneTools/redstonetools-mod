package tools.redstone.redstonetools;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import tools.redstone.redstonetools.utils.DependencyLookup;


public class RedstoneToolsGameRules {
    private RedstoneToolsGameRules() {
    }

    public static GameRules.Key<GameRules.BooleanRule> DO_CONTAINER_DROPS;
    public static GameRules.Key<GameRules.BooleanRule> DO_BLOCK_UPDATES_AFTER_EDIT;

    public static void register() {
        DO_CONTAINER_DROPS = GameRuleRegistry.register("doContainerDrops", GameRules.Category.DROPS, GameRuleFactory.createBooleanRule(true));

        if (DependencyLookup.WORLDEDIT_LOADED) {
            DO_BLOCK_UPDATES_AFTER_EDIT = GameRuleRegistry.register("doBlockUpdatesAfterEdit", GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(false));
        }
    }
}
