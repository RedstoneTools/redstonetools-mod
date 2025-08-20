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
import tools.redstone.redstonetools.utils.BlockColor;
import tools.redstone.redstonetools.utils.EnumUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class BlockColorArgumentType implements ArgumentType<BlockColor> {

	private static final Collection<String> EXAMPLES = Arrays.asList("blue", "red", "magenta");

	private static final String[] SUGGESTIONS = EnumUtils.lowercaseNames(BlockColor.values());

	public BlockColorArgumentType() {
	}

	public static BlockColorArgumentType blockcolor() {
		return new BlockColorArgumentType();
	}

	public static BlockColor getBlockColor(final CommandContext<?> context, final String name) {
		return context.getArgument(name, BlockColor.class);
	}

	@Override
	public BlockColor parse(final StringReader reader) throws CommandSyntaxException {
		final int start = reader.getCursor();
		final String result = reader.readString();

		BlockColor color = EnumUtils.byNameOrNull(BlockColor.values(), result);
		if (color == null) {
			reader.setCursor(start);
			throw new SimpleCommandExceptionType(Text.literal("Could not resolve block color!")).create();
		}
		return color;
	}

	@Override
	public String toString() {
		return "blockcolorargument()";
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
