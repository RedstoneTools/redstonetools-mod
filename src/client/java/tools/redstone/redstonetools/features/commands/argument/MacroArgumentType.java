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
import tools.redstone.redstonetools.macros.Macro;
import tools.redstone.redstonetools.macros.MacroManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MacroArgumentType  implements ArgumentType<Macro> {

    private static final Collection<String> EXAMPLES = List.of(" ");

    public MacroArgumentType() {
    }
    public static MacroArgumentType macro() {
        return new MacroArgumentType();
    }

    public static Macro getMacro(final CommandContext<?> context, final String name) throws CommandSyntaxException {
        return context.getArgument(name, Macro.class);
    }

    @Override
    public Macro parse(final StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        final String result = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());
        final Macro macro = MacroManager.getMacro(result);
        if (macro == null) {
            reader.setCursor(start);
            throw new SimpleCommandExceptionType(Text.literal("Macro '" + result + "' doesn't exist!")).create();
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
        MacroManager.getMacros().forEach(macro -> macroNamesList.add(macro.name));
        return CommandSource.suggestMatching(macroNamesList, builder);
    }
}
