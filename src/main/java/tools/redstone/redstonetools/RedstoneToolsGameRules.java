package tools.redstone.redstonetools;

//? if <=1.21.10 {
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;

public class RedstoneToolsGameRules {
	private RedstoneToolsGameRules() {
	}

	public static GameRules.Key<GameRules.BooleanValue> DO_CONTAINER_DROPS;
//	public static GameRules.Key<GameRules.BooleanRule> DO_BLOCK_UPDATES_AFTER_EDIT;

	public static void register() {
		DO_CONTAINER_DROPS = GameRuleRegistry.register("doContainerDrops", GameRules.Category.DROPS, GameRuleFactory.createBooleanRule(true));

//		if (DependencyLookup.WORLDEDIT_PRESENT) {
//			DO_BLOCK_UPDATES_AFTER_EDIT = GameRuleRegistry.register("doBlockUpdatesAfterEdit", GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(false));
//		}
	}
}
//?} else {
/*import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;

import java.util.function.ToIntFunction;

public class RedstoneToolsGameRules {
	private RedstoneToolsGameRules() {
	}

	public static GameRule<Boolean> DO_CONTAINER_DROPS;
//	public static GameRule<Boolean> DO_BLOCK_UPDATES_AFTER_EDIT;

	public static void register() {
		DO_CONTAINER_DROPS = registerBooleanRule("do_container_drops", GameRuleCategory.DROPS, true);

//		if (DependencyLookup.WORLDEDIT_PRESENT) {
//			DO_BLOCK_UPDATES_AFTER_EDIT = registerBooleanRule("doBlockUpdatesAfterEdit", GameRuleCategory.UPDATES, false);
//		}
	}

	private static GameRule<Boolean> registerBooleanRule(String name, GameRuleCategory category, boolean defaultValue) {
		return registerRule(
			name,
			category,
			GameRuleType.BOOL,
			BoolArgumentType.bool(),
			Codec.BOOL,
			defaultValue,
			FeatureFlagSet.of(),
			GameRuleTypeVisitor::visitBoolean,
			value -> value ? 1 : 0
		);
	}

	private static <T> GameRule<T> registerRule(
		String name,
		GameRuleCategory category,
		GameRuleType type,
		ArgumentType<T> argumentType,
		Codec<T> codec,
		T defaultValue,
		FeatureFlagSet requiredFeatures,
		net.minecraft.world.level.gamerules.GameRules.VisitorCaller<T> acceptor,
		ToIntFunction<T> commandResultSupplier
	) {
		return Registry.register(
			BuiltInRegistries.GAME_RULE, Identifier.fromNamespaceAndPath(RedstoneTools.MOD_ID, name), new GameRule<>(category, type, argumentType, acceptor, codec, commandResultSupplier, defaultValue, requiredFeatures)
		);
	}
}
*///?}
