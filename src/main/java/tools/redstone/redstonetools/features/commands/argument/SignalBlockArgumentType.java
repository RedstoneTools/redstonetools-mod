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
import tools.redstone.redstonetools.utils.EnumUtils;
import tools.redstone.redstonetools.utils.SignalBlock;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class SignalBlockArgumentType implements ArgumentType<SignalBlock> {

	private static final Collection<String> EXAMPLES = Arrays.asList("blue", "red", "magenta");

	private static final String[] SUGGESTIONS = EnumUtils.lowercaseNames(SignalBlock.values());

	public SignalBlockArgumentType() {
	}

	public static SignalBlockArgumentType signalblock() {
		return new SignalBlockArgumentType();
	}

	public static SignalBlock getSignalBlock(final CommandContext<?> context, final String name) {
		return context.getArgument(name, SignalBlock.class);
	}

	@Override
	public SignalBlock parse(final StringReader reader) throws CommandSyntaxException {
		final int start = reader.getCursor();
		final String result = reader.readString();

		SignalBlock signalBlock = EnumUtils.byNameOrNull(SignalBlock.values(), result);
		if (signalBlock == null) {
			reader.setCursor(start);
			throw new SimpleCommandExceptionType(Text.literal("Could not resolve signal block!")).create();
		}
		return signalBlock;
	}

	@Override
	public String toString() {
		return "signalblockargument()";
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
