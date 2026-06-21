package tools.redstone.redstonetools.features.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import tools.redstone.redstonetools.config.MacroManager;
import tools.redstone.redstonetools.config.Macros;
import tools.redstone.redstonetools.config.option.ConfigMacro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class MacroArgumentType implements ArgumentType<ConfigMacro> {
	private static final Collection<String> EXAMPLES = List.of(" ");

	public MacroArgumentType() {
	}

	public static MacroArgumentType macro() {
		return new MacroArgumentType();
	}

	public static ConfigMacro getMacro(final CommandContext<?> context, final String name) throws CommandSyntaxException {
		return context.getArgument(name, ConfigMacro.class);
	}

	@Override
	public ConfigMacro parse(final StringReader reader) throws CommandSyntaxException {
		final int start = reader.getCursor();
		final String result = reader.getRemaining();
		reader.setCursor(reader.getTotalLength());
		final ConfigMacro macro = MacroManager.getMacro(result);
		if (macro == null) {
			reader.setCursor(start);
			throw new SimpleCommandExceptionType(Component.literal("Macro '" + result + "' doesn't exist!")).create();
		}
		return macro;
	}

	@Override
	public String toString() {
		return ("macro()");
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		List<String> macroNamesList = new ArrayList<>();
		Macros.getMacros().forEach(macro -> macroNamesList.add(macro.getMacroName()));
		macroNamesList.sort(String.CASE_INSENSITIVE_ORDER);
		return SharedSuggestionProvider.suggest(macroNamesList, builder);
	}
}
