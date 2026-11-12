package tools.redstone.redstonetools.utils;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;

public class ArgumentUtils {
	public static final String[] BLOCK_COLOR_SUGGESTIONS = EnumUtils.lowercaseNames(BlockColor.values());

	public static final String[] SIGNAL_BLOCK_SUGGESTIONS = EnumUtils.lowercaseNames(SignalBlock.values());

	public static final String[] COLORED_BLOCK_TYPE_SUGGESTIONS = EnumUtils.lowercaseNames(ColoredBlockType.values());

	public static final SuggestionProvider<CommandSourceStack> BLOCK_COLOR_SUGGESTION_PROVIDER = (context, builder) -> {
		Stream<String> names = Arrays.stream(BLOCK_COLOR_SUGGESTIONS);

		SharedSuggestionProvider.suggest(names, builder);
		return builder.buildFuture();
	};

	public static final SuggestionProvider<CommandSourceStack> SIGNAL_BLOCK_SUGGESTION_PROVIDER = (context, builder) -> {
		Stream<String> names = Arrays.stream(SIGNAL_BLOCK_SUGGESTIONS);

		SharedSuggestionProvider.suggest(names, builder);
		return builder.buildFuture();
	};

	public static final SuggestionProvider<CommandSourceStack> COLORED_BLOCK_TYPE_SUGGESTION_PROVIDER = (context, builder) -> {
		Stream<String> names = Arrays.stream(COLORED_BLOCK_TYPE_SUGGESTIONS);

		SharedSuggestionProvider.suggest(names, builder);
		return builder.buildFuture();
	};

	public static final SuggestionProvider<CommandSourceStack> MASK_SUGGESTION_PROVIDER = (context, builder) -> {
		var player = context.getSource().getPlayer();
		if (player == null || !DependencyLookup.WORLDEDIT_PRESENT) {
			return builder.buildFuture();
		}

		SharedSuggestionProvider.suggest(WorldEditUtils.suggestMask(player, builder.getRemaining()), builder);
		return builder.buildFuture();
	};

	public static final SuggestionProvider<CommandSourceStack> SLAB_SUGGESTION_PROVIDER = (context, builder) -> {
		List<String> names = new ArrayList<>();

		for (Block block : BuiltInRegistries.BLOCK) {
			if (block instanceof SlabBlock) {
				names.add(blockPath(block));
			}
		}

		SharedSuggestionProvider.suggest(names, builder);
		return builder.buildFuture();
	};

	public static String blockPath(Block block) {
		return BuiltInRegistries.BLOCK.getKey(block).getPath();
	}

	public static SignalBlock parseSignalBlock(CommandContext<CommandSourceStack> context, final String name) throws CommandSyntaxException {
		String result = context.getArgument(name, String.class);
		SignalBlock signalBlock = EnumUtils.byNameOrNull(SignalBlock.values(), result);
		if (signalBlock == null) {
			throw new SimpleCommandExceptionType(Component.literal("Could not resolve signal block!")).create();
		}
		return signalBlock;
	}

	public static BlockColor parseBlockColor(CommandContext<CommandSourceStack> context, final String name) throws CommandSyntaxException {
		String result = context.getArgument(name, String.class);
		BlockColor color = EnumUtils.byNameOrNull(BlockColor.values(), result);
		if (color == null) {
			throw new SimpleCommandExceptionType(Component.literal("Could not resolve block color!")).create();
		}
		return color;
	}

	public static ColoredBlockType parseColoredBlockType(CommandContext<CommandSourceStack> context, final String name) throws CommandSyntaxException {
		String result = context.getArgument(name, String.class);
		ColoredBlockType blockType = EnumUtils.byNameOrNull(ColoredBlockType.values(), result);
		if (blockType == null) {
			throw new SimpleCommandExceptionType(Component.literal("Could not resolve colored block type!")).create();
		}
		return blockType;
	}
}
