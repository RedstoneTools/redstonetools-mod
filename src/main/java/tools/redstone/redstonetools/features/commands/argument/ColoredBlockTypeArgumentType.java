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

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class ColoredBlockTypeArgumentType implements ArgumentType<ColoredBlockType> {

	private static final Collection<String> EXAMPLES = Arrays.asList("wool", "concrete", "terracotta");

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

		ColoredBlockType blockType =
				switch (result.toUpperCase()) {
					case ("WOOL") -> ColoredBlockType.WOOL;
					case ("GLASS") -> ColoredBlockType.GLASS;
					case ("CONCRETE") -> ColoredBlockType.CONCRETE;
					case ("TERRACOTTA") -> ColoredBlockType.TERRACOTTA;
					default -> {
						reader.setCursor(start);
						throw new SimpleCommandExceptionType(Text.literal("Could not resolve block color!")).create();
					}
				};
		return blockType;
	}

	@Override
	public String toString() {
		return ("coloredblocktype()");
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}

	final String[] blockTypes = {
			"wool", "glass", "concrete", "terracotta"
	};

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return CommandSource.suggestMatching(blockTypes, builder);
	}
}
