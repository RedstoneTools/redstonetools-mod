package tools.redstone.redstonetools.features.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.utils.ColoredBlockType;
import tools.redstone.redstonetools.utils.EnumUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class ColoredBlockTypeArgumentType implements ArgumentType<ColoredBlockType> {

	private static final Collection<String> EXAMPLES = Arrays.asList("wool", "concrete", "terracotta");

	private static final String[] SUGGESTIONS = EnumUtils.lowercaseNames(ColoredBlockType.values());

	public ColoredBlockTypeArgumentType() {
	}

	public static ColoredBlockTypeArgumentType coloredblocktype() {
		return new ColoredBlockTypeArgumentType();
	}

	public static ColoredBlockType getColoredBlockType(final CommandContext<?> context, final String name) {
		return context.getArgument(name, ColoredBlockType.class);
	}

	@Override
	public ColoredBlockType parse(final StringReader reader) throws CommandSyntaxException {
		final int start = reader.getCursor();
		final String result = reader.readString();

		ColoredBlockType blockType = EnumUtils.byNameOrNull(ColoredBlockType.values(), result);
		if (blockType == null) {
			reader.setCursor(start);
			throw new SimpleCommandExceptionType(Text.literal("Could not resolve block color!")).create();
		}
		return blockType;
	}

	@Override
	public String toString() {
		return "coloredblocktypeargument()";
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return CommandSource.suggestMatching(SUGGESTIONS, builder);
	}
}
