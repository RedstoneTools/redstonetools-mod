package tools.redstone.redstonetools.utils;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.stream.Stream;

public class ArgumentUtils {
	public static final String[] BLOCK_COLOR_SUGGESTIONS = EnumUtils.lowercaseNames(BlockColor.values());

	public static final String[] SIGNAL_BLOCK_SUGGESTIONS = EnumUtils.lowercaseNames(SignalBlock.values());

	public static final String[] DIRECTION_SUGGESTIONS = EnumUtils.lowercaseNames(DirectionArgument.values());

	public static final String[] COLORED_BLOCK_TYPE_SUGGESTIONS = EnumUtils.lowercaseNames(ColoredBlockType.values());

	public static final SuggestionProvider<ServerCommandSource> BLOCK_COLOR_SUGGESTION_PROVIDER = (context, builder) -> {
		Stream<String> names = Arrays.stream(BLOCK_COLOR_SUGGESTIONS);

		CommandSource.suggestMatching(names, builder);
		return builder.buildFuture();
	};

	public static final SuggestionProvider<ServerCommandSource> SIGNAL_BLOCK_SUGGESTION_PROVIDER = (context, builder) -> {
		Stream<String> names = Arrays.stream(SIGNAL_BLOCK_SUGGESTIONS);

		CommandSource.suggestMatching(names, builder);
		return builder.buildFuture();
	};

	public static final SuggestionProvider<ServerCommandSource> DIRECTION_SUGGESTION_PROVIDER = (context, builder) -> {
		Stream<String> names = Arrays.stream(DIRECTION_SUGGESTIONS);

		CommandSource.suggestMatching(names, builder);
		return builder.buildFuture();
	};

	public static final SuggestionProvider<ServerCommandSource> COLORED_BLOCK_TYPE_SUGGESTION_PROVIDER = (context, builder) -> {
		Stream<String> names = Arrays.stream(COLORED_BLOCK_TYPE_SUGGESTIONS);

		CommandSource.suggestMatching(names, builder);
		return builder.buildFuture();
	};

	public static SignalBlock parseSignalBlock(CommandContext<ServerCommandSource> context, final String name) throws CommandSyntaxException {
		String result = context.getArgument(name, String.class);
		SignalBlock signalBlock = EnumUtils.byNameOrNull(SignalBlock.values(), result);
		if (signalBlock == null) {
			throw new SimpleCommandExceptionType(Text.literal("Could not resolve signal block!")).create();
		}
		return signalBlock;
	}

	public static BlockColor parseBlockColor(CommandContext<ServerCommandSource> context, final String name) throws CommandSyntaxException {
		String result = context.getArgument(name, String.class);
		BlockColor color = EnumUtils.byNameOrNull(BlockColor.values(), result);
		if (color == null) {
			throw new SimpleCommandExceptionType(Text.literal("Could not resolve block color!")).create();
		}
		return color;
	}

	public static ColoredBlockType parseColoredBlockType(CommandContext<ServerCommandSource> context, final String name) throws CommandSyntaxException {
		String result = context.getArgument(name, String.class);
		ColoredBlockType blockType = EnumUtils.byNameOrNull(ColoredBlockType.values(), result);
		if (blockType == null) {
			throw new SimpleCommandExceptionType(Text.literal("Could not resolve colored block type!")).create();
		}
		return blockType;
	}

	public static DirectionArgument parseDirection(CommandContext<ServerCommandSource> context, final String name) throws CommandSyntaxException {
		String result = context.getArgument(name, String.class);
		DirectionArgument direction = EnumUtils.byNameOrNull(DirectionArgument.values(), result);
		if (direction == null) {
			throw new SimpleCommandExceptionType(Text.literal("Could not resolve direction!")).create();
		}
		return direction;
	}
}
